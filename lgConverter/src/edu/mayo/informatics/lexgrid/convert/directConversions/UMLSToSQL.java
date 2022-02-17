
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.collections.map.LRUMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.UMLSBaseCode;

/**
 * A tool to load the LexGrid SQL table from UMLS.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 9147 $ checked in on $Date: 2008-07-11
 *          21:20:31 +0000 (Fri, 11 Jul 2008) $
 */
public class UMLSToSQL extends UMLSBaseCode {
    // maintain load state ...
    private String codingSchemeName_;
    private String registeredName_;
    private HashSet alreadyLoadedAssociations = new HashSet();
    private HashSet loadedRelations_ = new HashSet();
    private String lastAssociation;

    private boolean usingManifest_ = false;

    // the map and the static int's are used to map the mrsat entries to the
    // proper places.
    // see the initMRSATMap method.
    private Hashtable mrsatMap_ = new Hashtable();
    private Hashtable atnRepresentationalMap_ = new Hashtable();

    private static final Integer COMMENT = new Integer(1);
    private static final Integer INSTRUCTION = new Integer(2);
    private static final Integer PRESENTATION = new Integer(3);
    private static final Integer SKIP = new Integer(4);

    int instructionCounter_ = 1;
    int presentationCounter_ = 1;
    int commentCounter_ = 1;
    int definitionCounter_ = 1;
    int propertyCounter_ = 1;
    int cuiCounter_ = 1;
    int semTypeCounter_ = 1;

    PreparedStatement getSnomedLanguage_;

    // Define separate containers for standard UMLS relations and
    // unique relation name attributes defined per source.
    private static final String SRC_RELATIONS = "Relations";
    private static final String STD_RELATIONS = "UMLS_Relations";

    // Supporting inner classes ...
    private class AUI_PROP {
        String atn = "";
        String atv = "";
        String stype = "";
        String metaui = "";

        AUI_PROP(String atn, String atv, String stype, String metaui) {
            this.atn = atn;
            this.atv = atv;
            this.stype = stype;
            this.metaui = metaui;
        }
    }

    private class RelationType {
        String relation;
        String relationDescription;
        boolean isNative;

        public RelationType(String relation, String relationDescription, boolean isNative) {
            this.relation = relation;
            this.relationDescription = relationDescription;
            this.isNative = isNative;
        }

    }

    /**
     * Class to convert from UMLS to SQL.
     * 
     * @param sqlServer
     *            location of SQL server
     * @param sqlDriver
     *            SQL driver class
     * @param sqlUserName
     *            username for server authentication
     * @param sqlPassword
     *            password for server authentication
     * @param sqlTablePrefix
     *            table prefix to use for generated files
     * @param umlsServer
     *            location of UMLS server
     * @param umlsDriver
     *            UMLS driver class
     * @param umlsUserName
     *            username for server authentication
     * @param umlsPassword
     *            password for server authentication
     * @param codingScheme
     *            CodingScheme to be converted
     * @param loadPrefs
     *            Loader Preferences
     * @param manifestLocation
     *            location of Manifest file
     * @param enforceIntegrity
     *            enforce foreign key constraints on tables
     * @param director
     *            message director for log output
     * @throws Exception
     */
    public UMLSToSQL(String sqlServer, String sqlDriver, String sqlUserName, String sqlPassword, String sqlTablePrefix,
            String umlsServer, String umlsDriver, String umlsUserName, String umlsPassword, String codingScheme,
            LoaderPreferences loadPrefs, URI manifestLocation, boolean enforceIntegrity, LgMessageDirectorIF director)
            throws Exception {
        log = LogManager.getLogger("convert.UMLSToSQL");
        messages_ = director;
        manifestLocation_ = manifestLocation;
        loadPrefs_ = loadPrefs;
        try {
            initIsoMap();
            initMRSATMap();
            initMRCONSOTTYMAP();

            makeConnections(umlsServer, umlsDriver, umlsUserName, umlsPassword, sqlServer, sqlDriver, sqlUserName,
                    sqlPassword, sqlTablePrefix, enforceIntegrity);

            initOtherSqlStatements();
            loadMRRANK();

            // If they didn't pass a coding scheme, just create the tables and
            // exit.
            if (codingScheme == null || codingScheme.trim().length() == 0) {
                return;
            }

            loadCodingScheme(codingScheme);
            closeOtherSqlStatement();
            closeConnections();
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed", e);
        } finally {
            if (sqlConnection_ != null) {
                sqlConnection_.close();
            }
            if (umlsConnection_ != null) {
                umlsConnection_.close();
            }
            if (umlsConnection2_ != null) {
                umlsConnection2_.close();
            }
        }
    }

    public String getLoadedCodingSchemeName() {
        return codingSchemeName_;
    }

    /*
     * enter the mappings from MRSAT ATN codes to the locations we want them.
     */
    private void initMRSATMap() {
        mrsatMap_.put("AN", COMMENT);
        mrsatMap_.put("CX", COMMENT);
        mrsatMap_.put("HN", COMMENT);

        mrsatMap_.put("EV", PRESENTATION);
        atnRepresentationalMap_.put("EV", "Abbrev");

        mrsatMap_.put("DID", SKIP);
        mrsatMap_.put("MUI", SKIP);

        // SNOMEDCT puts the language codes in attributes, instead of putting
        // them on the concept
        // don't want to add them as attributes - these will be pulled out in a
        // custom case in the load
        // concept options as necessary.
        mrsatMap_.put("LANGUAGECODE", SKIP);
        mrsatMap_.put("SUBSETLANGUAGECODE", SKIP);

        // also don't need these from snomed - because they don't really make
        // sense the way
        // that we load them - they have these for each presentation - while in
        // our representation
        // they attributes apply to the entire concept
        mrsatMap_.put("DESCRIPTIONSTATUS", SKIP);
        mrsatMap_.put("DESCRIPTIONTYPE", SKIP);
        mrsatMap_.put("INITIALCAPITALSTATUS", SKIP);
        mrsatMap_.put("CHARACTERISTICTYPE", SKIP);
        mrsatMap_.put("REFINABILITY", SKIP);
        mrsatMap_.put("SUBSETMEMBER", SKIP);
    }

    private void initOtherSqlStatements() throws SQLException {
        getSnomedLanguage_ = umlsConnection_
                .prepareStatement("SELECT ATV FROM MRSAT WHERE CUI = ? AND SAB = 'SNOMEDCT' AND LUI = ? AND SUI = ? AND (ATN = 'LANGUAGECODE' OR ATN = 'SUBSETLANGUAGECODE')");
    }

    private void closeOtherSqlStatement() throws SQLException {
        if (getSnomedLanguage_ != null) {
            getSnomedLanguage_.close();
        }
    }

    /*
     * kick off the process of loading the coding scheme
     */
    private void loadCodingScheme(String UMLSCodingSchemeName) throws SQLException {
        messages_.info("Loading " + UMLSCodingSchemeName);
        PreparedStatement getCodingSchemeInfo = umlsConnection_
                .prepareStatement("SELECT  SON, SVER, SCC, LAT, SSN, SCIT, TFR FROM MRSAB WHERE RSAB = ?");
        getCodingSchemeInfo.setString(1, UMLSCodingSchemeName);

        ResultSet results = getCodingSchemeInfo.executeQuery();

        if (results.next()) {
            // change Coding Scheme Name according to the manifest.
            // Coding Scheme Name is the only thing that needs to be
            // set 'pre-load', everything else can be set 'post-load'.
            String codingSchemeName = getCodingSchemeNameFromManifest(results.getString("SSN"));
            codingSchemeName_ = codingSchemeName;

            String formalName = results.getString("SON");
            String registeredName = getRegisteredNameFromManifest(getISOString(UMLSCodingSchemeName));
            registeredName_ = registeredName;

            String defaultLanguage = results.getString("LAT");
            defaultLanguage_ = defaultLanguage;
            if (UMLSCodingSchemeName.equals("SNOMEDCT")) {
                // snomed has its languages in a weird way - hard code this.
                defaultLanguage_ = "en";
            }
            String representsVersion = results.getString("SVER");
            if (representsVersion == null || representsVersion.length() == 0) {
                messages_.warn("The Version number could not be read from the MRSAB file, SVER column.");
                representsVersion = SQLTableConstants.TBLCOLVAL_MISSING;
            }
            boolean isNative = false;
            int approxNumConcepts = 0;
            String anc = results.getString("TFR");
            try {
                approxNumConcepts = Integer.parseInt(anc);
            } catch (Exception e) {
                // do nothing
            }
            String entityDescription = results.getString("SCIT");
            String copyright = results.getString("SCC");

            messages_.info("Cleaning tables");
            sqlTableUtility_.cleanTables(codingSchemeName);

            addToCodingScheme(codingSchemeName, registeredName, representsVersion, formalName, defaultLanguage_, 
                    approxNumConcepts, null, 0, null, entityDescription, copyright);

            messages_.info("Loaded 1 coding scheme");

            loadCodingSchemeMultiAttributes(UMLSCodingSchemeName, codingSchemeName);
            loadCodingSchemeSupportedAttributes(UMLSCodingSchemeName, codingSchemeName);
            
            loadDefaultSupportedNamespace(codingSchemeName);
            loadConcepts(UMLSCodingSchemeName, codingSchemeName);
            loadRelations(UMLSCodingSchemeName, codingSchemeName);
            loadContexts(UMLSCodingSchemeName, codingSchemeName);                 

            buildRootNodes(codingSchemeName);
        }
        results.close();
        getCodingSchemeInfo.close();
    }

    /*
     * load the codingSchemeMultiAttributes table
     */

    /*
     * Iterate MRHIER for context information to be added as Association
     * Qualifications
     */
    private void loadContexts(String UMLSCodingSchemeName, String codingSchemeName) throws SQLException {
        messages_.info("Processing HCD-tagged MRHIER entries.");

        boolean constructHCD = false;

        /*
         * Create a temporary view (that will be destroyed at the end of the
         * method) to speed up the queries in this method
         */
        try {
            messages_.info("loading contexts - getting a total count");

            String getCodingSchemeInfoSQL = "SELECT COUNT(1) as cnt FROM MRHIER" + " WHERE SAB = ? ";
            if (!constructHCD) {
                getCodingSchemeInfoSQL = getCodingSchemeInfoSQL + " AND HCD != ? ";
            }

            PreparedStatement getCodingSchemeInfo = umlsConnection2_.prepareStatement(umlsSqlModifier_
                    .modifySQL(getCodingSchemeInfoSQL));
            ResultSet results = null;

            getCodingSchemeInfo.setString(1, UMLSCodingSchemeName);
            // this is weird, but its the easiest way to get the
            // appropriate quotes around NULL
            if (!constructHCD)
                getCodingSchemeInfo.setString(2, "NULL");
            int total = 0;
            try {
                results = getCodingSchemeInfo.executeQuery();

                results.next();
                total = results.getInt("cnt");

                results.close();
            } finally {
                getCodingSchemeInfo.close();
            }

            int start = 0;
            int contextCount = 0;
            int rowCount = 0;

            getCodingSchemeInfoSQL = "SELECT MRHIER.CUI, MRHIER.AUI, MRHIER.PTR, MRHIER.HCD, "
                    + " MRHIER.SAB, MRHIER.RELA, MRHIER.CXN " + " FROM MRHIER " + " WHERE SAB = ? ";
            if (!constructHCD) {
                getCodingSchemeInfoSQL = getCodingSchemeInfoSQL + " AND HCD != ? ";
            }
            getCodingSchemeInfoSQL = getCodingSchemeInfoSQL + " ORDER BY {BINARY} MRHIER.CUI {LIMIT}";
            getCodingSchemeInfo = umlsConnection2_.prepareStatement(umlsSqlModifier_.modifySQL(getCodingSchemeInfoSQL));

            // Count var usage to make sure things are placed correctly
            int boundVar = 1;
            getCodingSchemeInfo.setString(boundVar++, UMLSCodingSchemeName);
            // this is weird, but its the easiest way to get the
            // appropriate quotes around NULL
            if (!constructHCD)
                getCodingSchemeInfo.setString(boundVar++, "NULL");
            try {
                // collect them all into batches of concept codes. when the code
                // changes, load that code
                while (start < total) {
                    messages_.info("Fetching a batch of results");

                    if (umlsSqlModifier_.getDatabaseType().equals("MySQL")) {
                        // mysql doesn't stream results - the {LIMIT above and
                        // this is for getting limits on mysql code}
                        getCodingSchemeInfo.setInt(boundVar, start);
                        getCodingSchemeInfo.setInt(boundVar + 1, batchSize);
                        start += batchSize;
                    } else if (umlsSqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                        // postgres properly streams results, we can just set
                        // the fetch size, and only loop once
                        getCodingSchemeInfo.setFetchSize(batchSize);
                        umlsConnection2_.setAutoCommit(false);
                        start = total;
                    } else {
                        start = total;
                    }

                    results = getCodingSchemeInfo.executeQuery();
                    try {
                        messages_.debug("query finished, processing results");

                        while (results.next()) {
                            rowCount++;
                            // store all the data from this row.

                            AssociationQualification aq = new AssociationQualification();

                            aq.codingSchemeName = codingSchemeName;
                            String cui = results.getString("CUI");
                            aq.sourceConceptAUI = results.getString("AUI");
                            aq.sourceConceptCode = mapCUIToCode(cui, aq.sourceConceptAUI, UMLSCodingSchemeName)[0].code;
                            aq.qualifierName = "HCD";
                            aq.qualifierValue = results.getString("HCD");
                            aq.pathToRoot = results.getString("PTR");

                            contextCount += loadContext(aq, constructHCD, results.getString("RELA"), contextCount);

                            if (rowCount % 10 == 0) {
                                messages_.busy();
                            }

                            if (rowCount % 1000 == 0) {
                                messages_.info("On context " + rowCount + " out of " + total + " - found "
                                        + contextCount + " contextual links");
                            }
                        }
                    } finally {
                        results.close();
                    }
                }
                messages_.info("Loaded " + contextCount + " contextual links from  " + rowCount + " contexts");
            } finally {
                getCodingSchemeInfo.close();
            }
        } finally {
        }
    }

    private void loadCodingSchemeMultiAttributes(String UMLSCodingSchemeName, String codingSchemeName)
            throws SQLException {
        messages_.info("loading coding scheme multi attributes");
        // things like localName - source (which has a locatorInfo)

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_SOURCE);
        insertIntoCodingSchemeMultiAttributes.setString(3, "UMLS - "
                + getSourceVersionString("Unknown version of the UMLS"));
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        String iso = getISOString(UMLSCodingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, iso);
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        if (iso.startsWith("urn:iso:")) {

            insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
            insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
            insertIntoCodingSchemeMultiAttributes.setString(3, iso.substring("urn:iso:".length()));
            insertIntoCodingSchemeMultiAttributes.setString(4, "");
            insertIntoCodingSchemeMultiAttributes.setString(5, "");

            insertIntoCodingSchemeMultiAttributes.executeUpdate();
        }
    }

    /*
     * load the codingSchemeSupported Attributes table
     */
    private void loadCodingSchemeSupportedAttributes(String UMLSCodingSchemeName, String codingSchemeName)
            throws SQLException {
        messages_.info("loading supported attributes");

        // language - done later

        // format
        // arbitrary choices
        insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_FORMAT,
                SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN, null, null, null, null);
        insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_FORMAT,
                SQLTableConstants.TBLCOLVAL_FORMAT_TXT_HTML, null, null, null, null);
        insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_FORMAT,
                SQLTableConstants.TBLCOLVAL_FORMAT_TXT_XML, null, null, null, null);

        // property - these are loaded after the concepts.
        // property qualifiers
        supportedPropertyQualifiers_.add("HCD");
        loadSupportedPropertyQualifiers(codingSchemeName);

        // codingScheme
        // loaded later

        // source
        // currently none the way we load it.

        // association
        populateSupportedAssociations(UMLSCodingSchemeName);
        for (int i = 0; i < supportedAssociations_.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION, (supportedAssociations_[i]).name, null, null,
                    null, null);
        }
        messages_.info("added " + supportedAssociations_.length + " associations");

        // associationQualifier for contexts
        supportedAssociationQualifiers_.add("HCD");
        // add SMQ's to the associationQualifiers if this is medDRA
        if (UMLSCodingSchemeName.equals("MDR")) {
            supportedAssociationQualifiers_.add("SMQ_TERM_LEVEL");
            supportedAssociationQualifiers_.add("SMQ_TERM_CAT");
            supportedAssociationQualifiers_.add("SMQ_TERM_ADDVERSION");
            supportedAssociationQualifiers_.add("SMQ_TERM_WEIGHT");
            supportedAssociationQualifiers_.add("SMQ_TERM_SCOPE");
            supportedAssociationQualifiers_.add("SMQ_TERM_STATUS");

        }
        // load the associationQualfiers
        loadSupportedAssociationQualifiers(codingSchemeName);

        // conceptStatus
        // TODO dig these out of the history - (but not now)

        // data type
        // no data types.

        // representationForm - from the atn codes in mrsat
        Enumeration values = atnRepresentationalMap_.elements();
        int i = 0;
        while (values.hasMoreElements()) {
            String var = (String) values.nextElement();
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM, var, null, null, null, null);
            i++;
        }
        // more representationalForm - these are for tty codes in mrconso
        values = mrconsoRepresentationalMap_.elements();
        while (values.hasMoreElements()) {
            String var = (String) values.nextElement();
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM, var, null, null, null, null);
            i++;
        }
        messages_.info("Loaded " + i + " representational forms");
    }

    private void populateSupportedAssociations(String UMLSCodingSchemeName) throws SQLException {
        messages_.info("Getting the descriptive associations");
        PreparedStatement getRelations = umlsConnection_
                .prepareStatement("SELECT DISTINCT RELA, DIR FROM MRREL WHERE SAB = ?");

        getRelations.setString(1, UMLSCodingSchemeName);

        ResultSet relations = getRelations.executeQuery();
        Hashtable relationsHolder = new Hashtable();

        while (relations.next()) {
            String temp = relations.getString("RELA");
            String tempDirFlag = relations.getString("DIR");
            if (temp == null || temp.length() == 0) {
                continue;
            }
            mapSupportedAssociationsHelper(temp, UMLSCodingSchemeName, tempDirFlag, "RELA", relationsHolder);
        }

        messages_.info("Getting the base umls associations");
        getRelations = umlsConnection_.prepareStatement("SELECT DISTINCT REL, DIR FROM MRREL WHERE SAB = ?");
        getRelations.setString(1, UMLSCodingSchemeName);

        relations = getRelations.executeQuery();
        while (relations.next()) {
            String temp = relations.getString("REL");
            String tempDirFlag = relations.getString("DIR");
            if (temp == null || temp.length() == 0) {
                continue;
            }
            mapSupportedAssociationsHelper(temp, UMLSCodingSchemeName, tempDirFlag, "REL", relationsHolder);
        }
        getRelations.close();

        supportedAssociations_ = new Association[relationsHolder.size()];
        Enumeration elements = relationsHolder.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            Association temp = (Association) elements.nextElement();
            supportedAssociations_[i++] = temp;
        }
    }

    /*
     * load the concepts for a coding scheme
     */
    private void loadConcepts(String UMLSCodingSchemeName, String codingSchemeName) throws SQLException {
        messages_.info("loading concepts - getting a total count");

        PreparedStatement getCodingSchemeInfo = umlsConnection2_
                .prepareStatement("SELECT  COUNT(*) as cnt FROM MRCONSO WHERE SAB = ?");

        getCodingSchemeInfo.setString(1, UMLSCodingSchemeName);

        ResultSet results = getCodingSchemeInfo.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getCodingSchemeInfo.close();

        int start = 0;
        String lastCode = null;
        ArrayList conceptPresentations = new ArrayList();
        int codeCount = 0;
        int rowCount = 0;

        // collect them all into batches of concept codes. when the code
        // changes, load that code
        while (start < total) {
            messages_.info("Fetching a batch of results");
            getCodingSchemeInfo = umlsConnection2_.prepareStatement(umlsSqlModifier_
                    .modifySQL("SELECT  LAT, CODE, CUI, LUI, SUI, AUI, TS, STT, STR, TTY "
                            + " FROM MRCONSO WHERE SAB = ? ORDER BY {BINARY} CODE {LIMIT}"));

            getCodingSchemeInfo.setString(1, UMLSCodingSchemeName);

            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (umlsSqlModifier_.getDatabaseType().equals("MySQL")) {
                getCodingSchemeInfo.setInt(2, start);
                getCodingSchemeInfo.setInt(3, batchSize);
                start += batchSize;
            } else if (umlsSqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                getCodingSchemeInfo.setFetchSize(batchSize);
                umlsConnection2_.setAutoCommit(false);
                start = total;
            } else {
                start = total;
            }

            results = getCodingSchemeInfo.executeQuery();

            log.debug("query finished, processing results");

            while (results.next()) {
                rowCount++;
                // store all the data from this row.

                ConceptPresentation temp = new ConceptPresentation();
                temp.conceptCode = results.getString("CODE");
                temp.cui = results.getString("CUI");

                // there are some concepts that just have "NOCODE" as the
                // code... need to make them unique.
                if (temp.conceptCode.equals("NOCODE")) {
                    temp.conceptCode = temp.conceptCode + "-" + temp.cui;
                }

                if (lastCode != null && !temp.conceptCode.equals(lastCode)) {
                    loadConcept(UMLSCodingSchemeName, codingSchemeName, (ConceptPresentation[]) conceptPresentations
                            .toArray(new ConceptPresentation[conceptPresentations.size()]));

                    conceptPresentations.clear();
                    commentCounter_ = 1;
                    presentationCounter_ = 1;
                    instructionCounter_ = 1;
                    definitionCounter_ = 1;
                    propertyCounter_ = 1;
                    cuiCounter_ = 1;
                    semTypeCounter_ = 1;

                    if (codeCount % 10 == 0) {
                        messages_.busy();
                    }

                    codeCount++;
                    if (codeCount % 1000 == 0) {
                        messages_.info("On row " + rowCount + " out of " + total + " rows - found " + codeCount
                                + " concepts");
                    }
                }

                temp.language = results.getString("LAT");

                if (UMLSCodingSchemeName.equals("SNOMEDCT")) {
                    // snomed has a different way of recording the language of
                    // the presentation...
                    String snomedLanguage = getSnomedLanguageForPresentation(results.getString("CUI"), results
                            .getString("LUI"), results.getString("SUI"));
                    if (snomedLanguage != null && snomedLanguage.length() > 0) {
                        temp.language = snomedLanguage;
                    }
                }
                // add for populating the supportedLanguages later
                supportedLanguages_.add(temp.language);

                temp.presentationFormat = SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN;
                temp.TTY = results.getString("TTY");
                temp.representationForm = temp.TTY;
                temp.value = results.getString("STR");
                temp.TS = results.getString("TS");
                temp.STT = results.getString("STT");
                temp.AUI = results.getString("AUI");
                temp.isPreferred = new Boolean(false);
                temp.source = UMLSCodingSchemeName;

                // See if there is a "better" value for the representationalForm
                // mapping
                String repFormMap = (String) mrconsoRepresentationalMap_.get(temp.representationForm);
                if (repFormMap != null) {
                    temp.representationForm = repFormMap;
                }

                lastCode = temp.conceptCode;
                conceptPresentations.add(temp);
            }
            results.close();
        }

        // load the last one
        if (lastCode != null) {
            // need to add this, just once - assuming there were codes, so this
            // is a good place for it.
            supportedPropertyTypes_.add(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
            loadConcept(UMLSCodingSchemeName, codingSchemeName, (ConceptPresentation[]) conceptPresentations
                    .toArray(new ConceptPresentation[conceptPresentations.size()]));

            codeCount++;
        }

        messages_.info("Loaded " + codeCount + " concepts from  " + rowCount + " rows");
        
        // we are done with inserting entities, close the preparedStatement.
        insertIntoEntities.close();
        insertIntoEntityType.close();
        
        
        updateApproxNumberOfConcepts(codeCount, codingSchemeName);
        loadSupportedProperties(codingSchemeName);
        loadSupportedLanguages(codingSchemeName);
        getCodingSchemeInfo.close();
    }

    /*
     * Helper method to load all the properties and presentations of a concept
     */
    private void loadConcept(String UMLSCodingSchemeName, String codingSchemeName,
            ConceptPresentation[] conceptPresentations) throws SQLException {
        // order them - this will organize them by language, and put the "best"
        // presentation
        // first for each language. default language will be the at the very
        // top.
        Arrays.sort(conceptPresentations, new ConceptPresentationSorter());

        // now set each isPreferred to true for the first entry of each language
        String prevLang = conceptPresentations[0].language;
        conceptPresentations[0].isPreferred = new Boolean(true);
        for (int i = 1; i < conceptPresentations.length; i++) {
            if (!conceptPresentations[i].language.equals(prevLang)) {
                conceptPresentations[i].isPreferred = new Boolean(true);
            }
            prevLang = conceptPresentations[i].language;
        }

        // load the entry into the concepts table - must be at least 1 row. the
        // first one will
        // be the best text presentation to use here.
        try {
            addConceptToConcepts(codingSchemeName, conceptPresentations[0].conceptCode, null, null, null, new Boolean(
                    true), null, new Boolean(false), SQLTableConstants.TBLCOL_ISACTIVE, new Boolean(false),
                    conceptPresentations[0].value);
        } catch (SQLException e) {
            // Recover from a failure adding a new code. Likely caused by trying
            // to insert a duplicate code - some code systems are case sensitive
            // - and
            // most of our databases aren't.
            log.error("Problem inserting new code " + conceptPresentations[0].conceptCode, e);
            messages_.info("ERROR - Problem inserting new code " + conceptPresentations[0].conceptCode);
            return;
        }

        LRUMap TValues = new LRUMap(20);

        // load its properties
        for (int i = 0; i < conceptPresentations.length; i++) {
            String propertyId = "T-" + presentationCounter_++;
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                    conceptPresentations[i].conceptCode, propertyId, SQLTableConstants.TBLCOLVAL_PRESENTATION,
                    SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION, conceptPresentations[i].language,
                    conceptPresentations[i].presentationFormat, conceptPresentations[i].isPreferred, null, null,
                    conceptPresentations[i].representationForm, conceptPresentations[i].value);
            TValues.put(conceptPresentations[i].AUI, propertyId);
        }

        // load the definitions
        addToDefinitions(UMLSCodingSchemeName, conceptPresentations[0].cui, codingSchemeName,
                conceptPresentations[0].conceptCode);

        // load the other properties
        loadOtherProperties(UMLSCodingSchemeName, conceptPresentations[0].cui, codingSchemeName,
                conceptPresentations[0].conceptCode, TValues);

        // TODO dig the history out - add the modVersion (not now)
        // addConceptToConceptsMultiAttributes(codingSchemeName, conceptCode,
        // "modVersion", "");

        // this is where the usageContext would be loaded - but we don't have
        // any.
        // also, if we were to load the UMLS as a coding scheme (instead of
        // individual coding schemes) this is
        // where we would put the source information
        // addConceptToConceptPropertyMultiAttributes(codingSchemeName,
        // conceptCode, propertyId, "", "", "",
        // "");
    }

    /*
     * This is for mapping the MRSAT table into comments, instructions,
     * presentations, and just plain old properties.
     */
    private void loadOtherProperties(String UMLSCodingSchemeName, String cui, String codingSchemeName,
            String conceptCode, LRUMap TValues) throws SQLException {
        // Drop the CUI in.
        supportedPropertyTypes_.add("UMLS_CUI");
        addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, conceptCode,
                "CUI-" + cuiCounter_++, "property", "UMLS_CUI", null, null, null, null, null, null, cui);

        // get the semantic type(s)
        supportedPropertyTypes_.add("Semantic_Type");
        PreparedStatement getSemTypes = umlsConnection_.prepareStatement("SELECT STY FROM MRSTY WHERE CUI = ?");
        getSemTypes.setString(1, cui);

        ResultSet semTypes = getSemTypes.executeQuery();
        while (semTypes.next()) {
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, conceptCode, "SemType-"
                    + semTypeCounter_++, "property", "Semantic_Type", null,
                    SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN, null, null, null, null, semTypes.getString("STY"));
        }

        getSemTypes.close();

        // load the other properties
        PreparedStatement getProperties = umlsConnection_
                .prepareStatement("SELECT ATN, ATV, STYPE, METAUI FROM MRSAT WHERE CODE = ? AND SAB = ?");

        getProperties.setString(1, conceptCode);
        getProperties.setString(2, UMLSCodingSchemeName);

        ResultSet properties = getProperties.executeQuery();

        ArrayList auiProps = new ArrayList();

        while (properties.next()) {
            String propertyFlag = properties.getString("ATN");
            String propertyValue = properties.getString("ATV");
            String propertyStype = properties.getString("STYPE");
            String propertyMetaui = properties.getString("METAUI");

            String propertyId = null;
            String propertyName = null;
            String propertyType = null;
            String representationalForm = null;
            Boolean isPreferred = null;

            if (mrsatMap_.get(propertyFlag) != null
                    && (propertyStype.equalsIgnoreCase("CODE") || propertyStype.equalsIgnoreCase("SCUI") || propertyStype
                            .equalsIgnoreCase("SDUI"))) {
                Integer type = (Integer) mrsatMap_.get(propertyFlag);
                if (type.intValue() == SKIP.intValue()) {
                    continue;
                } else if (type.intValue() == COMMENT.intValue()) {
                    propertyId = "C-" + commentCounter_++;
                    propertyType = SQLTableConstants.TBLCOLVAL_COMMENT;
                    propertyName = SQLTableConstants.TBLCOLVAL_COMMENT;
                    isPreferred = new Boolean(false);
                } else if (type.intValue() == INSTRUCTION.intValue()) {
                    propertyId = "I-" + instructionCounter_++;
                    propertyType = SQLTableConstants.TBLCOLVAL_INSTRUCTION;
                    propertyName = SQLTableConstants.TBLCOLVAL_INSTRUCTION;
                    isPreferred = new Boolean(false);
                } else if (type.intValue() == PRESENTATION.intValue()) {
                    propertyId = "T-" + presentationCounter_++;
                    propertyType = SQLTableConstants.TBLCOLVAL_PRESENTATION;
                    propertyName = SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
                    TValues.put(propertyMetaui, propertyId);
                    isPreferred = new Boolean(false);
                }
            } else if (propertyStype.equalsIgnoreCase("SAUI")) {
                auiProps.add(new AUI_PROP(propertyFlag, propertyValue, propertyStype, propertyMetaui));
                continue;
            } else {
                propertyId = "P-" + propertyCounter_++;
                propertyType = SQLTableConstants.TBLCOLVAL_PROPERTY;
                propertyName = propertyFlag;
            }

            representationalForm = (String) atnRepresentationalMap_.get(propertyFlag);
            supportedPropertyTypes_.add(propertyName);
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, conceptCode, propertyId,
                    propertyType, propertyName, null, SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN, isPreferred, null,
                    null, representationalForm, propertyValue);
        }

        Iterator aPIter = auiProps.iterator();
        while (aPIter.hasNext()) {
            AUI_PROP ap = (AUI_PROP) aPIter.next();
            String apPropertyId = (String) TValues.get(ap.metaui);
            if (apPropertyId == null)
                continue;
            if (!supportedPropertyQualifiers_.contains(ap.atn))
                supportedPropertyQualifiers_.add(ap.atn);
            addConceptToEntityPropertyMultiAttributes(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                    conceptCode, apPropertyId, SQLTableConstants.TBLCOLVAL_QUALIFIER, ap.atn, ap.atv, "");
        }

        getProperties.close();
    }

    /*
     * Load the definitions for a concept
     */
    private void addToDefinitions(String UMLSCodingSchemeName, String cui, String codingSchemeName, String conceptCode)
            throws SQLException {
        PreparedStatement getDefinitions = umlsConnection_
                .prepareStatement("SELECT DEF FROM MRDEF WHERE CUI = ? AND SAB = ?");

        getDefinitions.setString(1, cui);
        getDefinitions.setString(2, UMLSCodingSchemeName);

        ResultSet definitions = getDefinitions.executeQuery();
        boolean isPreferred = true;
        while (definitions.next()) {
            supportedPropertyTypes_.add(SQLTableConstants.TBLCOLVAL_DEFINITION);
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, conceptCode, "D-"
                    + definitionCounter_++, SQLTableConstants.TBLCOLVAL_DEFINITION,
                    SQLTableConstants.TBLCOLVAL_DEFINITION, null, null, new Boolean(isPreferred), null, null, null,
                    definitions.getString("DEF"));
            isPreferred = false;
            // umls doesn't seem to have the notion of a preferred definition,
            // so I'll just set the first one to
            // preferred.
        }
        getDefinitions.close();
    }

    private String getSnomedLanguageForPresentation(String cui, String lui, String sui) throws SQLException {
        getSnomedLanguage_.setString(1, cui);
        getSnomedLanguage_.setString(2, lui);
        getSnomedLanguage_.setString(3, sui);

        ResultSet results = getSnomedLanguage_.executeQuery();
        String result = null;
        if (results.next()) {
            result = results.getString("ATV");
        }
        results.close();
        return result;
    }

    private void loadRelations(String UMLSCodingSchemeName, String codingSchemeName) throws SQLException {
        // Sort the associations, loading RELA's first and keeping
        // primary names (potentially qualified by different SABs)
        // together.

        // If there are any association qualifiers from MRSAT set a boolean
        // value
        boolean hasPossibleRUIQualifiers = false;
        messages_.info("Getting association qualifiers");
        ResultSet assocQualresults = getAllAssocQualifiersFromRRF_.executeQuery();
        try {
            while (assocQualresults.next()) {
                hasPossibleRUIQualifiers = true;
                // Cache the RUI from the results.
                associationQualifierRUI_.add(assocQualresults.getString("METAUI"));

            }
        } finally {
            assocQualresults.close();
        }
        messages_.debug("Distinct both way association qualifiers for this terminology: "
                + associationQualifierRUI_.size());

        Arrays.sort(supportedAssociations_, new AssociationSorter());

        for (int i = 0; i < supportedAssociations_.length; i++) {
            Association assoc = (Association) supportedAssociations_[i];
            if (lastAssociation == null || !lastAssociation.equals(assoc.name)) {
                // Moving on to a new association name.
                lastAssociation = assoc.name;
                // Clear the hashtable used to track source/target combinations
                // loaded.
                alreadyLoadedAssociations.clear();
            }

            int count = loadRelationsHelper(assoc, UMLSCodingSchemeName, codingSchemeName, hasPossibleRUIQualifiers);
            if (count == 0) {
                messages_.info("No relations were found for " + assoc.toShortString());
                log.warn("No relations were found for " + assoc.toShortString());
            }
        }

        // populate the supported coding schemes, now that we know what they
        // are.
        Iterator values = supportedCodingSchemes_.iterator();
        int i = 0;
        while (values.hasNext()) {
            String var = (String) values.next();
            String urn = getISOString(mapCodeSystemNameToSAB(var));
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME, var, urn, null, null, null);
            i++;
        }

        // If the Manifest has changed the coding schem name, we must add that
        // also
        if (manifestNameChange_) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME, codingSchemeName_, registeredName_, null, null,
                    null);
            i++;
            messages_.info("Loaded the user defined (Manifest) Coding Scheme name to supported Coding Schemes");
        }

        messages_.info("Loaded " + i + " supported coding schemes");
    }

    private int loadRelationsHelper(Association assoc, String UMLSCodingSchemeName, String codingSchemeName,
            boolean hasPossibleRUIQualifiers) throws SQLException {
        PreparedStatement getRRFRelationsCount = umlsConnection2_
                .prepareStatement("SELECT COUNT(*) AS cnt FROM MRREL WHERE " + assoc.rrfField + " = ? AND SAB = ?");
        // Note: ordered to enable a cache to not load duplicates.
        PreparedStatement getRRFRelations = umlsConnection2_.prepareStatement(umlsSqlModifier_
                .modifySQL("SELECT  CUI1, AUI1, CUI2, AUI2, RUI FROM MRREL WHERE " + assoc.rrfField
                        + " = ? AND SAB = ? ORDER BY CUI1 {LIMIT}"));
        int count = 0;

        try {
            int start = 0;
            int total = 0;
            int dupeCount = 0;

            getRRFRelationsCount.setString(1, assoc.rrfName);
            getRRFRelationsCount.setString(2, UMLSCodingSchemeName);

            messages_.info("Counting relations for " + assoc.name);
            ResultSet results = getRRFRelationsCount.executeQuery();
            results.next();
            total = results.getInt("cnt");
            results.close();

            while (start < total) {
                getRRFRelations.setString(1, assoc.rrfName);
                getRRFRelations.setString(2, UMLSCodingSchemeName);

                // mysql doesn't stream results - the {LIMIT above and this is
                // for getting limits on mysql code}
                if (umlsSqlModifier_.getDatabaseType().equals("MySQL")) {
                    getRRFRelations.setInt(3, start);
                    getRRFRelations.setInt(4, batchSize * 3);
                    start += batchSize * 3;
                }
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                else if (umlsSqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                    getRRFRelations.setFetchSize(batchSize * 3);
                    umlsConnection2_.setAutoCommit(false);
                    start = total;
                } else {
                    start = total;
                }

                messages_.info("Getting a batch of relations for " + assoc.rrfName);
                results = getRRFRelations.executeQuery();

                while (results.next()) {
                    // If the source association definition is reversed, we need
                    // to flip
                    // the source and target concepts accordingly.
                    boolean isReversed = assoc.rrfSourceDirectionalityReversed;
                    String cui = results.getString(isReversed ? "CUI2" : "CUI1");
                    String aui = results.getString(isReversed ? "AUI2" : "AUI1");
                    // Getting RUI so we can check against the list of distinct
                    // qualifier METAUI's
                    String rui = results.getString("RUI");
                    CodeHolder[] sourceCode = mapCUIToCode(cui, aui, UMLSCodingSchemeName);

                    if (sourceCode == null || sourceCode.length == 0) {
                        log.error("Could not find sourceCode for " + cui + " : " + aui);
                        continue;
                    }

                    cui = results.getString((isReversed ? "CUI1" : "CUI2"));
                    aui = results.getString((isReversed ? "AUI1" : "AUI2"));

                    CodeHolder[] targetCode = mapCUIToCode(cui, aui, UMLSCodingSchemeName);

                    if (targetCode == null || targetCode.length == 0) {
                        log.error("Could not find targetCode for " + cui + " : " + aui);
                        continue;
                    }

                    /*
                     * Most terminologies have a one to one mapping from cui -
                     * aui pairs to unique codes. So there will only be one
                     * source and one target concept code. RXNorm and MTH,
                     * however, don't always provide AUI's. When no AUI's are
                     * provided, it is possible to have the CUI's map to more
                     * than one source or target code.
                     */
                    for (int i = 0; i < sourceCode.length; i++) {
                        for (int j = 0; j < targetCode.length; j++) {
                            int tempval = addConceptAssociationToConceptsHelper(codingSchemeName, sourceCode[i], assoc,
                                    targetCode[j], rui, hasPossibleRUIQualifiers);

                            if (tempval > 0) {
                                count++;
                                
                                if (count % 100 == 0) {
                                    messages_.busy();
                                }

                                if (count % 10000 == 0) {
                                    messages_.info("Loaded " + count + " out of a possible total of " + total);
                                }
                            } else if (tempval < 0) {
                                dupeCount++;
                            }
                        }
                    }                    
                }
                results.close();
            }
            messages_.info("Loaded " + count + " out of a possible total of " + total);
            if (dupeCount > 0) {
                messages_.info("Encountered " + dupeCount + " duplicates");
            }
        } finally {
            getRRFRelations.close();
            getRRFRelationsCount.close();
        }
        return count;
    }

    /**
     * Add a code to code association.
     * 
     * @param currentCodingScheme
     * @param sourceCode
     * @param association
     * @param targetCode
     * @param hasQualifiers
     * @param rui
     * @return 1 if the association was added; 0 if an error occurred; -1 if the
     *         association was already defined.
     * @throws SQLException
     */
    private int addConceptAssociationToConceptsHelper(String currentCodingScheme, CodeHolder sourceCode,
            Association association, CodeHolder targetCode, String rui, boolean hasPossibleRUIQualifiers)
            throws SQLException

    {

        RelationType relationType = getRelationType(association.rrfField, sourceCode.codingScheme,
                targetCode.codingScheme);

        if (!loadedRelations_.contains(currentCodingScheme + relationType.relation)) {
            messages_.info("Adding the relations container to put the associations under.");

            addRelationToRelations(currentCodingScheme, relationType.relation, new Boolean(relationType.isNative),
                    relationType.relationDescription);
            loadedRelations_.add(currentCodingScheme + relationType.relation);
        }

        if (!isLoaded(association, currentCodingScheme, relationType.relation)) {
            // If hierarchical, indicate transitivity.
            Boolean transitive = new Boolean(isHierarchicalAssociation(association, currentCodingScheme));
            messages_.info("Adding association: " + association.name);
            addAssociationToAssociations(currentCodingScheme, relationType.relation, association.name,
                    association.rrfName, association.rrfInverse, "", new Boolean(true), transitive, null, null, null,
                    null, null, null, null, null, null, "UMLS-defined relationships");
            markLoaded(association, currentCodingScheme, relationType.relation);
        }

        int returnVal = 0;

        // No need for association name here, since we always go through in a
        // known order.
        String key = sourceCode.codingScheme + ":" + sourceCode.code + ":" + targetCode.codingScheme + ":"
                + targetCode.code;

        // Already registered this one?
        if (!alreadyLoadedAssociations.contains(key)) {
            // Try optimistic insert. On error, check for duplicate just
            // in case there was some failure to cache the key.
            // Otherwise, throw the original err.
            ResultSet assocQual = null;
            try {

                String multiKey = null;
                // generic association qualifier name
                boolean ruiQualifierExists = false;

                if (hasPossibleRUIQualifiers && associationQualifierRUI_.contains(rui)) {
                    ruiQualifierExists = true;
                }
                // Only create the key if the RUI qualifier exists
                if (ruiQualifierExists) {
                    multiKey = generateUniqueKey(new String[] { currentCodingScheme, sourceCode.codingScheme,
                            sourceCode.code, relationType.relation, association.name, targetCode.codingScheme,
                            targetCode.code });
                }

                // if we haven't changed the name by the manifest, we can load
                // the associations
                // with the source name they pull from the RRF Files
                if (!manifestNameChange_) {
                    addEntityAssociationToEntity(currentCodingScheme, sourceCode.codingScheme,
                            SQLTableConstants.ENTITYTYPE_CONCEPT, sourceCode.code, relationType.relation,
                            association.name, targetCode.codingScheme, SQLTableConstants.ENTITYTYPE_CONCEPT,
                            targetCode.code, multiKey, null, null);
                    alreadyLoadedAssociations.add(key);
                } else {
                    // if we did change the name, all these associations have to
                    // be changed to belong to the
                    // new Coding Scheme name the user defined
                    addEntityAssociationToEntity(currentCodingScheme, codingSchemeName_,
                            SQLTableConstants.ENTITYTYPE_CONCEPT, sourceCode.code, relationType.relation,
                            association.name, codingSchemeName_, SQLTableConstants.ENTITYTYPE_CONCEPT, targetCode.code,
                            multiKey, null, null);
                    alreadyLoadedAssociations.add(key);
                }

                // The qualifier is present and valid then we want it inserted
                // the CQuals table
                // This insert has to happen after the association insert to
                // prevent
                // Foreign Key conflicts
                if (ruiQualifierExists) {
                    getAssocQualifierFromRRF_.setString(1, rui);
                    assocQual = getAssocQualifierFromRRF_.executeQuery();
                    String qualifierName = null;
                    String qualifierValue = null;

                    while (assocQual.next()) {
                        qualifierName = assocQual.getString(1);
                        qualifierValue = assocQual.getString(2);
                        addEntityAssociationQualifierToEntityAssociation(currentCodingScheme, multiKey, qualifierName,
                                qualifierValue);
                    }
                }

                returnVal = 1;
            } catch (SQLException sqle) {
                ResultSet rs = null;
                getAssocInstance_.setString(1, currentCodingScheme);
                getAssocInstance_.setString(2, relationType.relation);
                getAssocInstance_.setString(3, association.name);
                getAssocInstance_.setString(4, sourceCode.codingScheme);
                getAssocInstance_.setString(5, sourceCode.code);
                getAssocInstance_.setString(6, targetCode.codingScheme);
                getAssocInstance_.setString(7, targetCode.code);
                try {
                    rs = getAssocInstance_.executeQuery();
                    if (rs.next()) {
                        alreadyLoadedAssociations.add(key);
                        returnVal = -1;
                    } else {
                        throw sqle;
                    }
                } catch (Exception e) {
                    log.warn("Association instance not inserted.", sqle);
                } finally {
                    if (rs != null)
                        rs.close();

                    if (assocQual != null)
                        assocQual.close();

                }
            }
        } else {
            returnVal = -1;
        }
        return returnVal;
    }

    private RelationType getRelationType(String relationSource, String sourceCodingScheme, String targetCodingScheme) {
        String relation;
        String relationDescription;
        boolean isNative;

        supportedCodingSchemes_.add(sourceCodingScheme);
        supportedCodingSchemes_.add(targetCodingScheme);

        // REL and RELA relations are split across relation containers to
        // separate standard UMLS relation names from source-specific labels.
        // However, with interpretation of NLM as curator both are tagged as
        // native (defined by curator). This also serves to make default queries
        // against coded node sets and graphs more inclusive.
        if (relationSource.equals("REL")) {
            isNative = true;
            relation = STD_RELATIONS;
            relationDescription = "UMLS-defined relationships (e.g. RB, RN, CHD, PAR)";
        } else // 'RELA'
        {
            isNative = true;
            relation = SRC_RELATIONS;
            relationDescription = "Source-defined relationships.";
        }

        return new RelationType(relation, relationDescription, isNative);
    }

    // //////////////////////////////////////////////////////////////
    // Code for tracking hierarchical associations used in each
    // container, and according to the direction of navigation from
    // parent to child...
    // //////////////////////////////////////////////////////////////

    /**
     * Build navigable relations from special endpoint nodes '@' and '@@' to
     * first level nodes in hierarchical associations.
     * 
     * @param codingSchemeName
     *            The LexGrid code system name.
     * @throws SQLException
     */
    protected void buildRootNodes(String codingSchemeName) throws SQLException {
        // Calculate for the source relations container ...
        SABString[] names = getHierRelas();
        for (int i = 0; i < names.length; i++) {
            SABString name = names[i];
            if (isLoaded(name.str, codingSchemeName, SRC_RELATIONS)) {
                buildRootNode(codingSchemeName, false, new SABString[] { name }, SRC_RELATIONS);
            }
        }

        // Calculate for standard umls relations ...
        names = getHierRels();
        for (int i = 0; i < names.length; i++) {
            SABString name = names[i];
            if (isLoaded(name.str, codingSchemeName, STD_RELATIONS)) {
                buildRootNode(codingSchemeName, false, new SABString[] { name }, STD_RELATIONS);
            }
        }
    }
}