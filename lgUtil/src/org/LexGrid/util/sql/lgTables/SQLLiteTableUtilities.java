
package org.LexGrid.util.sql.lgTables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.LexGrid.util.sql.GenericSQLModifier;
import org.apache.log4j.Logger;

/**
 * Class for creating, adding/removing constraints, and cleaning the SQLLite
 * tables.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class SQLLiteTableUtilities {
    private Connection sqlConnection_;
    private static Logger log = Logger.getLogger("convert.SQLLiteTableUtilities");
    private ArrayList tableIndexSql_ = new ArrayList();

    private GenericSQLModifier gsm_;

    /**
     * @param sqlConnection
     * @throws Exception
     */
    public SQLLiteTableUtilities(Connection sqlConnection) throws Exception {
        sqlConnection_ = sqlConnection;
        gsm_ = new GenericSQLModifier(sqlConnection_, true);
        log.debug("initing index creation sql");
        initCreateIndexTableSql();
    }

    /**
     * Returns the specific database type
     * 
     * @return database type
     */
    public String getDatabaseType() {
        return gsm_.getDatabaseType();
    }

    /**
     * Creates the Tables
     * 
     * @throws SQLException
     */
    public void createAllTables() throws SQLException {
        log.debug("createAllTables called");
        try {
            sqlConnection_.prepareStatement(
                    gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^codingScheme^ ("
                            + " ^codingSchemeName^ {limitedText}(70) NOT NULL,"
                            + " ^codingSchemeId^ {limitedText}(100) NOT NULL,"
                            + " ^defaultLanguage^ {limitedText}(32) default 'EN' NOT NULL,"
                            + " ^representsVersion^ {limitedText}(50) default NULL,"
                            + " ^formalName^ {limitedText}(250) NOT NULL,"
                            + " ^approxNumConcepts^ {bigInt} default 100,"
                            + " ^source^ {limitedText}(50) default NULL," + " ^entityDescription^ {unlimitedText},"
                            + " ^copyright^ {unlimitedText}," + " ^addEntry^ {boolean} default NULL,"
                            + " ^updateEntry^ {boolean} default NULL," + " ^deleteEntry^ {boolean} default NULL,"
                            + " PRIMARY KEY (^codingSchemeName^)," + " UNIQUE (^codingSchemeId^)" + ") {TYPE}"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }

        try {
            sqlConnection_.prepareStatement(
                    gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^associationDefinition^ ("
                            + " ^codingSchemeName^ {limitedText}(70) NOT NULL,"
                            + " ^association^ {limitedText}(100) NOT NULL,"
                            + " ^reverseName^ {limitedText}(50) default NULL,"
                            + " ^associationDescription^ {unlimitedText}," + " ^isTransitive^ {boolean} default NULL,"
                            + " ^isSymmetric^ {boolean} default NULL," + " ^isReflexive^ {boolean} default NULL,"
                            + " ^addEntry^ {boolean} default NULL," + " ^updateEntry^ {boolean} default NULL,"
                            + " ^deleteEntry^ {boolean} default NULL,"
                            + " PRIMARY KEY  (^codingSchemeName^, ^association^)" + ") {TYPE}")).execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_.prepareStatement(
                    gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^codedEntry^ ("
                            + " ^codingSchemeName^ {limitedText}(70) NOT NULL,"
                            + " ^conceptCode^ {limitedText}(100) NOT NULL,"
                            + " ^conceptName^ {limitedText}(250) NOT NULL," + " ^conceptDescription^ {unlimitedText},"
                            + " ^isActive^ {boolean} default {true},"
                            + " ^conceptStatus^ {limitedText}(10) default 'Active' NOT NULL,"
                            + " ^addEntry^ {boolean} default NULL," + " ^updateEntry^ {boolean} default NULL,"
                            + " ^deleteEntry^ {boolean} default NULL,"
                            + " PRIMARY KEY  (^codingSchemeName^, ^conceptCode^)" + ") {TYPE}")).execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("CREATE TABLE {IF NOT EXISTS} ^conceptAssociation^ ("
                                            + " ^sourceCodingSchemeName^ {limitedText}(70) NOT NULL,"
                                            + " ^sourceConceptCode^ {limitedText}(100) NOT NULL,"
                                            + " ^association^ {limitedText}(100) NOT NULL,"
                                            + " ^targetCodingSchemeName^ {limitedText}(70) NOT NULL,"
                                            + " ^targetConceptCode^ {limitedText}(100) NOT NULL,"
                                            + " ^addEntry^ {boolean} default NULL,"
                                            + " ^updateEntry^ {boolean} default NULL,"
                                            + " ^deleteEntry^ {boolean} default NULL,"
                                            + " PRIMARY KEY  (^sourceCodingSchemeName^, ^sourceConceptCode^, ^association^, ^targetCodingSchemeName^, ^targetConceptCode^)"
                                            + ") {TYPE}")).execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }

        try {
            sqlConnection_.prepareStatement(
                    gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^codingSchemeSupportedAttributes^ ("
                            + " ^codingSchemeName^ {limitedText}(70) NOT NULL,"
                            + " ^supportedAttributeTag^ {limitedText}(20) NOT NULL,"
                            + " ^supportedAttributeValue^ {limitedText}(250) NOT NULL,"
                            + " ^urn^ {limitedText}(50) default NULL," + " ^addEntry^ {boolean} default NULL,"
                            + " ^updateEntry^ {boolean} default NULL," + " ^deleteEntry^ {boolean} default NULL,"
                            + " PRIMARY KEY (^codingSchemeName^, ^supportedAttributeTag^, ^supportedAttributeValue^)"
                            + ") {TYPE}")).execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }

        try {
            sqlConnection_.prepareStatement(
                    gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^conceptProperty^ ("
                            + " ^codingSchemeName^ {limitedText}(70) default 'textualPresentation' NOT NULL,"
                            + " ^conceptCode^ {limitedText}(100) NOT NULL,"
                            + " ^property^ {limitedText}(250) NOT NULL," + " ^propertyId^ {limitedText}(50) NOT NULL,"
                            + " ^propertyText^ {unlimitedText}," + " ^language^ {limitedText}(32) default NULL,"
                            + " ^format^ {limitedText}(50) default NULL," + " ^isPreferred^ {boolean} default NULL,"
                            + " ^addEntry^ {boolean} default NULL," + " ^updateEntry^ {boolean} default NULL,"
                            + " ^deleteEntry^ {boolean} default NULL,"
                            + " PRIMARY KEY (^codingSchemeName^, ^conceptCode^, ^property^, ^propertyId^)"
                            + " ) {TYPE}")).execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("already an object") == -1) {
                log.error(e);
                throw e;
            }
        }

        createTableIndexes();
    }

    /**
     * Creates the constraints on the tables
     * 
     * @throws SQLException
     */
    public void createTableConstraints() throws SQLException {
        log.debug("Creating table constraints");
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^codedEntry^ ADD CONSTRAINT ^a^ FOREIGN KEY (^codingSchemeName^) REFERENCES ^codingScheme^ (^codingSchemeName^)"))
                    .execute();
        } catch (SQLException e) {
            // try to figure out what the databases return if the constraint
            // already exists... (mysql does the errno 121
            // business
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }

        // mysql's foreign constraints didn't add the proper indexes in older
        // versions of mysql.
        if (gsm_.getDatabaseType().equals("MySQL")) {
            try {
                sqlConnection_
                        .prepareStatement(
                                gsm_
                                        .modifySQL("ALTER TABLE ^conceptAssociation^ ADD INDEX ^b1^ (^sourceCodingSchemeName^, ^association^)"))
                        .execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                        && e.toString().indexOf("-601") == -1 && e.toString().indexOf("Duplicate") == -1) {
                    log.error(e);
                    throw e;
                }
            }
        }

        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^conceptAssociation^ ADD CONSTRAINT ^b^ FOREIGN KEY (^sourceCodingSchemeName^, ^association^) REFERENCES ^associationDefinition^ (^codingSchemeName^, ^association^)"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^conceptAssociation^ ADD CONSTRAINT ^c^ FOREIGN KEY (^sourceCodingSchemeName^, ^sourceConceptCode^) REFERENCES ^codedEntry^ (^codingSchemeName^, ^conceptCode^)"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^codingSchemeSupportedAttributes^ ADD CONSTRAINT ^e^ FOREIGN KEY (^codingSchemeName^) REFERENCES ^codingScheme^ (^codingSchemeName^)"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^conceptProperty^ ADD CONSTRAINT ^f^ FOREIGN KEY (^codingSchemeName^, ^conceptCode^) REFERENCES ^codedEntry^ (^codingSchemeName^, ^conceptCode^)"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }
        try {
            sqlConnection_
                    .prepareStatement(
                            gsm_
                                    .modifySQL("ALTER TABLE ^associationDefinition^ ADD CONSTRAINT ^g^ FOREIGN KEY (^codingSchemeName^) REFERENCES ^codingScheme^ (^codingSchemeName^)"))
                    .execute();
        } catch (SQLException e) {
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("-601") == -1 && e.toString().indexOf("errno: 121") == -1) {
                log.error(e);
                throw e;
            }
        }
    }

    /*
     * Remove table constraints
     */
    public void dropTableConstraints() throws SQLException {
        log.debug("removing table constraints");
        try {
            try {
                sqlConnection_.prepareStatement(gsm_.modifySQL("ALTER TABLE ^codedEntry^ {DROPFOREIGNKEY} ^a^"))
                        .execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
            try {
                sqlConnection_
                        .prepareStatement(gsm_.modifySQL("ALTER TABLE ^conceptAssociation^ {DROPFOREIGNKEY} ^b^"))
                        .execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
            try {
                sqlConnection_
                        .prepareStatement(gsm_.modifySQL("ALTER TABLE ^conceptAssociation^ {DROPFOREIGNKEY} ^c^"))
                        .execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
            try {
                sqlConnection_.prepareStatement(
                        gsm_.modifySQL("ALTER TABLE ^codingSchemeSupportedAttributes^ {DROPFOREIGNKEY} ^e^")).execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
            try {
                sqlConnection_.prepareStatement(gsm_.modifySQL("ALTER TABLE ^conceptProperty^ {DROPFOREIGNKEY} ^f^"))
                        .execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
            try {
                sqlConnection_.prepareStatement(
                        gsm_.modifySQL("ALTER TABLE ^associationDefinition^ {DROPFOREIGNKEY} ^g^")).execute();
            } catch (SQLException e) {
                if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1
                        && e.toString().indexOf("not found") == -1 && e.toString().indexOf("errno: 152") == -1) {
                    log.error(e);
                    throw e;
                }
            }
        } catch (SQLException e) {
            log.error(e);
            throw e;
        }
    }

    /**
     * Remove all data from the tables of a given Coding Scheme
     * 
     * @param codingScheme
     *            target Coding Scheme
     * @throws SQLException
     */
    public void cleanTables(String codingScheme) throws SQLException {
        log.debug("Cleaning tables of the '" + codingScheme + "' coding scheme");
        PreparedStatement deleteAssociations = sqlConnection_
                .prepareStatement("DELETE FROM conceptAssociation where conceptAssociation.sourceCodingSchemeName = ?");
        deleteAssociations.setString(1, codingScheme);
        deleteAssociations.execute();

        PreparedStatement deleteProperties = sqlConnection_
                .prepareStatement("DELETE FROM conceptProperty where conceptProperty.codingSchemeName = ?");
        deleteProperties.setString(1, codingScheme);
        deleteProperties.execute();

        PreparedStatement deleteSupportedAttributes = sqlConnection_
                .prepareStatement("DELETE FROM codingSchemeSupportedAttributes where codingSchemeSupportedAttributes.codingSchemeName = ?");
        deleteSupportedAttributes.setString(1, codingScheme);
        deleteSupportedAttributes.execute();

        PreparedStatement deleteCodedEntry = sqlConnection_
                .prepareStatement("DELETE FROM codedEntry where codedEntry.codingSchemeName = ?");
        deleteCodedEntry.setString(1, codingScheme);
        deleteCodedEntry.execute();

        PreparedStatement deleteAssociationDefinitions = sqlConnection_
                .prepareStatement("DELETE FROM associationDefinition where associationDefinition.codingSchemeName = ?");
        deleteAssociationDefinitions.setString(1, codingScheme);
        deleteAssociationDefinitions.execute();

        PreparedStatement deleteCodingScheme = sqlConnection_
                .prepareStatement("DELETE FROM codingScheme where codingScheme.codingSchemeName = ?");
        deleteCodingScheme.setString(1, codingScheme);
        deleteCodingScheme.execute();
    }

    private void initCreateIndexTableSql() {
        if (gsm_.getDatabaseType().equals("PostgreSQL") || gsm_.getDatabaseType().startsWith("DB2")) {
            // no reason to put an index on propertyValue on PostgreSQL, since
            // it can't be used anyway
            // (due to case sensitivity issues) and it causes errors when long
            // properties are added.
            // same problems with DB2
            tableIndexSql_.add("CREATE INDEX ^i1^ ON ^conceptProperty^ (^codingSchemeName^ , ^property^) ");
        } else {
            tableIndexSql_
                    .add("CREATE INDEX ^i1^ ON ^conceptProperty^ (^codingSchemeName^ , ^property^ , ^propertyText^ {DEFAULT_INDEX_SIZE}) ");
        }
        tableIndexSql_
                .add("CREATE INDEX ^i2^ ON ^conceptAssociation^ (^sourceCodingSchemeName^, ^association^, ^targetConceptCode^)");
    }

    private void createTableIndexes() throws SQLException {
        log.debug("Creating table indexes");

        for (int i = 0; i < tableIndexSql_.size(); i++) {
            try {
                sqlConnection_.prepareStatement(gsm_.modifySQL((String) tableIndexSql_.get(i))).execute();
            } catch (SQLException e) {
                // try to figure out what the databases return if the constraint
                // already exists... (mysql does the errno
                // 121
                // business
                if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                        && e.toString().indexOf("errno: 121") == -1 && e.toString().indexOf("-601") == -1
                        && e.toString().indexOf("Duplicate key name") == -1) {
                    log.error("Problem creating the index " + (String) tableIndexSql_.get(i), e);
                    throw e;
                } else {
                    log.debug("The index " + (String) tableIndexSql_.get(i) + " already exits");
                }
            }
        }
    }
}