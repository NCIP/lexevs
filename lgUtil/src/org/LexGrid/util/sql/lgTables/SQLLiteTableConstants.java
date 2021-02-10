
package org.LexGrid.util.sql.lgTables;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Constants like the insert statements for the SQLLite tables.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class SQLLiteTableConstants {
    public SQLLiteTableConstants() {
        init();
    }

    // numbers a just keys... variable names represent all tables.
    public static final String INSERT_INTO_CODING_SCHEME = "1";
    public static final String INSERT_INTO_CODING_SCHEME_SUPPORTED_ATTRIBUTES = "2";
    public static final String INSERT_INTO_CODED_ENTRY = "3";
    public static final String INSERT_INTO_CONCEPT_PROPERTY = "4";
    public static final String INSERT_INTO_ASSOCIATION_DEFINITION = "5";
    public static final String INSERT_INTO_CONCEPT_ASSOCIATION = "6";

    private Hashtable statements = new Hashtable();

    /**
     * Initializes table constants
     */
    public void init() {
        statements
                .put(
                        INSERT_INTO_CODING_SCHEME,
                        "INSERT INTO codingScheme (codingSchemeName, codingSchemeId, defaultLanguage, representsVersion, formalName, approxNumConcepts, source, entityDescription, copyright, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statements
                .put(
                        INSERT_INTO_ASSOCIATION_DEFINITION,
                        "INSERT INTO associationDefinition (codingSchemeName, association, reverseName, associationDescription, isTransitive, isSymmetric, isReflexive, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statements
                .put(
                        INSERT_INTO_CODING_SCHEME_SUPPORTED_ATTRIBUTES,
                        "INSERT INTO codingSchemeSupportedAttributes (codingSchemeName, supportedAttributeTag, supportedAttributeValue, urn, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statements
                .put(
                        INSERT_INTO_CODED_ENTRY,
                        "INSERT INTO codedEntry (codingSchemeName, conceptCode, conceptName, conceptDescription, isActive, conceptStatus, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statements
                .put(
                        INSERT_INTO_CONCEPT_ASSOCIATION,
                        "INSERT INTO conceptAssociation (sourceCodingSchemeName, sourceConceptCode, association, targetCodingSchemeName, targetConceptCode, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statements
                .put(
                        INSERT_INTO_CONCEPT_PROPERTY,
                        "INSERT INTO conceptProperty (codingSchemeName, conceptCode, property, propertyId, propertyText, language, format, isPreferred, addEntry, updateEntry, deleteEntry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

    /**
     * Returns the pre-defined statements
     * 
     * @return statements
     */
    public String[] getStatements() {
        Enumeration temp = statements.keys();
        String[] keys = new String[statements.size()];
        int i = 0;
        while (temp.hasMoreElements()) {
            keys[i++] = (String) temp.nextElement();
        }
        return keys;
    }

    /**
     * Returns a specific statement
     * 
     * @param key
     *            statement to look for
     * @return statement
     */
    public String getStatementSQL(String key) {
        return (String) statements.get(key);
    }

}