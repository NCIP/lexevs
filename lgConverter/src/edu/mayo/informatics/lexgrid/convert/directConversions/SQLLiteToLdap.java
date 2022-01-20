
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SchemaViolationException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Convert-o-matic to take content from a SQL Lite database, convert it, and put
 * it into a LDAP database.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 8601 $ checked in on $Date: 2008-06-05
 *          22:28:55 +0000 (Thu, 05 Jun 2008) $
 */
public class SQLLiteToLdap {
    private Connection sqlConnection_;
    private LdapContext ldapConnection_;
    private GenericSQLModifier gsm_;

    private ArrayList associationsToDelete_;
    private ArrayList codingSchemesToDelete_;
    private String defaultLanguage_;

    private static Logger log = LogManager.getLogger("convert.SqlLiteToLdap");
    private LgMessageDirectorIF messages_;

    private SearchControls searchControls_;

    /**
     * Converts the contents of a SQLLite database into a LDAP database
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
    public SQLLiteToLdap(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword,
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
        try {
            try {
                sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
                gsm_ = new GenericSQLModifier(sqlConnection_);
            } catch (ClassNotFoundException e) {
                log.error("The class you specified for your sql driver could not be found on the path.", e);
                messages_
                        .fatalAndThrowException("The class you specified for your sql driver could not be found on the path.");
            }

            connectToLdap(ldapUsername, ldapPassword, ldapAddress, ldapService);

            log.debug("Validating the text values");
            validateCodingSchemeTextValues(codingScheme, failOnError);
            log.debug("Loading the coding scheme");
            String codingSchemeLdapString = loadCodingScheme(codingScheme, failOnError);
            log.debug("Loading coding scheme supported attributes");
            loadCodingSchemeSupportedAttributes(codingSchemeLdapString, codingScheme, failOnError);
            log.debug("loading coded entries");
            loadCodedEntries("dc=Concepts," + codingSchemeLdapString, codingScheme, failOnError);
            log.debug("loading concept properties");
            loadConceptProperty("dc=Concepts," + codingSchemeLdapString, codingScheme, failOnError);
            log.debug("loading association definitions");
            loadAssociationDefinitions(codingSchemeLdapString, codingScheme, failOnError);
            log.debug("loading concept associations");
            loadConceptAssociations(codingSchemeLdapString, codingScheme, failOnError);
            log.debug("deleting flagged associations");
            deleteAssociations(codingSchemeLdapString, failOnError);
            log.debug("deleting falgged coding schemes");
            deleteCodingSchemes(failOnError);
        } catch (Exception e) {
            log.error(e);
            messages_.fatalAndThrowException(e.toString());
        } finally {
            if (sqlConnection_ != null) {
                sqlConnection_.close();
            }
            if (ldapConnection_ != null) {
                ldapConnection_.close();
            }
        }
    }

    /*
     * Figure out the codingschemeName of an id for a coding scheme in the ldap
     * server.
     */
    private String getCurrentCodingSchemeName(String id) throws NamingException {
        log.debug("getCurrentCodingSchemeName called - id:" + id);
        String name = "";
        String filter = "(&(objectClass=codingSchemeClass)(localName=" + id + "))";

        setSearchControls(searchControls_, SearchControls.SUBTREE_SCOPE, 0, 1, false, new String[] {});

        NamingEnumeration results = ldapConnection_.search(name, filter, searchControls_);
        String codingSchemeName = null;
        if (results.hasMore()) {
            SearchResult sRes = (SearchResult) results.next();
            codingSchemeName = sRes.getName();
            codingSchemeName = codingSchemeName.substring("codingScheme=".length(), codingSchemeName.indexOf(','));
        }
        results.close();
        return codingSchemeName;
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

        searchControls_ = new SearchControls();
    }

    /*
     * Load the coding Scheme table information into ldap.
     */
    private String loadCodingScheme(String codingSchemeName, boolean failOnError) throws Exception {
        log.debug("loadCodingScheme called - codingSchemeName:" + codingSchemeName);

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
            attributes.put("dc", "codingSchemes");
            ldapConnection_.createSubcontext("dc=CodingSchemes", attributes);
        } catch (NameAlreadyBoundException e1) {
            // do nothing
        }

        PreparedStatement getCodingSchemeAdditions = sqlConnection_
                .prepareStatement(gsm_
                        .modifySQL("SELECT * FROM codingScheme"
                                + " WHERE codingSchemeName = ? AND (updateEntry = {true} OR addEntry = {true} OR deleteEntry = {true})"));
        getCodingSchemeAdditions.setString(1, codingSchemeName);

        String ldapName = "codingScheme=" + codingSchemeName + ", dc=CodingSchemes";

        ResultSet results = getCodingSchemeAdditions.executeQuery();
        codingSchemesToDelete_ = new ArrayList();
        if (results.next()) {
            String codingSchemeId = results.getString("codingSchemeId");
            String defaultLanguage = results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);
            String representsVersion = results.getString("representsVersion");
            String formalName = results.getString(SQLTableConstants.TBLCOL_FORMALNAME);
            int approxNumConcepts = results.getInt("approxNumConcepts");
            String source = results.getString("source");
            String entityDescription = results.getString("entityDescription");
            String copyright = results.getString("copyright");

            defaultLanguage_ = defaultLanguage;

            boolean update = DBUtility.getbooleanFromResultSet(results, "updateEntry");
            boolean add = DBUtility.getbooleanFromResultSet(results, "addEntry");
            // boolean delete = Utility.getbooleanFromResultSet(results,
            // "deleteEntry");

            if (add) {
                Attributes attributes = new BasicAttributes("objectClass", "codingSchemeClass");
                attributes.put("codingScheme", codingSchemeName);
                attributes.put("representsVersion", representsVersion);
                attributes.put(SQLTableConstants.TBLCOL_REGISTEREDNAME, "urn:oid:" + codingSchemeId);
                attributes.put("supportedLanguage", defaultLanguage);
                attributes.put("supportedFormat", "unknown");
                attributes.put("supportedProperty", "unknown");
                attributes.put(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, defaultLanguage);
                attributes.put(SQLTableConstants.TBLCOL_FORMALNAME, codingSchemeName);

                BasicAttribute temp = new BasicAttribute("localName");
                temp.add(codingSchemeId);
                temp.add(codingSchemeName);
                attributes.put(temp);

                try {
                    ldapConnection_.createSubcontext(ldapName, attributes);

                    attributes = new BasicAttributes("objectClass", "concepts");
                    attributes.put("dc", "Concepts");
                    ldapConnection_.createSubcontext("dc=Concepts," + ldapName, attributes);

                    attributes = new BasicAttributes("objectClass", "relations");
                    attributes.put("dc", "Relations");
                    ldapConnection_.createSubcontext("dc=Relations," + ldapName, attributes);
                } catch (NameAlreadyBoundException e) {
                    log.error("The coding scheme you are trying to add already exists in the database.", e);
                    if (failOnError) {
                        messages_
                                .fatalAndThrowException("The coding scheme you are trying to add already exists in the database.");
                    } else {
                        messages_
                                .info("** ERROR ** The coding scheme you are trying to add already exists in the database.");
                    }
                }
            }

            if (update || add) {
                String currentCodingSchemeName = getCurrentCodingSchemeName(codingSchemeId);
                if (!currentCodingSchemeName.equals(codingSchemeName)) {
                    log.error("Renaming the coding scheme is not supported.  Sorry.");
                    messages_.fatalAndThrowException("Renaming the coding scheme is not supported.  Sorry.");
                }

                modifyAttribute(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, defaultLanguage, ldapName);
                modifyAttribute("representsVersion", representsVersion, ldapName);
                modifyAttribute(SQLTableConstants.TBLCOL_FORMALNAME, formalName, ldapName);
                modifyAttribute("approxNumConcepts", approxNumConcepts + "", ldapName);
                modifyAttribute("source", source, ldapName);
                modifyAttribute("entityDescription", entityDescription, ldapName);
                modifyAttribute("copyright", copyright, ldapName);
                modifyAttribute(SQLTableConstants.TBLCOL_FORMALNAME, codingSchemeName, ldapName);

            } else
            // remove
            {
                codingSchemesToDelete_.add(codingSchemeName);
            }

            messages_.info("Modified 1 coding scheme.");

        }
        results.close();
        getCodingSchemeAdditions.close();
        return ldapName;
    }

    /*
     * Modify a single-value attribute in the ldap database. Handles addition,
     * removal, and replacement.
     */
    private void modifyAttribute(String attribute, String value, String ldapName) throws NamingException {
        log.debug("modifyAttribute called - attribute:" + attribute + " value:" + value + " ldapName:" + ldapName);
        if (value != null && value.length() > 0) {
            ldapConnection_.modifyAttributes(ldapName, DirContext.REPLACE_ATTRIBUTE, new BasicAttributes(attribute,
                    value));
        } else {
            try {
                ldapConnection_.modifyAttributes(ldapName, DirContext.REMOVE_ATTRIBUTE, new BasicAttributes(attribute,
                        null));
            } catch (NoSuchAttributeException e) {
                // log.warn("tried to remove an attribute that was non-existant",
                // e);
                // do nothing - this just means that the attribute didn't exist
                // on the ldap server
            }
        }
    }

    /*
     * Modify a multivalue attribute on in the ldap database. Handles addition,
     * removal and replacement.
     */
    private void modifyMultiValueAttribute(String attribute, String[] value, String ldapName) throws NamingException {
        log.debug("modifyMultiValueAttribute called - attribute:" + attribute + " ldapName:" + ldapName);
        if (value != null && value.length > 0) {
            BasicAttribute newAttribute = new BasicAttribute(attribute);
            for (int i = 0; i < value.length; i++) {
                newAttribute.add(value[i]);
            }
            BasicAttributes attributes = new BasicAttributes();
            attributes.put(newAttribute);

            ldapConnection_.modifyAttributes(ldapName, DirContext.REPLACE_ATTRIBUTE, attributes);
        } else {
            try {
                ldapConnection_.modifyAttributes(ldapName, DirContext.REMOVE_ATTRIBUTE, new BasicAttributes(attribute,
                        null));
            } catch (NoSuchAttributeException e) {
                // log.warn("tried to remove an attribute that was non-existant",
                // e);
                // do nothing - this just means that the attribute didn't exist
                // on the ldap server
            }
        }
    }

    /*
     * Load the coding scheme supported attributes table into ldap.
     */
    private void loadCodingSchemeSupportedAttributes(String name, String codingSchemeName, boolean failOnError)
            throws Exception {
        log
                .debug("loadCodingSchemeSupportedAttributes called - name:" + name + " codingSchemeName:"
                        + codingSchemeName);
        messages_.info("Modifying coding scheme Attributes");
        int attributeCount = 0;
        PreparedStatement getCodingSchemeSupportedAttributesModifications = sqlConnection_
                .prepareStatement("SELECT * FROM codingSchemeSupportedAttributes"
                        + " WHERE codingSchemeName = ? ORDER BY supportedAttributeTag");

        getCodingSchemeSupportedAttributesModifications.setString(1, codingSchemeName);
        ResultSet results = getCodingSchemeSupportedAttributesModifications.executeQuery();

        String currentAttribute = null;
        ArrayList values = new ArrayList();

        // Check for results on the search.
        boolean loop = results.next();
        if (!loop) {
            log.error("There are no attributes for the terminology.  Some are required.");
            if (failOnError) {
                messages_.fatalAndThrowException("There are no attributes for the terminology.  Some are required.");
            } else {
                messages_.info("** ERROR ** - There are no attributes for the terminology.  Some are required.");
            }
        }
        // Get the first row of values.
        String supportedAttributeTag = results.getString("supportedAttributeTag");
        String supportedAttributeValue = results.getString("supportedAttributeValue");
        String urn = results.getString("urn");
        boolean isRemoved = DBUtility.getbooleanFromResultSet(results, "deleteEntry");

        currentAttribute = supportedAttributeTag;
        boolean quit = false;

        while (!quit) {
            // if the attribute from the last row != the attribute from this
            // row, then add the data for the last row.
            if (!currentAttribute.equals(supportedAttributeTag) || !loop) {
                try {
                    if (currentAttribute.equals("Format")) {
                        modifyMultiValueAttribute("supportedFormat", (String[]) values
                                .toArray(new String[values.size()]), name);
                    } else if (currentAttribute.equals("Association")) {
                        modifyMultiValueAttribute("supportedAssociation", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else if (currentAttribute.equals("Context")) {
                        modifyMultiValueAttribute("supportedContext", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else if (currentAttribute.equals("Source")) {
                        modifyMultiValueAttribute("supportedSource", (String[]) values
                                .toArray(new String[values.size()]), name);
                    } else if (currentAttribute.equals("AssociationQualifier")) {
                        modifyMultiValueAttribute("supportedQualifier", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else if (currentAttribute.equals("Property")) {
                        modifyMultiValueAttribute("supportedProperty", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else if (currentAttribute.equals("Language")) {
                        modifyMultiValueAttribute("supportedLanguage", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else if (currentAttribute.equals("CodingScheme")) {
                        modifyMultiValueAttribute("supportedCodingScheme", (String[]) values.toArray(new String[values
                                .size()]), name);
                    } else {
                        log.error("There is an invalid attribute entry " + currentAttribute + " in the sql table.");
                        if (failOnError) {
                            messages_.fatalAndThrowException("There is an invalid attribute entry " + currentAttribute
                                    + " in the sql table.");
                        } else {
                            messages_.info("** ERROR ** There is an invalid attribute entry " + currentAttribute
                                    + " in the sql table.");
                        }
                    }
                }
                // happens if you try to erase required attributes.
                catch (SchemaViolationException e) {
                    if (isRemoved) {
                        log
                                .warn("Could not remove the entry, because it violates the schema to do so.  This is probably ok if you are erasing the entire coding scheme.");
                        messages_
                                .info("** WARNING ** Could not remove the entry, because it violates the schema to do so.");
                        messages_.info("This is probably ok if you are erasing the entire coding scheme.");
                    } else {
                        log.error(e);
                        if (failOnError) {
                            messages_.fatalAndThrowException(e.toString());
                        } else {
                            messages_.info("** ERROR ** " + e.toString());
                        }
                    }
                }
                // don't need to do this when we are going to stop. keeps the
                // counter correct (by not doing this if we
                // are already on the last row)
                if (loop) {
                    // finished adding the last row, set up the next row.
                    currentAttribute = supportedAttributeTag;
                    values.clear();
                    if (!isRemoved) {
                        if (urn != null && urn.length() > 0) {
                            supportedAttributeValue = urn + " " + supportedAttributeValue;
                        }
                        values.add(supportedAttributeValue);
                    }
                    attributeCount++;
                }
            }

            // still on the same attribute (the attribute has more than one
            // value)
            else {
                if (!isRemoved) {
                    if (urn != null && urn.length() > 0) {
                        supportedAttributeValue = urn + " " + supportedAttributeValue;
                    }
                    values.add(supportedAttributeValue);
                }
                attributeCount++;
            }

            // I need to go through the loop one time after the last result. So,
            // I set quit equal to
            // true the time after the last result
            quit = !loop;
            // Then, make sure I don't call results.next if I have already
            // gotten to the end
            if (!quit) {
                loop = results.next();
                // and don't try to set the values for the next loop if I am at
                // the end...
                if (loop) {

                    supportedAttributeTag = results.getString("supportedAttributeTag");
                    supportedAttributeValue = results.getString("supportedAttributeValue");
                    urn = results.getString("urn");
                    isRemoved = DBUtility.getbooleanFromResultSet(results, "deleteEntry");
                }
            }
        }
        results.close();
        getCodingSchemeSupportedAttributesModifications.close();
        messages_.info("Added " + attributeCount + " attributes");
    }

    /*
     * load the association defintitions table into ldap
     */
    private void loadAssociationDefinitions(String name, String codingSchemeName, boolean failOnError) throws Exception {
        // TODO - this has a big bug right now - it assumes that the
        // associations came from the dc=Relations
        // node - when in fact, they may not have. Not sure how to handle this
        // yet.
        log.debug("loadAssociationDefinitions called - name:" + name + " codingSchemeName:" + codingSchemeName);
        int associationCount = 0;

        PreparedStatement getAssociationDefinitions = sqlConnection_
                .prepareStatement("SELECT * FROM associationDefinition" + " WHERE codingSchemeName = ?");

        getAssociationDefinitions.setString(1, codingSchemeName);
        ResultSet results = getAssociationDefinitions.executeQuery();

        messages_.info("Modifying Assocation Definitions");
        associationCount = 0;

        ArrayList associations = new ArrayList();
        associationsToDelete_ = new ArrayList();

        while (results.next()) {
            associationCount++;
            String association = results.getString("association");
            String reverseName = results.getString("reverseName");
            String associationDescription = results.getString("associationDescription");
            boolean isTransitive = DBUtility.getbooleanFromResultSet(results, "isTransitive");
            boolean isSymmetric = DBUtility.getbooleanFromResultSet(results, "isSymmetric");
            boolean isReflexive = DBUtility.getbooleanFromResultSet(results, "isReflexive");

            boolean addEntry = DBUtility.getbooleanFromResultSet(results, "addEntry");
            boolean updateEntry = DBUtility.getbooleanFromResultSet(results, "updateEntry");
            boolean deleteEntry = DBUtility.getbooleanFromResultSet(results, "deleteEntry");

            if (!deleteEntry) {
                associations.add(association);
            }

            if (addEntry) {
                BasicAttributes attributes = new BasicAttributes("association", association);
                attributes.put("objectClass", "associationClass");
                attributes.put("forwardName", association);
                attributes.put("reverseName", reverseName);
                try {
                    ldapConnection_
                            .createSubcontext("association=" + association + ",dc=Relations," + name, attributes);
                } catch (NameAlreadyBoundException e) {
                    if (failOnError) {
                        messages_
                                .fatalAndThrowException("The association you are trying to add already exists in the database.");
                    } else {
                        messages_
                                .info("** ERROR ** The association you are trying to add already exists in the database.");
                    }
                }
            }

            if (updateEntry || addEntry) {
                modifyAttribute("reverseName", reverseName, "association=" + association + ",dc=Relations," + name);
                modifyAttribute("entityDescription", associationDescription, "association=" + association
                        + ",dc=Relations," + name);
                modifyAttribute("association", association, "association=" + association + ",dc=Relations," + name);
                modifyAttribute("isTransitive", (isTransitive + "").toUpperCase(), "association=" + association
                        + ",dc=Relations," + name);
                modifyAttribute("isSymmetric", (isSymmetric + "").toUpperCase(), "association=" + association
                        + ",dc=Relations," + name);
                modifyAttribute("isReflexive", (isReflexive + "").toUpperCase(), "association=" + association
                        + ",dc=Relations," + name);

            } else if (deleteEntry) {
                associationsToDelete_.add(association);
            }
        }

        results.close();
        // set up the supportedAssociations - this will overwrite all of them.
        modifyMultiValueAttribute("supportedAssociation", (String[]) associations.toArray(new String[associations
                .size()]), name);

        messages_.info("Modified or loaded " + associationCount + " associations definition");
        getAssociationDefinitions.close();
    }

    /*
     * Delete the associations that are supposed to be deleted.
     */
    private void deleteAssociations(String ldapName, boolean failOnError) throws Exception {
        log.debug("deleteAssociations called - ldapName" + ldapName);
        messages_.info("removing deleted associations.");
        for (int i = 0; i < associationsToDelete_.size(); i++) {
            try {
                ldapConnection_.destroySubcontext("association=" + associationsToDelete_.get(i) + ",dc=Relations,"
                        + ldapName);
            } catch (Exception e) {
                log.error("Couldn't remove the association " + associationsToDelete_.get(i)
                        + ".  This association probably still has sub-entries.", e);
                if (failOnError) {
                    messages_.fatalAndThrowException("Couldn't remove the association " + associationsToDelete_.get(i)
                            + ".  This association probably still has sub-entries.\n" + e.toString());
                } else {
                    messages_.info("** ERROR ** Couldn't remove the association " + associationsToDelete_.get(i)
                            + ".  This association probably still has sub-entries.\n" + e.toString());
                }
            }
        }
    }

    /*
     * delete coding schemes that are supposed to be deleted
     */
    private void deleteCodingSchemes(boolean failOnError) throws Exception {
        log.debug("deleteCodingSchemes called");
        messages_.info("removing deleted coding schemes.");
        for (int i = 0; i < codingSchemesToDelete_.size(); i++) {
            try {
                ldapConnection_.destroySubcontext("dc=Concepts, codingScheme=" + (String) codingSchemesToDelete_.get(i)
                        + ",dc=codingSchemes");
                ldapConnection_.destroySubcontext("dc=Relations, codingScheme="
                        + (String) codingSchemesToDelete_.get(i) + ",dc=codingSchemes");
                ldapConnection_.destroySubcontext("codingScheme=" + (String) codingSchemesToDelete_.get(i)
                        + ",dc=codingSchemes");
            } catch (Exception e) {
                log.error("Couldn't remove the codingScheme " + codingSchemesToDelete_.get(i)
                        + ".  This association probably still has sub-entries.\n", e);
                if (failOnError) {
                    messages_.fatalAndThrowException("Couldn't remove the codingScheme "
                            + codingSchemesToDelete_.get(i) + ".  This association probably still has sub-entries.\n"
                            + e.toString());
                } else {
                    messages_.info("** ERROR ** Couldn't remove the codingScheme " + codingSchemesToDelete_.get(i)
                            + ".  This association probably still has sub-entries.\n" + e.toString());
                }
            }
        }
    }

    /*
     * load the coded entries table into ldap.
     */
    private void loadCodedEntries(String name, String codingSchemeName, boolean failOnError) throws Exception {
        log.debug("loadCodedEntries called - name:" + name + " codingSchemeName:" + codingSchemeName);
        messages_.info("modifying concept codes");
        PreparedStatement getConceptCodeModifications = sqlConnection_
                .prepareStatement(gsm_
                        .modifySQL("SELECT * FROM codedEntry"
                                + " WHERE codingSchemeName = ? AND (addEntry = {true} OR updateEntry = {true} OR deleteEntry = {true})"));

        PreparedStatement checkPropertiesTable = sqlConnection_
                .prepareStatement("SELECT count(conceptProperty.codingSchemeName) as found" + " FROM conceptProperty"
                        + " WHERE codingSchemeName = ?" + " AND conceptCode = ?" + " AND property = ?");

        getConceptCodeModifications.setString(1, codingSchemeName);
        ResultSet results = getConceptCodeModifications.executeQuery();

        int codeCount = 0;
        while (results.next()) {
            boolean isActive = DBUtility.getbooleanFromResultSet(results, "isActive");
            String conceptCode = results.getString("conceptCode");
            String conceptStatus = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
            String preferredDesignation = results.getString("conceptName");
            String preferredDescription = results.getString("conceptDescription");
            boolean deleteEntry = DBUtility.getbooleanFromResultSet(results, "deleteEntry");
            boolean addEntry = DBUtility.getbooleanFromResultSet(results, "addEntry");

            String tempLdapName = "conceptCode=" + DBUtility.escapeLdapCode(conceptCode) + "," + name;
            if (deleteEntry) {
                String filter = "(objectClass=*)";

                setSearchControls(searchControls_, SearchControls.SUBTREE_SCOPE, 0, 0, false, new String[] {});

                NamingEnumeration ldapResults = null;
                try {
                    ldapResults = ldapConnection_.search(tempLdapName, filter, searchControls_);
                    // remove subcontexts (this will probably break if there are
                    // more than one level - but there should
                    // never be)
                    while (ldapResults.hasMore()) {
                        SearchResult sRes = (SearchResult) ldapResults.next();
                        if (sRes.getName().length() > 0) {
                            ldapConnection_.destroySubcontext(sRes.getName() + "," + tempLdapName);
                        }
                    }
                    ldapConnection_.destroySubcontext(tempLdapName);

                    // TODO remove all relations this takes part in.
                } catch (NameNotFoundException e) {
                    log.error("The code you are trying to remove '" + conceptCode + "' does not exist in ldap." + e);
                    if (failOnError) {
                        messages_.fatalAndThrowException("The code you are trying to remove '" + conceptCode
                                + "' does not exist in ldap.");
                    } else {
                        messages_.info("** ERROR **The code you are trying to remove '" + conceptCode
                                + "' does not exist in ldap.");
                    }
                }

            } else
            // a modification or addition
            {
                if (addEntry) {
                    Attributes attributes = new BasicAttributes("objectClass", "codedEntry");
                    attributes.put("conceptCode", conceptCode);
                    try {
                        ldapConnection_.createSubcontext(tempLdapName, attributes);

                        // If there aren't any entries in the properties table
                        // for this row, put in the
                        // definition and presentation from the codedEntry table
                        checkPropertiesTable.setString(1, codingSchemeName);
                        checkPropertiesTable.setString(2, conceptCode);
                        checkPropertiesTable.setString(3, "definition");

                        ResultSet propertiesResult = checkPropertiesTable.executeQuery();
                        if (propertiesResult.next() && !propertiesResult.getBoolean("found")
                                && preferredDescription != null && preferredDescription.length() > 0) {
                            createProperty(tempLdapName, "D-1", "definition", true, preferredDescription, null, null);
                        }

                        checkPropertiesTable.setString(1, codingSchemeName);
                        checkPropertiesTable.setString(2, conceptCode);
                        checkPropertiesTable.setString(3, "textualPresentation");

                        propertiesResult = checkPropertiesTable.executeQuery();
                        if (propertiesResult.next() && propertiesResult.getInt("found") == 0
                                && preferredDesignation != null && preferredDesignation.length() > 0) {
                            createProperty(tempLdapName, "T-1", "textualPresentation", true, preferredDesignation,
                                    null, null);
                        }
                    } catch (NameAlreadyBoundException e) {
                        log.error("The concept code '" + conceptCode
                                + " ' that you are trying to add already exists in the database.", e);
                        if (failOnError) {
                            messages_.fatalAndThrowException("The concept code '" + conceptCode
                                    + " ' that you are trying to add already exists in the database.");
                        } else {
                            messages_.info("** ERROR ** The concept code '" + conceptCode
                                    + "' that you are trying to add already exists in the database.");
                        }
                    }
                }
                modifyAttribute("isActive", (isActive + "").toUpperCase(), tempLdapName);
                modifyAttribute("entityDescription", preferredDesignation, tempLdapName);
                modifyAttribute(SQLTableConstants.TBLCOL_CONCEPTSTATUS, conceptStatus, tempLdapName);

                // modify the two prefered entries...
                if (!addEntry) {
                    String filter = "(&(|(objectClass=definition)(property=textualPresentation))(isPreferred=TRUE)(|(!(language=*))(language="
                            + defaultLanguage_ + ")))";

                    setSearchControls(searchControls_, SearchControls.SUBTREE_SCOPE, 0, 0, false, new String[] {
                            "propertyId", "objectClass" });

                    NamingEnumeration ldapResults = ldapConnection_.search(tempLdapName, filter, searchControls_);
                    // remove subcontexts (this will probably break if there are
                    // more than one level - but there should
                    // never be)
                    while (ldapResults.hasMore()) {
                        SearchResult sRes = (SearchResult) ldapResults.next();
                        String propertyId = (String) sRes.getAttributes().get("propertyId").get();

                        if (sRes.getAttributes().get("objectClass").contains("definition")) {
                            modifyAttribute("text", preferredDescription, "propertyId=" + propertyId + ","
                                    + tempLdapName);
                        } else
                        // must be textual presentation
                        {
                            modifyAttribute("text", preferredDesignation, "propertyId=" + propertyId + ","
                                    + tempLdapName);
                        }
                    }
                }
            }

            if (codeCount % 10 == 0) {
                messages_.busy();
            }
            if (++codeCount % 1000 == 0) {
                messages_.info("modified or added " + codeCount + " concept Codes...");
            }
        }
        results.close();
        messages_.info("modified or added " + codeCount + " concept Codes");
        getConceptCodeModifications.close();
        checkPropertiesTable.close();

    }

    /*
     * load the concept property table into ldap
     */
    private void loadConceptProperty(String ldapName, String codingSchemeName, boolean failOnError) throws Exception {
        log.debug("loadConceptProperty called - ldapName:" + ldapName + " codingSchemeName:" + codingSchemeName);
        messages_.info("modifying concept properties");
        int propertyCount = 0;
        PreparedStatement getConceptPropertyChanges = sqlConnection_
                .prepareStatement(gsm_
                        .modifySQL("SELECT * FROM  conceptProperty"
                                + " WHERE codingSchemeName = ? AND (addEntry = {true} OR updateEntry = {true} OR deleteEntry = {true})"));

        getConceptPropertyChanges.setString(1, codingSchemeName);

        ResultSet results = getConceptPropertyChanges.executeQuery();

        while (results.next()) {
            String conceptCode = results.getString("conceptCode");
            String propertyId = results.getString("propertyId");
            String propertyText = results.getString("propertyText");
            String language = results.getString("language");
            String format = results.getString("format");
            String property = results.getString("property");
            boolean isPreferred = DBUtility.getbooleanFromResultSet(results, "isPreferred");
            boolean addEntry = DBUtility.getbooleanFromResultSet(results, "addEntry");
            // boolean updateEntry = results.getBoolean("updateEntry");
            boolean deleteEntry = DBUtility.getbooleanFromResultSet(results, "deleteEntry");

            String tempLdapName = "conceptCode=" + DBUtility.escapeLdapCode(conceptCode) + "," + ldapName;
            if (deleteEntry) {
                try {
                    ldapConnection_.destroySubcontext("propertyId=" + propertyId + "," + tempLdapName);
                } catch (NameNotFoundException e) {
                    log
                            .warn(
                                    "The property '"
                                            + propertyId
                                            + "' does not exist for concept '"
                                            + conceptCode
                                            + "'.  This is probably because the parent concept has already been removed - so this is not an error since you wanted to erase it anyway.",
                                    e);
                    // I'm just not going to say anything right now. Not a real
                    // problem.
                    // messages_.info("** WARNING ** The property '" +
                    // propertyId + "' does not exist for concept
                    // '" +
                    // conceptCode + "'.");
                    // messages_.info("This is probably because the parent
                    // concept has already been removed - so
                    // this is
                    // not an error since you wanted to erase it anyway.");
                }
            } else if (addEntry) {
                try {
                    createProperty(tempLdapName, propertyId, property, isPreferred, propertyText, language, format);
                } catch (NameAlreadyBoundException e) {
                    log.error("The property '" + propertyId + " ' already exists for the concept '" + conceptCode
                            + "' .", e);
                    if (failOnError) {
                        messages_.fatalAndThrowException("The property '" + propertyId
                                + " ' already exists for the concept '" + conceptCode + "' .");
                    } else {
                        messages_.info("** ERROR ** The property '" + propertyId
                                + " ' already exists for the concept '" + conceptCode + "' .");
                    }
                }
            } else
            // update
            {
                modifyAttribute("text", propertyText, "propertyId=" + propertyId + "," + tempLdapName);
                modifyAttribute("language", language, "propertyId=" + propertyId + "," + tempLdapName);
                modifyAttribute("presentationFormat", format, "propertyId=" + propertyId + "," + tempLdapName);
                modifyAttribute("isPreferred", (isPreferred + "").toUpperCase(), "propertyId=" + propertyId + ","
                        + tempLdapName);
            }

            propertyCount++;
            if (propertyCount % 10 == 0) {
                messages_.busy();
            }
            if (propertyCount % 1000 == 0) {
                messages_.info("modified, added or removed " + propertyCount + " properties...");
            }
        }
        results.close();
        getConceptPropertyChanges.close();
        messages_.info("modified, added or removed " + propertyCount + " properties");
    }

    /*
     * helper method to create a property in the ldap database
     */
    private void createProperty(String parentLdapName, String id, String property, boolean isPreferred, String text,
            String language, String format) throws NamingException {
        log.debug("createProperty called - parentLdapName:" + parentLdapName + " id:" + id + " property:" + property
                + " isPreferred:" + isPreferred + " text:" + text + " language:" + language + " format:" + format);
        BasicAttributes attributes = new BasicAttributes("propertyId", id);
        BasicAttribute temp = new BasicAttribute("objectClass");
        temp.add("propertyClass");
        if (property.equals("textualPresentation")) {
            temp.add("presentation");
        } else if (property.equals("definition")) {
            temp.add("definition");
        }
        attributes.put(temp);
        if (isPreferred) {
            attributes.put("isPreferred", (isPreferred + "").toUpperCase());
        }
        attributes.put("text", text);
        attributes.put("property", property);
        ldapConnection_.createSubcontext("propertyId=" + id + "," + parentLdapName, attributes);
        // the rest are optional, so I'll let the modify function take care of
        // them.
        modifyAttribute("language", language, "propertyId=" + id + "," + parentLdapName);
        modifyAttribute("presentationFormat", format, "propertyId=" + id + "," + parentLdapName);
    }

    /*
     * load the conceptAssociations table into the ldap database
     */
    private void loadConceptAssociations(String ldapName, String sourceCodingSchemeName, boolean failOnError)
            throws Exception {
        log.debug("loadConceptAssociations called - ldapName:" + ldapName + " sourceCodingSchemeName:"
                + sourceCodingSchemeName);
        int relationCount = 0;

        messages_.info("modifying concept associations.");
        PreparedStatement getAssociations = sqlConnection_
                .prepareStatement(gsm_
                        .modifySQL("SELECT * FROM  conceptAssociation"
                                + " WHERE sourceCodingSchemeName = ? AND (addEntry = {true} OR updateEntry = {true} OR deleteEntry = {true})"));

        getAssociations.setString(1, sourceCodingSchemeName);

        ResultSet results = getAssociations.executeQuery();

        while (results.next()) {
            relationCount++;
            String sourceConceptCode = results.getString("sourceConceptCode");
            String association = results.getString("association");
            // String targetCodingSchemeName =
            // results.getString("targetCodingSchemeName");
            String targetConceptCode = results.getString("targetConceptCode");
            boolean addEntry = DBUtility.getbooleanFromResultSet(results, "addEntry");
            boolean updateEntry = DBUtility.getbooleanFromResultSet(results, "updateEntry");
            // boolean deleteEntry = Utility.getbooleanFromResultSet(results,
            // "deleteEntry");

            if (updateEntry) {
                log.error("Updating entries is not supported on concept associations, since all columns are keys.");
                if (failOnError) {
                    messages_
                            .fatalAndThrowException("Updating entries is not supported on concept associations, since all columns are keys.");
                } else {
                    messages_
                            .info("** ERROR ** Updating entries is not supported on concept associations, since all columns are keys.");
                }
            } else if (addEntry) {
                Attributes attributes;
                try {
                    attributes = new BasicAttributes("objectClass", "associationInstance");
                    attributes.put("sourceConcept", sourceConceptCode);
                    ldapConnection_.createSubcontext("sourceConcept=" + sourceConceptCode + ",association="
                            + association + ",dc=Relations," + ldapName, attributes);
                } catch (NameAlreadyBoundException e) {
                    // this isn't a problem. sources can have multiple targets,
                    // so the source may already exist.
                }

                try {
                    attributes = new BasicAttributes("objectClass", "associationTarget");
                    attributes.put("targetConcept", targetConceptCode);
                    ldapConnection_.createSubcontext("targetConcept=" + targetConceptCode + ",sourceConcept="
                            + sourceConceptCode + ",association=" + association + ",dc=Relations," + ldapName,
                            attributes);
                } catch (NameAlreadyBoundException e) {
                    log.error("The relation to concept code  '" + targetConceptCode
                            + " ' that you are trying to add already exists in the database.", e);
                    if (failOnError) {
                        messages_.fatalAndThrowException("The relation to concept code  '" + targetConceptCode
                                + " ' that you are trying to add already exists in the database.");
                    } else {
                        messages_.info("** ERROR ** The relation to concept code  '" + targetConceptCode
                                + " ' that you are trying to add already exists in the database.");
                    }
                }

                // TODO deal with links
            } else
            // delete entry
            {
                try {
                    ldapConnection_.destroySubcontext("targetConcept=" + targetConceptCode + ",sourceConcept="
                            + sourceConceptCode + ",association=" + association + ",dc=Relations," + ldapName);

                    try {
                        ldapConnection_.destroySubcontext("sourceConcept=" + sourceConceptCode + ",association="
                                + association + ",dc=Relations," + ldapName);
                    } catch (ContextNotEmptyException e) {
                        // do nothing - just means that there were still others
                        // that used this source node
                        // TODO this is dependant on subtree deletes not being
                        // implemented on openldap. To use this
                        // code on an ldap database that does support subtree
                        // deletes, this code needs to be re-written.

                    }
                } catch (NameNotFoundException e) {
                    log.error("The association '" + sourceConceptCode + "' to '" + targetConceptCode
                            + "' that you are trying to remove does not exist.", e);
                    if (failOnError) {
                        messages_.fatalAndThrowException("The association '" + sourceConceptCode + "' to '"
                                + targetConceptCode + "' that you are trying to remove does not exist.");
                    } else {
                        messages_.info("** ERROR ** The association '" + sourceConceptCode + "' to '"
                                + targetConceptCode + "' that you are trying to remove does not exist.");
                    }
                }

            }

            if (relationCount % 10 == 0) {
                messages_.busy();
            }
            if (relationCount % 1000 == 0) {
                messages_.info("loaded/modified " + relationCount + " relations...");
            }
        }
        results.close();
        getAssociations.close();
        messages_.info("loaded " + relationCount + " relations");
    }

    /*
     * do a query to check the integrity of the text values in the database.
     */
    private void validateCodingSchemeTextValues(String codingSchemeName, boolean failOnError) throws Exception {
        log.debug("validateCodingSchemeTextValues called - codingSchemeName:" + codingSchemeName);
        PreparedStatement validateDefinitions = sqlConnection_.prepareStatement(gsm_
                .modifySQL("SELECT codedEntry.conceptCode " + " FROM (codedEntry INNER JOIN conceptProperty"
                        + " ON (codedEntry.codingSchemeName = conceptProperty.codingSchemeName)"
                        + " AND (codedEntry.conceptCode = conceptProperty.conceptCode))" + " INNER JOIN codingScheme"
                        + " ON (conceptProperty.language = codingScheme.defaultLanguage)"
                        + " AND (conceptProperty.codingSchemeName = codingScheme.codingSchemeName)"
                        + " WHERE codedEntry.codingSchemeName = ?" + " AND conceptProperty.property='definition'"
                        + " AND codedEntry.conceptDescription <> propertyText"
                        + " AND conceptProperty.isPreferred={true}"));

        validateDefinitions.setString(1, codingSchemeName);
        ResultSet results = validateDefinitions.executeQuery();
        // one and only one result
        StringBuffer temp = new StringBuffer();
        while (results.next()) {
            temp.append(results.getString("conceptCode") + ",");
        }
        results.close();
        validateDefinitions.close();
        if (temp.length() > 0) {
            if (failOnError) {
                messages_
                        .fatalAndThrowException("Your coded definition in the coded entry table does not match the value of the preferred definition for the concepts\n: "
                                + temp.toString());
            } else {
                messages_
                        .info("** ERROR ** Your coded definition in the coded entry table does not match the value of the preferred definition for the concepts\n: "
                                + temp.toString());
            }

        }

        PreparedStatement validateDesignations = sqlConnection_.prepareStatement(gsm_
                .modifySQL("SELECT codedEntry.conceptCode " + " FROM (codedEntry INNER JOIN conceptProperty"
                        + " ON (codedEntry.codingSchemeName = conceptProperty.codingSchemeName)"
                        + " AND (codedEntry.conceptCode = conceptProperty.conceptCode))" + " INNER JOIN codingScheme"
                        + " ON (conceptProperty.language = codingScheme.defaultLanguage)"
                        + " AND (conceptProperty.codingSchemeName = codingScheme.codingSchemeName)"
                        + " WHERE codedEntry.codingSchemeName = ?"
                        + " AND conceptProperty.property='textualPresentation'"
                        + " AND codedEntry.conceptName <> propertyText" + " AND conceptProperty.isPreferred={true}"));

        validateDesignations.setString(1, codingSchemeName);
        results = validateDesignations.executeQuery();
        // one and only one result
        temp = new StringBuffer();
        while (results.next()) {
            temp.append(results.getString("conceptCode") + ",");
        }
        results.close();
        validateDesignations.close();
        if (temp.length() > 0) {
            log
                    .error("Your code name in the coded entry table does not match the value of the preferred designation for the concepts: "
                            + temp.toString());
            if (failOnError) {
                messages_
                        .fatalAndThrowException("Your code name in the coded entry table does not match the value of the preferred designation for the concepts: "
                                + temp.toString());
            } else {
                messages_
                        .info("** ERROR ** Your code name in the coded entry table does not match the value of the preferred designation for the concepts: "
                                + temp.toString());
            }
        }

    }

    /**
     * Given a searchControls Object, and the values that you wish to set, this
     * method sets those values. This method is used because many of the classes
     * have a single searchControls object that is reused, and not all searches
     * require all of these controls to be set. By using this method, you are
     * requred to set each control to what is should be for this search.
     * 
     * @param scope
     * @param timeout
     * @param limit
     * @param returningObjFlag
     * @param attributes
     * @see SearchControls
     */
    private static void setSearchControls(SearchControls searchControls, int scope, int timeout, long limit,
            boolean returningObjFlag, String[] attributes) {
        log.debug("setSearchControls called - scope:" + scope + " timeout:" + timeout + " limit:" + limit
                + " returnObjFlag:" + returningObjFlag);
        searchControls.setSearchScope(scope);
        searchControls.setTimeLimit(timeout);
        searchControls.setCountLimit(limit);
        searchControls.setDerefLinkFlag(false);
        searchControls.setReturningObjFlag(returningObjFlag);
        searchControls.setReturningAttributes(attributes);
    }
}