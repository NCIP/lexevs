/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLLiteTableConstants;
import org.LexGrid.util.sql.lgTables.SQLLiteTableUtilities;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Convert-o-matic to take sql and put it into sql lite.
 * 
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 8601 $ checked in on $Date: 2008-06-05
 *          22:28:55 +0000 (Thu, 05 Jun 2008) $
 */
public class SQLToSQLLite {
    private Connection sqlLiteConnection_;
    private Connection sqlConnection_;
    private SQLTableConstants stc_;

    private static Logger log = LogManager.getLogger("convert.SqlLiteToSql");

    private GenericSQLModifier sqlModifier_;

    private SQLLiteTableUtilities sqlLiteTableUtility_;
    private LgMessageDirectorIF messages_;
    private String defaultLanguage_;
    private final int batchSize = Constants.mySqlBatchSize;
    private SQLLiteTableConstants tableConstants_;

    /**
     * Convert-o-matic to take sql and put it into sqllite.
     * 
     * @param sqlLiteServer
     *            address of the SQLLite server
     * @param sqlLiteDriver
     *            SQLLite driver class
     * @param sqlLiteUsername
     *            username for server authentification
     * @param sqlLitePassword
     *            password for server authentification
     * @param sqlServer
     *            address of the SQL server
     * @param sqlDriver
     *            SQL driver class
     * @param sqlUsername
     *            username for server authentification
     * @param sqlPassword
     *            password for server authentification
     * @param codingScheme
     *            CodingScheme to be converted - if null or blank, converts all.
     * @param messages
     *            output dialog
     * @param enableConstraints
     *            adds table constraits to enable foriegn key constraints
     * @throws Exception
     */
    public SQLToSQLLite(String sqlLiteServer, String sqlLiteDriver, String sqlLiteUsername, String sqlLitePassword,
            String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword, String tablePrefix,
            String codingScheme, LgMessageDirectorIF messages, boolean enableConstraints) throws Exception {
        messages_ = messages;

        try {
            log.debug("Connecting to sql lite database");
            sqlLiteConnection_ = DBUtility.connectToDatabase(sqlLiteServer, sqlLiteDriver, sqlLiteUsername,
                    sqlLitePassword);
        } catch (ClassNotFoundException e) {
            messages_
                    .fatalAndThrowException("The class you specified for your sql driver could not be found on the path.");
        }

        try {
            log.debug("Connecting to sql database");
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
            stc_ = new SQLTableUtilities(sqlConnection_, tablePrefix).getSQLTableConstants();
        } catch (ClassNotFoundException e) {
            messages_
                    .fatalAndThrowException("The class you specified for your sql driver could not be found on the path.");
        }

        tableConstants_ = new SQLLiteTableConstants();

        String[] codingSchemesToConvert;
        if (codingScheme != null && codingScheme.length() > 0) {
            codingSchemesToConvert = new String[] { codingScheme };
        } else {
            ArrayList temp = new ArrayList();

            PreparedStatement ps = sqlConnection_.prepareStatement("Select codingSchemeName from "
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME));
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                temp.add(results.getString("codingSchemeName"));
            }
            results.close();
            ps.close();

            codingSchemesToConvert = (String[]) temp.toArray(new String[temp.size()]);
        }

        try {
            for (int i = 0; i < codingSchemesToConvert.length; i++) {
                convert(codingSchemesToConvert[i], enableConstraints);
            }
        } catch (Exception e) {
            messages_.fatalAndThrowException("Failed...", e);
        } finally {
            if (sqlLiteConnection_ != null) {
                sqlLiteConnection_.close();
            }
            if (sqlConnection_ != null) {
                sqlConnection_.close();
            }
        }
    }

    private void convert(String codingScheme, boolean enableConstraints) throws Exception {
        sqlLiteTableUtility_ = new SQLLiteTableUtilities(sqlLiteConnection_);
        sqlModifier_ = new GenericSQLModifier(sqlConnection_, true);
        messages_.info("Converting " + codingScheme);
        messages_.info("Creating tables");
        sqlLiteTableUtility_.createAllTables();

        if (enableConstraints) {
            messages_.info("adding table constraints");
            sqlLiteTableUtility_.createTableConstraints();
        } else {
            messages_.info("removing table constraints");
            sqlLiteTableUtility_.dropTableConstraints();
        }

        // If they didn't pass a coding scheme, just create the tables and exit.
        if (codingScheme == null || codingScheme.trim().length() == 0) {
            return;
        }

        messages_.info("Cleaning tables");
        sqlLiteTableUtility_.cleanTables(codingScheme);

        messages_.info("Loading coding scheme");
        loadCodingScheme(codingScheme);
        messages_.info("Loading supported attributes");
        loadCodingSchemeSupportedAttributes(codingScheme);
        messages_.info("Loading coded entries");
        loadCodedEntries(codingScheme);
        messages_.info("Loading concept properties");
        loadConceptProperties(codingScheme);
        messages_.info("Loading association definitions");
        loadAssociationDefinitions(codingScheme);
        messages_.info("Loading associations");
        loadConceptAssociations(codingScheme);
    }

    private void loadCodingScheme(String codingScheme) throws SQLException {
        log.debug("loadCodingScheme called");
        PreparedStatement populateCodingScheme = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME));

        PreparedStatement getCodingScheme = sqlConnection_.prepareStatement("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + " WHERE codingSchemeName = ?");
        getCodingScheme.setString(1, codingScheme);

        ResultSet results = getCodingScheme.executeQuery();

        if (results.next()) {
            String registeredName = (stc_.supports2009Model() ? results
                    .getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI) : results
                    .getString(SQLTableConstants.TBLCOL_REGISTEREDNAME));
            String defaultLanguage = results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);
            defaultLanguage_ = defaultLanguage;
            String representsVersion = results.getString("representsVersion");
            String formalName = results.getString(SQLTableConstants.TBLCOL_FORMALNAME);
            int approxNumConcepts = results.getInt("approxNumConcepts");
            String source = "";
            String entityDescription = results.getString("entityDescription");
            String copyright = results.getString("copyright");
            results.close();

            // get the source attribute
            PreparedStatement getCodingSchemeSource = sqlConnection_.prepareStatement("SELECT attributeValue FROM "
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES)
                    + " WHERE codingSchemeName = ? AND " + (stc_.supports2009Model() ? "typeName" : "attributeName")
                    + " = ?");
            getCodingSchemeSource.setString(1, codingScheme);
            getCodingSchemeSource.setString(2, "source");

            results = getCodingSchemeSource.executeQuery();
            if (results.next()) {
                source = results.getString("attributeValue");
            }
            results.close();
            getCodingSchemeSource.close();

            int k = 1;
            populateCodingScheme.setString(k++, codingScheme);
            populateCodingScheme.setString(k++, registeredName);
            populateCodingScheme.setString(k++, representsVersion);
            populateCodingScheme.setString(k++, formalName);
            populateCodingScheme.setString(k++, defaultLanguage);
            populateCodingScheme.setInt(k++, approxNumConcepts);
            DBUtility.setBooleanOnPreparedStatment(populateCodingScheme, k++, null, true);
            populateCodingScheme.setInt(k++, 0); // TODO entryStateId here
            populateCodingScheme.setString(k++, null); // TODO releaseURI here
            populateCodingScheme.setString(k++, entityDescription);
            populateCodingScheme.setString(k++, copyright);

            populateCodingScheme.executeUpdate();

            messages_.info("Added 1 coding scheme.");
        }
        populateCodingScheme.close();
        getCodingScheme.close();

    }

    private void loadCodingSchemeSupportedAttributes(String codingSchemeName) throws SQLException {
        log.debug("loadCodingSchemeSupportedAttributes called");
        messages_.info("Adding Attributes");
        int attributeCount = 0;
        PreparedStatement populateCodingSchemeAttributes = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME_SUPPORTED_ATTRIBUTES));

        PreparedStatement getCodingSchemeSupportedAttribute = sqlConnection_.prepareStatement("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES)
                + " WHERE codingSchemeName = ?");

        getCodingSchemeSupportedAttribute.setString(1, codingSchemeName);

        ResultSet results = getCodingSchemeSupportedAttribute.executeQuery();

        while (results.next()) {
            populateCodingSchemeAttributes.setString(1, results.getString("codingSchemeName"));
            populateCodingSchemeAttributes.setString(2, results.getString("supportedAttributeTag"));
            populateCodingSchemeAttributes.setString(3, results.getString(stc_.supports2009Model() ? "id"
                    : "supportedAttributeValue"));
            populateCodingSchemeAttributes.setString(4, results.getString("urn"));
            DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 5, null, true);
            DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 6, null, true);
            DBUtility.setBooleanOnPreparedStatment(populateCodingSchemeAttributes, 7, null, true);

            attributeCount++;
            populateCodingSchemeAttributes.executeUpdate();

        }
        results.close();
        populateCodingSchemeAttributes.close();
        getCodingSchemeSupportedAttribute.close();
        messages_.info("Added " + attributeCount + " attributes");
    }

    private void loadAssociationDefinitions(String codingSchemeName) throws SQLException {
        log.debug("loadAssociationDefinitions called");
        int associationCount = 0;

        messages_.info("Adding Assocation Definitions");

        associationCount = 0;
        PreparedStatement loadAssociationDefinitions = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_ASSOCIATION_DEFINITION));

        PreparedStatement getAssociationDefinitions = sqlConnection_
                .prepareStatement("SELECT distinct (association), reverseName, entityDescription, isTransitive, isSymmetric, isReflexive FROM "
                        + stc_.getTableName(SQLTableConstants.ASSOCIATION) + " WHERE codingSchemeName = ?");
        getAssociationDefinitions.setString(1, codingSchemeName);

        ResultSet results = getAssociationDefinitions.executeQuery();

        while (results.next()) {
            String association = results.getString("association");
            String reverseName = results.getString("reverseName");
            String entityDescription = results.getString("entityDescription");
            Boolean isTransitive = DBUtility.getBooleanFromResultSet(results, "isTransitive");
            Boolean isSymmetric = DBUtility.getBooleanFromResultSet(results, "isSymmetric");
            Boolean isReflexive = DBUtility.getBooleanFromResultSet(results, "isReflexive");

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
            loadAssociationDefinitions.executeUpdate();
            associationCount++;
        }
        results.close();
        loadAssociationDefinitions.close();
        getAssociationDefinitions.close();
        messages_.info("Loaded " + associationCount + " associations definition");
    }

    private void loadCodedEntries(String codingSchemeName) throws SQLException {
        log.debug("loadCodedEntries called");
        messages_.info("loading concept codes - getting a total count");

        PreparedStatement getCodedEntries = sqlConnection_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY) + " WHERE codingSchemeName = ?");

        getCodedEntries.setString(1, codingSchemeName);

        ResultSet results = getCodedEntries.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getCodedEntries.close();

        int start = 0;
        int codeCount = 0;

        PreparedStatement loadCodedEntry = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODED_ENTRY));

        getCodedEntries = sqlConnection_.prepareStatement(sqlModifier_.modifySQL("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY) + " WHERE codingSchemeName = ? {LIMIT}"));

        // This while loop is for mysql - it gets results by a batch at a time.
        // this loop will only run once with other databases.
        while (start < total) {
            messages_.info("Getting a results from sql (a page if using mysql)");

            getCodedEntries.setString(1, codingSchemeName);
            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (sqlModifier_.getDatabaseType().equals("MySQL")) {
                getCodedEntries.setInt(2, start);
                getCodedEntries.setInt(3, batchSize);
                start += batchSize;
            } else if (sqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                getCodedEntries.setFetchSize(batchSize);
                sqlConnection_.setAutoCommit(false);
                start = total;
            } else {
                start = total;
            }

            results = getCodedEntries.executeQuery();

            while (results.next()) {
                String code = results.getString("conceptCode");

                Boolean isActive = DBUtility.getBooleanFromResultSet(results, "isActive");

                String conceptStatus = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
                if (conceptStatus == null) {
                    conceptStatus = "Active";
                }

                loadCodedEntry.setString(1, codingSchemeName);
                loadCodedEntry.setString(2, code);
                loadCodedEntry.setString(3, SQLTableConstants.TBLCOLVAL_MISSING); // this
                                                                                  // will
                                                                                  // be
                                                                                  // replaced
                                                                                  // in
                                                                                  // loadConceptProperties
                loadCodedEntry.setString(4, null); // the definition will be put
                                                   // in later.
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 5, isActive, true);
                loadCodedEntry.setString(6, conceptStatus);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 7, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 8, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadCodedEntry, 9, null, true);

                // log.debug("inserting code '" + code + "' into database");
                loadCodedEntry.executeUpdate();

                if (codeCount++ % 10 == 0) {
                    messages_.busy();
                }
                if (codeCount % 1000 == 0) {
                    messages_.info("Loaded " + codeCount + " codes out of " + total);
                }
            }
            results.close();
            if (!sqlModifier_.getDatabaseType().equals("MySQL")) {
                // only do the while loop and limit thing if using mysql
                break;
            }
        }
        loadCodedEntry.close();
        getCodedEntries.close();
        messages_.info("loaded " + codeCount + " concept Codes");
    }

    private void loadConceptProperties(String codingSchemeName) throws SQLException {
        messages_.info("loading concept properties - getting a total count");

        PreparedStatement getConceptProperties = sqlConnection_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " WHERE codingSchemeName = ?");

        getConceptProperties.setString(1, codingSchemeName);

        ResultSet results = getConceptProperties.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getConceptProperties.close();

        int start = 0;
        int propertyCount = 0;

        String property = "";
        String propertyId = "";

        PreparedStatement loadConceptProperty = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_PROPERTY));

        PreparedStatement updateCodedEntryDescription = sqlLiteConnection_
                .prepareStatement("UPDATE codedEntry SET conceptDescription = ? WHERE codingSchemeName = ? AND conceptCode = ?");

        PreparedStatement updateCodedEntryPresentation = sqlLiteConnection_
                .prepareStatement("UPDATE codedEntry SET conceptName = ? WHERE codingSchemeName = ? AND conceptCode = ?");

        getConceptProperties = sqlConnection_.prepareStatement(sqlModifier_.modifySQL("SELECT * FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " WHERE codingSchemeName = ? {LIMIT}"));

        // This while loop is for mysql - it gets results by a batch at a time.
        // this loop will only run once with other databases.
        while (start < total) {
            messages_.info("Getting a results from sql (a page if using mysql)");

            getConceptProperties.setString(1, codingSchemeName);
            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (sqlModifier_.getDatabaseType().equals("MySQL")) {
                getConceptProperties.setInt(2, start);
                getConceptProperties.setInt(3, batchSize);
                start += batchSize;
            } else if (sqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                getConceptProperties.setFetchSize(batchSize);
                sqlConnection_.setAutoCommit(false);
                start = total;
            } else {
                start = total;
            }

            results = getConceptProperties.executeQuery();

            while (results.next()) {
                if (stc_.supports2009Model()) {
                    String propertyType = results.getString("propertyType");
                    if (propertyType.equalsIgnoreCase("presentation")) {
                        property = "textualPresentation";
                    } else if (propertyType.equalsIgnoreCase("definition")) {
                        property = "definition";
                    } else {
                        property = propertyType.toLowerCase();
                    }
                } else {
                    property = results.getString("property");
                }
                propertyId = results.getString("propertyId");
                String language = results.getString("language");
                if (language == null || language.length() == 0) {
                    language = defaultLanguage_;
                }

                String conceptCode = results.getString("conceptCode");
                String value = results.getString("propertyValue");
                Boolean isPreferred = DBUtility.getBooleanFromResultSet(results, "isPreferred");
                if (isPreferred == null) {
                    isPreferred = new Boolean(false);
                }

                if (property.equals("definition") && language.equals(defaultLanguage_) && isPreferred.booleanValue()) {
                    // if this is the preferred definition, put it into the
                    // coded entry table.
                    updateCodedEntryDescription.setString(1, value);
                    updateCodedEntryDescription.setString(2, codingSchemeName);
                    updateCodedEntryDescription.setString(3, conceptCode);
                    updateCodedEntryDescription.execute();
                } else if (property.equals("textualPresentation") && language.equals(defaultLanguage_)
                        && isPreferred.booleanValue()) {
                    // if this is the preferred definition, put it into the
                    // coded entry table.
                    updateCodedEntryPresentation.setString(1, value);
                    updateCodedEntryPresentation.setString(2, codingSchemeName);
                    updateCodedEntryPresentation.setString(3, conceptCode);
                    updateCodedEntryPresentation.execute();
                }

                loadConceptProperty.setString(1, codingSchemeName);
                loadConceptProperty.setString(2, conceptCode);
                loadConceptProperty.setString(3, property);
                loadConceptProperty.setString(4, propertyId);
                loadConceptProperty.setString(5, value);
                loadConceptProperty.setString(6, language);
                loadConceptProperty.setString(7, results.getString("presentationFormat"));
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 8, isPreferred, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 9, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 10, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptProperty, 11, null, true);

                loadConceptProperty.executeUpdate();
                if (propertyCount++ % 10 == 0) {
                    messages_.busy();
                }
                if (propertyCount % 1000 == 0) {
                    messages_.info("Loaded " + propertyCount + " properties out of " + total);
                }
            }
            results.close();
            if (!sqlModifier_.getDatabaseType().equals("MySQL")) {
                // only do the while loop and limit thing if using mysql
                break;
            }
        }
        loadConceptProperty.close();
        updateCodedEntryDescription.close();
        updateCodedEntryPresentation.close();
        getConceptProperties.close();
        messages_.info("loaded " + propertyCount + " properties");
    }

    private void loadConceptAssociations(String sourceCodingSchemeName) throws SQLException {
        messages_.info("loading associations - getting a total count");

        PreparedStatement getConceptAssociations = sqlConnection_.prepareStatement("SELECT COUNT(*) as cnt FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE codingSchemeName = ?");

        getConceptAssociations.setString(1, sourceCodingSchemeName);

        ResultSet results = getConceptAssociations.executeQuery();

        results.next();
        int total = results.getInt("cnt");

        results.close();
        getConceptAssociations.close();

        int start = 0;
        int relationCount = 0;
        int skipCount = 0;

        PreparedStatement loadConceptAssociations = sqlLiteConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_ASSOCIATION));

        getConceptAssociations = sqlConnection_
                .prepareStatement(sqlModifier_
                        .modifySQL("SELECT association, sourceCodingSchemeName, sourceConceptCode, targetCodingSchemeName, targetConceptCode FROM "
                                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
                                + " WHERE codingSchemeName = ? {LIMIT}"));

        // This while loop is for mysql - it gets results by a batch at a time.
        // this loop will only run once with other databases.
        while (start < total) {
            messages_.info("Getting a results from sql (a page if using mysql)");

            getConceptAssociations.setString(1, sourceCodingSchemeName);
            // mysql doesn't stream results - the {LIMIT above and this is for
            // getting limits on mysql code}
            if (sqlModifier_.getDatabaseType().equals("MySQL")) {
                getConceptAssociations.setInt(2, start);
                getConceptAssociations.setInt(3, batchSize);
                start += batchSize;
            } else if (sqlModifier_.getDatabaseType().equals("PostgreSQL")) {
                // postgres properly streams results, we can just set the fetch
                // size, and only loop once
                getConceptAssociations.setFetchSize(batchSize);
                sqlConnection_.setAutoCommit(false);
                start = total;
            } else {
                start = total;
            }
            results = getConceptAssociations.executeQuery();

            while (results.next()) {
                String tempSourceCodingSchemeName = results.getString("sourceCodingSchemeName");
                if (!tempSourceCodingSchemeName.equals(sourceCodingSchemeName)) {
                    skipCount++;

                    if (skipCount == 1) {
                        messages_
                                .info("Skipping relation(s) because LexGrid Lite does not support relations with external source coding schemes.");
                    }
                    continue;
                }

                loadConceptAssociations.setString(1, tempSourceCodingSchemeName);
                loadConceptAssociations.setString(2, results.getString("sourceConceptCode"));
                loadConceptAssociations.setString(3, results.getString("association"));
                loadConceptAssociations.setString(4, results.getString("targetCodingSchemeName"));
                loadConceptAssociations.setString(5, results.getString("targetConceptCode"));
                DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 6, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 7, null, true);
                DBUtility.setBooleanOnPreparedStatment(loadConceptAssociations, 8, null, true);

                loadConceptAssociations.executeUpdate();
                if (relationCount++ % 10 == 0) {
                    messages_.busy();
                }
                if (relationCount % 1000 == 0) {
                    messages_.info("Loaded " + relationCount + " associations out of " + total);
                }
            }
            results.close();
            if (!sqlModifier_.getDatabaseType().equals("MySQL")) {
                // only do the while loop and limit thing if using mysql
                break;
            }
        }
        if (skipCount > 0) {
            messages_.info("skipped " + skipCount + " associations");
        }
        loadConceptAssociations.close();
        getConceptAssociations.close();
        messages_.info("loaded " + relationCount + " associations");
    }
}