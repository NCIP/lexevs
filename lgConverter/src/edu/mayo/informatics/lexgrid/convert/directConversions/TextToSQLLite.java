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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLLiteTableConstants;
import org.LexGrid.util.sql.lgTables.SQLLiteTableUtilities;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.TextUtility;

/**
 * Load simple tab delimited text files into sql lite.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 8756 $ checked in on $Date: 2008-06-12
 *          16:26:57 +0000 (Thu, 12 Jun 2008) $
 */
public class TextToSQLLite {
    private String token_ = "\t";
    private LgMessageDirectorIF messages_;
    private Connection sqlConnection_;
    private SQLLiteTableUtilities tableUtility_;
    private SQLLiteTableConstants tableConstants_;
    private Concept specialConcept = new Concept("@", "Top Thing",
            "Points to all concepts that aren't children of any other concepts", -1);

    private String codingSchemeName_;
    private LoaderPreferences loaderPrefs_;

    /**
     * Convert Text to Sql Lite. Format of the text file is as follows: Format
     * a: (ignore the *'s) (this does not display properly in HTML - please see
     * the readme file)
     * 
     * <pre>
     * &lt;codingSchemeName&gt;\t&lt;codingSchemeId&gt;\t&lt;defaultLanguage&gt;\t&lt;formalName&gt;[\t&lt;version&gt;][\t&lt;source&gt;][\t&lt;description&gt;][\t&lt;copyright&gt;]
     * &lt;name1&gt;[\t &lt;description&gt;] 
     * \t &lt;name2&gt;[\t &lt;description&gt;] 
     * \t\t &lt;name3&gt;[\t &lt;description&gt;]
     * \t\t &lt;name4&gt;[\t &lt;description&gt;]
     * </pre>
     * 
     * Where the leading tabs represent hierarchical hasSubtype relationship
     * nesting (name1 hasSubtype name2 and name2 hasSubtype name3,)
     * 
     * Lines starting with "#" are view comments - they are completely ignored.
     * 
     * Rules - if <name>doesn't already exist in the database, assign it a
     * unique numeric concept code. Name becomes the entity description and
     * preferred presentation. If description is supplied, it becomes the
     * definition. - if <name>already exists in the database, use the
     * pre-assigned code. If <description>is supplied (a) if one doesn't exist,
     * already, use the supplied one (b) if one exists already and it doesn't
     * match - issue a warning.
     * 
     * Format b:
     * 
     * <pre>
     * &lt;code&gt;\t&lt;name&gt;[\t&lt;description&gt;]
     * </pre>
     * 
     * Same as (a) except that the concept codes are part of the input. If the
     * same code occurs twice, the names must match. Description rules same as
     * above
     * 
     * @param fileLocation
     *            location of the tab delimited file
     * @param token
     *            parsing token, if null default is "/t"
     * @param sqlLiteServer
     *            location of the SQLLite server
     * @param sqlLiteDriver
     *            driver class
     * @param sqlLiteUsername
     *            username for server authentification
     * @param sqlLitePassword
     *            password for server authenification
     * @param loaderPrefs
     *            Loader Preferences
     * @param messageDirector
     *            log message output
     * @param forceFormatB
     *            Force reading of a format A file as Format B
     * @throws Exception
     */
    public TextToSQLLite(String fileLocation, String token, String sqlLiteServer, String sqlLiteDriver,
            String sqlLiteUsername, String sqlLitePassword, LoaderPreferences loaderPrefs,
            LgMessageDirectorIF messageDirector, boolean forceFormatB) throws Exception {
        messages_ = messageDirector;
        tableConstants_ = new SQLLiteTableConstants();
        loaderPrefs_ = loaderPrefs;
        if (token != null && token.length() > 0) {
            token_ = token;
        }

        // this verifies all of the rules except the description rules - and
        // determines A or B.
        CodingScheme codingScheme = TextUtility.readAndVerifyConcepts(fileLocation, messages_, token_, forceFormatB);
        codingSchemeName_ = codingScheme.codingSchemeName;

        // set up the sql tables
        prepareDatabase(codingScheme.codingSchemeName, sqlLiteServer, sqlLiteDriver, sqlLiteUsername, sqlLitePassword);

        // load the concepts, verify the description status.
        loadConcepts(codingScheme);

        loadRelations(codingScheme);

        sqlConnection_.close();

    }

    private void prepareDatabase(String codingScheme, String sqlServer, String sqlDriver, String sqlUsername,
            String sqlPassword) throws Exception {
        try {
            messages_.info("Connecting to database");
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
            // gsm_ = new GenericSQLModifier(sqlConnection_);
        } catch (ClassNotFoundException e) {
            messages_
                    .fatalAndThrowException("FATAL ERROR - The class you specified for your sql driver could not be found on the path.");
        }

        tableUtility_ = new SQLLiteTableUtilities(sqlConnection_);

        messages_.info("Creating tables");
        tableUtility_.createAllTables();

        messages_.info("Creating constraints");
        tableUtility_.createTableConstraints();

        messages_.info("Cleaning tables");
        tableUtility_.cleanTables(codingScheme);
    }

    private void loadConcepts(CodingScheme codingScheme) throws Exception {
        PreparedStatement insert = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME));

        messages_.info("Loading coding scheme");
        insert.setString(1, codingScheme.codingSchemeName);
        insert.setString(2, codingScheme.codingSchemeId);
        insert.setString(3, codingScheme.defaultLanguage);
        insert.setString(4, codingScheme.representsVersion);
        insert.setString(5, codingScheme.formalName);
        insert.setInt(6, codingScheme.concepts.length); // this is not the
                                                        // actual number of
                                                        // concepts
        insert.setString(7, codingScheme.source);
        insert.setString(8, codingScheme.entityDescription);
        insert.setString(9, codingScheme.copyright);
        DBUtility.setBooleanOnPreparedStatment(insert, 10, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insert, 11, new Boolean("false"), true);
        DBUtility.setBooleanOnPreparedStatment(insert, 12, new Boolean("false"), true);

        try {
            insert.executeUpdate();
        } catch (SQLException e) {
            messages_.fatalAndThrowException(
                    "FATAL ERROR - It is likely that your coding scheme name or CodingSchemeId is not unique.", e);
        }

        insert.close();

        try {
            messages_.info("Loading coding scheme supported attributes");
            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODING_SCHEME_SUPPORTED_ATTRIBUTES));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, "Language");
            insert.setString(3, codingScheme.defaultLanguage);
            insert.setString(4, "");
            DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 6, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("false"), true);
            insert.executeUpdate();
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, "Association");
            insert.setString(3, "hasSubtype");
            insert.setString(4, "");
            DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 6, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("false"), true);
            insert.executeUpdate();
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, "Property");
            insert.setString(3, "definition");
            insert.setString(4, "");
            DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 6, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("false"), true);
            insert.executeUpdate();
            if (codingScheme.source != null && codingScheme.source.length() > 0) {
                insert.setString(1, codingScheme.codingSchemeName);
                insert.setString(2, "Source");
                insert.setString(3, codingScheme.source);
                insert.setString(4, "");
                DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
                DBUtility.setBooleanOnPreparedStatment(insert, 6, new Boolean("false"), true);
                DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("false"), true);
                insert.executeUpdate();
            }
            insert.close();
        } catch (SQLException e) {
            messages_.fatalAndThrowException("FATAL ERROR - Problem loading the coding scheme supported attributes", e);
        }

        messages_.info("Loading association definition");
        try {
            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_ASSOCIATION_DEFINITION));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, "hasSubtype");
            insert.setString(3, "isA");
            insert.setString(4, "The parent child relationships.");
            DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 6, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 8, new Boolean("true"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 9, new Boolean("false"), true);
            DBUtility.setBooleanOnPreparedStatment(insert, 10, new Boolean("false"), true);
            insert.executeUpdate();
            insert.close();
        } catch (SQLException e) {
            messages_.fatalAndThrowException("FATAL ERROR - Problem loading the association definition", e);
        }

        insert = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CODED_ENTRY));

        PreparedStatement insertIntoConceptProperty = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_PROPERTY));

        PreparedStatement checkForCode = sqlConnection_
                .prepareStatement("SELECT count(*) as found from codedEntry WHERE codingSchemeName = ? AND conceptCode = ?");

        PreparedStatement checkForDefinition = sqlConnection_
                .prepareStatement("SELECT count(*) as found from conceptProperty WHERE codingSchemeName = ? AND conceptCode = ? AND property = ?");

        PreparedStatement updateConcept = sqlConnection_
                .prepareStatement("UPDATE codedEntry SET conceptDescription = ? WHERE codingSchemeName = ? AND conceptCode = ?");

        messages_.info("Loading coded entry and concept property");

        Concept[] concepts = codingScheme.concepts;

        for (int i = 0; i < concepts.length; i++) {
            try {
                checkForCode.setString(1, codingScheme.codingSchemeName);
                checkForCode.setString(2, concepts[i].code);
                ResultSet results = checkForCode.executeQuery();
                // only one result
                results.next();
                if (results.getInt("found") == 0) {
                    if (concepts[i].name == null || concepts[i].name.length() == 0) {
                        messages_.fatalAndThrowException("FATAL ERROR - The concept '" + concepts[i].code
                                + "' is missing the name.  Name is required.");
                    }
                    // only add it to the codedEntry table if it is not already
                    // there.
                    insert.setString(1, codingScheme.codingSchemeName);
                    insert.setString(2, concepts[i].code);
                    insert.setString(3, concepts[i].name);
                    insert.setString(4, concepts[i].description); // this is the
                                                                  // definition
                    DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("true"), true);
                    insert.setString(6, "Active");
                    DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("true"), true);
                    DBUtility.setBooleanOnPreparedStatment(insert, 8, new Boolean("false"), true);
                    DBUtility.setBooleanOnPreparedStatment(insert, 9, new Boolean("false"), true);

                    insert.executeUpdate();

                    // also add the textualPresentation to the conceptProperty
                    // table
                    insertIntoConceptProperty.setString(1, codingScheme.codingSchemeName);
                    insertIntoConceptProperty.setString(2, concepts[i].code);
                    insertIntoConceptProperty.setString(3, "textualPresentation");
                    insertIntoConceptProperty.setString(4, "p-1");
                    insertIntoConceptProperty.setString(5, concepts[i].name);
                    insertIntoConceptProperty.setString(6, codingScheme.defaultLanguage);
                    insertIntoConceptProperty.setString(7, "");
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 8, new Boolean("true"), true);
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 9, new Boolean("true"), true);
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 10, new Boolean("false"), true);
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 11, new Boolean("false"), true);

                    insertIntoConceptProperty.executeUpdate();
                }
                results.close();
                if (concepts[i].description != null && concepts[i].description.length() > 0) {
                    // description to definition
                    // check for match first
                    checkForDefinition.setString(1, codingScheme.codingSchemeName);
                    checkForDefinition.setString(2, concepts[i].code);
                    checkForDefinition.setString(3, "definition");

                    results = checkForDefinition.executeQuery();
                    // always one result
                    results.next();
                    if (results.getInt("found") > 0) {
                        messages_.info("WARNING - The concept code: '" + concepts[i].code + "' name: '"
                                + concepts[i].name + "' has multiple descriptions.  Skipping later descriptions.");
                    } else {
                        insertIntoConceptProperty.setString(1, codingScheme.codingSchemeName);
                        insertIntoConceptProperty.setString(2, concepts[i].code);
                        insertIntoConceptProperty.setString(3, "definition");
                        insertIntoConceptProperty.setString(4, "d-1");
                        insertIntoConceptProperty.setString(5, concepts[i].description);
                        insertIntoConceptProperty.setString(6, codingScheme.defaultLanguage);
                        insertIntoConceptProperty.setString(7, "");
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 8, new Boolean("true"), true);
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 9, new Boolean("true"), true);
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 10, new Boolean("false"),
                                true);
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 11, new Boolean("false"),
                                true);

                        insertIntoConceptProperty.executeUpdate();

                        updateConcept.setString(1, concepts[i].description);
                        updateConcept.setString(2, codingScheme.codingSchemeName);
                        updateConcept.setString(3, concepts[i].code);

                        updateConcept.executeUpdate();
                    }
                    results.close();
                }
                if (i % 10 == 0) {
                    messages_.busy();
                }
            } catch (Exception e) {
                messages_.fatalAndThrowException("Problem loading concept code " + concepts[i], e);
            }
        }

        // Add the special code
        insert.setString(1, codingScheme.codingSchemeName);
        insert.setString(2, specialConcept.code);
        insert.setString(3, specialConcept.name);
        insert.setString(4, specialConcept.description); // this is the
                                                         // definition
        DBUtility.setBooleanOnPreparedStatment(insert, 5, new Boolean("false"), true);
        insert.setString(6, "Active");
        DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insert, 8, new Boolean("false"), true);
        DBUtility.setBooleanOnPreparedStatment(insert, 9, new Boolean("false"), true);

        insert.executeUpdate();

        insertIntoConceptProperty.setString(1, codingScheme.codingSchemeName);
        insertIntoConceptProperty.setString(2, specialConcept.code);
        insertIntoConceptProperty.setString(3, "textualPresentation");
        insertIntoConceptProperty.setString(4, "p-1");
        insertIntoConceptProperty.setString(5, specialConcept.name);
        insertIntoConceptProperty.setString(6, codingScheme.defaultLanguage);
        insertIntoConceptProperty.setString(7, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 8, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 9, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 10, new Boolean("false"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 11, new Boolean("false"), true);

        insertIntoConceptProperty.executeUpdate();

        insertIntoConceptProperty.setString(1, codingScheme.codingSchemeName);
        insertIntoConceptProperty.setString(2, specialConcept.code);
        insertIntoConceptProperty.setString(3, "definition");
        insertIntoConceptProperty.setString(4, "d-1");
        insertIntoConceptProperty.setString(5, specialConcept.description);
        insertIntoConceptProperty.setString(6, codingScheme.defaultLanguage);
        insertIntoConceptProperty.setString(7, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 8, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 9, new Boolean("true"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 10, new Boolean("false"), true);
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, 11, new Boolean("false"), true);

        insertIntoConceptProperty.executeUpdate();

        insert.close();
        insertIntoConceptProperty.close();
        checkForCode.close();
        checkForDefinition.close();
        updateConcept.close();
    }

    private void loadRelations(CodingScheme codingScheme) throws Exception {
        messages_.info("Loading relationships");
        PreparedStatement insertIntoConceptAssociations = sqlConnection_.prepareStatement(tableConstants_
                .getStatementSQL(SQLLiteTableConstants.INSERT_INTO_CONCEPT_ASSOCIATION));

        PreparedStatement checkForAssociation = sqlConnection_
                .prepareStatement("SELECT count(*) as found from conceptAssociation WHERE sourceCodingSchemeName = ? AND sourceConceptCode = ? AND association = ? AND targetCodingSchemeName = ? AND targetConceptCode = ?");

        Concept[] concepts = codingScheme.concepts;

        for (int i = 0; i < concepts.length; i++) {
            try {
                Concept parent = TextUtility.getParent(concepts, i);
                if (parent == null) {
                    parent = specialConcept;
                }
                checkForAssociation.setString(1, codingScheme.codingSchemeName);
                checkForAssociation.setString(2, parent.code);
                checkForAssociation.setString(3, "hasSubtype");
                checkForAssociation.setString(4, codingScheme.codingSchemeName);
                checkForAssociation.setString(5, concepts[i].code);
                ResultSet results = checkForAssociation.executeQuery();
                // always one result
                results.next();
                if (results.getInt("found") > 0) {
                    messages_.info("WARNING - Relationship '" + parent.code + "' (" + parent.name + ") to '"
                            + concepts[i].code + "' (" + concepts[i].name + ") already exists.  Skipping.");
                    continue;
                }
                insertIntoConceptAssociations.setString(1, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(2, parent.code);
                insertIntoConceptAssociations.setString(3, "hasSubtype");
                insertIntoConceptAssociations.setString(4, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(5, concepts[i].code);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 6, new Boolean("true"), true);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 7, new Boolean("false"), true);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 8, new Boolean("false"), true);
                insertIntoConceptAssociations.executeUpdate();
                if (i % 10 == 0) {
                    messages_.busy();
                }
                results.close();
            } catch (SQLException e) {
                messages_.fatalAndThrowException("Problem loading relationships for " + concepts[i], e);
            }
        }

        insertIntoConceptAssociations.close();
        checkForAssociation.close();
    }

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return this.codingSchemeName_;
    }

}