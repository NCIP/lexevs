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
package org.LexGrid.util.sql.lgTables;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.apache.commons.collections.map.LRUMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to make the tables for the new SQL format.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 */
@Deprecated
public class SQLTableUtilities {
    private Connection sqlConnection_;
    private DataSource connectionPool_;

    private static Logger log = LogManager.getLogger("convert.SQL");

    private Hashtable<String, String> defaultTableCreateSql_ = new Hashtable<String, String>();
    private ArrayList<String> defaultTableIndexSql_ = new ArrayList<String>();
    private ArrayList<String> defaultTableForeignKeySql_ = new ArrayList<String>();
    private ArrayList<String> defaultTableDropForeignKeySql_ = new ArrayList<String>();
    
    private GenericSQLModifier gsm_;
    private SQLTableConstants stc_;
    private String tablePrefix_;

    public static final String versionString = "1.8";
    public static final String tableStructureDescription = "This is version 1.8 of the LexGrid SQL format - this is compatible with the 2009/01 LexGrid Schema";

    /**
     * @param sqlConnection
     *            connection to a SQL database
     * @throws Exception
     */
    public SQLTableUtilities(Connection sqlConnection, String tablePrefix) throws Exception {
        sqlConnection_ = sqlConnection;
        connectionPool_ = null;
        tablePrefix_ = tablePrefix;
        gsm_ = new GenericSQLModifier(sqlConnection_);

        if (doTablesExist()) {
            stc_ = new SQLTableConstants(getExistingTableVersion(), tablePrefix_);
        } else {
            stc_ = new SQLTableConstants(versionString, tablePrefix_);
        }
    }

    /**
     * Use this constructor if you would rather have it check out connections
     * from the pool as needed.
     * 
     * @param connectionPool
     * @param tablePrefix
     * @throws Exception
     */
    public SQLTableUtilities(DataSource connectionPool, String tablePrefix) throws Exception {
        sqlConnection_ = null;
        connectionPool_ = connectionPool;
        tablePrefix_ = tablePrefix;

        Connection temp = getConnection();
        try {

            gsm_ = new GenericSQLModifier(temp);
        } finally {

            returnConnection(temp);
        }

        if (doTablesExist()) {
            stc_ = new SQLTableConstants(getExistingTableVersion(), tablePrefix_);
        } else {
            stc_ = new SQLTableConstants(versionString, tablePrefix_);
        }
    }

    private Connection getConnection() {
        if (sqlConnection_ != null) {
            return sqlConnection_;
        } else {
            try {
                return connectionPool_.getConnection();
            } catch (Exception e) {
                return null;
            }
        }
    }

    private void returnConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
           //
        }
    }

    public SQLTableConstants getSQLTableConstants() {
        return stc_;
    }

    public GenericSQLModifier getGenericSQLModifier() {
        return gsm_;
    }

    /**
     * Initializes and creates the tables
     */
    private void initTableCreateSQL() {
        // Please do not format this file or this method as it is ready to read
        // and check the
        // way it is.
        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ASSOCIATION), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.ASSOCIATION) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ASSOCIATIONNAME + "^ {limitedText}(100) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_FORWARDNAME + "^ {limitedText}(100) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_REVERSENAME + "^ {limitedText}(100) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_INVERSEID + "^ {limitedText}(100) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISNAVIGABLE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISTRANSITIVE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISANTITRANSITIVE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISSYMMETRIC + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISANTISYMMETRIC + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISREFLEXIVE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISANTIREFLEXIVE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISFUNCTIONAL + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISREVERSEFUNCTIONAL + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL," + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.CODING_SCHEME), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMEURI + "^ {limitedText}(250) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_REPRESENTSVERSION + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_FORMALNAME + "^ {limitedText}(250) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_DEFAULTLANGUAGE + "^ {limitedText}(32) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS + "^ {bigInt} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default {true}," + " ^"
                + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_RELEASEURI + "^ {limitedText}(250) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_COPYRIGHT + "^ {unlimitedText} default NULL," + " PRIMARY KEY (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)," + " UNIQUE (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMEURI + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_TYPENAME + "^ {limitedText}(30) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL1 + "^ {limitedText}(250) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL2 + "^ {limitedText}(250) default NULL," + " PRIMARY KEY ( ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_TYPENAME
                        + "^, ^" + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP) + "^ ("
                        + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYID + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYNAME + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_LANGUAGE + "^ {limitedText}(32) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_FORMAT + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default {true}," + " ^"
                        + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYVALUE + "^ {unlimitedText} default NULL,"
                        + " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_PROPERTYID + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_TYPENAME + "^ {limitedText}(30) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL1 + "^ {limitedText}(250) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL2 + "^ {limitedText}(250) default NULL," + " PRIMARY KEY  (^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID
                        + "^, ^" + SQLTableConstants.TBLCOL_TYPENAME + "^, ^" + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                        + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES),
                "CREATE TABLE {IF NOT EXISTS} ^"
                        + stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + "^ (" + " ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + "^ {limitedText}(30) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ID + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_URI + "^ {limitedText}(250) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_IDVALUE + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL1 + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL2 + "^ {limitedText}(250) default NULL," + " PRIMARY KEY (^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + "^, ^" + SQLTableConstants.TBLCOL_ID
                        + "^, ^" + SQLTableConstants.TBLCOL_IDVALUE + "^, ^" + SQLTableConstants.TBLCOL_VAL1 + "^)"
                        + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.ENTITY) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISDEFINED + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISANONYMOUS + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default {true}," + " ^"
                + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL," + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^ )" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_TYPE), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_TYPE) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYTYPE + "^ {limitedText}(50) NOT NULL," + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYTYPE + "^)"
                + ") {TYPE} {lgTableCharSet}");

        String tempIndexString = "";
        // DB2 and Oracle doesn't support unique keys on columns that can have
        // nulls
        if ((!gsm_.getDatabaseType().startsWith("DB2")) && (!gsm_.getDatabaseType().startsWith("Oracle")))

        {
            tempIndexString = ", UNIQUE (^" + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^, ^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)";
        }

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ASSOCIATIONINSTANCEID + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISDEFINING + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISINFERRED + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " PRIMARY KEY  (^" +
                            SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^) " +
                            ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ASSOCIATIONINSTANCEID + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISDEFINING + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISINFERRED + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_DATAVALUE + "^ {unlimitedText} NOT NULL," + " PRIMARY KEY  (^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME
                        + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^, ^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^)" + tempIndexString
                        + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_QUALIFIERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_QUALIFIERVALUE + "^ {limitedText}(250) NOT NULL,"
                        + " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^, ^" + SQLTableConstants.TBLCOL_QUALIFIERNAME
                        + "^, ^" + SQLTableConstants.TBLCOL_QUALIFIERVALUE + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS)
                        + "^ (" + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL,"
                        + " ^" + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_QUALIFIERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_QUALIFIERVALUE + "^ {limitedText}(250) NOT NULL,"
                        + " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^, ^" + SQLTableConstants.TBLCOL_QUALIFIERNAME
                        + "^)" + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (" + " ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYID + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYTYPE + "^ {limitedText}(15) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYNAME + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_LANGUAGE + "^ {limitedText}(32) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_FORMAT + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISPREFERRED + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_DEGREEOFFIDELITY + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_REPRESENTATIONALFORM + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ISACTIVE + "^ {boolean} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYVALUE + "^ {unlimitedText} NOT NULL," + " PRIMARY KEY (^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                        + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^)" + " ) {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS),
                "CREATE TABLE {IF NOT EXISTS} ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS) + "^ ("
                        + " ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_SOURCEPROPERTYID + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_LINK + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_TARGETPROPERTYID + "^ {limitedText}(50) NOT NULL,"
                        + " PRIMARY KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                        + "^, ^" + SQLTableConstants.TBLCOL_SOURCEPROPERTYID + "^, ^" + SQLTableConstants.TBLCOL_LINK
                        + "^, ^" + SQLTableConstants.TBLCOL_TARGETPROPERTYID + "^)" + " ) {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES),
                "CREATE TABLE {IF NOT EXISTS} ^"
                        + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + "^ (" + " ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_PROPERTYID + "^ {limitedText}(50) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_TYPENAME + "^ {limitedText}(30) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_QUALIFIERTYPE + "^ {limitedText}(50) default NULL," + " ^"
                        + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL1 + "^ {limitedText}(250) NOT NULL," + " ^"
                        + SQLTableConstants.TBLCOL_VAL2 + "^ {limitedText}(250) default NULL," + " PRIMARY KEY (^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                        + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^, ^" + SQLTableConstants.TBLCOL_TYPENAME
                        + "^, ^" + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + "^, ^" + SQLTableConstants.TBLCOL_VAL1
                        + "^)" + " ) {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.RELATION), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.RELATION) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ISNATIVE + "^ {boolean} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL," + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)"
                + ") {TYPE} {lgTableCharSet}");

        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTRY_STATE), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^ {bigInt} NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTRYTYPE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_OWNER + "^ {limitedText}(250) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_STATUS + "^ {limitedText}(50) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_EFFECTIVEDATE + "^ {dateTime} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_EXPIRATIONDATE + "^ {dateTime} default NULL," + " ^"
                + SQLTableConstants.TBLCOL_REVISIONID + "^ {limitedText}(50) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_PREVREVISIONID + "^ {limitedText}(50) default NULL," + " ^"
                + SQLTableConstants.TBLCOL_CHANGETYPE + "^ {limitedText}(15) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_RELATIVEORDER + "^ {bigInt} NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_PREVENTRYSTATEID + "^ {bigInt} default NULL,"
                + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_ENTRYSTATEID + "^)"
                + ") {TYPE} {lgTableCharSet}");
        
        
       
        defaultTableCreateSql_.put(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE), "CREATE TABLE {IF NOT EXISTS} ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + "^ (" + " ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^ {limitedText}(200) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^ {limitedText}(50) NOT NULL," + " ^"
                + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^ {limitedText}(50) NOT NULL," + " PRIMARY KEY  (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^, ^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^"
                + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^, ^"
                + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^)" + ") {TYPE} {lgTableCharSet}");

       
    }

    /**
     * Initializes, creates, and indexes all tables
     * 
     * @throws SQLException
     */
    public void createDefaultTables() throws SQLException {
        createDefaultTables(true);
    }
    
    public Set<String> getDefaultTableKeys(){
        return defaultTableCreateSql_.keySet();
    }
    
    /**
     * Initializes and creates all tables, optionally indexing
     * 
     * @throws SQLException
     */
    public void createDefaultTables(boolean index) throws SQLException {
        log.debug("createDefaultTables called");

        if (doTablesExist()) {
            log.debug("tables already exist, not creating new ones");
            return;
        }

        if (defaultTableCreateSql_.size() == 0) {
            log.debug("initing table creation sql for version " + stc_.getVersion());
            initTableCreateSQL();
        }

        createMetaDataTable();

        Connection conn = getConnection();
        try {

            Enumeration tempEnum = defaultTableCreateSql_.keys();
            while (tempEnum.hasMoreElements()) {
                String key = (String) tempEnum.nextElement();
                log.debug("Creating " + key + " table");
                createTable(conn, gsm_.modifySQL((String) defaultTableCreateSql_.get(key)), key);
            }
        } finally {
            returnConnection(conn);
        }

        if(index){
            createDefaultTableIndexes();
        }
    }
    
    public void createSystemReleaseTables() throws SQLException {
        log.debug("createSystemReleaseTables called");

        Connection conn = getConnection();
        String create = null;
        try {
            create = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE) + "^ (" + " ^"
                    + SQLTableConstants.TBLCOL_RELEASEID + "^ {limitedText}(50) NOT NULL," + " ^"
                    + stc_.getCorrectColumnName(SQLTableConstants.TBLCOL_RELEASEURI)+ "^ {limitedText}(250) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_BASEDONRELEASE + "^ {limitedText}(250) default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_RELEASEDATE + "^ {dateTime} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_RELEASEAGENCY + "^ {limitedText}(250) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL,"
                    + " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_RELEASEID + "^, ^"
                    + stc_.getCorrectColumnName(SQLTableConstants.TBLCOL_RELEASEURI) + "^)) {TYPE} {lgTableCharSet}");

            createTable(conn, create, stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE));

            create = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE_REFS) + "^ (" + " ^"
                    + SQLTableConstants.TBLCOL_RELEASEID + "^ {limitedText}(50) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_RELEASETYPE + "^ {limitedText}(15) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_REFERENCETYPE + "^ {limitedText}(15) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_VERSION + "^ {limitedText}(50) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_LOCALID + "^ {limitedText}(50) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_URN + "^ {limitedText}(50) default NULL," + " PRIMARY KEY  (^"
                    + SQLTableConstants.TBLCOL_RELEASEID + "^, ^" + SQLTableConstants.TBLCOL_RELEASETYPE + "^, ^"
                    + SQLTableConstants.TBLCOL_REFERENCETYPE + "^, ^" + SQLTableConstants.TBLCOL_VERSION
                    + "^)) {TYPE} {lgTableCharSet}");
            createTable(conn, create, stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE_REFS));

            // create some indexes
            String createIndexSql = gsm_.modifySQL("CREATE INDEX ^isr1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE) + "^ (^"
                    + SQLTableConstants.TBLCOL_RELEASEDATE + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^isr2^ ON ^"
                    + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE) + "^ (^" + SQLTableConstants.TBLCOL_RELEASEID
                    + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            // create foreign keys
            if (stc_.supports2009Model()) {
                // m043346: i don't see where the systemReleaseRefs table used
                // so i'll no-op this code for 2009
            } else {
                String createFKSql = gsm_.modifySQL("ALTER TABLE ^"
                        + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE_REFS)
                        + "^ ADD CONSTRAINT ^sr^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_RELEASEID
                        + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE) + "^ (^"
                        + SQLTableConstants.TBLCOL_RELEASEID + "^)");
                createForeignKey(conn, createFKSql, createFKSql);
            }
        } finally {
            returnConnection(conn);
        }
    }

    public void createNCIHistoryTable() throws SQLException {
        log.debug("createNCIHistoryTable called");

        Connection conn = getConnection();
        try {
            String createTable = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (" + " ^"
                    + stc_.entityCodeOrEntityId + "^ {limitedText}(100) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_CONCEPTNAME + "^ {unlimitedText} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_EDITACTION + "^ {limitedText}(10) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_EDITDATE + "^ {dateTime} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_REFERENCECODE + "^ {limitedText}(100) default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_REFERENCENAME + "^ {unlimitedText} default NULL)"
                    + " {TYPE} {lgTableCharSet}");
            createTable(conn, createTable, stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY));

            // create some indexes
            String createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^" + stc_.entityCodeOrEntityId
                    + "^, ^" + SQLTableConstants.TBLCOL_EDITACTION + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh2^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^"
                    + SQLTableConstants.TBLCOL_EDITDATE + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh3^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^"
                    + SQLTableConstants.TBLCOL_REFERENCECODE + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);
        } finally {
            returnConnection(conn);
        }
    }

    public void createConceptHistoryTable() throws SQLException {
        log.debug("createConceptHistoryTable called");

        Connection conn = getConnection();
        try {
            String createTable = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY) + "^ (" + " ^" + stc_.entityCodeOrEntityId
                    + "^ {limitedText}(100) NOT NULL," + " ^" + SQLTableConstants.TBLCOL_CONCEPTNAME
                    + "^ {unlimitedText} NOT NULL," + " ^" + SQLTableConstants.TBLCOL_EDITACTION
                    + "^ {limitedText}(10) NOT NULL," + " ^" + SQLTableConstants.TBLCOL_EDITDATE
                    + "^ {dateTime} NOT NULL," + " ^" + SQLTableConstants.TBLCOL_REFERENCECODE
                    + "^ {limitedText}(100) default NULL," + " ^" + SQLTableConstants.TBLCOL_REFERENCENAME
                    + "^ {unlimitedText} default NULL)" + " {TYPE} {lgTableCharSet}");
            createTable(conn, createTable, stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY));

            // create some indexes
            String createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY) + "^ (^" + stc_.entityCodeOrEntityId
                    + "^, ^" + SQLTableConstants.TBLCOL_EDITACTION + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh2^ ON ^"
                    + stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY) + "^ (^" + SQLTableConstants.TBLCOL_EDITDATE
                    + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh3^ ON ^"
                    + stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY) + "^ (^"
                    + SQLTableConstants.TBLCOL_REFERENCECODE + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);
        } finally {
            returnConnection(conn);
        }
    }

    public void createCodingSchemeVersionsTable() throws SQLException {
        log.debug("createCodingSchemeVersionsTable called");

        Connection conn = getConnection();
        try {
            String createTable = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME_VERSIONS) + "^ (" + " ^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ {limitedText}(70) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMEKEY + "^ {limitedText}(70) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_VERSION + "^ {limitedText}(50) default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_ISCOMPLETE + "^ {boolean} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_VERSIONDATE + "^ {dateTime} default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_EFFECTIVEDATE + "^ {dateTime} default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_VERSIONORDER + "^ {bigInt} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_RELEASEURN + "^ {limitedText}(250) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_CHANGEDOCUMENTATION + "^ {unlimitedText} default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_CHANGEINSTRUCTIONS + "^ {unlimitedText} default NULL),"
                    + " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)) {TYPE} {lgTableCharSet}");
            createTable(conn, createTable, stc_.getTableName(SQLTableConstants.CODING_SCHEME_VERSIONS));

            // create some indexes
            // (none yet)

            // create foreign keys
            String createFKSql = gsm_.modifySQL("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME_VERSIONS)
                    + "^ ADD CONSTRAINT ^csvfk^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                    + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");
            createForeignKey(conn, createFKSql, createFKSql);

        } finally {
            returnConnection(conn);
        }
    }

    public void createVersionsTable() throws SQLException {
        log.debug("createNCIHistoryTable called");

        Connection conn = getConnection();
        try {
            String createTable = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (" + " ^"
                    + stc_.entityCodeOrEntityId + "^ {limitedText}(100) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_CONCEPTNAME + "^ {unlimitedText} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_EDITACTION + "^ {limitedText}(10) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_EDITDATE + "^ {dateTime} NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_REFERENCECODE + "^ {limitedText}(100) default NULL," + " ^"
                    + SQLTableConstants.TBLCOL_REFERENCENAME + "^ {unlimitedText} default NULL) "
                    + "{TYPE} {lgTableCharSet}");
            createTable(conn, createTable, stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY));

            // TODO: DB Changes (check)
            // create some indexes
            String createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^" + stc_.entityCodeOrEntityId
                    + "^, ^" + SQLTableConstants.TBLCOL_EDITACTION + "^) ");

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh2^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^"
                    + SQLTableConstants.TBLCOL_EDITDATE + "^) ");

            createIndexSql = gsm_.modifySQL("CREATE INDEX ^inh3^ ON ^"
                    + stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY) + "^ (^"
                    + SQLTableConstants.TBLCOL_REFERENCECODE + "^) ");
            createIndex(conn, createIndexSql, createIndexSql);
        } finally {
            returnConnection(conn);
        }
    }

    public void createMetaDataTable() throws SQLException {
        log.debug("createMetaDataTable called");

        Connection conn = getConnection();
        try {
            String createTable = gsm_.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
                    + stc_.getTableName(SQLTableConstants.LEXGRID_TABLE_META_DATA) + "^ (" + " ^"
                    + SQLTableConstants.TBLCOL_VERSION + "^ {limitedText}(50) NOT NULL," + " ^"
                    + SQLTableConstants.TBLCOL_DESCRIPTION + "^ {limitedText}(255) default NULL"
                    + ") {TYPE} {lgTableCharSet}");

            boolean created = createTable(conn, createTable, stc_
                    .getTableName(SQLTableConstants.LEXGRID_TABLE_META_DATA));

            if (created) {
                log.debug("Inserting version identifier");
                PreparedStatement tempInsert = conn.prepareStatement(gsm_.modifySQL(stc_
                        .getInsertStatementSQL(SQLTableConstants.LEXGRID_TABLE_META_DATA)));
                tempInsert.setString(1, versionString);
                tempInsert.setString(2, tableStructureDescription);
                tempInsert.executeUpdate();
                tempInsert.close();
            }
        } finally {
            returnConnection(conn);
        }
    }
    
    private void initCreateIndexTableSql() {

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY) + "^ (^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_TYPE) + "^ (^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "ip1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        if (gsm_.getDatabaseType().equals("PostgreSQL") || gsm_.getDatabaseType().startsWith("DB2")
                || gsm_.getDatabaseType().startsWith("Oracle"))

        {
            // no reason to put an index on propertyValue on PostgreSQL, since
            // it can't be used anyway
            // (due to case sensitivity issues) and it causes errors when long
            // properties are added.
            // db2 and Oracle don't support indexing the column data type.
            defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                    + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYTYPE + "^ ) ");
        } else {
            defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                    + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYVALUE + "^ {DEFAULT_INDEX_SIZE}, ^"
                    + SQLTableConstants.TBLCOL_PROPERTYTYPE + "^) ");
        }

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i11^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i12^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i13^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i13a^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + "^ (^"
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i13b^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + "^ (^"
                + SQLTableConstants.TBLCOL_PROPERTYID + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^ ) ");

        defaultTableIndexSql_
                .add("CREATE INDEX ^" + tablePrefix_ + "i2^ ON ^"
                        + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
                        + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)");

        defaultTableIndexSql_
                .add("CREATE INDEX ^" + tablePrefix_ + "i3^ ON ^"
                        + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
                        + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^, ^"
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                        + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)");
        
        defaultTableIndexSql_
                .add("CREATE INDEX ^" + tablePrefix_ + "iee1^ ON ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
                    + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
                    + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^"
                    + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^)");
        
        defaultTableIndexSql_
        .add("CREATE INDEX ^" + tablePrefix_ + "iee2^ ON ^"
            + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
            + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
            + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^, ^"
            + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^)");      
        
        defaultTableIndexSql_
            .add("CREATE INDEX ^" + tablePrefix_ + "iee3^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^, ^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)");
        
        defaultTableIndexSql_
        .add("CREATE INDEX ^" + tablePrefix_ + "iee4^ ON ^"
            + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
            + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^, ^"
            + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^, ^"
            + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
            + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)");        

        // other databases have a unique key here, DB2 and Oracle dont support
        // unique keys on null, so just
        // make a
        // normal key.
        if ((gsm_.getDatabaseType().startsWith("DB2")) || (gsm_.getDatabaseType().startsWith("Oracle"))) {

            defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i4^ ON ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ (^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                    + "^)");

            defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "i5^ ON ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) + "^ (^"
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                    + "^)");
        }
        
        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "ie3^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY) + "^ (^" 
                    + SQLTableConstants.TBLCOL_ENTRYSTATEID 
                + "^ ) ");
        
        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "tt1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + "^ (^"
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^" 
                + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^, ^" 
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^) ");
        
        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "tt2^ ON ^"
            + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + "^ (^"
            + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^, ^" 
            + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^, ^" 
            + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
            + "^) ");

        defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "tt3^ ON ^"
            + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + "^ (^"
            + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
            + SQLTableConstants.TBLCOL_SOURCEENTITYCODE + "^, ^"
            + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE + "^)");

       defaultTableIndexSql_.add("CREATE INDEX ^" + tablePrefix_ + "tt4^ ON ^"
           + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + "^ (^"
           + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
           + SQLTableConstants.TBLCOL_TARGETENTITYCODE + "^, ^"
           + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE + "^)");     

        defaultTableIndexSql_.add("CREATE INDEX ^qualsKey1^ ON ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS) + "^ (^"
                + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + "^) ");    
    }
    
    public void createDefaultTableIndexes() throws SQLException {
        log.debug("Creating table indexes");

        if (defaultTableIndexSql_.size() == 0) {
            log.debug("initing index creation sql");
            initCreateIndexTableSql();
        }
        Connection conn = getConnection();
        try {
            for (int i = 0; i < defaultTableIndexSql_.size(); i++) {
                createIndex(conn, gsm_.modifySQL((String) defaultTableIndexSql_.get(i)), (String) defaultTableIndexSql_
                        .get(i));
            }
        } finally {
            returnConnection(conn);
        }
    }

    private void initDefaultTableForeignKeySQL() {
        log.debug("initializing foreign key sql");
        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES) + "^ ADD CONSTRAINT ^"
                + tablePrefix_ + "a^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + "^ ADD CONSTRAINT ^"
                + tablePrefix_ + "b^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "c^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_TYPE)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "e^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.ENTITY) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "e^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.ENTITY) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + "^ ADD CONSTRAINT ^"
                + tablePrefix_ + "f^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^"
                + SQLTableConstants.TBLCOL_PROPERTYID + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.RELATION)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "g^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ASSOCIATION)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "i^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.RELATION) + "^ (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^)");

        // mysql doesn't automaticaly create this index like it should.
        if (gsm_.getDatabaseType().equals("MySQL")) {
            defaultTableForeignKeySql_.add("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ ADD INDEX ^"
                    + tablePrefix_ + "j1^ (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                    + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^)");
        }

        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + "^ ADD CONSTRAINT ^"
                + tablePrefix_ + "j^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.ASSOCIATION) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^ )");

        // old version of mysql don't automaticaly create this index like it
        // should.
        if (gsm_.getDatabaseType().equals("MySQL")) {
            defaultTableForeignKeySql_.add("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) + "^ ADD INDEX ^" + tablePrefix_
                    + "m1^ (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                    + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^"
                    + SQLTableConstants.TBLCOL_ENTITYCODE + "^)");
        }

        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) + "^ ADD CONSTRAINT ^" + tablePrefix_
                + "m^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.ASSOCIATION) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_CONTAINERNAME + "^, ^"
                + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" 
                + SQLTableConstants.TBLCOL_ENTITYCODE + "^)");

        // DB2 and Oracle doesn't allow foreign keys with columns that allow
        // null
        if ((!gsm_.getDatabaseType().startsWith("DB2")) && (!gsm_.getDatabaseType().startsWith("Oracle")))

        {
            // defaultTableForeignKeySql_.add("ALTER TABLE ^"
            // +
            // stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)
            // + "^ ADD CONSTRAINT ^o^ FOREIGN KEY (^"
            // + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
            // + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
            // + "^) REFERENCES ^" +
            // stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
            // + "^ (^"
            // + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
            // + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
            // + "^)");
            //
            // defaultTableForeignKeySql_.add("ALTER TABLE ^"
            // +
            // stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS)
            // + "^ ADD CONSTRAINT ^p^ FOREIGN KEY (^"
            // + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
            // + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
            // + "^) REFERENCES ^" +
            // stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA) +
            // "^ (^"
            // + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
            // + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
            // + "^)");
        }

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "q^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_SOURCEPROPERTYID + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "r^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE
                + "^, ^" + SQLTableConstants.TBLCOL_TARGETPROPERTYID + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                + "^, ^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP)
                + "^ ADD CONSTRAINT ^" + tablePrefix_ + "s^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                + "^) REFERENCES ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^)");

        defaultTableForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB) + "^ ADD CONSTRAINT ^"
                + tablePrefix_ + "t^ FOREIGN KEY (^" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^"
                + SQLTableConstants.TBLCOL_PROPERTYID + "^) REFERENCES ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP) + "^ (^"
                + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + "^, ^" + SQLTableConstants.TBLCOL_PROPERTYID + "^)");

        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.CODING_SCHEME)
        // + "^ ADD CONSTRAINT ^u^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");
        //		
        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP)
        // + "^ ADD CONSTRAINT ^v^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");
        //		
        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.ENTITY)
        // + "^ ADD CONSTRAINT ^w^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");
        //		
        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY)
        // + "^ ADD CONSTRAINT ^x^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");
        //		
        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA)
        // + "^ ADD CONSTRAINT ^y^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");
        //		
        // defaultTableForeignKeySql_.add("ALTER TABLE ^"
        // + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
        // + "^ ADD CONSTRAINT ^z^ FOREIGN KEY (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^) REFERENCES ^" +
        // stc_.getTableName(SQLTableConstants.ENTRY_STATE) + "^ (^"
        // + SQLTableConstants.TBLCOL_ENTRYSTATEID
        // + "^)");

    }

    /**
     * Inserts table constraints
     * 
     * @throws SQLException
     */
    public void createDefaultTableConstraints() throws SQLException {
        log.debug("Creating table constraints");
        if (!doTablesExist()) {
            log.debug("Tables don't exist - returning.");
            return;
        }

        if (defaultTableForeignKeySql_.size() == 0) {
            log.debug("initing the addForeignKey sql code");
            initDefaultTableForeignKeySQL();
        }

        Connection conn = getConnection();
        try {

            for (int i = 0; i < defaultTableForeignKeySql_.size(); i++) {
                createForeignKey(conn, gsm_.modifySQL((String) defaultTableForeignKeySql_.get(i)),
                        (String) defaultTableForeignKeySql_.get(i));
            }
        } finally {
            returnConnection(conn);
        }
    }
    
    private void initDropDefaultTableForeignKeySQL() {
        log.debug("initializing drop foreign key sql");
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ASSOCIATION)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "i^" : "^ {DROPFOREIGNKEY} ^i^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "a^" : "^ {DROPFOREIGNKEY} ^a^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "s^" : "^ {DROPFOREIGNKEY} ^s^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "t^" : "^ {DROPFOREIGNKEY} ^t^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "b^" : "^ {DROPFOREIGNKEY} ^b^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "c^" : "^ {DROPFOREIGNKEY} ^c^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "m^" : "^ {DROPFOREIGNKEY} ^m^"));
        
        // mysql doesn't automaticaly create this index like it should.
        if (gsm_.getDatabaseType().equals("MySQL")) {
            defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA)
                    + (stc_.supports2009Model() ? "^ DROP INDEX ^" + tablePrefix_ + "m1^" : "^ DROP INDEX ^m1^"));
        }
        
        // These aren't created on DB2
        // SOD - the code to create foreign keys for entityAssnsToEQual and entityAssnsToDQuals has been commented.
        //       since they are created, no need to remove them
        if ((!gsm_.getDatabaseType().startsWith("DB2")) && (!gsm_.getDatabaseType().startsWith("Oracle"))) {
//            defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
//                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)
//                    + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "o^"
//                            : "^ {DROPFOREIGNKEY} ^o^"));
//            defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
//                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS)
//                    + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "p^"
//                            : "^ {DROPFOREIGNKEY} ^p^"));
        }
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "j^" : "^ {DROPFOREIGNKEY} ^j^"));

        // mysql doesn't automaticaly create this index like it should.
        if (gsm_.getDatabaseType().equals("MySQL")) {
            defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)
                    + (stc_.supports2009Model() ? "^ DROP INDEX ^" + tablePrefix_ + "j1^" : "^ DROP INDEX ^j1^"));
        }
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "e^" : "^ {DROPFOREIGNKEY} ^e^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "q^" : "^ {DROPFOREIGNKEY} ^q^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "r^" : "^ {DROPFOREIGNKEY} ^r^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "f^" : "^ {DROPFOREIGNKEY} ^f^"));
        
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.ENTITY_TYPE)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "e^" : "^ {DROPFOREIGNKEY} ^e^"));
        
        if (!stc_.supports2009Model()) {
            defaultTableDropForeignKeySql_.add("ALTER TABLE ^"
                    + stc_.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES) + "^ {DROPFOREIGNKEY} ^h^");
        }
        defaultTableDropForeignKeySql_.add("ALTER TABLE ^" + stc_.getTableName(SQLTableConstants.RELATION)
                + (stc_.supports2009Model() ? "^ {DROPFOREIGNKEY} ^" + tablePrefix_ + "g^" : "^ {DROPFOREIGNKEY} ^g^"));
        
        
        

        
    }

    /**
     * Drops the constraints from the table
     * 
     * @throws SQLException
     */
    public void dropDefaultTableConstraints() throws SQLException {
        log.debug("removing table constraints");

        if (!doTablesExist()) {
            log.debug("Tables don't exist - returning.");
            return;
        }

        if (defaultTableDropForeignKeySql_.size() == 0) {
            log.debug("initing drop foreign key sql");
            initDropDefaultTableForeignKeySQL();
        }

        Connection conn = getConnection();

        try {
            for (int i = 0; i < defaultTableDropForeignKeySql_.size(); i++) {
                PreparedStatement ps = null;
                try {
                    ps = conn.prepareStatement(gsm_.modifySQL((String) defaultTableDropForeignKeySql_.get(i)));
                    ps.execute();
                } catch (SQLException e) {
                    if (e.toString().indexOf("does not exist") == -1 && e.toString().indexOf("undefined") == -1) {
                        log.error("Problem dropping the foreign key " + (String) defaultTableDropForeignKeySql_.get(i),
                                e);
                        throw e;
                    } else {
                        log.debug("The foreign constraint " + (String) defaultTableDropForeignKeySql_.get(i)
                                + " doesn't exist");
                    }
                } finally {
                    if (ps != null) {
                        ps.close();
                    }

                }
            }

        } catch (SQLException e) {
            if (gsm_.getDatabaseType().equals("MySQL")) {
                log.warn("****WARNING****: - Could not remove the database constraints on your mysql database.");
                log
                        .warn("This means either A) - there are no constraints to remove, or B) your mysql version is < 4.0.18.");
            } else {
                log.error("Error removing constraints", e);
                throw e;
            }
        } finally {
            returnConnection(conn);
        }
    }
    
    public String getExistingTableVersion() {
        PreparedStatement checkVersion = null;

        if (!doTablesExist()) {
            log.debug("Tables don't exist - returning.");
            return null;
        }

        String result = "<1.5";

        Connection conn = getConnection();

        try {
            // can't use stc_ stuff here, because we need this data to initilize
            // it.
            checkVersion = conn.prepareStatement("Select " + SQLTableConstants.TBLCOL_VERSION + " from " + tablePrefix_
                    + SQLTableConstants.TBL_LEXGRID_TABLE_META_DATA);
            ResultSet results = checkVersion.executeQuery();
            if (results.next()) {
                result = results.getString(SQLTableConstants.TBLCOL_VERSION);
            }
            results.close();
            return result;

        } catch (SQLException e) {
            return "<1.5";
        } finally {
            try {
                if (checkVersion != null) {
                    checkVersion.close();
                }
            } catch (SQLException e) {
                // do nothing
            }
            returnConnection(conn);
        }
    }
    
    public static boolean doTablesExist(String server, String driver, String username, String password, String prefix) {
        Connection temp = null;
        try {
            temp = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities stu = new SQLTableUtilities(temp, prefix);
            return stu.doTablesExist();

        } catch (Exception e) {
            return false;
        } finally {
            if (temp != null) {
                try {
                    temp.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static boolean doHistoryTablesExist(String server, String driver, String username, String password,
            String prefix) {
        Connection temp = null;
        try {
            temp = DBUtility.connectToDatabase(server, driver, username, password);
            SQLTableUtilities stu = new SQLTableUtilities(temp, prefix);
            return stu.doHistoryTablesExist();
        } catch (Exception e) {
            return false;
        } finally {
            if (temp != null) {
                try {
                    temp.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public boolean doTablesExist() {
        return doesTableExist(SQLTableConstants.TBL_LEXGRID_TABLE_META_DATA);
    }

    public boolean doHistoryTablesExist() {
        return doesTableExist(SQLTableConstants.TBL_NCI_THESAURUS_HISTORY);
    }

    private boolean doesTableExist(String tableName) {
        PreparedStatement query = null;

        if (tablePrefix_ == null) {
            tablePrefix_ = "";
        }

        Connection conn = getConnection();

        try {
            query = conn.prepareStatement("Select count(*) from " + tablePrefix_ + tableName);
            ResultSet results = query.executeQuery();
            if (results.next()) {
                // if the table doesn't exist, it should throw an exception.
                results.close();
                return true;
            } else {
                return false; // should not be used.
            }
        } catch (SQLException e1) {
            return false;
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (SQLException e) {
                // do nothing
            }
            returnConnection(conn);
        }
    }

    /**
     * Deletes all the data from the tables of a given Coding Scheme
     * 
     * does not delete data from system release or nci history tables
     * 
     * @param codingScheme
     *            target Coding Scheme
     * @throws SQLException
     */
    public void cleanTables(String codingScheme) throws SQLException {
        if (!doTablesExist()) {
            log.debug("Tables don't exist - returning.");
            return;
        }
        Connection conn = getConnection();
        try {
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ASSOCIATION), codingScheme, true);
            if (!stc_.supports2009Model()) {
                cleanTable(conn, stc_.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES), codingScheme, true);
            }
            cleanTable(conn, stc_.getTableName(SQLTableConstants.RELATION), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_TYPE), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES), codingScheme,
                    true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB), codingScheme, true);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP), codingScheme, true);
            // this table is optional
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME_VERSIONS), codingScheme, false);
            cleanTable(conn, stc_.getTableName(SQLTableConstants.CODING_SCHEME), codingScheme, true);
            // optional table
            cleanTable(conn, stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE),
                    codingScheme, false);
        } finally {
            returnConnection(conn);
        }
    }

    /**
     * Drop all of the tables from a LexGrid database (in the correct order)
     * 
     * This does drop all tables - including the optional ones.
     * 
     * @param data.codingScheme
     *            target Coding Scheme
     * @throws SQLException
     */
    public void dropTables() throws SQLException {
        
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA));
        temp.add(stc_.getTableName(SQLTableConstants.ASSOCIATION));
        if (!stc_.supports2009Model()) {
            temp.add(stc_.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES));
        }
        temp.add(stc_.getTableName(SQLTableConstants.RELATION));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY));
        if (stc_.supports2009Model()) {
            temp.add(stc_.getTableName(SQLTableConstants.ENTITY_TYPE));
            temp.add(stc_.getTableName(SQLTableConstants.ENTRY_STATE));
        }
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME_PROP));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME));
        temp.add(stc_.getTableName(SQLTableConstants.LEXGRID_TABLE_META_DATA));
        temp.add(stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE_REFS));
        temp.add(stc_.getTableName(SQLTableConstants.SYSTEM_RELEASE));
        temp.add(stc_.getTableName(SQLTableConstants.CODING_SCHEME_VERSIONS));
        temp.add(stc_.getTableName(SQLTableConstants.NCI_THESAURUS_HISTORY));
        temp.add(stc_.getTableName(SQLTableConstants.CONCEPT_HISTORY));
        temp.add(stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE));
        temp.add(stc_.getTableName(SQLTableConstants.INSTANCE));

        Connection conn = getConnection();

        try {
            for (int i = 0; i < temp.size(); i++) {
                dropTable(conn, (String) temp.get(i));
            }
        } finally {
            returnConnection(conn);
        }
    }
    
    public void computeTransitivityTable(String codingScheme, LgMessageDirectorIF md) throws SQLException {
        Connection conn = getConnection();
        try {
            
            // now, the fun part...
            PreparedStatement getTransitiveAssociations = conn.prepareStatement("Select "
                    + stc_.containerNameOrContainerDC + ", " 
                    + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + ", "
                    + stc_.entityCodeOrAssociationId + " from "
                    + stc_.getTableName(SQLTableConstants.ASSOCIATION) + " Where "
                    + SQLTableConstants.TBLCOL_ISTRANSITIVE + " = ? AND " + stc_.codingSchemeNameOrId + " = ?");

            DBUtility.setBooleanOnPreparedStatment(getTransitiveAssociations, 1, new Boolean(true));
            getTransitiveAssociations.setString(2, codingScheme);

            ArrayList<StringTriple> transitiveAssociations = new ArrayList<StringTriple>();

            ResultSet results = getTransitiveAssociations.executeQuery();
            while (results.next()) {
                StringTriple temp = new StringTriple();
                temp.a = results.getString(stc_.containerNameOrContainerDC);
                temp.b = results.getString(stc_.entityCodeOrAssociationId);
                temp.c = results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE);
                transitiveAssociations.add(temp);
            }
            results.close();
            getTransitiveAssociations.close();

            PreparedStatement getAllRelations = conn.prepareStatement("Select " + stc_.sourceCSIdOrEntityCodeNS + ", "
                    + stc_.sourceEntityCodeOrId + ", " + stc_.targetCSIdOrEntityCodeNS + ", "
                    + stc_.targetEntityCodeOrId + " from "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " where "
                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                    + stc_.entityCodeOrAssociationId + " = ?");

            PreparedStatement insertIntoTransitive = conn.prepareStatement(stc_
                    .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE));

            PreparedStatement getTargetsOfSource = conn.prepareStatement("SELECT " + stc_.targetCSIdOrEntityCodeNS
                    + ", " + stc_.targetEntityCodeOrId + " FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                    + stc_.entityCodeOrAssociationId + " = ? and " + stc_.sourceCSIdOrEntityCodeNS + " = ? and "
                    + stc_.sourceEntityCodeOrId + " = ?");

            PreparedStatement getSourceCodes = conn.prepareStatement("SELECT Distinct " + stc_.sourceCSIdOrEntityCodeNS
                    + ", " + stc_.sourceEntityCodeOrId + " FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                    + stc_.entityCodeOrAssociationId + " = ?");
            try {
                for (int i = 0; i < transitiveAssociations.size(); i++) {
                    // make a hashset the holds the entire current set of
                    // relations.
                    getAllRelations.setString(1, codingScheme);
                    getAllRelations.setString(2, transitiveAssociations.get(i).a);
                    getAllRelations.setString(3, transitiveAssociations.get(i).b);
                                      
                    String sourceECNS = null;
                    String sourceEC = null;
                    String targetECNS = null;
                    String targetEC = null;
                    results = getAllRelations.executeQuery();
                    LRUMap insertedCache = new LRUMap(50000);
                    while (results.next()) {
                        sourceECNS = results.getString(stc_.sourceCSIdOrEntityCodeNS);
                        sourceEC = results.getString(stc_.sourceEntityCodeOrId);
                        targetECNS = results.getString(stc_.targetCSIdOrEntityCodeNS);
                        targetEC = results.getString(stc_.targetEntityCodeOrId);
                        
                        if (!sourceEC.equals("@") && !targetEC.equals("@@"))
                        {
                            StringTriple sourceCode = new StringTriple();                            
                            sourceCode.a = sourceECNS;
                            sourceCode.c = sourceEC;
                            StringTriple targetCode = new StringTriple();                            
                            targetCode.a = targetECNS;
                            targetCode.c = targetEC;
                            insertIntoTransitiveClosure(codingScheme, insertIntoTransitive, transitiveAssociations.get(i), sourceCode, targetCode, insertedCache);
                           
                        }
                    }
                    results.close();

                   

                    // get the unique source codes for this relationship - and
                    // get all of the codes.
                    md.info("ComputeTransitive - Processing " + ( transitiveAssociations.get(i)).b);
                    getSourceCodes.setString(1, codingScheme);
                    getSourceCodes.setString(2, transitiveAssociations.get(i).a);
                    getSourceCodes.setString(3, transitiveAssociations.get(i).b);

                    results = getSourceCodes.executeQuery();

                    ArrayList<StringTriple> sourceCodes = new ArrayList<StringTriple>();
                    sourceECNS = null;
                    sourceEC = null;
                    targetECNS = null;
                    targetEC = null;
                    while (results.next()) {
                        sourceECNS = results.getString(stc_.sourceCSIdOrEntityCodeNS);
                        sourceEC = results.getString(stc_.sourceEntityCodeOrId);
                        if (!sourceEC.equals("@"))
                        {
                            StringTriple temp = new StringTriple();
                            
                            temp.a = sourceECNS;
                            temp.c = sourceEC;
                            sourceCodes.add(temp);
                        }
                    }
                    results.close();

                    // Now I have all of the top source codes for this
                    // relationship. Need to recurse down the
                    // tree
                    // adding nodes to the transitive table as necessary.

                    for (int j = 0; j < sourceCodes.size(); j++) {
                        getTargetsOfSource.setString(1, codingScheme);
                        getTargetsOfSource.setString(2, transitiveAssociations.get(i).a);
                        getTargetsOfSource.setString(3, transitiveAssociations.get(i).b);
                        getTargetsOfSource.setString(4, sourceCodes.get(j).a);
                        getTargetsOfSource.setString(5, sourceCodes.get(j).c);

                        results = getTargetsOfSource.executeQuery();
                        ArrayList<StringTriple> targetCodes = new ArrayList<StringTriple>();
                        sourceECNS = null;
                        sourceEC = null;
                        targetECNS = null;
                        targetEC = null;
                        while (results.next()) {
                            targetECNS = results.getString(stc_.targetCSIdOrEntityCodeNS);
                            targetEC = results.getString(stc_.targetEntityCodeOrId);
                            if (!targetEC.equals("@@"))
                            {
                                StringTriple temp = new StringTriple();
                                temp.a = targetECNS;
                                temp.c = targetEC;    
                                targetCodes.add(temp);
                            }
                        }
                        results.close();

                        processTransitive(codingScheme,  transitiveAssociations.get(i),
                                 sourceCodes.get(j), targetCodes, getTargetsOfSource, 
                                insertIntoTransitive, insertedCache);
                    }

                }
            } finally {
                getAllRelations.close();
                insertIntoTransitive.close();
                getTargetsOfSource.close();
                getSourceCodes.close();
            }
        } finally {
            returnConnection(conn);
        }
    }

    private void processTransitive(String codingScheme, StringTriple association, StringTriple sourceCode,
            ArrayList<StringTriple> targetCodes, PreparedStatement getTargetsOfSource, 
            PreparedStatement insertIntoTransitive, LRUMap insertedCache) throws SQLException {
        // The next target of each of the passed in targetCodes needs to be
        // added to the transitive table.

        for (int i = 0; i < targetCodes.size(); i++) {

            getTargetsOfSource.setString(1, codingScheme);
            getTargetsOfSource.setString(2, association.a);
            getTargetsOfSource.setString(3, association.b);
            getTargetsOfSource.setString(4, targetCodes.get(i).a);
            getTargetsOfSource.setString(5, targetCodes.get(i).c);

            ArrayList<StringTriple> targetTargets = new ArrayList<StringTriple>();
            String targetECNS = null;
            String targetEC = null;
            ResultSet results = getTargetsOfSource.executeQuery();
            while (results.next()) {
                targetECNS = results.getString(stc_.targetCSIdOrEntityCodeNS);
                targetEC =  results.getString(stc_.targetEntityCodeOrId);
                if (!targetEC.equals("@@"))
                {
                    StringTriple temp = new StringTriple();
                    temp.a = targetECNS;
                    temp.c = targetEC;
    
                    targetTargets.add(temp);
                }
            }
            results.close();

            // need to add an entry for the source code to each of these target
            // codes (if it doesn't already
            // exist, and if there isn't an entry in the regular table already

            for (int j = 0; j < targetTargets.size(); j++) {
                if (sourceCode.a.equals(targetTargets.get(j).a)
                        && sourceCode.c.equals(targetTargets.get(j).c)) {
                    // if they equal each other, there is something wrong with
                    // the code system. But I don't
                    // want to fail.. so skip it.

                    continue;
                }
                
                boolean iInserted = insertIntoTransitiveClosure(codingScheme, insertIntoTransitive, association, sourceCode, targetTargets.get(j), insertedCache);               
                if (!iInserted) {
                    // If I didn't insert it into the transitive table, it was
                    // already there
                    // or unnecessary. No need to do the recursion below, so
                    // remove it.
                    targetTargets.remove(j);
                    j--;
                }
            }

            // Now, need to recurse.
            while (targetTargets.size() > 0) {
                if (sourceCode.a.equals( targetTargets.get(0).a)
                // && sourceCode.b.equals((
                // targetTargets.get(0)).b)
                        && sourceCode.c.equals( targetTargets.get(0).c)) {
                    // if they equal each other, there is something wrong with
                    // the code system. But I don't
                    // want to fail.. so skip it.
                    targetTargets.remove(0);
                    continue;
                }

                // need to pass in an array list - put the current item in one.
                ArrayList<StringTriple> temp = new ArrayList<StringTriple>();
                temp.add(targetTargets.get(0));
                // remove it, since we will be done with it after this.
                targetTargets.remove(0);
                processTransitive(codingScheme, association, sourceCode, temp, getTargetsOfSource, 
                        insertIntoTransitive, insertedCache);
            }
        }
    }
    
    private boolean insertIntoTransitiveClosure(String codingScheme, PreparedStatement insertTransitiveStmt,
            StringTriple association, StringTriple sourceCode, StringTriple targetCode, LRUMap insertedCache) {
        String key = sourceCode.a + ":" + sourceCode.c + ":" + targetCode.a + ":" + targetCode.c;

        boolean iInserted = false;

        if (!insertedCache.containsKey(key)) {
            // if it is not loaded in the main table, or already loaded
            // in the transitive table
            try {
                int k = 1;
                insertTransitiveStmt.setString(k++, codingScheme);
                insertTransitiveStmt.setString(k++, association.a);
                insertTransitiveStmt.setString(k++, association.c);
                insertTransitiveStmt.setString(k++, association.b);
                insertTransitiveStmt.setString(k++, sourceCode.a);
                insertTransitiveStmt.setString(k++, sourceCode.c);
                insertTransitiveStmt.setString(k++, targetCode.a);
                insertTransitiveStmt.setString(k++, targetCode.c);

                insertTransitiveStmt.execute();
                insertedCache.put(key, null);
                iInserted = true;
            } catch (SQLException e) {
                log.debug(e);
                // assume an exception means that it is a duplicate
                // error. ignore.
                // cheaper to do this (in theory) than check ahead of
                // time - duplicates should
                // be abnormal, not the rule. And we have a cache now.
            }
            
        }
        return iInserted;
    }

 

    private class StringTriple {
        String a;
        String b;
        String c;
    }

    /**
     * Get the native relation for a coding scheme. If none are marked as
     * native, returns an arbitrary relation name. Returns null if none were
     * found.
     * 
     * @param codingScheme
     * @return
     * @throws SQLException
     */
    public String getNativeRelation(String codingScheme) throws SQLException {
        Connection conn = getConnection();
        try {
            if (!doTablesExist()) {
                log.debug("Tables don't exist - returning.");
                return null;
            }

            PreparedStatement getNativeRelation = conn.prepareStatement("SELECT " + stc_.containerNameOrContainerDC
                    + ", " + SQLTableConstants.TBLCOL_ISNATIVE + " FROM "
                    + stc_.getTableName(SQLTableConstants.RELATION) + " WHERE " + stc_.codingSchemeNameOrId + " = ?");

            // figure out the name of the native relation

            getNativeRelation.setString(1, codingScheme);
            ResultSet results = getNativeRelation.executeQuery();

            // can't use orderby on the boolean column, because some databases
            // put trues first,
            // while others put falses first. instead, I'll just get them all,
            // and scan for a true.

            String relationName = null;
            while (results.next()) {
                relationName = results.getString(stc_.containerNameOrContainerDC);
                boolean isNative = DBUtility.getbooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISNATIVE);
                if (isNative) {
                    break;
                }
            }
            results.close();
            getNativeRelation.close();

            return relationName;
        } finally {
            returnConnection(conn);
        }
    }

    /**
     * Add the root or tail relationship node for an association name in a
     * coding scheme.
     * 
     * @param codingScheme
     *            The coding scheme to add the root node to.
     * @param associationNames
     *            The association name(s) to calculate the root node for. If you
     *            provide more than one association, the root node will be
     *            calculated using all of the association names (at the same
     *            time). If you don't provide any association names, all
     *            associations names will be used (at the same time).
     * @param relationName
     *            The relation name that contains the association. If null, the
     *            native relation for the coding scheme is used.
     * @param root
     *            - true for root, false for tail.
     * @throws SQLException
     */
    public void addRootRelationNode(String codingScheme, String[] associationNames, String relationName, boolean root)
            throws SQLException {
        addRootRelationNode(codingScheme, associationNames, null, relationName, root);
    }

    /**
     * Add the root or tail relationship node for an association name in a
     * coding scheme.
     * 
     * @param codingScheme
     *            The coding scheme to add the root node to.
     * @param associationNames
     *            The association name(s) to calculate the root node for. If you
     *            provide more than one association, the root node will be
     *            calculated using all of the association names (at the same
     *            time). If you don't provide any association names, all
     *            associations names will be used (at the same time).
     * @param synNames
     *            The association name(s) that define synonymous relationships
     *            between concepts. If provided, nodes that do not directly
     *            participate in an association above but are synonymous with a
     *            node that does participate are not included in the
     *            calculation. If empty or null, synonymy is not considered as
     *            part of the calculation.
     * @param relationName
     *            The relation name that contains the association. If null, the
     *            native relation for the coding scheme is used.
     * @param root
     *            - true for root, false for tail.
     * @throws SQLException
     */
    public void addRootRelationNode(String codingScheme, String[] associationNames, String[] synNames,
            String relationName, boolean root) throws SQLException {
        if (!doTablesExist()) {
            log.debug("Tables don't exist - returning.");
            return;
        }

        String type = (root ? "root" : "tail");

        boolean useAll = false;

        if (associationNames == null || associationNames.length == 0) {
            useAll = true;
        }

        Connection conn = getConnection();
        try {
            if (relationName == null || relationName.length() < 1) {
                relationName = getNativeRelation(codingScheme);
                if (relationName == null || relationName.length() < 1) {
                    log.debug("The relation could not be found.");
                    return;
                }
            }

            StringBuffer query = new StringBuffer("SELECT " + stc_.targetEntityCodeOrId + " FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ?" + " AND " + stc_.containerNameOrContainerDC + " = ?");
            if (!useAll) {
                query.append(" AND (");

                for (int i = 0; i < associationNames.length; i++) {
                    query.append(stc_.entityCodeOrAssociationId + " = ? OR ");
                }

                // trim the last 'OR '
                query.setLength(query.length() - 3);
                query.append(")");
            }
            query
                    .append(" AND " + stc_.sourceCSIdOrEntityCodeNS + " = ? AND " + stc_.targetCSIdOrEntityCodeNS
                            + " = ?");

            PreparedStatement checkForAssociation = conn.prepareStatement(gsm_.modifySQL(query.toString()));
            checkForAssociation.setMaxRows(1);

            int i = 1;
            checkForAssociation.setString(i++, codingScheme);
            checkForAssociation.setString(i++, relationName);
            if (!useAll) {
                for (int j = 0; j < associationNames.length; j++) {
                    checkForAssociation.setString(i++, associationNames[j]);
                }
            }
            checkForAssociation.setString(i++, codingScheme);
            checkForAssociation.setString(i++, codingScheme);

            ResultSet results = checkForAssociation.executeQuery();
            boolean hasResults = results.next();
            results.close();
            checkForAssociation.close();
            if (!hasResults) {
                log.debug("None of the provided associations are present in the table.  " + "No reason to calculate "
                        + type + " nodes - returning ");
                return;
            }

            query.setLength(0);

            query.append("SELECT " + stc_.entityCodeOrId + " FROM " + stc_.getTableName(SQLTableConstants.ENTITY)
                    + " WHERE " + stc_.codingSchemeNameOrId + " = ?" + " AND " + SQLTableConstants.TBLCOL_ISACTIVE
                    + " = ?" + " AND " + stc_.entityCodeOrId + " NOT IN (" + " SELECT "
                    + (root ? stc_.targetEntityCodeOrId : stc_.sourceEntityCodeOrId) + " FROM "
                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                    + stc_.codingSchemeNameOrId + " = ?" + " AND " + stc_.containerNameOrContainerDC + " = ?");
            if (!useAll) {
                query.append(" AND (");

                for (int j = 0; j < associationNames.length; j++) {
                    query.append(stc_.entityCodeOrAssociationId + " = ? OR ");
                }

                // trim the last 'OR '
                query.setLength(query.length() - 3);
                query.append(")");
            }
            query.append(" AND " + stc_.sourceCSIdOrEntityCodeNS + " = ? AND " + stc_.targetCSIdOrEntityCodeNS
                    + " = ?)");

            PreparedStatement getNodes = conn.prepareStatement(gsm_.modifySQL(query.toString()));

            PreparedStatement insertIntoConcepts = conn.prepareStatement(stc_
                    .getInsertStatementSQL(SQLTableConstants.ENTITY));
            PreparedStatement insertIntoConceptAssociationsToConcept = conn.prepareStatement(stc_
                    .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));
            PreparedStatement insertIntoAssociations = conn.prepareStatement(stc_
                    .getInsertStatementSQL(SQLTableConstants.ASSOCIATION));

            // add the node to the concepts table
            insertIntoConcepts.setString(1, codingScheme);
            insertIntoConcepts.setString(2, codingScheme);
            insertIntoConcepts.setString(3, (root ? "@" : "@@"));
            DBUtility.setBooleanOnPreparedStatment(insertIntoConcepts, 4, null);
            DBUtility.setBooleanOnPreparedStatment(insertIntoConcepts, 5, null);
            DBUtility.setBooleanOnPreparedStatment(insertIntoConcepts, 6, new Boolean(true));
            insertIntoConcepts.setInt(7, 0); // entryStateId here
            insertIntoConcepts.setString(8, type + " relation node for relations");

            try {
                insertIntoConcepts.executeUpdate();
            } catch (SQLException e) {
                // assume this means that the association is already in the
                // table.
            }

            insertIntoConcepts.close();

            // if they ask me to calculate root nodes based on multiple
            // associations
            // then I want to use a special association name to mark this one.
            // need
            // to add it to the associations table, so I don't have foreign key
            // violations.
            // if they only provide one association name, then I will just use
            // that association
            // name.
            if (useAll || associationNames.length > 1) {
                int k = 1;
                insertIntoAssociations.setString(k++, codingScheme);
                insertIntoAssociations.setString(k++, relationName);
                insertIntoAssociations.setString(k++, codingScheme);
                insertIntoAssociations.setString(k++, "-multi-assn-@-root-");
                insertIntoAssociations.setString(k++, "-multi-assn-@-root-");
                insertIntoAssociations.setString(k++, "Not Applicable");
                insertIntoAssociations.setString(k++, "Not Applicable");
                insertIntoAssociations.setString(k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, null);
                if( stc_.supports2009Model() )
                    insertIntoAssociations.setInt(k++, -1);// entryStateId
                insertIntoAssociations.setString(k++, null);

                try {
                    insertIntoAssociations.executeUpdate();
                } catch (SQLException e) {
                    // assume this means it already exists
                }
                insertIntoAssociations.close();

            }

            // find all the nodes that need to be referenced and insert rows for
            // them

            i = 1;
            getNodes.setString(i++, codingScheme);
            DBUtility.setBooleanOnPreparedStatment(getNodes, i++, new Boolean(true));
            getNodes.setString(i++, codingScheme);
            getNodes.setString(i++, relationName);
            if (!useAll) {
                for (int j = 0; j < associationNames.length; j++) {
                    getNodes.setString(i++, associationNames[j]);
                }
            }
            getNodes.setString(i++, codingScheme);
            getNodes.setString(i++, codingScheme);

            results = getNodes.executeQuery();
            Set candidateCodes = new HashSet();
            try {
                while (results.next()) {
                    String target = results.getString(stc_.entityCodeOrId);
                    if (target.equals((root ? "@" : "@@"))) {
                        // Already linked to root; don't add this one
                        continue;
                    }

                    // Add code as candidate to be linked to root
                    candidateCodes.add(target);
                }
            } finally {
                results.close();
                getNodes.close();
            }

            // If synonymous relations are indicated, filter candidates having
            // synonymous concepts not in the candidate list.
            if (synNames != null && synNames.length > 0) {
                StringBuffer sb = new StringBuffer("SELECT " + stc_.targetEntityCodeOrId + " FROM ").append(
                        stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                        " WHERE " + stc_.codingSchemeNameOrId + " = '").append(codingScheme).append('\'').append(
                        " AND " + stc_.containerNameOrContainerDC + " = '").append(relationName).append('\'').append(
                        " AND " + stc_.sourceEntityCodeOrId + " = ?").append(
                        " AND " + stc_.entityCodeOrAssociationId + " IN (");
                for (int s = 0; s < synNames.length; s++) {
                    if (s > 0)
                        sb.append(',');
                    sb.append('\'').append(synNames[s]).append('\'');
                }
                sb.append(")");
                PreparedStatement getSynonyms = conn.prepareStatement(gsm_.modifySQL(sb.toString()));
                try {
                    // Check each concept tagged as a synonym.
                    Collection codesToRemove = new ArrayList();
                    for (Iterator sourceCodes = candidateCodes.iterator(); sourceCodes.hasNext();) {
                        String sourceCode = (String) sourceCodes.next();
                        getSynonyms.setString(1, sourceCode);
                        ResultSet rs = getSynonyms.executeQuery();
                        try {
                            // Is the synonym's code participating as an
                            // intermediate node
                            // in the hierarchy?
                            while (rs.next()) {
                                String synCode = rs.getString(1);
                                if (!candidateCodes.contains(synCode)) {
                                    codesToRemove.add(sourceCode);
                                    break;
                                }
                            }
                        } finally {
                            rs.close();
                        }
                    }
                    // Remove those detected to have a synonym that is not a
                    // root node.
                    candidateCodes.removeAll(codesToRemove);
                } finally {
                    getSynonyms.close();
                }
            }

            // Insert root relations for remaining candidates
            for (Iterator candidates = candidateCodes.iterator(); candidates.hasNext();) {
                String target = (String) candidates.next();
                int col = 1;
                insertIntoConceptAssociationsToConcept.setString(col++, codingScheme);
                insertIntoConceptAssociationsToConcept.setString(col++, relationName);
                
                insertIntoConceptAssociationsToConcept.setString(col++, codingScheme);
                // use a special association name if there is more than one
                // association provided.
                insertIntoConceptAssociationsToConcept.setString(col++,
                        ((useAll || associationNames.length > 1) ? "-multi-assn-@-root-" : associationNames[0]));
                insertIntoConceptAssociationsToConcept.setString(col++, codingScheme);
                if (root) {
                    insertIntoConceptAssociationsToConcept.setString(col++, "@");
                } else {
                    insertIntoConceptAssociationsToConcept.setString(col++, target);
                }
                insertIntoConceptAssociationsToConcept.setString(col++, codingScheme);
                if (root) {
                    insertIntoConceptAssociationsToConcept.setString(col++, target);
                } else {
                    insertIntoConceptAssociationsToConcept.setString(col++, "@@");
                }
                
                //always populate the multiattributeskey -- in this case a random UUID
                insertIntoConceptAssociationsToConcept.setString(col++, UUID.randomUUID().toString());             
                
                insertIntoConceptAssociationsToConcept.setString(col++, null);
                DBUtility
                        .setBooleanOnPreparedStatment(insertIntoConceptAssociationsToConcept, col++, new Boolean(null));
                DBUtility
                        .setBooleanOnPreparedStatment(insertIntoConceptAssociationsToConcept, col++, new Boolean(null));
                DBUtility
                        .setBooleanOnPreparedStatment(insertIntoConceptAssociationsToConcept, col++, new Boolean(null));
                insertIntoConceptAssociationsToConcept.setInt(col++, 0); // entryStateId
                                                                         // here
                insertIntoConceptAssociationsToConcept.executeUpdate();
            }
            insertIntoConceptAssociationsToConcept.close();
        } finally {
            returnConnection(conn);
        }
    }

    /**
     * Remove the root ('@') or tail ('@@') relationship node for the given
     * coding scheme.
     * 
     * @param codingScheme
     *            The coding scheme to remove the root node from.
     * @param relationName
     *            The relation container for the root node. If null, the native
     *            relation for the coding scheme is used.
     * @param root
     *            - true for root ('@'), false for tail ('@@').
     * @throws SQLException
     */
    public void removeRootRelationNode(String codingScheme, String relationName, boolean root) throws SQLException {
        if (!doTablesExist())
            return;

        int count = 0;
        Connection conn = getConnection();
        try {
            // Define the SQL statements to locate and delete affected entries
            // ...
            StringBuffer sb = new StringBuffer("SELECT * FROM ").append(
                    stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY))
                    .append(
                            " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC
                                    + " = ? AND ").append(
                            root ? (stc_.sourceEntityCodeOrId + " = '@'") : (stc_.targetEntityCodeOrId + " = '@@'"));
            PreparedStatement getRoots = conn.prepareStatement(gsm_.modifySQL(sb.toString()));

            sb = new StringBuffer("DELETE FROM ").append(
                    stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                    " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                            + stc_.entityCodeOrAssociationId + " = ?").append(
                    " AND " + stc_.sourceCSIdOrEntityCodeNS + " = ? AND " + stc_.sourceEntityCodeOrId + " = ?").append(
                    " AND " + stc_.targetCSIdOrEntityCodeNS + " = ? AND " + stc_.targetEntityCodeOrId + " = ?");
            PreparedStatement deleteAssoc = conn.prepareStatement(gsm_.modifySQL(sb.toString()));

            sb = new StringBuffer("DELETE FROM ").append(
                    stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)).append(
                    " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                            + " = ?");
            PreparedStatement deleteCQual = conn.prepareStatement(gsm_.modifySQL(sb.toString()));

            // Locate matching entries and clear, along with associated
            // qualifiers ...
            try {
                getRoots.setString(1, codingScheme);
                getRoots.setString(2, relationName != null ? relationName : getNativeRelation(codingScheme));
                ResultSet rs = getRoots.executeQuery();
                while (rs.next()) {
                    // Remove matching qualifiers ...
                    String multiKey = rs.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                    if (multiKey != null && multiKey.length() > 0) {
                        deleteCQual.clearParameters();
                        deleteCQual.clearWarnings();
                        deleteCQual.setString(1, codingScheme);
                        deleteCQual.setString(2, multiKey);
                        deleteCQual.execute();
                    }

                    // Remove the association/source/target ...
                    deleteAssoc.clearParameters();
                    deleteAssoc.clearWarnings();
                    deleteAssoc.setString(1, codingScheme);
                    deleteAssoc.setString(2, relationName);
                    deleteAssoc.setString(3, rs.getString(stc_.entityCodeOrAssociationId));
                    deleteAssoc.setString(4, rs.getString(stc_.sourceCSIdOrEntityCodeNS));
                    deleteAssoc.setString(5, rs.getString(stc_.sourceEntityCodeOrId));
                    deleteAssoc.setString(6, rs.getString(stc_.targetCSIdOrEntityCodeNS));
                    deleteAssoc.setString(7, rs.getString(stc_.targetEntityCodeOrId));
                    if (!deleteAssoc.execute() && deleteAssoc.getUpdateCount() > 0)
                        count += deleteAssoc.getUpdateCount();
                }
                rs.close();
            } finally {
                getRoots.close();
                deleteAssoc.close();
                deleteCQual.close();
            }
        } finally {
            returnConnection(conn);
            log.info("Removed " + count + " root associations.");
        }
    }

    private void createIndex(Connection conn, String createIndexString, String indexName) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(createIndexString);
            ps.execute();
        } catch (SQLException e) {
            // try to figure out what the databases return if the constraint
            // already exists... (mysql
            // does the errno 121 business
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("errno: 121") == -1 && e.toString().indexOf("-601") == -1
                    && e.toString().indexOf("Duplicate key name") == -1) {
                log.error("Problem creating the index " + createIndexString, e);
                throw e;
            } else {
                log.debug("The index " + indexName + " already exits");
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private void createForeignKey(Connection conn, String createFKString, String FKName) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(createFKString);
            ps.execute();
        } catch (SQLException e) {
            // try to figure out what the databases return if the constraint
            // already exists... (mysql
            // does the errno 121 business
            if (e.toString().indexOf("already") == -1 && e.toString().indexOf("existing") == -1
                    && e.toString().indexOf("Duplicate") == -1 && e.toString().indexOf("-601") == -1
                    && e.toString().indexOf("errno: 121") == -1) {
                log.error("Problem loading the foreign key " + createFKString, e);
                throw e;
            } else {
                log.debug("The foreign constraint " + FKName + " already exits");
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /*
     * returns true if the table was created, false otherwise (already existed)
     */
    private boolean createTable(Connection conn, String createTableString, String tableName) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(createTableString);
            ps.execute();
            return true;
        } catch (SQLException e) {
            // If the error is anything other than table already exists, throw
            // an error.
            if (e.toString().indexOf("already exists") == -1 && e.toString().indexOf("identical") == -1
                    && e.toString().indexOf("-601") == -1
                    && e.toString().indexOf("already used by an existing object") == -1
                    && e.toString().indexOf("already an object") == -1) {
                log.error("Problem creating the " + tableName + " table. Syntax used=" + createTableString, e);
                throw e;
            } else {
                log.debug("The table " + tableName + " already appears to exist.");
                return false;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private void cleanTable(Connection conn, String tableName, String codingScheme, boolean failOnError)
            throws SQLException {
        PreparedStatement delete = null;
        try {
            String csnColumnName = SQLTableConstants.TBLCOL_CODINGSCHEMENAME;

            if (tableName.indexOf("codingScheme") == -1) // It is NOT one of
                // coding scheme tables
                csnColumnName = stc_.codingSchemeNameOrId;

            delete = conn.prepareStatement("DELETE FROM " + tableName + " where " + tableName + "." + csnColumnName
                    + " = ?");
            delete.setString(1, codingScheme);
            delete.execute();
            delete.close();
        } catch (SQLException e) {
            if (e.toString().indexOf("exist") > 0 || e.toString().indexOf("not found") > 0
                    || e.toString().indexOf("undefined") > 0) {
                log.debug("Error cleaning table - probably means the table doesn't exist", e);
            } else if (failOnError) {
                throw e;
            } else {
                log.warn("Error cleaning table", e);
            }
        } finally {
            if (delete != null) {
                delete.close();
            }
        }
    }

    private void dropTable(Connection conn, String tableName) {
        PreparedStatement delete = null;
        try {
            delete = conn.prepareStatement(gsm_.modifySQL("DROP TABLE " + tableName + " {CASCADE} "));
            delete.executeUpdate();
            delete.close();
        } catch (SQLException e) {
            log.info("Failed while dropping the table - it probably didn't exist." + tableName, e);
        } finally {
            if (delete != null) {
                try {
                    delete.close();
                } catch (SQLException e) {
                    // noop
                }
            }
        }
    }

    /**
     * Runs SQL Statement "SELECT" with supplied attributes and where clause
     * 
     * @param tableName
     * @param attributeNames
     * @param whereClause
     * @return
     * @throws SQLException
     */
    public ResultSet extractDataFromDB(String tableName, Map attributeNames, String whereClause, String dbType)
            throws SQLException {

        StringBuffer stmt = new StringBuffer();
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;

        stmt.append("SELECT ");

        for (int i = 0; i < attributeNames.size(); i++) {
            stmt.append(attributeNames.get("" + (i + 1)) + ",");
        }

        stmt = stmt.deleteCharAt(stmt.length() - 1);

        stmt.append(" FROM ");
        stmt.append(tablePrefix_ + tableName);

        if (whereClause != null && !whereClause.equals("")) {
            stmt.append(" WHERE ");
            stmt.append(whereClause);
        }

        log.debug("************ SELECT QUERY ************");
        log.debug(stmt.toString());
        log.debug("**************************************");

        try {
            String statement = new GenericSQLModifier(dbType, false).modifySQL(stmt.toString());

            prepStmt = sqlConnection_.prepareStatement(statement, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = prepStmt.executeQuery();
        } catch (Exception e) {
            log.error("Exception @ extractDataFromDB: " + e.getMessage());
        } finally {
            // prepStmt.close();
        }

        return resultSet;
    }

    /**
     * Runs SQL Statement "INSERT" on the given table and and table prefix for
     * the supplied attributeValues
     * 
     * @param table
     * @param attributeValues
     * @return
     * @throws SQLException
     */
    public boolean insertRow(String table, Map attributeValues) throws SQLException {

        PreparedStatement prepStmt = null;
        Object attribute = null;
        boolean success = false;

        try {
            prepStmt = sqlConnection_.prepareStatement(getSQLTableConstants().getInsertStatementSQL(table));

            for (int i = 0; i < attributeValues.size(); i++) {

                attribute = attributeValues.get("" + (i + 1));

                // If null, we are unable to determine the SQL param type,
                // so String is assumed by default.
                if (attribute == null) {
                    prepStmt.setString(i + 1, null);
                } else if (attribute instanceof String) {
                    prepStmt.setString(i + 1, (String) attribute);
                } else if (attribute instanceof Blob) {
                    prepStmt.setBlob(i + 1, (Blob) attribute);
                } else if (attribute instanceof Boolean) {
                    prepStmt.setBoolean(i + 1, ((Boolean) attribute).booleanValue());
                } else if (attribute instanceof Byte) {
                    prepStmt.setByte(i + 1, ((Byte) attribute).byteValue());
                } else if (attribute instanceof byte[]) {
                    prepStmt.setBytes(i + 1, (byte[]) attribute);
                } else if (attribute instanceof Date) {
                    prepStmt.setDate(i + 1, (Date) attribute);
                } else if (attribute instanceof Double) {
                    prepStmt.setDouble(i + 1, ((Double) attribute).doubleValue());
                } else if (attribute instanceof Float) {
                    prepStmt.setFloat(i + 1, ((Float) attribute).floatValue());
                } else if (attribute instanceof Integer) {
                    prepStmt.setInt(i + 1, ((Integer) attribute).intValue());
                } else if (attribute instanceof Long) {
                    prepStmt.setLong(i + 1, ((Long) attribute).longValue());
                } else if (attribute instanceof Short) {
                    prepStmt.setShort(i + 1, ((Short) attribute).shortValue());
                } else if (attribute instanceof Timestamp) {
                    prepStmt.setTimestamp(i + 1, (Timestamp) attribute);
                }
            }

            success = prepStmt.execute();
        } finally {
            prepStmt.close();
        }

        return success;
    }

    /**
     * Runs SQL Statement "UPDATE" on the given tableName with attribute values
     * and where clause.
     * 
     * @param tableName
     * @param attributeNameValue
     * @param whereClause
     * @return
     * @throws SQLException
     */
    public int updateRow(String tableName, Map attributeNameValue, String whereClause, String dbType)
            throws SQLException {

        StringBuffer stmt = new StringBuffer();
        PreparedStatement prepStmt = null;
        int rowsUpdated = 0;
        Object attribute = null;
        Iterator itr = null;
        String[] key = new String[attributeNameValue.size()];
        int count = 0;

        stmt.append("UPDATE " + tablePrefix_ + tableName.trim() + " SET ");

        itr = attributeNameValue.keySet().iterator();

        while (itr.hasNext()) {
            key[count] = (String) itr.next();
            stmt.append(key[count++] + " = ?,");
        }

        /*
         * for (int i = 0; i < attributeNames.size(); i++) {
         * stmt.append(attributeNames.get(i) + " = ?,"); }
         */

        stmt = stmt.deleteCharAt(stmt.length() - 1);

        if (whereClause != null && !"".equals(whereClause)) {
            stmt.append(" WHERE ");
            stmt.append(whereClause);
        }

        // stmt = stmt.deleteCharAt(stmt.length());

        log.debug("************ UPDATE QUERY ************");
        log.debug(stmt.toString());
        log.debug("**************************************");
        try {

            String statement = new GenericSQLModifier(dbType, false).modifySQL(stmt.toString());

            prepStmt = sqlConnection_.prepareStatement(statement);

            itr = attributeNameValue.keySet().iterator();

            for (count = 0; count < key.length; count++) {

                attribute = attributeNameValue.get(key[count]);

                if (attribute instanceof String) {
                    prepStmt.setString(count + 1, (String) attribute);
                } else if (attribute instanceof Blob) {
                    prepStmt.setBlob(count + 1, (Blob) attribute);
                } else if (attribute instanceof Boolean) {
                    prepStmt.setBoolean(count + 1, ((Boolean) attribute).booleanValue());
                } else if (attribute instanceof Byte) {
                    prepStmt.setByte(count + 1, ((Byte) attribute).byteValue());
                } else if (attribute instanceof byte[]) {
                    prepStmt.setBytes(count + 1, (byte[]) attribute);
                } else if (attribute instanceof Date) {
                    prepStmt.setDate(count + 1, (Date) attribute);
                } else if (attribute instanceof Double) {
                    prepStmt.setDouble(count + 1, ((Double) attribute).doubleValue());
                } else if (attribute instanceof Float) {
                    prepStmt.setFloat(count + 1, ((Float) attribute).floatValue());
                } else if (attribute instanceof Integer) {
                    prepStmt.setInt(count + 1, ((Integer) attribute).intValue());
                } else if (attribute instanceof Long) {
                    prepStmt.setLong(count + 1, ((Long) attribute).longValue());
                } else if (attribute instanceof Short) {
                    prepStmt.setShort(count + 1, ((Short) attribute).shortValue());
                } else if (attribute instanceof Timestamp) {
                    prepStmt.setTimestamp(count + 1, (Timestamp) attribute);
                }
            }

            rowsUpdated = prepStmt.executeUpdate();
        } catch (Exception e) {
            log.error("Exception @ updateRow: " + e.getMessage());
        } finally {
            prepStmt.close();
        }

        return rowsUpdated;

    }
    
    
}