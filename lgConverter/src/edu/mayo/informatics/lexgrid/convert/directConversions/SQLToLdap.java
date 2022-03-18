
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Convert-o-matic to take things from the sql format, and populate ldap.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 8601 $ checked in on $Date: 2008-06-05
 *          22:28:55 +0000 (Thu, 05 Jun 2008) $
 */
public class SQLToLdap {
    private Connection sqlConnection_;
    private Connection sqlConnection2_;
    private LdapContext ldapConnection_;
    private GenericSQLModifier gsm_;

    private SQLTableConstants stc_;

    private static Logger log = LogManager.getLogger("convert.SqlToLdap");
    private LgMessageDirectorIF messages_;

    private boolean failOnError_ = false;
    private final int batchSize = Constants.mySqlBatchSize; // used by mysql

    /**
     * Load a terminology from the sql tables back into the ldap server.
     * 
     * @param sqlServer
     *            address of the SQL server
     * @param sqlDriver
     *            the driver class to use for the sql connection (driver must be
     *            on the path)
     * @param sqlUsername
     *            for authenification on SQL server
     * @param sqlPassword
     *            for authenification on SQL server
     * @param ldapUsername
     *            for authenification on LDAP server
     * @param ldapPassword
     *            for authenification on SQL server
     * @param ldapAddress
     *            location of the LDAP server
     * @param ldapService
     *            the service to connect to on the ldap server.
     * @param codingScheme
     *            The coding scheme to transfer
     * @param failOnError
     *            false means fail on minor errors.
     * @throws Exception
     */
    public SQLToLdap(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword, String tablePrefix,
            String ldapUsername, String ldapPassword, String ldapAddress, String ldapService, String codingScheme,
            boolean failOnError, LgMessageDirectorIF messages) throws Exception {
        // make sure that the service and address don't end up with extra
        // slashes
        while (ldapService.charAt(0) == '/' || ldapService.charAt(0) == '\\') {
            ldapService = ldapService.substring(1, ldapService.length());
        }

        while (ldapAddress.charAt(ldapAddress.length() - 1) == '/'
                || ldapAddress.charAt(ldapAddress.length() - 1) == '\\') {
            ldapAddress = ldapAddress.substring(0, ldapAddress.length() - 1);
        }
        messages_ = messages;
        failOnError_ = failOnError;

        // try
        // {
        try {
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
            gsm_ = new GenericSQLModifier(sqlConnection_);

            stc_ = new SQLTableUtilities(sqlConnection_, tablePrefix).getSQLTableConstants();

            sqlConnection2_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
        } catch (ClassNotFoundException e) {
            log.error("The class you specified for your sql driver could not be found on the path.", e);
            messages_
                    .fatalAndThrowException("The class you specified for your sql driver could not be found on the path.");
        }

        connectToLdap(ldapUsername, ldapPassword, ldapAddress, ldapService);

        loadBaseLdapService(ldapService);
        String codingSchemeLdapString = loadCodingScheme(codingScheme);
        loadConceptCodes(codingScheme, codingSchemeLdapString);
        loadRelations(codingScheme, codingSchemeLdapString);
        loadAssociations(codingScheme, codingSchemeLdapString);

        sqlConnection_.close();
        sqlConnection2_.close();
        ldapConnection_.close();
    }

    /*
     * Make the connection to the ldap server.
     */
    private void connectToLdap(String userName, String userPassword, String address, String service)
            throws NamingException {
        log.debug("connectToLdap called");
        if (userName == null) {
            userName = "";
        }
        if (userPassword == null) {
            userPassword = "";
        }

        if (!address.endsWith("/")) {
            address += "/";
        }

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, address + "" + service);
        env.put(Context.REFERRAL, "follow");
        env.put("java.naming.ldap.derefAliases", "never");

        // these only work in 1.4... and beyond
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        env.put("com.sun.jndi.ldap.connect.timeout", "3000");

        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, userPassword);

        ldapConnection_ = new InitialLdapContext(env, null);
    }

    /*
     * Create the initial ldap objects
     */
    private void loadBaseLdapService(String ldapService) throws NamingException {
        log.debug("loadBaseLdapService called - ldapService: " + ldapService);
        try {
            NameParser parser = ldapConnection_.getNameParser("");
            Name parsedName = parser.parse(ldapConnection_.getNameInNamespace());
            String service = parsedName.get(parsedName.size() - 1);
            service = service.substring("service=".length());

            Attributes attributes = new BasicAttributes("objectClass", "serviceClass");
            attributes.put("service", service);
            ldapConnection_.createSubcontext("", attributes);
        } catch (NameAlreadyBoundException e1) {
            // do nothing
        }

        try {
            Attributes attributes = new BasicAttributes("objectClass", "codingSchemes");
            attributes.put("dc", SQLTableConstants.TBLCOLVAL_DC_CODINGSCHEMES);
            ldapConnection_.createSubcontext("dc=" + SQLTableConstants.TBLCOLVAL_DC_CODINGSCHEMES, attributes);
        } catch (NameAlreadyBoundException e1) {
            // do nothing
        }
    }

    /*
     * Load the coding Scheme table information into ldap.
     */
    private String loadCodingScheme(String codingSchemeName) throws Exception {
        log.debug("loadCodingScheme called - codingSchemeName:" + codingSchemeName);

        PreparedStatement getCodingSchemeAdditions = sqlConnection_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + " WHERE "
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?"));
        getCodingSchemeAdditions.setString(1, codingSchemeName);

        String ldapName = "codingScheme=" + DBUtility.escapeLdapCode(codingSchemeName) + ", dc="
                + SQLTableConstants.TBLCOLVAL_DC_CODINGSCHEMES;

        try {
            ResultSet results = getCodingSchemeAdditions.executeQuery();
            if (results.next()) {
                String formalName = results.getString(SQLTableConstants.TBLCOL_FORMALNAME);
                String registeredName = results.getString(SQLTableConstants.TBLCOL_REGISTEREDNAME);
                String defaultLanguage = results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);
                String representsVersion = results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION);
                Boolean isNative = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISNATIVE);
                int approxNumConcepts = results.getInt(SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS);
                String entityDescription = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);
                String copyright = results.getString(SQLTableConstants.TBLCOL_COPYRIGHT);

                // required attributes
                Attributes attributes = new BasicAttributes("objectClass", "codingSchemeClass");
                attributes.put("codingScheme", codingSchemeName);
                attributes.put(SQLTableConstants.TBLCOL_FORMALNAME, formalName);
                attributes.put(SQLTableConstants.TBLCOL_REGISTEREDNAME, registeredName);
                attributes.put(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, defaultLanguage);
                attributes.put(SQLTableConstants.TBLCOL_REPRESENTSVERSION, representsVersion);

                // placeholders will be overwritten later...
                attributes.put("supportedLanguage", "PLACEHOLDER");
                attributes.put("supportedFormat", "PLACEHOLDER");
                attributes.put("supportedProperty", "PLACEHOLDER");

                // localName is a multi attribute...
                BasicAttribute temp = new BasicAttribute("localName");
                temp.add(registeredName);
                temp.add(codingSchemeName);
                attributes.put(temp);

                // create the node, put in the required attributes
                ldapConnection_.createSubcontext(ldapName, attributes);

                // put in all optional attributes
                modifyAttribute(SQLTableConstants.TBLCOL_ISNATIVE, isNative, ldapName);
                modifyAttribute(SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS, approxNumConcepts + "", ldapName);
                modifyAttribute(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, entityDescription, ldapName);
                modifyAttribute(SQLTableConstants.TBLCOL_COPYRIGHT, copyright, ldapName);

                results.close();
                getCodingSchemeAdditions.close();

                // Put in the multi attributes
                PreparedStatement getCodingSchemeMultiAttributes = sqlConnection_.prepareStatement(gsm_
                        .modifySQL("SELECT * FROM "
                                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES) + " WHERE "
                                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? ORDER BY "
                                + SQLTableConstants.TBLCOL_TYPENAME + " "));

                getCodingSchemeMultiAttributes.setString(1, codingSchemeName);

                results = getCodingSchemeMultiAttributes.executeQuery();

                String currentAttributeName = null;
                ArrayList currentAttributeValues = new ArrayList();

                while (results.next()) {
                    String attributeName = results.getString(SQLTableConstants.TBLCOL_TYPENAME);
                    String attributeValue = results.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    if (currentAttributeName == null) {
                        currentAttributeName = attributeName;
                    }
                    if (!attributeName.equals(currentAttributeName)) {
                        modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                                .toArray(new String[currentAttributeValues.size()]), ldapName);
                        currentAttributeName = attributeName;
                        currentAttributeValues.clear();
                    }
                    currentAttributeValues.add(attributeValue);
                }
                results.close();
                getCodingSchemeMultiAttributes.close();
                // put in the last one
                modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                        .toArray(new String[currentAttributeValues.size()]), ldapName);

                // Put in the supported attributes
                PreparedStatement getCodingSchemeSupportedAttributes = sqlConnection_.prepareStatement(gsm_
                        .modifySQL("SELECT * FROM "
                                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " WHERE "
                                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? ORDER BY "
                                + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + ""));

                getCodingSchemeSupportedAttributes.setString(1, codingSchemeName);

                results = getCodingSchemeSupportedAttributes.executeQuery();

                String currentSupportedAttributeTag = null;
                ArrayList currentSupportedAttributeValues = new ArrayList();

                while (results.next()) {
                    String supportedAttributeTag = results.getString(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG);
                    String supportedAttributeValue = results.getString(SQLTableConstants.TBLCOL_ID);
                    String urn = results.getString(SQLTableConstants.TBLCOL_URN);

                    if (currentSupportedAttributeTag == null) {
                        currentSupportedAttributeTag = supportedAttributeTag;
                    }
                    if (!supportedAttributeTag.equals(currentSupportedAttributeTag)) {
                        modifyMultiValueAttribute("supported" + currentSupportedAttributeTag,
                                (String[]) currentSupportedAttributeValues
                                        .toArray(new String[currentSupportedAttributeValues.size()]), ldapName);
                        currentSupportedAttributeTag = supportedAttributeTag;
                        currentSupportedAttributeValues.clear();
                    }
                    if (urn != null) {
                        supportedAttributeValue = urn.trim() + " " + supportedAttributeValue;
                    }
                    currentSupportedAttributeValues.add(supportedAttributeValue);
                }
                results.close();
                getCodingSchemeSupportedAttributes.close();
                // put in the last one
                modifyMultiValueAttribute("supported" + currentSupportedAttributeTag,
                        (String[]) currentSupportedAttributeValues.toArray(new String[currentSupportedAttributeValues
                                .size()]), ldapName);

                messages_.info("Loaded 1 coding scheme.");
            } else {
                results.close();
                messages_.fatalAndThrowException("Did not find any coding schemes");
            }
        } catch (NameAlreadyBoundException e) {
            handleError(messages_, "Coding Scheme already exists", e, failOnError_);
        } catch (Exception e) {
            handleError(messages_, "Problem loading coding scheme", e, failOnError_);
        }
        return ldapName;
    }

    private void loadConceptCodes(String codingSchemeName, String ldapName) throws Exception {
        log.debug("loadConceptCodes called - codingSchemeName:" + codingSchemeName + " ldapName: " + ldapName);

        messages_.info("Loading concepts");
        ldapName = "dc=Concepts," + ldapName;

        Attributes attributes;
        try {
            attributes = new BasicAttributes("objectClass", "concepts");
            attributes.put("dc", SQLTableConstants.TBLCOLVAL_DC_CONCEPTS);
            ldapConnection_.createSubcontext(ldapName, attributes);
        } catch (NameAlreadyBoundException e) {
            handleError(messages_, "Concepts Node already exists", e, failOnError_);
        }

        messages_.info("loading concepts - getting a total count");

        PreparedStatement getConcepts = sqlConnection_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY) + " WHERE " + stc_.codingSchemeNameOrId + " = ?");

        getConcepts.setString(1, codingSchemeName);

        ResultSet results = getConcepts.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getConcepts.close();

        int start = 0;
        int codeCount = 0;

        // This while loop is for mysql - it gets results by a batch at a time.
        // this loop will only run once with other databases.
        while (start < total) {
            messages_.info("Getting a results from sql (a page if using mysql)");
            getConcepts = sqlConnection_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY) + " WHERE " + stc_.codingSchemeNameOrId
                    + " = ? {LIMIT}"));

            getConcepts.setString(1, codingSchemeName);

            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (gsm_.getDatabaseType().equals("MySQL")) {
                getConcepts.setInt(2, start);
                getConcepts.setInt(3, batchSize);
                start += batchSize;
            } else if (gsm_.getDatabaseType().equals("PostgreSQL")) {
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                getConcepts.setFetchSize(batchSize);
                sqlConnection_.setAutoCommit(false);
                start = total;
            } else {
                start = total;
            }

            results = getConcepts.executeQuery();

            log.debug("query finished - processing results");

            while (results.next()) {
                String conceptCode = results.getString(stc_.entityCodeOrId);
                Boolean firstVersion = DBUtility
                        .getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_FIRSTRELEASE);
                Boolean lastVersion = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_DEPRECATED);
                Boolean isActive = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE);
                String conceptStatus = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
                Boolean isAnonymous = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISANONYMOUS);
                String entityDescription = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

                try {

                    String codeLdapName = " " + stc_.entityCodeOrId + " =" + DBUtility.escapeLdapCode(conceptCode)
                            + "," + ldapName;

                    // required attributes
                    // TODO: DB Chagnes (change codedEntry)
                    attributes = new BasicAttributes("objectClass", "codedEntry");
                    attributes.put(stc_.entityCodeOrId, conceptCode);

                    // placeholders will be overwritten later...

                    // create the node, put in the required attributes
                    ldapConnection_.createSubcontext(codeLdapName, attributes);

                    // put in all optional attributes
                    modifyAttribute(SQLTableConstants.TBLCOL_FIRSTRELEASE, firstVersion, codeLdapName);
                    modifyAttribute("lastVersion", lastVersion, codeLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_ISACTIVE, isActive, codeLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_CONCEPTSTATUS, conceptStatus, codeLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_ISANONYMOUS, isAnonymous, codeLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, entityDescription, codeLdapName);

                    loadCodeProperties(codeLdapName, conceptCode, codingSchemeName);

                    if (codeCount % 10 == 0) {
                        messages_.busy();
                    }

                    if (++codeCount % 1000 == 0) {
                        messages_.info("Loaded " + codeCount + " codes out of " + total);
                    }
                }

                catch (NameAlreadyBoundException e) {
                    handleError(messages_, "Concept code already exists " + conceptCode, e, failOnError_);
                } catch (NamingException e) {
                    handleError(messages_, "Problem loading concept code " + conceptCode, e, failOnError_);
                }
            }
            results.close();
            getConcepts.close();
            if (!gsm_.getDatabaseType().equals("MySQL")) {
                // only do the while loop and limit thing if using mysql
                break;
            }
        }
        messages_.info("Loaded " + codeCount + " codes.");
    }

    private void loadCodeProperties(String codeLdapName, String conceptCode, String codingSchemeName) throws Exception {
        try {
            // TODO handle conceptPropertyLink table when ldap supports it.
            PreparedStatement getProperties = sqlConnection2_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " WHERE " + stc_.codingSchemeNameOrId
                    + " = ? AND " + stc_.entityCodeOrEntityId + " = ?", false));
            getProperties.setString(1, codingSchemeName);
            getProperties.setString(2, conceptCode);
            ResultSet results = getProperties.executeQuery();
            while (results.next()) {
                String propertyId = results.getString(SQLTableConstants.TBLCOL_PROPERTYID);
                String property;
                String propertyType = results.getString(SQLTableConstants.TBLCOL_PROPERTYTYPE);
                if (propertyType.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_PRESENTATION)) {
                    property = SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
                } else if (propertyType.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_DEFINITION)) {
                    property = SQLTableConstants.TBLCOLVAL_DEFINITION;
                } else {
                    property = propertyType.toLowerCase();
                }

                property = results.getString(stc_.propertyOrPropertyName);
                String language = results.getString(SQLTableConstants.TBLCOL_LANGUAGE);
                String presentationFormat = results.getString(stc_.formatOrPresentationFormat);
                Boolean isPreferred = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISPREFERRED);
                String degreeOfFidelity = results.getString(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY);
                Boolean matchIfNoContext = DBUtility.getBooleanFromResultSet(results,
                        SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT);
                String representationalForm = results.getString(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM);
                String propertyValue = results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE);

                if (propertyValue == null || propertyValue.length() == 0) {
                    // don't bother to add this one.
                    continue;
                }

                String propertyLdapName = SQLTableConstants.TBLCOL_PROPERTYID + "="
                        + DBUtility.escapeLdapCode(propertyId) + "," + codeLdapName;

                // required attributes
                Attributes attributes = new BasicAttributes(SQLTableConstants.TBLCOL_PROPERTYID, propertyId);
                BasicAttribute objectClass = new BasicAttribute("objectClass", "propertyClass");
                attributes.put(stc_.propertyOrPropertyName, property);
                attributes.put("text", propertyValue);
                if (property.equals(SQLTableConstants.TBLCOLVAL_DEFINITION)) {
                    objectClass.add(SQLTableConstants.TBLCOLVAL_DEFINITION);
                } else if (property.equals(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION)) {
                    objectClass.add(SQLTableConstants.TBLCOLVAL_PRESENTATION);
                } else if (property.equals(SQLTableConstants.TBLCOLVAL_COMMENT)) {
                    objectClass.add(SQLTableConstants.TBLCOLVAL_COMMENT);
                } else if (property.equals(SQLTableConstants.TBLCOLVAL_INSTRUCTION)) {
                    objectClass.add(SQLTableConstants.TBLCOLVAL_INSTRUCTION);
                }
                attributes.put(objectClass);

                // create the node, put in the required attributes
                ldapConnection_.createSubcontext(propertyLdapName, attributes);

                // put in all optional attributes - these are available for all
                // property types
                modifyAttribute(SQLTableConstants.TBLCOL_LANGUAGE, language, propertyLdapName);
                modifyAttribute(stc_.formatOrPresentationFormat, presentationFormat, propertyLdapName);

                // attributes only available to presentations and definitions
                if (property.equals(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION)
                        || property.equals(SQLTableConstants.TBLCOLVAL_DEFINITION)) {
                    modifyAttribute(SQLTableConstants.TBLCOL_ISPREFERRED, isPreferred, propertyLdapName);
                }

                // properties only available to presentations
                if (property.equals(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION)) {
                    modifyAttribute(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY, degreeOfFidelity, propertyLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT, matchIfNoContext, propertyLdapName);
                    modifyAttribute(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM, representationalForm,
                            propertyLdapName);
                }

                // Put in the multi attributes
                PreparedStatement getConceptPropertyMultiAttributes = sqlConnection2_.prepareStatement(gsm_.modifySQL(
                        "SELECT " + SQLTableConstants.TBLCOL_TYPENAME + ", " + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                                + " FROM " + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES)
                                + " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.entityCodeOrEntityId
                                + " = ? AND " + SQLTableConstants.TBLCOL_PROPERTYID + " = ? ORDER BY "
                                + SQLTableConstants.TBLCOL_TYPENAME, false));

                getConceptPropertyMultiAttributes.setString(1, codingSchemeName);
                getConceptPropertyMultiAttributes.setString(2, conceptCode);
                getConceptPropertyMultiAttributes.setString(3, propertyId);

                ResultSet results2 = getConceptPropertyMultiAttributes.executeQuery();

                String currentAttributeName = null;
                ArrayList currentAttributeValues = new ArrayList();

                while (results2.next()) {
                    String attributeName = results2.getString(SQLTableConstants.TBLCOL_TYPENAME);
                    String attributeValue = results2.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    if (currentAttributeName == null) {
                        currentAttributeName = attributeName;
                    }
                    if (!attributeName.equals(currentAttributeName)) {
                        modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                                .toArray(new String[currentAttributeValues.size()]), propertyLdapName);
                        currentAttributeName = attributeName;
                        currentAttributeValues.clear();
                    }
                    currentAttributeValues.add(attributeValue);
                }
                results2.close();
                getConceptPropertyMultiAttributes.close();
                // put in the last one
                modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                        .toArray(new String[currentAttributeValues.size()]), propertyLdapName);

            }
            results.close();
            getProperties.close();
        } catch (Exception e) {
            handleError(messages_, "Problem adding properties for concept code " + conceptCode, e, failOnError_);
        }
    }

    private void loadRelations(String codingSchemeName, String ldapName) throws Exception {
        log.debug("loadRelations called - codingSchemeName:" + codingSchemeName + " ldapName: " + ldapName);

        messages_.info("Loading relations");
        PreparedStatement getRelations = sqlConnection_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.RELATION) + " WHERE " + stc_.codingSchemeNameOrId + " = ?"));

        getRelations.setString(1, codingSchemeName);

        ResultSet results = getRelations.executeQuery();
        int relations = 0;

        while (results.next()) {
            String relationName = results.getString(stc_.containerNameOrContainerDC);
            Boolean isNative = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISNATIVE);
            String entityDescription = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

            String relationLdapName = "dc=" + DBUtility.escapeLdapCode(relationName) + "," + ldapName;

            // required attributes
            Attributes attributes = new BasicAttributes("objectClass", "relations");
            attributes.put("dc", relationName);

            // create the node, put in the required attributes
            ldapConnection_.createSubcontext(relationLdapName, attributes);

            // put in all optional attributes
            modifyAttribute(SQLTableConstants.TBLCOL_ISNATIVE, isNative, relationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, entityDescription, relationLdapName);

            // Put in the multi attributes
            PreparedStatement getRelationMultiAttributes = sqlConnection2_.prepareStatement(gsm_.modifySQL("SELECT "
                    + SQLTableConstants.TBLCOL_TYPENAME + ", " + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + " FROM "
                    + stc_.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? ORDER BY "
                    + SQLTableConstants.TBLCOL_TYPENAME + " "));

            getRelationMultiAttributes.setString(1, codingSchemeName);
            getRelationMultiAttributes.setString(2, relationName);

            ResultSet results2 = getRelationMultiAttributes.executeQuery();

            String currentAttributeName = null;
            ArrayList currentAttributeValues = new ArrayList();

            while (results2.next()) {
                String attributeName = results2.getString(SQLTableConstants.TBLCOL_TYPENAME);
                String attributeValue = results2.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                if (currentAttributeName == null) {
                    currentAttributeName = attributeName;
                }
                if (!attributeName.equals(currentAttributeName)) {
                    modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                            .toArray(new String[currentAttributeValues.size()]), relationLdapName);
                    currentAttributeName = attributeName;
                    currentAttributeValues.clear();
                }
                currentAttributeValues.add(attributeValue);
            }
            results2.close();
            getRelationMultiAttributes.close();
            // put in the last one
            modifyMultiValueAttribute(currentAttributeName, (String[]) currentAttributeValues
                    .toArray(new String[currentAttributeValues.size()]), relationLdapName);

            messages_.busy();

            if (++relations % 1000 == 0) {
                messages_.info("Loaded " + relations + " relations.");
            }
        }
        results.close();
        getRelations.close();
        messages_.info("Loaded " + relations + " relations.");
    }

    private void loadAssociations(String codingSchemeName, String ldapName) throws Exception {
        log.debug("loadAssociations called - codingSchemeName:" + codingSchemeName + " ldapName: " + ldapName);
        messages_.info("Loading Associations");
        PreparedStatement getAssociations = sqlConnection_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.ASSOCIATION) + " WHERE " + stc_.codingSchemeNameOrId + " = ?"));

        getAssociations.setString(1, codingSchemeName);

        ResultSet results = getAssociations.executeQuery();
        int associationCount = 0;

        while (results.next()) {
            // TODO - handle inverse and isNavigable when they are added to ldap
            String relationName = results.getString(stc_.containerNameOrContainerDC);
            String association = results.getString(stc_.associationNameOrId);
            String forwardName = results.getString(SQLTableConstants.TBLCOL_FORWARDNAME);
            String reverseName = results.getString(SQLTableConstants.TBLCOL_REVERSENAME);
            Boolean isTransitive = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISTRANSITIVE);
            Boolean isAntiTransitive = DBUtility.getBooleanFromResultSet(results,
                    SQLTableConstants.TBLCOL_ISANTITRANSITIVE);
            Boolean isSymmetric = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISSYMMETRIC);
            Boolean isAntiSymmetric = DBUtility.getBooleanFromResultSet(results,
                    SQLTableConstants.TBLCOL_ISANTISYMMETRIC);
            Boolean isReflexive = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISREFLEXIVE);
            Boolean isAntiReflexive = DBUtility.getBooleanFromResultSet(results,
                    SQLTableConstants.TBLCOL_ISANTIREFLEXIVE);
            Boolean isFunctional = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISFUNCTIONAL);
            Boolean isReverseFunctional = DBUtility.getBooleanFromResultSet(results,
                    SQLTableConstants.TBLCOL_ISREVERSEFUNCTIONAL);
            Boolean isTranslationAssociation = DBUtility.getBooleanFromResultSet(results,
                    SQLTableConstants.TBLCOL_ISTRANSLATIONASSOCIATION);
            String targetCodingScheme = results.getString(SQLTableConstants.TBLCOL_TARGETCODINGSCHEME);
            String entityDescription = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

            messages_.info("Loading " + association);

            String associationLdapName = "association=" + DBUtility.escapeLdapCode(association) + ",dc="
                    + DBUtility.escapeLdapCode(relationName) + "," + ldapName;

            // required attributes
            Attributes attributes = new BasicAttributes("objectClass", "associationClass");
            attributes.put(stc_.associationNameOrId, association);
            attributes.put(SQLTableConstants.TBLCOL_FORWARDNAME, forwardName);
            attributes.put(SQLTableConstants.TBLCOL_REVERSENAME, reverseName);

            // create the node, put in the required attributes
            ldapConnection_.createSubcontext(associationLdapName, attributes);

            // put in all optional attributes
            modifyAttribute(SQLTableConstants.TBLCOL_ISTRANSITIVE, isTransitive, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISANTITRANSITIVE, isAntiTransitive, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISSYMMETRIC, isSymmetric, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISANTISYMMETRIC, isAntiSymmetric, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISREFLEXIVE, isReflexive, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISANTIREFLEXIVE, isAntiReflexive, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISFUNCTIONAL, isFunctional, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISREVERSEFUNCTIONAL, isReverseFunctional, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ISTRANSLATIONASSOCIATION, isTranslationAssociation,
                    associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_TARGETCODINGSCHEME, targetCodingScheme, associationLdapName);
            modifyAttribute(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, entityDescription, associationLdapName);

            loadAssociationsConceptsToConcepts(codingSchemeName, relationName, association, associationLdapName,
                    targetCodingScheme);
            loadAssociationsConceptsToData(codingSchemeName, relationName, association, associationLdapName);
            associationCount++;
        }
        results.close();
        getAssociations.close();
        messages_.info("Loaded " + associationCount + " associations.");
    }

    private void loadAssociationsConceptsToConcepts(String codingSchemeName, String relationName, String association,
            String associationLdapName, String defaultTargetCodingScheme) throws Exception {
        try {
            if (defaultTargetCodingScheme == null || defaultTargetCodingScheme.length() == 0) {
                defaultTargetCodingScheme = codingSchemeName;
            }
            messages_.info("loading associations (to concepts) for relationName '" + relationName + "' association '"
                    + association + "' - getting a total count");

            PreparedStatement getAssociations = sqlConnection2_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                    + stc_.entityCodeOrAssociationId + " = ?");

            getAssociations.setString(1, codingSchemeName);
            getAssociations.setString(2, relationName);
            getAssociations.setString(3, association);

            ResultSet results = getAssociations.executeQuery();
            results.next();
            int total = results.getInt("cnt");
            results.close();
            getAssociations.close();

            int start = 0;
            int associationCount = 0;
            // This while loop is for mysql - it gets results by a batch at a
            // time.
            // this loop will only run once with other databases.
            while (start < total) {
                messages_.info("Getting a results from sql (a page if using mysql)");
                getAssociations = sqlConnection2_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                        + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                        + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                        + stc_.entityCodeOrAssociationId + " = ? {LIMIT}"));

                getAssociations.setString(1, codingSchemeName);
                getAssociations.setString(2, relationName);
                getAssociations.setString(3, association);

                // mysql doesn't stream results - the {LIMIT} above and this is
                // for getting limits on mysql code}
                if (gsm_.getDatabaseType().equals("MySQL")) {
                    getAssociations.setInt(4, start);
                    getAssociations.setInt(5, batchSize);
                    start += batchSize;
                } else if (gsm_.getDatabaseType().equals("PostgreSQL")) {
                    // postgres properly streams results, we can just set the
                    // fetch size, and only loop once
                    getAssociations.setFetchSize(batchSize);
                    sqlConnection2_.setAutoCommit(false);
                    start = total;
                } else {
                    start = total;
                }

                results = getAssociations.executeQuery();
                log.debug("Query finished, processing results");

                while (results.next()) {
                    String sourceCodingSchemeName = results.getString(stc_.sourceCSIdOrEntityCodeNS);
                    String sourceConceptCode = results.getString(stc_.sourceEntityCodeOrId);
                    String targetCodingSchemeName = results.getString(stc_.targetCSIdOrEntityCodeNS);
                    String targetConceptCode = results.getString(stc_.targetEntityCodeOrId);
                    String multiAttributesKey = results.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                    Boolean firstVersion = DBUtility.getBooleanFromResultSet(results,
                            SQLTableConstants.TBLCOL_FIRSTRELEASE);
                    Boolean lastVersion = DBUtility.getBooleanFromResultSet(results,
                            SQLTableConstants.TBLCOL_DEPRECATED);

                    String sourceLdapName = stc_.sourceEntityCodeOrId + "="
                            + DBUtility.escapeLdapCode(sourceConceptCode) + "," + associationLdapName;
                    String targetLdapName = stc_.targetEntityCodeOrId + "="
                            + DBUtility.escapeLdapCode(targetConceptCode) + "," + sourceLdapName;

                    // source required attributes
                    Attributes attributes = new BasicAttributes("objectClass", "associationInstance");
                    attributes.put(stc_.sourceEntityCodeOrId, sourceConceptCode);

                    // create the node, put in the required attributes
                    try {
                        ldapConnection_.createSubcontext(sourceLdapName, attributes);

                        // put in all optional attributes
                        // source coding scheme only goes in if it is not
                        // default
                        if (!sourceCodingSchemeName.equals(codingSchemeName)) {
                            modifyAttribute(stc_.sourceCSIdOrEntityCodeNS, sourceCodingSchemeName, sourceLdapName);
                        }
                    } catch (NameAlreadyBoundException e) {
                        // this is ok
                        // (mostly) - I am ignoring a possible error here in a
                        // _really_ obscure case.
                        // If you have an association that contains two source
                        // codes - that are identical,
                        // except one of them is from an external terminology,
                        // that bit will get lost.
                        // you will need to put it into a different association
                        // and/or relation
                    }

                    // target required attributes
                    attributes = new BasicAttributes("objectClass", "associationTarget");
                    attributes.put(stc_.targetEntityCodeOrId, targetConceptCode);

                    // create the node, put in the required attributes
                    ldapConnection_.createSubcontext(targetLdapName, attributes);

                    // put in all optional attributes
                    // target coding scheme only goes in if it is not default
                    if (!targetCodingSchemeName.equals(defaultTargetCodingScheme)) {
                        modifyAttribute(stc_.targetCSIdOrEntityCodeNS, targetCodingSchemeName, targetLdapName);
                    }
                    modifyAttribute("firstVersion", firstVersion, targetLdapName);
                    modifyAttribute("lastVersion", lastVersion, targetLdapName);
                    loadAssociationsMultiAttributes(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS),
                            codingSchemeName, multiAttributesKey, targetLdapName);

                    if (associationCount % 10 == 0) {
                        messages_.busy();
                    }

                    if (++associationCount % 1000 == 0) {
                        messages_.info("Loaded " + associationCount + " association items out of " + total);
                    }
                }
                results.close();
                getAssociations.close();
                if (!gsm_.getDatabaseType().equals("MySQL")) {
                    // only do the while loop and limit thing if using mysql
                    break;
                }
            }
            messages_.info("Loaded " + associationCount + " association items.");
        } catch (Exception e) {
            handleError(messages_, "Problem loading associations for relationName '" + relationName + "' association '"
                    + association, e, failOnError_);
        }
    }

    private void loadAssociationsConceptsToData(String codingSchemeName, String relationName, String association,
            String associationLdapName) throws Exception {
        try {
            messages_.info("loading associations (to data) for relationName '" + relationName + "' association '"
                    + association + "' - getting a total count");

            PreparedStatement getAssociations = sqlConnection2_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                    + stc_.entityCodeOrAssociationId + " = ?");

            getAssociations.setString(1, codingSchemeName);
            getAssociations.setString(2, relationName);
            getAssociations.setString(3, association);

            ResultSet results = getAssociations.executeQuery();

            results.next();
            int total = results.getInt("cnt");

            results.close();
            getAssociations.close();

            int start = 0;
            int associationCount = 0;

            // This while loop is for mysql - it gets results by a batch at a
            // time.
            // this loop will only run once with other databases.
            while (start < total) {
                messages_.info("Getting a results from sql (a page if using mysql)");
                getAssociations = sqlConnection2_.prepareStatement(gsm_.modifySQL("SELECT * FROM "
                        + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) + " WHERE "
                        + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                        + stc_.entityCodeOrAssociationId + " = ? {LIMIT}"));

                getAssociations.setString(1, codingSchemeName);
                getAssociations.setString(2, relationName);
                getAssociations.setString(3, association);

                // mysql doesn't stream results - the {LIMIT} above and this is
                // for getting limits on mysql code}
                if (gsm_.getDatabaseType().equals("MySQL")) {
                    getAssociations.setInt(4, start);
                    getAssociations.setInt(5, batchSize);
                    start += batchSize;
                } else if (gsm_.getDatabaseType().equals("PostgreSQL")) {
                    // postgres properly streams results, we can just set the
                    // fetch size, and only loop once
                    getAssociations.setFetchSize(batchSize);
                    sqlConnection2_.setAutoCommit(false);
                    start = total;
                } else {
                    start = total;
                }
                results = getAssociations.executeQuery();
                log.debug("Query finished, processing results");

                while (results.next()) {

                    String sourceCodingSchemeName = results.getString(stc_.sourceCSIdOrEntityCodeNS);
                    String sourceConceptCode = results.getString(stc_.sourceEntityCodeOrId);
                    String id = results.getString(stc_.idOrDataId);
                    String multiAttributesKey = results.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                    String dataValue = results.getString(SQLTableConstants.TBLCOL_DATAVALUE);
                    Boolean firstVersion = DBUtility.getBooleanFromResultSet(results,
                            SQLTableConstants.TBLCOL_FIRSTRELEASE);
                    Boolean lastVersion = DBUtility.getBooleanFromResultSet(results,
                            SQLTableConstants.TBLCOL_DEPRECATED);
                    String sourceLdapName = stc_.sourceEntityCodeOrId + "="
                            + DBUtility.escapeLdapCode(sourceConceptCode) + "," + associationLdapName;
                    String targetLdapName = stc_.idOrDataId + "=" + DBUtility.escapeLdapCode(id) + "," + sourceLdapName;
                    // source required attributes
                    Attributes attributes = new BasicAttributes("objectClass", "associationInstance");
                    attributes.put(stc_.sourceEntityCodeOrId, sourceConceptCode);
                    // create the node, put in the required attributes
                    try {
                        ldapConnection_.createSubcontext(sourceLdapName, attributes);

                        // put in all optional attributes
                        // source coding scheme only goes in if it is not
                        // default
                        if (!sourceCodingSchemeName.equals(codingSchemeName)) {
                            modifyAttribute(stc_.sourceCSIdOrEntityCodeNS, sourceCodingSchemeName, sourceLdapName);
                        }
                    } catch (NameAlreadyBoundException e) {
                        // this is ok
                        // (mostly) - I am ignoring a possible error here in a
                        // _really_ obscure case.
                        // If you have an association that contains two source
                        // codes - that are identical,
                        // except one of them is from an external terminology,
                        // that bit will get lost.
                        // you will need to put it into a different association
                        // and/or relation
                    }
                    // target required attributes
                    attributes = new BasicAttributes("objectClass", "associationData");
                    attributes.put(stc_.idOrDataId, id);
                    attributes.put(SQLTableConstants.TBLCOL_DATAVALUE, dataValue);
                    // create the node, put in the required attributes
                    ldapConnection_.createSubcontext(targetLdapName, attributes);
                    modifyAttribute("firstVersion", firstVersion, targetLdapName);
                    modifyAttribute("lastVersion", lastVersion, targetLdapName);
                    loadAssociationsMultiAttributes(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS),
                            codingSchemeName, multiAttributesKey, targetLdapName);

                    if (associationCount % 10 == 0) {
                        messages_.busy();
                    }

                    if (++associationCount % 1000 == 0) {
                        messages_.info("Loaded " + associationCount + " association items out of " + total);
                    }
                }
                results.close();
                getAssociations.close();
                if (!gsm_.getDatabaseType().equals("MySQL")) {
                    // only do the while loop and limit thing if using mysql
                    break;
                }
            }
            messages_.info("Loaded " + associationCount + " association items.");
        } catch (Exception e) {
            handleError(messages_, "Problem loading associations for relationName '" + relationName + "' association '"
                    + association, e, failOnError_);
        }
    }

    private void loadAssociationsMultiAttributes(String table, String codingSchemeName, String multiAttributesKey,
            String targetLdapName) throws SQLException, NamingException {
        PreparedStatement getMultiAttributes = sqlConnection_.prepareStatement(gsm_.modifySQL("SELECT * FROM " + table
                + " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                + " = ?", false));

        getMultiAttributes.setString(1, codingSchemeName);
        getMultiAttributes.setString(2, multiAttributesKey);

        ResultSet results = getMultiAttributes.executeQuery();
        ArrayList modVersionValues = new ArrayList();

        while (results.next()) {
            String qualifierName;
            String qualifierValue = null;

            qualifierName = results.getString(SQLTableConstants.TBLCOL_QUALIFIERNAME);
            qualifierValue = results.getString(SQLTableConstants.TBLCOL_QUALIFIERVALUE);

            if (qualifierName != null) {

                String qualifierLdapName = "associationQualifier=" + DBUtility.escapeLdapCode(qualifierName) + ","
                        + targetLdapName;

                // source required attributes
                Attributes attributes = new BasicAttributes("objectClass", "associationQualification");
                attributes.put("associationQualifier", qualifierName);

                // create the node, put in the required attributes
                ldapConnection_.createSubcontext(qualifierLdapName, attributes);

                // add the optional attributes
                modifyAttribute("associationQualifierValue", qualifierValue, qualifierLdapName);

            } else {
                messages_.info("ERROR - Don't know how to handle attributeName of " + qualifierName + " in the "
                        + table + " table.  Was expecting 'modVersion' or 'qualifier'");
            }

        }
        results.close();
        getMultiAttributes.close();

        // put in the collected modVersion values
        modifyMultiValueAttribute("modVersion", (String[]) modVersionValues
                .toArray(new String[modVersionValues.size()]), targetLdapName);
    }

    private void modifyAttribute(String attribute, Boolean value, String ldapName) throws NamingException {
        // log.debug("modifyAttribute called - attribute:" + attribute +
        // " value:" + value + " ldapName:" + ldapName);
        if (value != null) {
            modifyAttribute(attribute, (value + "").toUpperCase(), ldapName);
        }
    }

    /*
     * Modify a single-value attribute in the ldap database. Handles addition,
     * removal, and replacement.
     */
    private void modifyAttribute(String attribute, String value, String ldapName) throws NamingException {
        // log.debug("modifyAttribute called - attribute:" + attribute +
        // " value:" + value + " ldapName:" + ldapName);
        if (value != null && value.length() > 0) {
            ldapConnection_.modifyAttributes(ldapName, DirContext.REPLACE_ATTRIBUTE, new BasicAttributes(attribute,
                    value));
        }
    }

    /*
     * Modify a multivalue attribute on in the ldap database. Handles addition,
     * removal and replacement.
     */
    private void modifyMultiValueAttribute(String attribute, String[] value, String ldapName) throws NamingException {
        // log.debug("modifyMultiValueAttribute called - attribute:" + attribute
        // + " ldapName:" + ldapName);
        if (value != null && value.length > 0) {
            BasicAttribute newAttribute = new BasicAttribute(attribute);
            for (int i = 0; i < value.length; i++) {
                newAttribute.add(value[i]);
            }
            BasicAttributes attributes = new BasicAttributes();
            attributes.put(newAttribute);

            ldapConnection_.modifyAttributes(ldapName, DirContext.REPLACE_ATTRIBUTE, attributes);
        }
    }

    private void handleError(LgMessageDirectorIF md, String message, Throwable exception, boolean rethrowException)
            throws Exception {
        if (rethrowException) {
            md.fatalAndThrowException(message, exception);
        } else {
            md.fatal(message, exception);
        }
    }
}