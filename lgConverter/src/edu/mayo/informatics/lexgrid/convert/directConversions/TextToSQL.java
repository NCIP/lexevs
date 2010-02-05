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
import java.util.Iterator;
import java.util.TreeSet;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Association;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.TextUtility;

/**
 * Converstion tool for loading a delimited text format into SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 8756 $ checked in on $Date: 2007-08-30
 *          17:13:22 +0000 (Thu, 30 Aug 2007) $
 */
public class TextToSQL {
    private String token_ = "\t";
    private LgMessageDirectorIF messages_;
    private Connection sqlConnection_;
    private SQLTableUtilities tableUtility_;
    private SQLTableConstants tableConstants_;
    private Concept specialConcept = new Concept("@", "Top Thing",
            "Points to all concepts that aren't children of any other concepts", -1);
    private String codingSchemeName_;
    private String representsVersion_;

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return this.codingSchemeName_;
    }

    /**
     * @return the representsVersion
     */
    public String getVersion() {
        return this.representsVersion_;
    }

    /**
     * Text to SQL Converter. Format of the text files is as follows: Format a:
     * (ignore the *'s) (this does not display properly in HTML - please see the
     * readme file)
     * 
     * <pre>
     * 
     *    &lt;codingSchemeName&gt;\t&lt;codingSchemeId&gt;\t&lt;defaultLanguage&gt;\t&lt;formalName&gt;[\t&lt;version&gt;][\t&lt;source&gt;][\t&lt;description&gt;][\t&lt;copyright&gt;]
     *    &lt;name1&gt;[\t &lt;description&gt;] 
     *    \t &lt;name2&gt;[\t &lt;description&gt;] 
     *    \t\t &lt;name3&gt;[\t &lt;description&gt;]
     *    \t\t &lt;name4&gt;[\t &lt;description&gt;]
     *    Relation=&quot;rel_name&quot; sourceCodingScheme=&quot;srcCS&quot; sourceCode=&quot;code&quot; targetCodingScheme=&quot;tgtCS&quot; targetCode=&quot;code&quot;
     * </pre>
     * 
     * Where the leading tabs represent hierarchical hasSubtype relationship
     * nesting (name1 hasSubtype name2 and name2 hasSubtype name3,) The line
     * that starts with keyword Relation is used to setup the association
     * information.
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
     * &lt;code&gt;
     *   \t&lt;name&gt;[\t&lt;description&gt;]
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
    public TextToSQL(String fileLocation, String token, String sqlLiteServer, String sqlLiteDriver,
            String sqlLiteUsername, String sqlLitePassword, String tablePrefix, LoaderPreferences loaderPrefs,
            LgMessageDirectorIF messageDirector, boolean forceFormatB) throws Exception {
        messages_ = messageDirector;
        if (token != null && token.length() > 0) {
            token_ = token;
        }

        // this verifies all of the rules except the description rules - and
        // determines A or B.
        CodingScheme codingScheme = TextUtility.readAndVerifyConcepts(fileLocation, messages_, token_, forceFormatB);
        codingSchemeName_ = codingScheme.codingSchemeName;
        representsVersion_ = codingScheme.representsVersion;

        // set up the sql tables
        prepareDatabase(codingScheme.codingSchemeName, sqlLiteServer, sqlLiteDriver, sqlLiteUsername, sqlLitePassword,
                tablePrefix);

        tableConstants_ = tableUtility_.getSQLTableConstants();

        // load the concepts, verify the description status.
        loadConcepts(codingScheme);

        loadHasSubtypeRelations(codingScheme);
        loadRelations(codingScheme);

        sqlConnection_.close();

    }

    private void prepareDatabase(String codingScheme, String sqlServer, String sqlDriver, String sqlUsername,
            String sqlPassword, String tablePrefix) throws Exception {
        try {
            messages_.info("Connecting to database");
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
            // gsm_ = new GenericSQLModifier(sqlConnection_);
        } catch (ClassNotFoundException e) {
            messages_
                    .fatalAndThrowException("FATAL ERROR - The class you specified for your sql driver could not be found on the path.");
        }

        tableUtility_ = new SQLTableUtilities(sqlConnection_, tablePrefix);

        messages_.info("Creating tables");
        tableUtility_.createDefaultTables();

        messages_.info("Creating constraints");
        tableUtility_.createDefaultTableConstraints();

        messages_.info("Cleaning tables");
        tableUtility_.cleanTables(codingScheme);
    }

    private void loadConcepts(CodingScheme codingScheme) throws Exception {
        PreparedStatement insert = sqlConnection_.prepareStatement(tableConstants_
                .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME));

        messages_.info("Loading coding scheme");
        int ii = 1;
        insert.setString(ii++, codingScheme.codingSchemeName);
        insert.setString(ii++, codingScheme.formalName);
        insert.setString(ii++, codingScheme.codingSchemeId);
        insert.setString(ii++, codingScheme.defaultLanguage);
        insert.setString(ii++, codingScheme.representsVersion);
        DBUtility.setBooleanOnPreparedStatment(insert, ii++, new Boolean(false));
        insert.setInt(ii++, codingScheme.concepts.length); // this is not the
        // actual number of
        // concepts
        if (tableUtility_.getSQLTableConstants().supports2009Model()) {
            DBUtility.setBooleanOnPreparedStatment(insert, ii++, null);
            DBUtility.setBooleanOnPreparedStatment(insert, ii++, null);
            DBUtility.setBooleanOnPreparedStatment(insert, ii++, null);
        }
        insert.setString(ii++, codingScheme.entityDescription);
        insert.setString(ii++, codingScheme.copyright);

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
                    .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE);
            insert.setString(3, codingScheme.defaultLanguage);
            insert.setString(4, "");
            if (tableConstants_.supports2009Model()) {
                insert.setString(5, "");
                insert.setString(6, "");
                insert.setString(7, "");
            }

            insert.executeUpdate();
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);
            insert.setString(3, SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION);
            insert.setString(4, "");
            if (tableConstants_.supports2009Model()) {
                insert.setString(5, "");
                insert.setString(6, "");
                insert.setString(7, "");
            }

            insert.executeUpdate();

            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);
            insert.setString(3, codingScheme.codingSchemeName);
            insert.setString(4, codingScheme.codingSchemeId);
            if (tableConstants_.supports2009Model()) {
                insert.setString(5, "");
                insert.setString(6, "");
                insert.setString(7, "");
            }

            insert.executeUpdate();

            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY);
            insert.setString(3, SQLTableConstants.TBLCOLVAL_DEFINITION);
            insert.setString(4, "");
            if (tableConstants_.supports2009Model()) {
                insert.setString(5, "");
                insert.setString(6, "");
                insert.setString(7, "");
            }

            insert.executeUpdate();
            insert.close();

            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES));

            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
            insert.setString(3, codingScheme.codingSchemeName);
            if (tableConstants_.supports2009Model()) {
                insert.setString(4, "");
                insert.setString(5, "");
            }

            insert.executeUpdate();

            String codingSchemeIdTemp = codingScheme.codingSchemeId;
            int temp = codingSchemeIdTemp.lastIndexOf(':');
            if (temp > 0 && ((temp + 1) <= codingSchemeIdTemp.length())) {
                codingSchemeIdTemp = codingSchemeIdTemp.substring(temp + 1);
            }

            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_LOCALNAME);
            insert.setString(3, codingSchemeIdTemp);
            if (tableConstants_.supports2009Model()) {
                insert.setString(4, "");
                insert.setString(5, "");
            }

            insert.executeUpdate();

            if (codingScheme.source != null && codingScheme.source.length() > 0) {
                insert.setString(1, codingScheme.codingSchemeName);
                insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                insert.setString(3, codingScheme.source);
                if (tableConstants_.supports2009Model()) {
                    insert.setString(4, "");
                    insert.setString(5, "");
                }

                insert.executeUpdate();
            }
            insert.close();
        } catch (SQLException e) {
            messages_.fatalAndThrowException("FATAL ERROR - Problem loading the coding scheme supported attributes", e);
        }

        messages_.info("Loading relation definition");
        try {
            insert = sqlConnection_.prepareStatement(tableConstants_.getInsertStatementSQL(SQLTableConstants.RELATION));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
            DBUtility.setBooleanOnPreparedStatment(insert, 3, new Boolean("true"), false);
            insert.setString(4, "");

            insert.executeUpdate();
            insert.close();
        } catch (SQLException e) {
            messages_.fatalAndThrowException("FATAL ERROR - Problem loading the relation definition", e);
        }

        messages_.info("Loading association definition");
        try {
            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getInsertStatementSQL(SQLTableConstants.ASSOCIATION));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
            insert.setString(3, SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION);
            insert.setString(4, SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION);
            insert.setString(5, SQLTableConstants.TBLCOLVAL_ISA_ASSOCIATION);
            insert.setString(6, ""); // TODO deal with isNavigable, inverse
            // properly - what should they be?
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 8, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 9, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 10, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 11, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 12, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 13, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 14, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 15, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 16, null, false);
            insert.setString(17, "");
            if( tableConstants_.supports2009Model() )
                insert.setInt(18, 0); //entryStateId.
            insert.setString(19, "The parent child relationships.");

            insert.executeUpdate();
            insert.close();
        } catch (SQLException e) {
            messages_.fatalAndThrowException("FATAL ERROR - Problem loading the association definition", e);
        }

        insert = sqlConnection_.prepareStatement(tableConstants_.getInsertStatementSQL(SQLTableConstants.ENTITY));

        PreparedStatement insertIntoConceptProperty = sqlConnection_.prepareStatement(tableConstants_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_PROPERTY));

        PreparedStatement checkForCode = sqlConnection_.prepareStatement("SELECT count(*) as found from "
                + tableConstants_.getTableName(SQLTableConstants.ENTITY) + " WHERE "
                + tableConstants_.codingSchemeNameOrId + " = ? AND " + tableConstants_.entityCodeOrId + " = ?");

        PreparedStatement checkForDefinition = sqlConnection_.prepareStatement("SELECT count(*) as found from "
                + tableConstants_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " WHERE "
                + tableConstants_.codingSchemeNameOrId + " = ? AND " + tableConstants_.entityCodeOrEntityId
                + " = ? AND " + tableConstants_.propertyOrPropertyName + " = ?");

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
                    int k = 1;
                    insert.setString(k++, codingScheme.codingSchemeName);
                    insert.setString(k++, concepts[i].code);
                    DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
                    DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
                    DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
                    DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("true"), false);
                    if (tableConstants_.supports2009Model()) {
                        DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("true"), false);
                        DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("false"), false);
                    }
                    insert.setString(k++, SQLTableConstants.TBLCOLVAL_STATUS_ACTIVE);
                    DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("false"), false);
                    insert.setString(k++, concepts[i].name);

                    insert.executeUpdate();

                    // also add the textualPresentation to the conceptProperty
                    // table
                    k = 1;
                    insertIntoConceptProperty.setString(k++, codingScheme.codingSchemeName);
                    insertIntoConceptProperty.setString(k++, SQLTableConstants.ENTITYTYPE_CONCEPT);
                    insertIntoConceptProperty.setString(k++, concepts[i].code);
                    insertIntoConceptProperty.setString(k++, "p-1");
                    insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_PRESENTATION);
                    insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
                    insertIntoConceptProperty.setString(k++, codingScheme.defaultLanguage);
                    insertIntoConceptProperty.setString(k++, "");
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, new Boolean("true"), false);
                    insertIntoConceptProperty.setString(k++, "");
                    DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, null, false);
                    insertIntoConceptProperty.setString(k++, "");
                    insertIntoConceptProperty.setString(k++, concepts[i].name);

                    insertIntoConceptProperty.executeUpdate();
                }

                if (concepts[i].description != null && concepts[i].description.length() > 0) {
                    // description to definition
                    // check for match first
                    checkForDefinition.setString(1, codingScheme.codingSchemeName);
                    checkForDefinition.setString(2, concepts[i].code);
                    checkForDefinition.setString(3, SQLTableConstants.TBLCOLVAL_DEFINITION);

                    results = checkForDefinition.executeQuery();
                    // always one result
                    results.next();
                    if (results.getInt("found") > 0) {
                        messages_.info("WARNING - The concept code: '" + concepts[i].code + "' name: '"
                                + concepts[i].name + "' has multiple descriptions.  Skipping later descriptions.");
                    } else {
                        int k = 1;
                        insertIntoConceptProperty.setString(k++, codingScheme.codingSchemeName);
                        insertIntoConceptProperty.setString(k++, SQLTableConstants.ENTITYTYPE_CONCEPT);
                        insertIntoConceptProperty.setString(k++, concepts[i].code);
                        insertIntoConceptProperty.setString(k++, "d-1");
                        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_DEFINITION);
                        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_DEFINITION);
                        insertIntoConceptProperty.setString(k++, codingScheme.defaultLanguage);
                        insertIntoConceptProperty.setString(k++, "");
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, new Boolean("true"),
                                false);
                        insertIntoConceptProperty.setString(k++, "");
                        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, null, false);
                        insertIntoConceptProperty.setString(k++, "");
                        insertIntoConceptProperty.setString(k++, concepts[i].description);

                        insertIntoConceptProperty.executeUpdate();
                    }
                }
                if (i % 10 == 0) {
                    messages_.busy();
                }
                results.close();
            } catch (Exception e) {
                messages_.fatalAndThrowException("Problem loading concept code " + concepts[i], e);
            }
        }

        // Add the special code
        int k = 1;
        insert.setString(k++, codingScheme.codingSchemeName);
        insert.setString(k++, specialConcept.code);
        DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
        DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
        DBUtility.setBooleanOnPreparedStatment(insert, k++, null);
        DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("true"), false);
        if (tableConstants_.supports2009Model()) {
            DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("false"), false);
        }
        insert.setString(k++, SQLTableConstants.TBLCOLVAL_STATUS_ACTIVE);
        DBUtility.setBooleanOnPreparedStatment(insert, k++, new Boolean("true"), false);
        insert.setString(k++, specialConcept.name);

        insert.executeUpdate();

        k = 1;
        insertIntoConceptProperty.setString(k++, codingScheme.codingSchemeName);
        insertIntoConceptProperty.setString(k++, SQLTableConstants.ENTITYTYPE_CONCEPT);
        insertIntoConceptProperty.setString(k++, specialConcept.code);
        insertIntoConceptProperty.setString(k++, "p-1");
        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_PRESENTATION);
        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
        insertIntoConceptProperty.setString(k++, codingScheme.defaultLanguage);
        insertIntoConceptProperty.setString(k++, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, new Boolean("true"), false);
        insertIntoConceptProperty.setString(k++, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, null, false);
        insertIntoConceptProperty.setString(k++, "");
        insertIntoConceptProperty.setString(k++, specialConcept.name);

        insertIntoConceptProperty.executeUpdate();

        k = 1;
        insertIntoConceptProperty.setString(k++, codingScheme.codingSchemeName);
        insertIntoConceptProperty.setString(k++, SQLTableConstants.ENTITYTYPE_CONCEPT);
        insertIntoConceptProperty.setString(k++, specialConcept.code);
        insertIntoConceptProperty.setString(k++, "d-1");
        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_DEFINITION);
        insertIntoConceptProperty.setString(k++, SQLTableConstants.TBLCOLVAL_DEFINITION);
        insertIntoConceptProperty.setString(k++, codingScheme.defaultLanguage);
        insertIntoConceptProperty.setString(k++, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, new Boolean("true"), false);
        insertIntoConceptProperty.setString(k++, "");
        DBUtility.setBooleanOnPreparedStatment(insertIntoConceptProperty, k++, null, false);
        insertIntoConceptProperty.setString(k++, "");
        insertIntoConceptProperty.setString(k++, specialConcept.description);

        insertIntoConceptProperty.executeUpdate();

        insert.close();
        insertIntoConceptProperty.close();
        checkForCode.close();
        checkForDefinition.close();
    }

    private void loadHasSubtypeRelations(CodingScheme codingScheme) throws Exception {
        messages_.info("Loading relationships");
        PreparedStatement insertIntoConceptAssociations = sqlConnection_.prepareStatement(tableConstants_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));

        PreparedStatement checkForAssociation = sqlConnection_.prepareStatement("SELECT count(*) as found from "
                + tableConstants_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                + tableConstants_.codingSchemeNameOrId + " = ? AND " + tableConstants_.containerNameOrContainerDC
                + " = ? AND " + tableConstants_.entityCodeOrAssociationId + " = ? AND "
                + tableConstants_.sourceCSIdOrEntityCodeNS + " = ? AND " + tableConstants_.sourceEntityCodeOrId
                + " = ? AND " + tableConstants_.targetCSIdOrEntityCodeNS + " = ? AND "
                + tableConstants_.targetEntityCodeOrId + " = ?");

        Concept[] concepts = codingScheme.concepts;

        for (int i = 0; i < concepts.length; i++) {
            try {
                Concept parent = TextUtility.getParent(concepts, i);
                if (parent == null) {
                    parent = specialConcept;
                }
                checkForAssociation.setString(1, codingScheme.codingSchemeName);
                checkForAssociation.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
                checkForAssociation.setString(3, SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION);
                checkForAssociation.setString(4, codingScheme.codingSchemeName);
                checkForAssociation.setString(5, parent.code);
                checkForAssociation.setString(6, codingScheme.codingSchemeName);
                checkForAssociation.setString(7, concepts[i].code);
                ResultSet results = checkForAssociation.executeQuery();
                // always one result
                results.next();
                if (results.getInt("found") > 0) {
                    messages_.info("WARNING - Relationship '" + parent.code + "' (" + parent.name + ") to '"
                            + concepts[i].code + "' (" + concepts[i].name + ") already exists.  Skipping.");
                    continue;
                }
                insertIntoConceptAssociations.setString(1, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
                insertIntoConceptAssociations.setString(3, SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION);
                insertIntoConceptAssociations.setString(4, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(5, SQLTableConstants.ENTITYTYPE_CONCEPT);
                insertIntoConceptAssociations.setString(6, parent.code);
                insertIntoConceptAssociations.setString(7, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(8, SQLTableConstants.ENTITYTYPE_CONCEPT);
                insertIntoConceptAssociations.setString(9, concepts[i].code);
                insertIntoConceptAssociations.setString(10, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 11, null, false);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 12, null, false);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 13, null, false);

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

    private void loadRelations(CodingScheme codingScheme) throws Exception {
        TreeSet relationNameSet = new TreeSet();
        messages_.info("Loading relationships");
        PreparedStatement insertIntoConceptAssociations = sqlConnection_.prepareStatement(tableConstants_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));

        PreparedStatement insert = sqlConnection_.prepareStatement(tableConstants_
                .getInsertStatementSQL(SQLTableConstants.ASSOCIATION));
        Association[] associations = codingScheme.associations;

        for (int i = 0; i < associations.length; i++) {
            try {
                relationNameSet.add(associations[i].getRelationName());
                insertIntoConceptAssociations.setString(1, codingScheme.codingSchemeName);
                insertIntoConceptAssociations.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
                insertIntoConceptAssociations.setString(3, associations[i].getRelationName());
                insertIntoConceptAssociations.setString(4, associations[i].getSourceCodingScheme());
                insertIntoConceptAssociations.setString(5, SQLTableConstants.ENTITYTYPE_CONCEPT);
                insertIntoConceptAssociations.setString(6, associations[i].getSourceCode());
                insertIntoConceptAssociations.setString(7, associations[i].getTargetCodingScheme());
                insertIntoConceptAssociations.setString(8, SQLTableConstants.ENTITYTYPE_CONCEPT);
                insertIntoConceptAssociations.setString(9, associations[i].getTargetCode());
                insertIntoConceptAssociations.setString(10, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 9, null, false);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 10, null, false);
                DBUtility.setBooleanOnPreparedStatment(insertIntoConceptAssociations, 11, null, false);

                insertIntoConceptAssociations.executeUpdate();
                if (i % 10 == 0) {
                    messages_.busy();
                }

            } catch (SQLException e) {
                messages_.fatalAndThrowException("Problem loading relationships for " + associations[i], e);
            }
        }

        for (Iterator i = relationNameSet.iterator(); i.hasNext();) {
            messages_.info("Loading coding scheme supported attributes");
            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));

            String rel_name = i.next().toString();
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);
            insert.setString(3, rel_name);
            insert.setString(4, "");
            insert.setString(5, "");
            insert.setString(6, "");
            insert.setString(7, "");

            try {
                insert.executeUpdate();
            } catch (SQLException ex) {
                messages_.fatalAndThrowException("Problem loading supportedAssociation for " + rel_name, ex);
            }

            insert = sqlConnection_.prepareStatement(tableConstants_
                    .getInsertStatementSQL(SQLTableConstants.ASSOCIATION));
            insert.setString(1, codingScheme.codingSchemeName);
            insert.setString(2, SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
            insert.setString(3, rel_name);
            insert.setString(4, rel_name);
            insert.setString(5, "");
            insert.setString(6, "");

            // TODO deal with isNavigable, inverse
            // properly - what should they be?
            DBUtility.setBooleanOnPreparedStatment(insert, 7, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 8, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 9, new Boolean("true"), false);
            DBUtility.setBooleanOnPreparedStatment(insert, 10, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 11, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 12, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 13, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 14, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 15, null, false);
            DBUtility.setBooleanOnPreparedStatment(insert, 16, null, false);
            insert.setString(17, "");
            if( tableConstants_.supports2009Model() )
                insert.setInt(18, 0);
            insert.setString(19, "");
            
            try {
                insert.executeUpdate();
            } catch (SQLException ex) {
                messages_.fatalAndThrowException("Problem loading Association for " + rel_name, ex);
            }

        }
        insert.close();
        insertIntoConceptAssociations.close();

    }
}