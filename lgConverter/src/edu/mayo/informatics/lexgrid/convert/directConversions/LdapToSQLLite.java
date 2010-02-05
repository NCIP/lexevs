/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLLiteTableConstants;
import org.LexGrid.util.sql.lgTables.SQLLiteTableUtilities;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.log4j.Logger;

import com.sun.jndi.ldap.ctl.PagedResultsControl;
import com.sun.jndi.ldap.ctl.PagedResultsResponseControl;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Convert-o-matic to take content from a LDAP database, convert it, and put it
 * into a SQL database.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 8601 $ checked in on $Date: 2008-06-05
 *          22:28:55 +0000 (Thu, 05 Jun 2008) $
 */
public class LdapToSQLLite {
    private Connection sqlConnection_;
    private LdapContext ldapConnection_;
    private LdapContext ldapConnection2_;
    private int pageSize = Constants.ldapPageSize;
    private int quickSearchLimit_ = 180000; // 3 minutes

    private static Logger log = Logger.getLogger("convert.LdapToSqlLite");

    private Hashtable env;

    private Hashtable tempAssociationHolder_;
    private SQLLiteTableUtilities tableUtility_;
    private LgMessageDirectorIF messages_;
    private String defaultLanguage_;
    private boolean enforceIntegrity_ = true;
    private SQLLiteTableConstants tableConstants_;

    /**
     * Converts the contents of an LDAP database into a SQLLite database
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
     *            name of the CodingScheme to be converted
     * @param enforceIntegrity
     *            when true, places certain restrictions on the data - places
     *            foriegn key restraints.
     * @param messages
     *            output for control messages
     * @throws Exception
     */
    public LdapToSQLLite(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword,
            String ldapUsername, String ldapPassword, String ldapAddress, String ldapService, String codingScheme,
            boolean enforceIntegrity, LgMessageDirectorIF messages) throws Exception {
        messages_ = messages;
        enforceIntegrity_ = enforceIntegrity;
        try {
            try {
                log.debug("Connecting to database");
                sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
            } catch (ClassNotFoundException e) {
                messages_.info("The class you specified for your sql driver could not be found on the path.");
            }

            tableUtility_ = new SQLLiteTableUtilities(sqlConnection_);

            log.debug("Creating tables");
            tableUtility_.createAllTables();
            if (enforceIntegrity_) {
                messages_.info("Creating constraints");
                tableUtility_.createTableConstraints();
            } else {
                messages_.info("Removing constraints");
                tableUtility_.dropTableConstraints();
            }

            // If they didn't pass a coding scheme, just create the tables and
            // exit.
            if (codingScheme == null || codingScheme.trim().length() == 0) {
                return;
            }

            messages_.info("Cleaning tables");
            tableUtility_.cleanTables(codingScheme);

            tableConstants_ = new SQLLiteTableConstants();

            messages_.info("Connecting to ldap");
            connectToLdap(ldapUsername, ldapPassword, ldapAddress, ldapService);

            String codingSchemeLdapString = "codingScheme=" + codingScheme + ", dc=codingSchemes";

            messages_.info("Loading coding scheme");
            String codingSchemeName = loadCodingScheme(codingSchemeLdapString);
            messages_.info("Loading supported attributes");
            loadCodingSchemeSupportedAttributes(codingSchemeLdapString, codingSchemeName);
            messages_.info("Loading coded entries");
            loadCodedEntries("dc=concepts, " + codingSchemeLdapString, codingSchemeName);
            messages_.info("Loading association definitions");
            loadAssociationDefinitions(codingSchemeLdapString, codingSchemeName);

            messages_.info("Loading associations");
            Enumeration tempEnum = tempAssociationHolder_.keys();
            while (tempEnum.hasMoreElements()) {
                String association = (String) tempEnum.nextElement();
                loadConceptAssociations(codingSchemeLdapString, codingSchemeName, (String) tempAssociationHolder_
                        .get(association), association);
            }
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed", e);
        } finally {
            if (sqlConnection_ != null) {
                sqlConnection_.close();
            }
            if (ldapConnection_ != null) {
                ldapConnection_.close();
            }
            if (ldapConnection2_ != null) {
                ldapConnection2_.close();
            }

        }
    }

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

        env = new Hashtable();
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
        ldapConnection2_ = new InitialLdapContext(env, null);
    }

    private void reconnectConnection1() throws NamingException {
        log.debug("Trying to reconnect a connection");
        try {
            ldapConnection_.close();
        } catch (NamingException e) {
            log.debug("error closing connection (during reconnect)", e);
        }
        ldapConnection_ = null;
        ldapConnection_ = new InitialLdapContext(env, null);
        log.debug("finished creating new connection");
    }

    private String loadCodingScheme(String name) throws NamingException, SQLException {
        log.debug("loadCodingScheme called");
        PreparedStatement populateCodingScheme = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME));

        Attributes attributes = ldapConnection_.getAttributes(name, new String[] {
                SQLTableConstants.TBLCOL_REGISTEREDNAME, "codingScheme", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE,
                "representsVersion", SQLTableConstants.TBLCOL_FORMALNAME, "approxNumConcepts", "source",
                "entityDescription", "localname" });

        String localName = null;
        if (attributes.get("localName") != null) {
            NamingEnumeration temp = attributes.get("localName").getAll();

            while (temp.hasMore()) {
                localName = (String) temp.next();
                if (DBUtility.validLocalName(localName)) {
                    break;
                } else {
                    localName = null;
                }
            }
        }

        String registeredName = attributes.get(SQLTableConstants.TBLCOL_REGISTEREDNAME) == null ? null
                : (String) attributes.get(SQLTableConstants.TBLCOL_REGISTEREDNAME).get();
        String codingScheme = attributes.get("codingScheme") == null ? null : (String) attributes.get("codingScheme")
                .get();
        String defaultLanguage = attributes.get(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE) == null ? null
                : (String) attributes.get(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE).get();
        String representsVersion = attributes.get("representsVersion") == null ? null : (String) attributes.get(
                "representsVersion").get();
        String formalName = attributes.get(SQLTableConstants.TBLCOL_FORMALNAME) == null ? null : (String) attributes
                .get(SQLTableConstants.TBLCOL_FORMALNAME).get();
        int approxNumConcepts = attributes.get("approxNumConcepts") == null ? 0 : Integer.parseInt((String) attributes
                .get("approxNumConcepts").get());
        String source = attributes.get("source") == null ? "" : (String) attributes.get("source").get();
        String entityDescription = attributes.get("entityDescription") == null ? null : (String) attributes.get(
                "entityDescription").get();
        String copyright = attributes.get("copyright") == null ? "" : (String) attributes.get("copyright").get();

        populateCodingScheme.setString(1, codingScheme);
        populateCodingScheme.setString(2, localName == null || localName.length() == 0 ? registeredName : localName);
        populateCodingScheme.setString(3, defaultLanguage);
        populateCodingScheme.setString(4, representsVersion);
        populateCodingScheme.setString(5, formalName);
        populateCodingScheme.setInt(6, approxNumConcepts);
        populateCodingScheme.setString(7, source);
        populateCodingScheme.setString(8, entityDescription);
        populateCodingScheme.setString(9, copyright);
        DBUtility.setBooleanOnPreparedStatment(populateCodingScheme, 10, null, true);
        DBUtility.setBooleanOnPreparedStatment(populateCodingScheme, 11, null, true);
        DBUtility.setBooleanOnPreparedStatment(populateCodingScheme, 12, null, true);

        defaultLanguage_ = defaultLanguage;

        populateCodingScheme.executeUpdate();
        populateCodingScheme.close();
        messages_.info("Added 1 coding scheme.");
        return codingScheme;
    }

    private void loadCodingSchemeSupportedAttributes(String name, String codingSchemeName) throws NamingException,
            SQLException {
        log.debug("loadCodingSchemeSupportedAttributes called");
        messages_.info("Adding Attributes");
        int attributeCount = 0;
        PreparedStatement populateCodingSchemeAttributes = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME_SUPPORTED_ATTRIBUTES));

        Attributes attributes = ldapConnection_.getAttributes(name, null);

        NamingEnumeration allAttributes = attributes.getAll();

        while (allAttributes.hasMore()) {
            Attribute temp = (Attribute) allAttributes.next();

            String supportedAttribute = temp.getID();
            if (supportedAttribute.equals("supportedFormat")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Format");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedAssociation")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Association");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedContext")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Context");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedSource")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Source");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedQualifier")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "AssociationQualifier");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedProperty")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String supportedAttributeValue = ((String) attributeValues.next());
                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Property");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, "");
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.equals("supportedLanguage")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String lang = "";
                    String urn = "";
                    String supportedAttributeValue = ((String) attributeValues.next());
                    int loc = supportedAttributeValue.indexOf(" ");
                    if (loc > 0) {
                        lang = supportedAttributeValue.substring(loc + 1, supportedAttributeValue.length());
                        urn = supportedAttributeValue.substring(0, loc);
                    } else {
                        lang = supportedAttributeValue;
                    }

                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "Language");
                    populateCodingSchemeAttributes.setString(3, lang);
                    populateCodingSchemeAttributes.setString(4, urn);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            } else if (supportedAttribute.equals("supportedCodingScheme")) {
                NamingEnumeration attributeValues = temp.getAll();
                while (attributeValues.hasMore()) {
                    String urn = ((String) attributeValues.next());
                    String supportedAttributeValue = "";
                    int x = urn.indexOf(" ");

                    if (x != -1) {
                        supportedAttributeValue = urn.substring(x + 1);
                        urn = urn.substring(0, x);
                    }

                    populateCodingSchemeAttributes.setString(1, codingSchemeName);
                    populateCodingSchemeAttributes.setString(2, "CodingScheme");
                    populateCodingSchemeAttributes.setString(3, supportedAttributeValue);
                    populateCodingSchemeAttributes.setString(4, urn);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);
                    attributeCount++;
                    populateCodingSchemeAttributes.executeUpdate();
                }
            }

            else if (supportedAttribute.startsWith("supported")) {
                log.error("didn't put " + supportedAttribute);
                messages_.info("didn't put " + supportedAttribute);
            }
        }
        populateCodingSchemeAttributes.close();
        messages_.info("Added " + attributeCount + " attributes");
    }

    private void loadAssociationDefinitions(String name, String codingSchemeName) throws NamingException, SQLException {
        log.debug("loadAssociationDefinitions called");
        int associationCount = 0;
        PreparedStatement loadAssociationDefinitions = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_ASSOCIATION_DEFINITION));
        try {
            messages_.info("Adding Assocation Definitions");
            tempAssociationHolder_ = new Hashtable();
            associationCount = 0;

            // This is a subtree search because SQL Lite doesn't keep track of
            // the dc=Relations nodes.
            // could make loading back into ldap interesting...
            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(0);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchControls.setTimeLimit(quickSearchLimit_);
            searchControls.setReturningAttributes(new String[] { "association", "reverseName", "entityDescription",
                    "isTransitive", "isSymmetric", "isReflexive", "targetCodingScheme" });

            String filter = "(objectClass=associationClass)";

            log.debug("searching on connection 1 for association definitions");
            NamingEnumeration tempEnum = ldapConnection_.search(name, filter, searchControls);

            while (tempEnum.hasMore()) {
                SearchResult sRes = (SearchResult) tempEnum.next();
                Attributes attributes = sRes.getAttributes();

                String association = attributes.get("association") == null ? null : (String) attributes.get(
                        "association").get();
                String reverseName = attributes.get("reverseName") == null ? null : (String) attributes.get(
                        "reverseName").get();
                String entityDescription = attributes.get("entityDescription") == null ? null : (String) attributes
                        .get("entityDescription").get();
                Boolean isTransitive = attributes.get("isTransitive") == null ? new Boolean(false) : new Boolean(
                        (String) attributes.get("isTransitive").get());
                Boolean isSymmetric = attributes.get("isSymmetric") == null ? new Boolean(false) : new Boolean(
                        (String) attributes.get("isSymmetric").get());
                Boolean isReflexive = attributes.get("isReflexive") == null ? new Boolean(false) : new Boolean(
                        (String) attributes.get("isReflexive").get());

                String targetCodingScheme = attributes.get("targetCodingScheme") == null ? codingSchemeName
                        : (String) attributes.get("targetCodingScheme").get();

                loadAssociationDefinitions.setString(1, codingSchemeName);
                loadAssociationDefinitions.setString(2, association);
                loadAssociationDefinitions.setString(3, reverseName);
                loadAssociationDefinitions.setString(4, entityDescription);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 5, isTransitive, true);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 6, isSymmetric, true);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 7, isReflexive, true);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 8, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 9, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadAssociationDefinitions, 10, null, true);
                try {
                    loadAssociationDefinitions.executeUpdate();
                    associationCount++;

                    String temp = sRes.getName().toString();

                    tempAssociationHolder_.put(temp, targetCodingScheme);
                } catch (SQLException e1) {
                    log.error("Problem loading associations", e1);
                    messages_
                            .info("The association '"
                                    + association
                                    + "' already exists in the database?  (Check log for error details) This can happen, because SQLLite ignores the relations - and only enters associations");
                }

            }

        } catch (NameNotFoundException e) {
            messages_.info("No Associations found.");
        } finally {
            loadAssociationDefinitions.close();
        }

        messages_.info("Loaded " + associationCount + " associations definition");

    }

    private void loadCodedEntries(String name, String codingSchemeName) throws NamingException, SQLException,
            IOException {
        log.debug("loadCodedEntries called");
        messages_.info("loading concept codes");
        PreparedStatement loadCodedEntry = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODED_ENTRY));

        int codeCount = 0;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(0);
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchControls.setTimeLimit(0);
        searchControls.setReturningAttributes(new String[] { "conceptCode", "isActive",
                SQLTableConstants.TBLCOL_CONCEPTSTATUS });

        String filter = "(&(objectClass=codedEntry))";
        ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize) });

        byte[] cookie = null;
        do {
            log.debug("searching on connection2");
            NamingEnumeration result = ldapConnection2_.search(name, filter, searchControls);

            while (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();

                Attributes attributes = sRes.getAttributes();

                String code = (String) attributes.get("conceptCode").get();

                boolean isActive = true;
                if (attributes.get("isActive") != null) {
                    isActive = new Boolean((String) attributes.get("isActive").get()).booleanValue();
                }

                String conceptStatus = attributes.get(SQLTableConstants.TBLCOL_CONCEPTSTATUS) == null ? "Active"
                        : (String) attributes.get(SQLTableConstants.TBLCOL_CONCEPTSTATUS).get();

                String preferredDesignation = "";
                String preferredDescription = "";
                try {
                    preferredDesignation = getConceptDetail("conceptCode=" + DBUtility.escapeLdapCode(code) + ", "
                            + name, "(&(property=textualPresentation)(isPreferred=TRUE)(|(!(language=*))(language="
                            + defaultLanguage_ + ")))", true);
                    preferredDescription = getConceptDetail("conceptCode=" + DBUtility.escapeLdapCode(code) + ", "
                            + name, "(&(property=definition)(isPreferred=TRUE)(|(!(language=*))(language="
                            + defaultLanguage_ + ")))", true);
                } catch (NamingException e3) {
                    log.error("Could not retreive preferedDesignation and/or preferredDescription for conceptCode "
                            + code, e3);
                    messages_.info("MISSING VALUE FOR conceptCode = " + code + " SEE LOG for details.");
                    reconnectConnection1(); // get a clean connection for
                                            // continuing.
                } catch (Exception e3) {
                    log.error("Could not retreive preferedDesignation and/or preferredDescription for conceptCode "
                            + code, e3);
                    messages_.info("MISSING VALUE FOR conceptCode = " + code + " SEE LOG for details.");
                    reconnectConnection1(); // get a clean connection for
                                            // continuing.
                }

                loadCodedEntry.setString(1, codingSchemeName);
                loadCodedEntry.setString(2, code);
                loadCodedEntry.setString(3, preferredDesignation);
                loadCodedEntry.setString(4, preferredDescription);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 5, new Boolean(isActive), true);
                loadCodedEntry.setString(6, conceptStatus);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 7, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 8, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 9, null, true);

                try {
                    log.debug("inserting code '" + code + "' into database");
                    loadCodedEntry.executeUpdate();
                } catch (SQLException e) {
                    log.error(e);
                    messages_.info("***ERROR*** - codingSchemeName: '" + codingSchemeName + "' code: '" + code
                            + "' preferredDesignation: '" + preferredDesignation + "' preferredDescription: '"
                            + preferredDescription + "' isActive: '" + isActive + "' "
                            + SQLTableConstants.TBLCOL_CONCEPTSTATUS + ": " + conceptStatus + "'");
                    throw e;
                }

                try {
                    loadConceptProperty("conceptCode=" + DBUtility.escapeLdapCode(code) + ", " + name,
                            codingSchemeName, code, true);
                } catch (NamingException e) {
                    log.error("MISSING PROPERTIES FOR conceptCode = " + code, e);
                    messages_.info("MISSING PROPERTIES FOR conceptCode = " + code + " SEE LOG for more details.");
                    reconnectConnection1(); // get a clean connection for
                                            // continuing.
                } catch (SQLException e) {
                    log.error("MISSING PROPERTIES FOR conceptCode = " + code, e);
                    messages_.info("MISSING PROPERTIES FOR conceptCode = " + code + " SEE LOG for more details.");
                } catch (Exception e) {
                    log.error("MISSING PROPERTIES FOR conceptCode = " + code, e);
                    messages_.info("MISSING PROPERTIES FOR conceptCode = " + code + " SEE LOG for more details.");
                    reconnectConnection1(); // get a clean connection for
                                            // continuing.
                }
                messages_.busy();
                codeCount++;

            }

            log.debug("getting controls");
            Control[] controls = ldapConnection2_.getResponseControls();
            if (controls != null) {
                for (int i = 0; i < controls.length; i++) {
                    if (controls[i] instanceof PagedResultsResponseControl) {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                        cookie = prrc.getCookie();
                        log.debug("found cookie");
                        break;
                    }
                }
            } else {
                cookie = null;
            }

            if (cookie != null) {
                log.debug("setting cookie");
                ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie,
                        Control.CRITICAL) });
                if (codeCount % 1000 == 0) {
                    messages_.info("Loaded " + codeCount + " codes...");
                }
            }

        } while (cookie != null);
        messages_.info("loaded " + codeCount + " concept Codes");
        loadCodedEntry.close();
    }

    private void loadConceptProperty(String ldapName, String codingSchemeName, String code, boolean autoRetry)
            throws SQLException, NamingException {
        log.debug("loadConceptProperty called - ldapName: " + ldapName + " codingSchemeName: " + codingSchemeName
                + " code: " + code);
        String property = "";
        String propertyId = "";
        PreparedStatement loadConceptProperty = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_PROPERTY));
        try {
            int propertyCount = 0;

            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(0);
            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            searchControls.setTimeLimit(quickSearchLimit_);
            searchControls.setReturningAttributes(new String[] { "property", "propertyId", "language", "isPreferred",
                    "text", "presentationFormat" });

            log.debug("Searching on connection1");
            NamingEnumeration result = ldapConnection_.search(ldapName, "(objectClass=propertyClass)", searchControls);

            while (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();
                Attributes attributes = sRes.getAttributes();

                property = (String) attributes.get("property").get();
                propertyId = (String) attributes.get("propertyId").get();

                loadConceptProperty.setString(1, codingSchemeName);
                loadConceptProperty.setString(2, code);
                loadConceptProperty.setString(3, property);
                loadConceptProperty.setString(4, propertyId);
                loadConceptProperty.setString(5, attributes.get("text") == null ? null : (String) attributes
                        .get("text").get());
                loadConceptProperty.setString(6, attributes.get("language") == null ? "" : (String) attributes.get(
                        "language").get());
                loadConceptProperty.setString(7, attributes.get("presentationFormat") == null ? ""
                        : (String) attributes.get("presentationFormat").get());
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 8,
                        attributes.get("isPreferred") == null ? new Boolean("False") : new Boolean((String) attributes
                                .get("isPreferred").get()), true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 9, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 10, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 11, null, true);

                loadConceptProperty.executeUpdate();
                propertyCount++;
            }
        } catch (SQLException e) {
            log.error(e);
            messages_.info("***ERROR*** - problem loading properties for a code  codingSchemeName: '"
                    + codingSchemeName + "' code: '" + code + "' property: '" + property + "' propertyId: '"
                    + propertyId + "'");

            throw e;
        } catch (NamingException e) {
            if (autoRetry) {
                // pass false to make sure we dont infinitely loop...
                log.debug("Retrying a call of loadConceptProperty");
                reconnectConnection1();
                loadConceptProperty(ldapName, codingSchemeName, code, false);
            } else {
                log.error("***ERROR*** - problem loading properties for a code  codingSchemeName: '" + codingSchemeName
                        + "' code: '" + code + "' property: '" + property + "' propertyId: '" + propertyId + "'", e);
                messages_.info("***ERROR*** - problem loading properties for a code  codingSchemeName: '"
                        + codingSchemeName + "' code: '" + code + "' property: '" + property + "' propertyId: '"
                        + propertyId + "'");

                throw e;
            }

        } finally {
            loadConceptProperty.close();
        }

        // messages_.info("loaded " + propertyCount + " properties");
    } /*
       * This is a helper method for loading the conceptCode table, it is used
       * to get the preferred designation and defintion
       */

    private String getConceptDetail(String name, String filter, boolean autoRetry) throws NamingException {
        log.debug("getConceptDetail called - name: " + name + " filter: " + filter);
        String resultToReturn = "";
        try {
            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(1);
            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            searchControls.setTimeLimit(quickSearchLimit_);
            searchControls.setReturningAttributes(new String[] { "text" });

            NamingEnumeration result = ldapConnection_.search(name, filter, searchControls);

            if (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();
                resultToReturn = (String) sRes.getAttributes().get("text").get();
            }
            log.debug("returning (successfully) from getConceptDetail");
        } catch (NamingException e) {
            if (autoRetry) {
                // pass false to make sure we dont infinitely loop...
                log.debug("Retrying a call of getConceptDetail");
                reconnectConnection1();
                return getConceptDetail(name, filter, false);
            } else {
                log.error("There was an error calling getConceptDetail.  Called with: - name: " + name + " filter: "
                        + filter, e);
                throw e;
            }
        }
        return resultToReturn;
    }

    private void loadConceptAssociations(String ldapName, String sourceCodingSchemeName, String targetCodingSchemeName,
            String association) throws SQLException, NamingException, IOException {
        log.debug("loadConceptAssociations called - association: " + association);
        messages_.info("loading associations for '" + association + "'.");
        PreparedStatement loadConceptAssociations = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_ASSOCIATION));

        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(0);
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchControls.setTimeLimit(0);
        searchControls.setReturningAttributes(new String[] { "sourceConcept", "sourceCodingScheme" });

        log.debug("setting controls");
        ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize) });

        int relationCount = 0;

        byte[] cookie = null;
        do {

            log.debug("searching on connection2");
            NamingEnumeration result = ldapConnection2_.search(association + ", " + ldapName,
                    "(objectClass=associationInstance)", searchControls);

            while (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();
                String sourceConceptCode = (String) sRes.getAttributes().get("sourceConcept").get();

                if (sRes.getAttributes().get("sourceCodingScheme") != null) {
                    String sourceCodingScheme = (String) sRes.getAttributes().get("sourceCodingScheme").get();
                    if (sourceCodingScheme != null && sourceCodingScheme.length() > 0) {
                        messages_
                                .info("***ERROR loading associations for "
                                        + sourceConceptCode
                                        + " under association "
                                        + association
                                        + " because it has a different sourceCodingScheme (which is not supported in LexGrid Lite");
                        log
                                .error("***ERROR loading associations for "
                                        + sourceConceptCode
                                        + " under association "
                                        + association
                                        + " because it has a different sourceCodingScheme (which is not supported in LexGrid Lite");
                        continue;
                    }
                }

                try {
                    // Actually do the addition here...
                    relationCount += loadAssociationsHelper(sourceConceptCode, association, ldapName,
                            sourceCodingSchemeName, targetCodingSchemeName, loadConceptAssociations, true);
                } catch (Exception e) {
                    messages_.info("***ERROR loading associations for " + sourceConceptCode + " under association "
                            + association);
                    log.error("***ERROR loading associations for " + sourceConceptCode + " under association "
                            + association, e);
                    reconnectConnection1();
                }

            }

            log.debug("getting controls");
            Control[] controls = ldapConnection2_.getResponseControls();
            if (controls != null) {
                for (int i = 0; i < controls.length; i++) {
                    if (controls[i] instanceof PagedResultsResponseControl) {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                        cookie = prrc.getCookie();
                        log.debug("got cookie");
                        break;
                    }
                }
            } else {
                cookie = null;
            }

            if (cookie != null) {
                log.debug("setting controls for next page");
                ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie,
                        Control.CRITICAL) });
            }

        } while (cookie != null);
        messages_.info("loaded " + relationCount + " relations");
        loadConceptAssociations.close();
    }

    private int loadAssociationsHelper(String sourceConceptCode, String association, String ldapName,
            String sourceCodingSchemeName, String targetCodingSchemeName, PreparedStatement loadConceptAssociations,
            boolean autoRetry) throws Exception {

        log.debug("loadAssociationsHelper called - sourceConceptCode:" + sourceConceptCode + " association: "
                + association);
        int relationCount = 0;
        try {
            ldapConnection_.setRequestControls(new Control[] { new PagedResultsControl(pageSize) });
            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(0);
            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            searchControls.setTimeLimit(quickSearchLimit_);
            searchControls.setReturningAttributes(new String[] { "targetConcept", "targetCodingScheme" });

            byte[] cookie = null;
            do {

                log.debug("searching on connection1");
                NamingEnumeration innerResult = ldapConnection_.search("sourceConcept="
                        + DBUtility.escapeLdapCode(sourceConceptCode) + ", " + association + ", " + ldapName,
                        "(objectClass=associationTarget)", searchControls);

                while (innerResult.hasMore()) {
                    SearchResult sResInner = (SearchResult) innerResult.next();
                    String targetConceptCode = (String) sResInner.getAttributes().get("targetConcept").get();

                    if (sResInner.getAttributes().get("targetCodingScheme") != null) {
                        String temp2 = (String) sResInner.getAttributes().get("targetCodingScheme").get();
                        if (temp2 != null && temp2.length() > 0) {
                            targetCodingSchemeName = temp2;
                        }
                    }

                    String shortAssociationText = association.substring("association=".length(), association
                            .indexOf(","));

                    loadConceptAssociations.setString(1, sourceCodingSchemeName);
                    loadConceptAssociations.setString(2, sourceConceptCode);
                    loadConceptAssociations.setString(3, shortAssociationText);
                    loadConceptAssociations.setString(4, targetCodingSchemeName);
                    loadConceptAssociations.setString(5, targetConceptCode);
                    DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 6, null, true);
                    DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 7, null, true);
                    DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 8, null, true);

                    try {
                        loadConceptAssociations.executeUpdate();
                        if (relationCount++ % 10 == 0) {
                            messages_.busy();
                        }
                    } catch (SQLException e) {
                        log.error("problem adding association", e);
                        messages_.info("***ERROR*** - sourceCodingScheme: '" + sourceCodingSchemeName
                                + "' sourceCode: '" + sourceConceptCode + "' targetConceptCode: '" + targetConceptCode
                                + "' - see log for details");
                        if (enforceIntegrity_) {
                            messages_
                                    .info("This is probably because the target coding scheme and/or concept code does not exist in the sql database, and you have enforce integrity turned on.");
                        }
                    }
                }
                log.debug("getting controls");
                Control[] controls = ldapConnection_.getResponseControls();
                if (controls != null) {
                    for (int i = 0; i < controls.length; i++) {
                        if (controls[i] instanceof PagedResultsResponseControl) {
                            PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                            cookie = prrc.getCookie();
                            log.debug("got cookie");
                            break;
                        }
                    }
                } else {
                    cookie = null;
                }

                if (cookie != null) {
                    log.debug("setting controls for next page");
                    ldapConnection_.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie,
                            Control.CRITICAL) });
                }

            } while (cookie != null);

            // unset the controls again
            ldapConnection_.setRequestControls(new Control[] {});
        } catch (Exception e) {
            if (autoRetry) {
                log.debug("There was an error getting the targets of " + sourceConceptCode + ". Retrying...", e);
                reconnectConnection1();
                return loadAssociationsHelper(sourceConceptCode, association, ldapName, sourceCodingSchemeName,
                        targetCodingSchemeName, loadConceptAssociations, false);
            } else {
                log.error("There was an error getting the targets of " + sourceConceptCode, e);
                throw e;
            }
        }
        return relationCount;
    }
}