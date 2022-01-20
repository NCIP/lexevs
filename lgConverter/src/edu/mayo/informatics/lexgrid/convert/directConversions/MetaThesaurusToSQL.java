
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.net.URI;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.UMLSBaseCode;

/**
 * A tool to load the LexGrid SQL table from UMLS - using the CUI's as the
 * concept codes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 9222 $ checked in on $Date: 2008-06-12
 *          11:25:47 -0500 (Thu, 12 Jun 2008) $
 */
// TODO need to set some associations as navigable, etc.
public class MetaThesaurusToSQL extends UMLSBaseCode {
    int presentationCounter_ = 1;
    int codeCounter_ = 1;
    int definitionCounter_ = 1;
    int cuiCounter_ = 1;
    int semTypeCounter_ = 1;
    int propertyLinksCounter_ = 0;

    private String codingSchemeName_ = "NCI MetaThesaurus";
    private String registeredName_;
    private String relationsName_ = "Relations";

    // maintain load state ...
    private LRUMap alreadyLoadedAssociations = new LRUMap(1);
    private String lastAssociation;
    private HashSet loadedRelations_ = new HashSet();

    // To control tagging of associations that are contextually linked
    // via individual text representations (AUIs) rather than at
    // the concept level. We handle through qualification by HCD value
    // of the involved text properties and associations, so that
    // contextual chains are preserved while full-fledged relations
    // remain defined at the concept level.

    /**
     * Class to convert from UMLS to SQL.
     * 
     * @param sqlServer
     *            location of SQL server
     * @param sqlDriver
     *            SQL driver class
     * @param sqlUserName
     *            user name for server authentication
     * @param sqlPassword
     *            password for server authentication
     * @param umlsServer
     *            location of UMLS server
     * @param umlsDriver
     *            UMLS driver class
     * @param umlsUserName
     *            user name for server authentication
     * @param umlsPassword
     *            password for server authentication
     * @param loadPrefs
     *            loader peferences
     * @param manifestLocation
     *            manifest location
     * @param enforceIntegrity
     *            enforce foreign key constraints on tables
     * @param rootRecalcOnly
     *            recalculate root nodes only
     * @param director
     *            message director for log output
     * @throws Exception
     */
    public MetaThesaurusToSQL(String sqlServer, String sqlDriver, String sqlUserName, String sqlPassword,
            String tablePrefix, String umlsServer, String umlsDriver, String umlsUserName, String umlsPassword,
            LoaderPreferences loadPrefs, URI manifestLocation, boolean enforceIntegrity, boolean rootRecalcOnly,
            LgMessageDirectorIF director) throws Exception {
        log = LogManager.getLogger("convert.MetaThesaurusToSQL");
        messages_ = director;
        loadPrefs_ = loadPrefs;
        manifestLocation_ = manifestLocation;

        try {
            initIsoMap();
            // initSpecialCaseRelationPairs();
            initMRCONSOTTYMAP();

            makeConnections(umlsServer, umlsDriver, umlsUserName, umlsPassword, sqlServer, sqlDriver, sqlUserName,
                    sqlPassword, tablePrefix, enforceIntegrity);

            loadMRRANK();
            loadLoaderPreferences();

            if (!rootRecalcOnly)
                loadCodingScheme();
            else
                rebuildRootNodes();
            closeConnections();
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed...", e);
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

    /*
     * kick off the process of loading the coding scheme
     */
    private void loadCodingScheme() throws SQLException {
        // change Coding Scheme Name according to the manifest.
        // Coding Scheme Name is the only thing that needs to be
        // set 'pre-load', everything else can be set 'post-load'.

        codingSchemeName_ = getCodingSchemeNameFromManifest(codingSchemeName_);

        messages_.info("loading " + codingSchemeName_);

        String codingSchemeName = codingSchemeName_;
        String formalName = "NCI MetaThesaurus";
        String registeredName = getRegisteredNameFromManifest(getISOString(codingSchemeName));
        registeredName_ = registeredName;
        String defaultLanguage = "ENG";
        defaultLanguage_ = defaultLanguage;
        String representsVersion = getSourceVersionString("Unknown version of the MetaThesaurus");
        boolean isNative = true;
        int approxNumConcepts = 0;
        String entityDescription = "NCI MetaThesaurus loaded from RRF files.";
        String copyright = new StringBuffer(1024)
                .append(
                        "Some material in the NCI Metathesaurus is from copyrighted sources of the respective copyright claimants. ")
                .append("All sources appearing in the NCI Metathesaurus are licensed or authorized for NCI use.")
                .append("\n")
                .append(
                        "Users of the NCI Metathesaurus are responsible for compliance with the terms of these licenses ")
                .append(
                        "and with any copyright restrictions and are referred to NCI Center of Bioinformatics for license terms ")
                .append("and to the copyright notices appearing in the original sources, ").append(
                        "all of which are obtainable online by reference at ").append("http://ncimeta.nci.nih.gov/.")
                .toString();

        messages_.info("Cleaning tables");
        sqlTableUtility_.cleanTables(codingSchemeName);
        
        addToCodingScheme(codingSchemeName, registeredName, representsVersion, formalName, defaultLanguage_, 
                approxNumConcepts, null, 0, null, entityDescription, copyright);
        
        messages_.info("Loaded 1 coding scheme");

        loadCodingSchemeMultiAttributes(codingSchemeName);
        loadCodingSchemeSupportedAttributes(codingSchemeName);

        loadConcepts(codingSchemeName);
        
        // Put here becuase HCD needs to be loaded as a supported Qualifier
        // before relations are loaded
        supportedAssociationQualifiers_.add("HCD");
        supportedAssociationQualifiers_.add(SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP);
        
        loadRelations(codingSchemeName);

        messages_.info("Loaded " + propertyLinksCounter_ + " Property Links");

        // Inserted To load MHIER contexts
        messages_.info("Processing HCD-tagged MRHIER entries.");
        loadContexts(codingSchemeName);

        messages_.info("Adding the root relation node ...");

        // make sure that the required association parts exist
        if (!loadedRelations_.contains(codingSchemeName + relationsName_)) {
            messages_.info("Adding the relation to put all of the associations under");

            addRelationToRelations(codingSchemeName, relationsName_, new Boolean(isNative),
                    "parent child relationships");
        }

        buildRootNodes(codingSchemeName);
    }

    /**
     * Adds qualification to concepts and associations in the LexGrid
     * repository.
     * 
     * @param aq
     *            Qualification information from the UMLS source.
     * @param constructHCD
     *            Indicates whether artificial context values should be
     *            constructed if not provided in the UMLS information.
     * @param rela
     *            The relationship attribute defined by UMLS (can be empty or
     *            null).
     * @param totalCount
     *            The total number of context links qualified previously.
     * @return The number of contextual links qualified in the repository for
     *         the given UMLS info.
     * @throws SQLException
     */
    protected int loadContext(AssociationQualification aq, boolean constructHCD, String rela, int totalCount)
            throws SQLException {
        // If a context identifier was assigned, use it.
        // If a context identifier is not assigned and the option to construct
        // is enabled,
        // derive one based on the root concept code and path to root AUI
        // values.
        int contextLinks = 0;
        String hcd = aq.qualifierValue;
        if (constructHCD && StringUtils.isBlank(hcd) && StringUtils.isNotBlank(aq.pathToRoot)
                && StringUtils.isNotBlank(aq.sourceConceptCode)) {
            MessageDigest md = getSHA1();
            md.reset();
            md.update(aq.pathToRoot.getBytes());
            hcd = String.valueOf(md.digest(aq.sourceConceptCode.getBytes()));
        }
        if (StringUtils.isBlank(hcd))
            return 0;

        // Iterate through the path to root and determine the codes for
        // participating AUIs. We maintain a LRU cache of AUIs to codes to
        // assist.
        // If the associated code is not in the cache, find and cache it here.
        ListOrderedMap orderedPtrAUIToCode = new ListOrderedMap();

        // Break up the path to root into AUIs ...
        String[] auis = aq.pathToRoot.split("\\.");
        if (auis.length > 0) {
            // Check the cache for each. If not found, perform and cache the
            // AUI to code mapping.
            PreparedStatement getPTRCode = umlsConnection2_.prepareStatement("SELECT CUI FROM MRCONSO WHERE AUI = ?");
            try {
                String nextCode, nextAUI;
                for (int i = 0; i < auis.length; i++) {
                    // Check for registered code in the cache.
                    nextAUI = auis[i];
                    nextCode = (String) auiToCodeCache_.get(nextAUI);

                    // If not cached, perform lookup ...
                    if (nextCode == null) {
                        getPTRCode.setString(1, nextAUI);
                        ResultSet ptrCodes = getPTRCode.executeQuery();
                        int count = 0;
                        try {
                            while (ptrCodes.next()) {
                                count++;
                                nextCode = ptrCodes.getString(1);
                            }
                        } finally {
                            ptrCodes.close();
                        }
                        // If one to one mapping (probably should always be, but
                        // doesn't
                        // hurt to check), add to the cache for quick lookup
                        // later...
                        if (count == 1)
                            auiToCodeCache_.put(nextAUI, nextCode);
                    }

                    // Was it resolved?
                    if (nextCode != null)
                        orderedPtrAUIToCode.put(nextAUI, nextCode);
                }
            } finally {
                getPTRCode.close();
            }
        }
        // Ensure we have included the original AUI to code mapping from the
        // provided UMLS qualification info; inserted last as the root
        // of the path.
        orderedPtrAUIToCode.put(aq.sourceConceptAUI, aq.sourceConceptCode);

        // /////////////////////////////////////////////////////////////////////
        // We have all the participating codes and AUIs.
        // Add context qualifiers to the text presentation of each concept
        // based on code/AUI pairs.
        // /////////////////////////////////////////////////////////////////////
        for (OrderedMapIterator omi = orderedPtrAUIToCode.orderedMapIterator(); omi.hasNext();) {
            omi.next();
            String aui = (String) omi.getKey();
            String code = (String) omi.getValue();
            if (code != null)
                qualifyConceptPresentation(code, aui, aq.codingSchemeName, aq.qualifierName, hcd);
        }

        // /////////////////////////////////////////////////////////////////////
        // At this point we have taken care of all the concept qualifiers.
        // Now find and similarly tag each participating association link
        // between AUIs in the path to root chain.
        // /////////////////////////////////////////////////////////////////////

        // Statements to find LexGrid association to concept mappings.
        // Check source to target (parent association as processed)
        // or target to source (child association as processed).

        // Honor the association specified in the MRHIER entry, if provided.
        // For example, the UMLS 'inverse_isa' is mapped on load to 'hasSubtype'
        // association name; account for that here.
        String assoc = mapRela(rela);

        // If a specific relation attribute (rela) was not provided, consider
        // all relevant
        // hierarchical associations (including UMLS standard or source-specific
        // names).
        String assocParam = StringUtils.isNotBlank(assoc) ? '\'' + assoc + '\''
                : toCommaDelimitedWithQuotes(getHierAssocNames(aq.codingSchemeName));

        // Create statements to navigate both directions (up & down the
        // contextual chain).
        PreparedStatement getRelationship_1 = sqlConnection_.prepareStatement(new StringBuffer("SELECT "
                + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + ", " + stc_.targetEntityCodeOrId + ", "
                + stc_.entityCodeOrAssociationId + ", " + stc_.sourceEntityCodeOrId + " FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " WHERE " + stc_.sourceEntityCodeOrId + " = ? AND " + stc_.targetEntityCodeOrId + " = ? AND ").append(
                stc_.codingSchemeNameOrId + " = ? AND " + stc_.entityCodeOrAssociationId + " IN (").append(assocParam)
                .append(")").toString());

        PreparedStatement getRelationship_2 = sqlConnection_.prepareStatement(new StringBuffer("SELECT "
                + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + ", " + stc_.targetEntityCodeOrId + ", "
                + stc_.entityCodeOrAssociationId + ", " + stc_.sourceEntityCodeOrId + " FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " WHERE " + stc_.targetEntityCodeOrId + " = ? AND " + stc_.sourceEntityCodeOrId + " = ? AND ").append(
                stc_.codingSchemeNameOrId + " = ? AND " + stc_.entityCodeOrAssociationId + " IN (").append(assocParam)
                .append(")").toString());

        // Statement to update a multi-attributes key for an association
        // mapping.
        PreparedStatement updateMAK = sqlConnection_.prepareStatement(new StringBuffer("UPDATE ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " SET " + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + " = ? " + " WHERE " + stc_.codingSchemeNameOrId
                        + " = ?").append(
                " AND " + stc_.sourceEntityCodeOrId + " = ? AND " + stc_.targetEntityCodeOrId + " = ?").append(
                " AND " + stc_.entityCodeOrAssociationId + " = ?").toString());

        // Locate and qualify each affected association link with the context ID
        // ...
        try {
            PreparedStatement[] stmts = new PreparedStatement[] { getRelationship_1, getRelationship_2 };
            for (int s = 0; s < stmts.length; s++) {
                PreparedStatement stmt = stmts[s];
                for (int i = orderedPtrAUIToCode.size() - 1; i > 0; i--) {
                    String code = (String) orderedPtrAUIToCode.getValue(i);
                    String codePrev = (String) orderedPtrAUIToCode.getValue(i - 1);
                    stmt.setString(1, code);
                    stmt.setString(2, codePrev);
                    stmt.setString(3, aq.codingSchemeName);

                    ResultSet results = stmt.executeQuery();
                    try {
                        // Iterate through all relevant association links ...
                        while (results.next()) {
                            String multiAttributesKey = results.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                            String targetConceptCode = results.getString(stc_.targetEntityCodeOrId);
                            String sourceConceptCode = results.getString(stc_.sourceEntityCodeOrId);
                            String association = results.getString(stc_.entityCodeOrAssociationId);

                            // If there is no key to correlate to the
                            // multi-attributes table,
                            // construct and add now.
                            if (multiAttributesKey == null) {
                                StringBuffer key = new StringBuffer().append(System.currentTimeMillis()).append(
                                        (int) Math.floor((Math.random() * 100000))).append(totalCount);
                                multiAttributesKey = key.substring(0, Math.min(50, key.length()));
                                updateMAK.setString(1, multiAttributesKey);
                                updateMAK.setString(2, aq.codingSchemeName);
                                updateMAK.setString(3, sourceConceptCode);
                                updateMAK.setString(4, targetConceptCode);
                                updateMAK.setString(5, association);
                                updateMAK.execute();
                            }

                            // Add a context qualifier to the multi-attributes
                            // table.
                            try {
                                addEntityAssociationQualifierToEntityAssociation(aq.codingSchemeName,
                                        multiAttributesKey, aq.qualifierName, hcd);
                                contextLinks++;
                            } catch (SQLException e) {
                                // Because we qualify all relationships along
                                // the PTR and
                                // the HCD is identical for siblings at the same
                                // PTR some
                                // exceptions with regards to identical keys
                                // will come up.

                                // We try to bypass altogether if the message
                                // indicates duplication.
                                // However, message text can vary based on the
                                // database engine.
                                // Rather than exit in error, log the message
                                // and continue.
                                if (!e.getMessage().contains("Duplicate")) {
                                    messages_.warn("Unable to add context qualifier to association.", e);
                                }
                            }
                        }
                    } finally {
                        results.close();
                    }
                }
            }
        } finally {
            updateMAK.close();
            getRelationship_1.close();
            getRelationship_2.close();
        }
        return contextLinks;
    }

    /*
     * Iterate MRHIER for context information to be added as Association
     * Qualifications
     */
    private void loadContexts(String codingSchemeName) throws SQLException {
        /*
         * Evaluate hierarchy processing.
         */

        boolean constructHCD = false;

        /*
         * Create a temporary view (that will be destroyed at the end of the
         * method) to speed up the queries in this method
         */
        try {
            messages_.info("loading contexts - getting a total count");

            String getCodingSchemeInfoSQL = "SELECT COUNT(*) as cnt FROM MRHIER";
            if (!constructHCD) {
                getCodingSchemeInfoSQL = getCodingSchemeInfoSQL + " WHERE HCD != ? ";
            }

            PreparedStatement getCodingSchemeInfo = umlsConnection2_.prepareStatement(umlsSqlModifier_
                    .modifySQL(getCodingSchemeInfoSQL));
            ResultSet results = null;

            // this is weird, but its the easiest way to get the
            // appropriate quotes around NULL
            if (!constructHCD)
                getCodingSchemeInfo.setString(1, "NULL");
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
                    + " MRHIER.SAB, MRHIER.RELA, MRHIER.CXN " + " FROM MRHIER "; // +
            if (!constructHCD) {
                getCodingSchemeInfoSQL = getCodingSchemeInfoSQL +

                " WHERE HCD != ? ";
            }
            getCodingSchemeInfoSQL = getCodingSchemeInfoSQL + " ORDER BY {BINARY} MRHIER.CUI {LIMIT}";
            getCodingSchemeInfo = umlsConnection2_.prepareStatement(umlsSqlModifier_.modifySQL(getCodingSchemeInfoSQL));

            // Count var usage to make sure things are placed correctly
            int boundVar = 1;
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
                            aq.sourceConceptCode = cui;
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

    /*
     * load the codingSchemeMultiAttributes table
     */
    private void loadCodingSchemeMultiAttributes(String codingSchemeName) throws SQLException {
        messages_.info("loading coding scheme multi attributes");
        // things like localName - source (which has a locatorInfo)

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_SOURCE);
        insertIntoCodingSchemeMultiAttributes.setString(3, "RRF Files");
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        String iso = getISOString(codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, iso);
        insertIntoCodingSchemeMultiAttributes.setString(4, "");
        insertIntoCodingSchemeMultiAttributes.setString(5, "");

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, "10001");
        if (stc_.supports2009Model()) {
            insertIntoCodingSchemeMultiAttributes.setString(4, "");
            insertIntoCodingSchemeMultiAttributes.setString(5, "");
        }

        insertIntoCodingSchemeMultiAttributes.executeUpdate();

        insertIntoCodingSchemeMultiAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeMultiAttributes.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
        insertIntoCodingSchemeMultiAttributes.setString(3, "NCI_MetaThesaurus");
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
    private void loadCodingSchemeSupportedAttributes(String codingSchemeName) throws SQLException {
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

        // populate the supported coding schemes, now that we know what they
        // are.
        String urn = registeredName_;
        insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME,
                codingSchemeName, urn, null, null, null);

        messages_.info("Loaded 1 supported " + SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);

        // Source
        // later

        // association
        populateSupportedAssociations();
        Set loadedNames = new HashSet();
        for (int i = 0; i < supportedAssociations_.length; i++) {
            // Note: Registered association names can be duplicated
            // in meta if defined for more than one source ontology.
            String name = supportedAssociations_[i].name;
            if (!loadedNames.contains(name)) {
                insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                        SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION, (supportedAssociations_[i]).name, null, null,
                        null, null);
                loadedNames.add(name);
            }
        }
        messages_.info("added " + supportedAssociations_.length + SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);

        // representationalForm - these are for tty codes in mrconso
        int i = 0;
        Enumeration values = mrconsoRepresentationalMap_.elements();
        while (values.hasMoreElements()) {
            String var = (String) values.nextElement();
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM, var, null, null, null, null);
            i++;
        }
        messages_.info("Loaded " + i + " " + SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM);
        
        loadDefaultSupportedNamespace(codingSchemeName);
    }

    /*
     * load the concepts for a coding scheme
     */
    private void loadConcepts(String codingSchemeName) throws SQLException {
        messages_.info("loading concepts - getting a total count");

        PreparedStatement getCodingSchemeInfo = umlsConnection2_
                .prepareStatement("SELECT  COUNT(*) as cnt FROM MRCONSO");

        ResultSet results = getCodingSchemeInfo.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getCodingSchemeInfo.close();

        int start = 0;
        String lastCui = null;
        ArrayList conceptPresentations = new ArrayList();
        int codeCount = 0;
        int rowCount = 0;

        // collect them all into batches of concept codes. when the code
        // changes, load that code
        while (start < total) {
            // mysql still can't seem to stream results properly, so I'm going
            // to batch them.
            messages_.info("Fetching a batch of results");
            getCodingSchemeInfo = umlsConnection2_
                    .prepareStatement(umlsSqlModifier_
                            .modifySQL("SELECT  LAT, CODE, AUI, CUI, TS, STT, ISPREF, STR, TTY, SAB FROM MRCONSO ORDER BY {BINARY} CUI {LIMIT}"));

            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (umlsSqlModifier_.getDatabaseType().equals("MySQL")) {
                getCodingSchemeInfo.setInt(1, start);
                getCodingSchemeInfo.setInt(2, batchSize);
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

                if (lastCui != null && !temp.cui.equals(lastCui)) {
                    loadConcept(codingSchemeName, (ConceptPresentation[]) conceptPresentations
                            .toArray(new ConceptPresentation[conceptPresentations.size()]));

                    conceptPresentations.clear();
                    presentationCounter_ = 1;
                    codeCounter_ = 1;
                    definitionCounter_ = 1;
                    cuiCounter_ = 1;
                    semTypeCounter_ = 1;

                    if (codeCount % 100 == 0) {
                        messages_.busy();
                    }

                    codeCount++;
                    if (codeCount % 10000 == 0) {
                        messages_.info("On row " + rowCount + " out of " + total + " rows - found " + codeCount
                                + " concepts");
                    }
                }

                temp.language = results.getString("LAT");

                // add for populating the supportedLanguages later
                supportedLanguages_.add(temp.language);

                temp.presentationFormat = null;
                temp.TTY = results.getString("TTY");
                temp.representationForm = temp.TTY;
                temp.value = results.getString("STR");
                temp.TS = results.getString("TS");
                temp.STT = results.getString("STT");
                temp.isPreferred = new Boolean(false);
                temp.source = results.getString("SAB");
                temp.ISPREF = results.getString("ISPREF");
                temp.AUI = results.getString("AUI");

                // See if there is a "better" value for the representationalForm
                // mapping
                String repFormMap = (String) mrconsoRepresentationalMap_.get(temp.representationForm);
                if (repFormMap != null) {
                    temp.representationForm = repFormMap;
                }

                lastCui = temp.cui;
                conceptPresentations.add(temp);
            }
            results.close();
            getCodingSchemeInfo.close();
        }

        // load the last one
        if (lastCui != null) {
            // need to add this, just once - assuming there were codes, so this
            // is a good place for it.
            supportedPropertyTypes_.add(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
            loadConcept(codingSchemeName, (ConceptPresentation[]) conceptPresentations
                    .toArray(new ConceptPresentation[conceptPresentations.size()]));

            codeCount++;
        }
        
        // we are done with inserting entities, close the preparedStatement.
        insertIntoEntities.close();        
        insertIntoEntityType.close();
        
        messages_.info("Loaded " + codeCount + " concepts from  " + rowCount + " rows");

        supportedPropertyQualifiers_.add("source-code");
        supportedPropertyQualifiers_.add("AUI");

        supportedPropertyQualifiers_.add("HCD");

        updateApproxNumberOfConcepts(codeCount, codingSchemeName);
        loadSupportedProperties(codingSchemeName);
        loadSupportedPropertyQualifiers(codingSchemeName);
        loadSupportedLanguages(codingSchemeName);
        loadSupportedSources(codingSchemeName);
    }

    /*
     * 
     * Helper method to load all the properties and presentations of a concept
     */
    private void loadConcept(String codingSchemeName, ConceptPresentation[] conceptPresentations) throws SQLException {
        // order them - this will organize them by language, and put the "best"
        // presentation
        // first for each language. default language will be the at the very
        // top.
        Arrays.sort(conceptPresentations, new ConceptPresentationSorter(true));

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
            addConceptToConcepts(codingSchemeName, conceptPresentations[0].cui, null, null, null, new Boolean(true),
                    null, new Boolean(false), SQLTableConstants.TBLCOLVAL_STATUS_ACTIVE, new Boolean(false),
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

        // load its properties
        for (int i = 0; i < conceptPresentations.length; i++) {
            // put in the presentation
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, conceptPresentations[i].cui,
                    "T-" + presentationCounter_, SQLTableConstants.TBLCOLVAL_PRESENTATION,
                    SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION, conceptPresentations[i].language,
                    conceptPresentations[i].presentationFormat, conceptPresentations[i].isPreferred, null, null,
                    conceptPresentations[i].representationForm, conceptPresentations[i].value);

            addConceptToEntityPropertyMultiAttributes(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                    conceptPresentations[i].cui, "T-" + presentationCounter_, SQLTableConstants.TBLCOLVAL_SOURCE,
                    conceptPresentations[i].source, "", "");

            // add this source to the source list in use...
            supportedSources_.add(conceptPresentations[i].source);

            if (conceptPresentations[i].conceptCode != null && conceptPresentations[i].conceptCode.length() > 0) {
                addConceptToEntityPropertyMultiAttributes(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                        conceptPresentations[i].cui, "T-" + presentationCounter_,
                        SQLTableConstants.TBLCOLVAL_QUALIFIER, "source-code", conceptPresentations[i].conceptCode, "");

                addConceptToEntityPropertyMultiAttributes(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                        conceptPresentations[i].cui, "T-" + presentationCounter_,
                        SQLTableConstants.TBLCOLVAL_QUALIFIER, "AUI", conceptPresentations[i].AUI, "");

            }

            presentationCounter_++;
        }

        // load the definitions
        try {
            addToDefinitions(conceptPresentations[0].cui, codingSchemeName);
        } catch (SQLException e) {
            // recover from an error loading definitions
            log.error("Problem loading definitions for code " + conceptPresentations[0].cui, e);
            messages_.info("ERROR - Problem inserting new code " + conceptPresentations[0].cui);

        }

        // load the other properties
        loadOtherProperties(conceptPresentations[0].cui, codingSchemeName);

    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    private void loadSupportedCodingSchemes(String codingSchemeName) throws SQLException {
        messages_.info("Adding the Supported Coding Schemes.");
        Iterator iter = supportedCodingSchemes_.iterator();

        int count = 0;
        while (iter.hasNext()) {
            String value = (String) iter.next();
            count++;
            // populate the supported coding schemes, now that we know what they
            // are.
            String urn = getISOString(value);
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME, value, urn, null, null, null);
        }

        messages_.info("Added " + count + "  Supported " + SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);

    }

    /*
     * Load the definitions for a concept
     */
    private void addToDefinitions(String cui, String codingSchemeName) throws SQLException {

        PreparedStatement getDefinitions = umlsConnection_.prepareStatement("SELECT DEF, SAB FROM MRDEF WHERE CUI = ?");

        getDefinitions.setString(1, cui);

        ResultSet definitions = getDefinitions.executeQuery();
        try {
            boolean isPreferred = true;
            while (definitions.next()) {
                supportedPropertyTypes_.add(SQLTableConstants.TBLCOLVAL_DEFINITION);
                addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, cui, "D-"
                        + definitionCounter_, SQLTableConstants.TBLCOLVAL_DEFINITION,
                        SQLTableConstants.TBLCOLVAL_DEFINITION, null, null, new Boolean(isPreferred), null, null, null,
                        definitions.getString("DEF"));
                isPreferred = false;
                // umls doesn't seem to have the notion of a preferred
                // definition, so I'll just set the first one to
                // preferred.

                addConceptToEntityPropertyMultiAttributes(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, cui,
                        "D-" + definitionCounter_++, SQLTableConstants.TBLCOLVAL_SOURCE, definitions.getString("SAB"),
                        "", "");
            }
        } finally {
            definitions.close();
            getDefinitions.close();
        }

    }

    /*
     * Load other properties - just semantic types now.
     */
    private void loadOtherProperties(String cui, String codingSchemeName) throws SQLException {
        // get the semantic type(s)
        supportedPropertyTypes_.add("Semantic_Type");
        PreparedStatement getSemTypes = umlsConnection_.prepareStatement("SELECT STY FROM MRSTY WHERE CUI = ?");
        getSemTypes.setString(1, cui);

        ResultSet semTypes = getSemTypes.executeQuery();
        while (semTypes.next()) {
            addToEntityProperty(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, cui, "SemType-"
                    + semTypeCounter_++, SQLTableConstants.TBLCOLVAL_PROPERTY, "Semantic_Type", null, null, null, null,
                    null, null, semTypes.getString("STY"));
        }
        semTypes.close();
        getSemTypes.close();
    }

    public static void main(String[] args) throws Exception {
        // Logger.getRootLogger().setLevel(Level.OFF);
        // new MetaThesaurusToSQL("jdbc:postgresql://mir04/LexGrid2",
        // "org.postgresql.Driver", "mirpub", "mirpub",
        // "jdbc:mysql://mir04/MetaThesaurus", "org.gjt.mm.mysql.Driver",
        // "mirpub", "mirpub", "NCI MetaThesaurus",
        // true, new CommandLineMessageDirector());
    }

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return this.codingSchemeName_;
    }

    // //////////////////////////////////////////////////////////////
    // Code for tracking associations used in each container,
    // root nodes, and hierarchical navigation ...
    // //////////////////////////////////////////////////////////////

    /**
     * Inner class to track keyed references to source qualifiers.
     */
    protected class QualifierHolder {
        String multiKey = null;
        Vector<String> sources = new Vector<String>();
        Vector<String> roleGroups = new Vector<String>();

        protected QualifierHolder(String key) {
            multiKey = key;
        }
    }

    /**
     * Add a code to code association.
     * 
     * @param currentCodingScheme
     * @param sourceCode
     * @param association
     * @param targetCode
     * @param sourceOfAssociation
     * @param forwardName
     * @param reverseName
     * @param count
     * @return true if the association was added; false if an error occurred or
     *         the association was already defined.
     * @throws SQLException
     */
    protected boolean addConceptAssociationToConceptsHelper(String currentCodingScheme, String sourceCode,
            String association, String targetCode, String sourceOfAssociation, String roleGroup, String forwardName, String reverseName,
            int count) throws SQLException {
        String relation = relationsName_;
        String relationDescription = "MetaThesaurus Relationships";

        // Register the higher level relations container if not present ...
        if (!loadedRelations_.contains(currentCodingScheme + relation)) {
            messages_.info("Adding the relation to put all of the associations under.");
            addRelationToRelations(currentCodingScheme, relation, Boolean.TRUE, relationDescription);
            loadedRelations_.add(currentCodingScheme + relation);
        }

        // Register the higher level association if not present ...
        if (!isLoaded(association, currentCodingScheme, relation)) {
            messages_.info("Adding the association to put sources and targets under.");
            boolean navigable = true;
            Boolean transitive = new Boolean(isHierarchicalName(forwardName, currentCodingScheme)
                    || isHierarchicalName(reverseName, currentCodingScheme));
            addAssociationToAssociations(currentCodingScheme, relation, association, forwardName, reverseName, "",
                    new Boolean(navigable), transitive, null, null, null, null, null, null, null, null, null,
                    "MetaThesaurus Associations");
            markLoaded(association, currentCodingScheme, relation);
        }

        // Finally, register the instance of the association (concept source to
        // target) ...
        // Return value - default indicates no association was loaded.
        boolean isAdded = false;
        boolean isDuplicate = false;

        // The cache key for this association.
        // Note: no need for source or target coding scheme names in here -
        // always NCI MetaThesaurus.
        // Shouldn't need the association name either, since we go through them
        // in a known order (per SAB).
        String assocKey = sourceCode + ":" + targetCode;

        // The key used to map between association and association qualifiers.
        String multiKey = null;
        QualifierHolder qHolder = (QualifierHolder) alreadyLoadedAssociations.get(assocKey);

        // Check the cache... already loaded this one? If so, move on to
        // qualification.
        if (qHolder != null)
            multiKey = qHolder.multiKey;
        else {
            // Note: LRU cache helps avoid duplicates, but is not a guarantee
            // given current order of processing of items and associations per
            // source (if same source/target/assoc combo exists for two
            // separate SABs, the cache entry may be dropped between
            // references).

            // Try optimistic insert. On error, check for duplicate and reuse
            // multiAttributesKey if present. If not, throw the original err.
            try {
                multiKey = generateUniqueKey(new String[] { currentCodingScheme, currentCodingScheme, sourceCode,
                        relation, association, currentCodingScheme, targetCode });
                addEntityAssociationToEntity(currentCodingScheme, currentCodingScheme,
                        SQLTableConstants.ENTITYTYPE_CONCEPT, sourceCode, relation, association, currentCodingScheme,
                        SQLTableConstants.ENTITYTYPE_CONCEPT, targetCode, multiKey, null, null);
                isAdded = true;
            } catch (SQLException sqle) {
                // Check for existing association (same link may be defined
                // for multiple sources in meta, etc. If found, extract the
                // existing multi-attribute key for reuse to add qualifiers.
                ResultSet rs = null;
                getAssocInstance_.setString(1, currentCodingScheme);
                getAssocInstance_.setString(2, relation);
                getAssocInstance_.setString(3, association);
                getAssocInstance_.setString(4, currentCodingScheme);
                getAssocInstance_.setString(5, sourceCode);
                getAssocInstance_.setString(6, currentCodingScheme);
                getAssocInstance_.setString(7, targetCode);
                multiKey = null;
                try {
                    rs = getAssocInstance_.executeQuery();
                    if (rs.next()) {
                        multiKey = rs.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                        isDuplicate = true;
                    } else {
                        throw sqle;
                    }
                } catch (Exception e) {
                    log.warn("Association instance not inserted.", sqle);
                } finally {
                    if (rs != null)
                        rs.close();
                }
            }
        }

        // Ensure registration of source qualifier, and cache for
        // future reference. Here we have no alternate path to follow
        // if an error occurs and the qualifier does or does not exist.
        // The load will not be terminated on failure to load a
        // qualifier. On error, log a warning and continue.
        if ((isAdded || isDuplicate) && multiKey != null && StringUtils.isNotBlank(sourceOfAssociation)) {
            if (qHolder == null) {
                qHolder = new QualifierHolder(multiKey);
                alreadyLoadedAssociations.put(assocKey, qHolder);
            }
            if (!qHolder.sources.contains(sourceOfAssociation)) {
                qHolder.sources.add(sourceOfAssociation);
                try {
                    addEntityAssociationQualifierToEntityAssociation(currentCodingScheme, multiKey,
                            sourceOfAssociation, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                } catch (SQLException sqle) {
                    ResultSet rs2 = null;
                    getAssocQualifier_.setString(1, currentCodingScheme);
                    getAssocQualifier_.setString(2, multiKey);
                    getAssocQualifier_.setString(3, sourceOfAssociation);
                    getAssocQualifier_.setString(4, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                    try {
                        rs2 = getAssocQualifier_.executeQuery();
                        if (!rs2.next())
                            throw sqle;
                    } catch (Exception e) {
                        log.warn("Association qualifier not inserted.", sqle);
                    } finally {
                        if (rs2 != null)
                            rs2.close();
                    }
                }
            }
        }
        
        // Ensure registration of roleGroup qualifier, and cache for
        // future reference. Here we have no alternate path to follow
        // if an error occurs and the qualifier does or does not exist.
        // The load will not be terminated on failure to load a
        // qualifier. On error, log a warning and continue.
        if ((isAdded || isDuplicate) && multiKey != null && StringUtils.isNotBlank(roleGroup)) {
            if (qHolder == null) {
                qHolder = new QualifierHolder(multiKey);
                alreadyLoadedAssociations.put(assocKey, qHolder);
            }
            if (!qHolder.roleGroups.contains(roleGroup)) {
                qHolder.roleGroups.add(roleGroup);
                try {
                    addEntityAssociationQualifierToEntityAssociation(currentCodingScheme, multiKey,
                            SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP, roleGroup);
                } catch (SQLException sqle) {
                    ResultSet rs2 = null;
                    getAssocQualifier_.setString(1, currentCodingScheme);
                    getAssocQualifier_.setString(2, multiKey);
                    getAssocQualifier_.setString(3, SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP);
                    getAssocQualifier_.setString(4, roleGroup);
                    try {
                        rs2 = getAssocQualifier_.executeQuery();
                        if (!rs2.next()) {
                            throw sqle;
                        }
                    } catch (Exception e) {
                        log.warn("Association qualifier not inserted.", sqle);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                    }
                }
            }
        }
        return isAdded;
    }

    /**
     * Add context qualifiers to text presentations for matching concepts
     * (Overrides UMLSBAaseCode method).
     * 
     * @param code
     *            The code of the concept to qualify.
     * @param aui
     *            The AUI identifying specific context text to be matched.
     * @param codingSchemeName
     *            The coding scheme container the code to be qualified.
     * @param qualifierName
     *            The qualifier name (e.g. 'HCD').
     * @param qualifierValue
     *            The qualifier value.
     * @throws SQLException
     */
    protected void qualifyConceptPresentation(String code, String aui, String codingSchemeName, String qualifierName,
            String qualifierValue) throws SQLException {
        // /////////////////////////////////////////////////////////////////////
        // Note: Separate the RRF and LexGrid queries into separate SQL since
        // content for each may be stored in separate databases (multi-db mode).
        // Syntax (and ability) to query across databases varies by database
        // engine.
        // /////////////////////////////////////////////////////////////////////

        // Locate matching root entry in MRCONSO ...
        PreparedStatement getMRCONSO = umlsConnection2_
                .prepareStatement("SELECT STR, TTY, LAT from MRCONSO WHERE MRCONSO.CUI = ? AND MRCONSO.AUI = ?");
        getMRCONSO.setString(1, code);
        getMRCONSO.setString(2, aui);
        try {
            // For each, find matching concept properties (text presentations)
            // and add
            // a property qualifier. Note: multiple qualifiers can exist per
            // term.
            // Note: Oracle was pretty finicky about how we defined the schema
            // to allow
            // for very large property values. Unfortunately it also does not
            // allow
            // query over these values (stored as CLOBS) without use of the
            // DBMS_LOB
            // package functions, and there it appeared we could not do better
            // than a
            // 'startsWith' match. For now, we strike a compromise by allowing
            // the
            // query to be narrower for non-Oracle, but supporting Oracle by
            // iterating
            // through the returned presentations to find a match.
            boolean narrowQuery = !sqlModifier_.getDatabaseType().equals("Oracle");
            StringBuffer stmt = new StringBuffer("select " + SQLTableConstants.TBLCOL_PROPERTYID + ", "
                    + SQLTableConstants.TBLCOL_PROPERTYVALUE + " from ").append(
                    stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY)).append(
                    " WHERE " + stc_.codingSchemeNameOrId + " = '" + codingSchemeName_ + "' AND "
                            + stc_.entityCodeOrEntityId + " = ? AND " + SQLTableConstants.TBLCOL_PROPERTYTYPE
                            + " = '" + SQLTableConstants.TBLCOLVAL_PRESENTATION + "' AND "
                            + SQLTableConstants.TBLCOL_REPRESENTATIONALFORM + " = ? AND "
                            + SQLTableConstants.TBLCOL_LANGUAGE + " = ?");
            if (narrowQuery)
                stmt.append(" AND " + SQLTableConstants.TBLCOL_PROPERTYVALUE + " = ? ");

            PreparedStatement getPropIds = sqlConnection_.prepareStatement(stmt.toString());

            ResultSet mrconso_results = getMRCONSO.executeQuery();
            try {
                while (mrconso_results.next()) {
                    String textToMatch = mrconso_results.getString(1);
                    String representationalForm = mrconso_results.getString(2);
                    String language = mrconso_results.getString(3);

                    getPropIds.setString(1, code);
                    getPropIds.setString(2, representationalForm);
                    getPropIds.setString(3, language);

                    if (narrowQuery) {
                        getPropIds.setString(4, textToMatch);
                    }

                    ResultSet propId_results = getPropIds.executeQuery();
                    try {
                        while (propId_results.next()) {
                            if (narrowQuery || textToMatch.equals(propId_results.getString(2))) {
                                addConceptToEntityPropertyMultiAttributes(codingSchemeName,
                                        SQLTableConstants.ENTITYTYPE_CONCEPT, code, propId_results.getString(1),
                                        SQLTableConstants.TBLCOLVAL_QUALIFIER, qualifierName, qualifierValue, "");
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        // This happens because it tries to enter the qualifier
                        // and it is already in the database.
                        // The qualifier was loaded from a previous association.
                        // No need to do anything.
                    } finally {
                        propId_results.close();
                    }
                }
            } finally {
                getPropIds.close();
            }
        } finally {
            getMRCONSO.close();
        }
    }

    /**
     * Gets the Presentation ID (for example, T1) given a specific AUI, Code,
     * and Coding Scheme Name first level nodes in hierarchical associations.
     * 
     * @param codingSchemeName
     *            The LexGrid code system name.
     * @param code
     *            The code (CUI) to look for
     * @param aui
     *            The AUI within the given code
     * @return String The Presentation ID
     * 
     */
    protected String getPresentationFromAUI(String codingSchemeName, String code, String aui) throws SQLException {
        getPropIds_.setString(1, codingSchemeName);
        getPropIds_.setString(2, code);
        getPropIds_.setString(3, aui);

        ResultSet propId_results = getPropIds_.executeQuery();
        try {
            while (propId_results.next()) {
                return propId_results.getString(1);
            }
            return null;
        } catch (SQLException e) {
            messages_.warn("Error getting Source and Id information from Presentation.");
            log.warn("Error getting Source and Id information from Presentation.");
            throw e;
        } finally {
            try {          
                if (propId_results != null) {
                    propId_results.close();
                }
            } catch (SQLException e) {
                messages_.info("Error closing SQL connection.");
                log.info("Error closing SQL connection.");
            }
        }
    }

    private HashMap<String, String> getSourceCodeAndPresentationIDsFromCUI(String cui) throws SQLException {

        StringBuffer stmtSource = new StringBuffer("select " + SQLTableConstants.TBLCOL_PROPERTYID + ", "
                + SQLTableConstants.TBLCOL_VAL1 + " from ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES)).append(
                " where " + SQLTableConstants.TBLCOL_CODINGSCHEMEID + "= '" + codingSchemeName_ + "' AND "
                        + stc_.entityCodeOrEntityId + " = ? AND " + SQLTableConstants.TBLCOL_TYPENAME + " = '"
                        + SQLTableConstants.TBLCOLVAL_QUALIFIER + "' AND " + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                        + " = 'source-code'");

        PreparedStatement getPossibleIds = null;
        ResultSet getPossibleIds_results = null;
        HashMap<String, String> propIdToSource = new HashMap();
        try {
            getPossibleIds = sqlConnection_.prepareStatement(stmtSource.toString());

            getPossibleIds.setString(1, cui);
            getPossibleIds_results = getPossibleIds.executeQuery();

            while (getPossibleIds_results.next()) {
                String propId = getPossibleIds_results.getString(SQLTableConstants.TBLCOL_PROPERTYID);
                String sourceValue = getPossibleIds_results.getString(SQLTableConstants.TBLCOL_VAL1);
                propIdToSource.put(propId, sourceValue);
            }
            getPossibleIds_results.close();
            getPossibleIds.close();

            return propIdToSource;
        } catch (SQLException e) {
            messages_.warn("Error getting Source and Id information from Presentation.");
            log.warn("Error getting Source and Id information from Presentation.");
            throw new SQLException(e.getMessage(), e.getSQLState());
        } finally {
            try {
                if (getPossibleIds_results != null) {
                    getPossibleIds_results.close();
                }
                if (getPossibleIds != null) {
                    getPossibleIds.close();
                }
            } catch (SQLException e) {
                messages_.info("Error closing SQL connection.");
                log.info("Error closing SQL connection.");

                // Just a SQL Housekeeping error -- return our result anyway.
                return propIdToSource;
            }
        }
    }

    /**
     * Build navigable relations from special endpoint nodes '@' and '@@' to
     * first level nodes in hierarchical associations.
     * 
     * @param codingSchemeName
     *            The LexGrid code system name.
     */
    protected void buildRootNodes(String codingSchemeName) throws SQLException {
        // Calculate for the source relations container ...
        SABString[] names = getHierRelas();
        for (int i = 0; i < names.length; i++) {
            SABString name = names[i];
            if (isLoaded(name.str, codingSchemeName, relationsName_)) {
                buildRootNode(codingSchemeName, true, new SABString[] { name }, relationsName_);
            }
        }

        // Calculate for standard umls relations ...
        names = getHierRels();
        for (int i = 0; i < names.length; i++) {
            SABString name = names[i];
            if (isLoaded(name.str, codingSchemeName, relationsName_)) {
                buildRootNode(codingSchemeName, true, new SABString[] { name }, relationsName_);
            }
        }
    }

    /**
     * Returns the names of the hierarchical associations in use for the LexGrid
     * code system of the given name.
     * 
     * @param codeSystemName
     * @return String[]
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected String[] getHierAssocNames(String codeSystemName) throws SQLException {
        SABString[] sab = getHierRels();
        SABString[] sab2 = getHierRelas();

        int count = getHierRels().length;
        int count2 = getHierRelas().length;

        String[] combinedString = new String[count + count2];

        for (int i = 0; i < count; i++) {
            combinedString[i] = sab[i].str;
        }
        int counter = 0;
        for (int i = count; i < count + count2; i++) {
            combinedString[i] = sab2[counter].str;
            counter++;
        }
        return combinedString;
    }

    /**
     * High level method with responsibility to map relationship content from
     * UMLS RRF-based format to LexGrid model.
     * 
     * @param codingSchemeName
     * @throws SQLException
     */
    protected void loadRelations(String codingSchemeName) throws SQLException {
        // Sort the associations, loading RELA's first and keeping
        // primary names (potentially qualified by different SABs)
        // together.
        Arrays.sort(supportedAssociations_, new AssociationSorter());

        for (int i = 0; i < supportedAssociations_.length; i++) {
            Association assoc = (Association) supportedAssociations_[i];
            if (lastAssociation == null || !lastAssociation.equals(assoc.name)) {
                // Moving on to a new association name.
                lastAssociation = assoc.name;
                // Reset the map used to track source/target combinations
                // loaded.
                // 100,000 should be more than big enough for the other
                // associations,
                // items are ordered by source code.
                alreadyLoadedAssociations.clear();
                alreadyLoadedAssociations = new LRUMap(100000);
            }

            int count = loadRelationsHelper(assoc, codingSchemeName);
            if (count == 0) {
                messages_.info("No relations were found for " + assoc.toShortString());
                log.warn("No relations were found for " + assoc.toShortString());
            }
        }

        // Now we know all the supported coding schemes (some may have been
        // identified as source or target while processing relationships)
        // and association qualifiers. Add them now to the coding scheme
        // metadata.
        loadSupportedCodingSchemes(codingSchemeName);
        loadSupportedAssociationQualifiers(codingSchemeName);
        loadSupportedPropertyLinks(codingSchemeName);
    }

    /**
     * Carry out the mapping of relationships from UMLS RRF-based format to
     * LexGrid model.
     * 
     * @param assoc
     * @param codingSchemeName
     * @return The number of association instances added to the LexGrid
     *         repository.
     * @throws SQLException
     */
    protected int loadRelationsHelper(Association assoc, String codingSchemeName) throws SQLException {
        // Statement to get count of items to process,
        // qualified by SAB if association is SAB-specific ...
        StringBuffer sb;
        sb = new StringBuffer(256).append("SELECT COUNT(*) AS cnt FROM MRREL WHERE ").append(assoc.rrfField).append(
                " = ?");
        if (StringUtils.isNotBlank(assoc.rrfSAB))
            sb.append(" AND SAB = ?");
        PreparedStatement getRelationsCount = umlsConnection2_.prepareStatement(sb.toString());

        // Statement to get items to process,
        // qualified by SAB if association is SAB-specific ...
        // Note: ordered to enable a cache to not load duplicates.
        sb = new StringBuffer(256).append("SELECT CUI1, CUI2, AUI1, AUI2, SAB, RG FROM MRREL WHERE ")
                .append(assoc.rrfField).append(" = ?");
        if (StringUtils.isNotBlank(assoc.rrfSAB))
            sb.append(" AND SAB = ?");
        sb.append(" ORDER BY CUI1 {LIMIT}");
        PreparedStatement getRelations = umlsConnection2_.prepareStatement(umlsSqlModifier_.modifySQL(sb.toString()));

        int count = 0;
        try {
            // Retrieve and print the count for progress monitoring ...
            int start = 0;
            int total = 0;
            getRelationsCount.setString(1, assoc.rrfName);
            if (StringUtils.isNotBlank(assoc.rrfSAB))
                getRelationsCount.setString(2, assoc.rrfSAB);

            messages_.info("Counting relations for " + assoc.rrfName);
            ResultSet results = getRelationsCount.executeQuery();
            results.next();
            total = results.getInt("cnt");
            results.close();

            // Process matching entries ...
            while (start < total) {
                int param = 0;
                getRelations.setString(++param, assoc.rrfName);
                if (StringUtils.isNotBlank(assoc.rrfSAB))
                    getRelations.setString(++param, assoc.rrfSAB);

                // MySQL doesn't stream results - the {LIMIT above and this is
                // for getting limits on MySQL code}
                if (umlsSqlModifier_.getDatabaseType().equals("MySQL")) {
                    getRelations.setInt(++param, start);
                    getRelations.setInt(++param, batchSize);
                    start += batchSize;
                }
                // PostgreSQL properly streams results, we can just set the
                // fetch size, and only loop once
                else if (umlsSqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                    getRelations.setFetchSize(batchSize);
                    umlsConnection2_.setAutoCommit(false);
                    start = total;
                } else if(umlsSqlModifier_.getDatabaseType().equals("ACCESS")) {
                    //Don't set the fetch size for MS Access
                    start = total;
                } else {
                    getRelations.setFetchSize(batchSize);
                    start = total;
                }

                messages_.info("Getting a batch of relations for " + assoc.rrfName);
                results = getRelations.executeQuery();

                while (results.next()) {
                    // If the source association definition is reversed, we need
                    // to flip
                    // the source and target concepts accordingly.
                    boolean isReversed = assoc.rrfSourceDirectionalityReversed;
                    String sourceCode = results.getString(isReversed ? "CUI2" : "CUI1");
                    String targetCode = results.getString(isReversed ? "CUI1" : "CUI2");
                    String sab = results.getString("SAB");

                    String aui1 = results.getString("AUI1");
                    String aui2 = results.getString("AUI2");
                    String roleGroup = results.getString("RG");

                    if (sourceCode.equals(targetCode)) {
                        try {
                            insertIntoConceptPropertyLinks(codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT,
                                    sourceCode, getPresentationFromAUI(codingSchemeName, sourceCode, aui1), assoc.name,
                                    getPresentationFromAUI(codingSchemeName, sourceCode, aui2));

                            supportedPropertyLinks_.add(assoc.name);
                            propertyLinksCounter_++;
                        } catch (Exception e) {
                            // Just in case there are duplicate listings -- do
                            // nothing.
                        }

                    } else {

                        boolean result = addConceptAssociationToConceptsHelper(codingSchemeName, sourceCode,
                                assoc.name, targetCode, sab, roleGroup, assoc.rrfName, assoc.rrfInverse, count);
                                
                        if (result) {
                            count++;
                            
                            if (count % 100 == 0)
                                messages_.busy();
                            if (count % 10000 == 0)
                                messages_.info("Loaded " + count + " out of a possible total of " + total);
                        }
                    }
                }
                results.close();
            }
            messages_.info("Loaded " + count + " out of a possible total of " + total);
        } finally {
            getRelations.close();
            getRelationsCount.close();
        }
        return count;
    }

    /**
     * Detect and register association information to the array of supported
     * associations maintained by the loader.
     * 
     * @throws SQLException
     */
    protected void populateSupportedAssociations() throws SQLException {
        messages_.info("Getting the descriptive associations");
        Map relationsHolder = new Hashtable();

        PreparedStatement getRelations = umlsConnection_.prepareStatement("SELECT DISTINCT RELA, SAB, DIR FROM MRREL");
        try {
            ResultSet relations = getRelations.executeQuery();
            while (relations.next()) {
                String temp = relations.getString("RELA");
                String tempDirFlag = relations.getString("DIR");
                String tempSAB = relations.getString("SAB");
                if (temp == null || temp.length() == 0)
                    continue;
                mapSupportedAssociationsHelper(temp, tempSAB, tempDirFlag, "RELA", relationsHolder);
            }
            relations.close();
        } finally {
            getRelations.close();
        }

        messages_.info("Getting the base associations");
        getRelations = umlsConnection_.prepareStatement("SELECT DISTINCT REL, SAB, DIR FROM MRREL");
        try {
            ResultSet relations = getRelations.executeQuery();
            while (relations.next()) {
                String temp = relations.getString("REL");
                String tempDirFlag = relations.getString("DIR");
                String tempSAB = relations.getString("SAB");
                if (temp.length() == 0)
                    continue;
                mapSupportedAssociationsHelper(temp, tempSAB, tempDirFlag, "REL", relationsHolder);
            }
            relations.close();
        } finally {
            getRelations.close();
        }

        supportedAssociations_ = (Association[]) relationsHolder.values().toArray(
                new Association[relationsHolder.size()]);
        messages_.info("done processing relations");
    }

    /**
     * Remove and rebuild navigable relations from special endpoint nodes '@'
     * and '@@' to first level nodes in hierarchical associations.
     * 
     * @throws SQLException
     */
    protected void rebuildRootNodes() throws SQLException {
        String codingSchemeName = codingSchemeName_ = getCodingSchemeNameFromManifest(codingSchemeName_);
        String relationsContainerName = relationsName_;
        sqlTableUtility_.removeRootRelationNode(codingSchemeName, relationsContainerName, true);
        sqlTableUtility_.removeRootRelationNode(codingSchemeName, relationsContainerName, false);
        populateSupportedAssociations();

        // Calculate for the source relations container ...
        buildRootNode(codingSchemeName, true, getHierRelas(), relationsName_);
        // Calculate for standard umls relations ...
        buildRootNode(codingSchemeName, true, getHierRels(), relationsName_);
    }
}