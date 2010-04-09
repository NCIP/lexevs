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
package org.lexgrid.valuesets.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.lexgrid.valuesets.helper.VSDConstants;

/**
 * <pre>
 * 
 *  Title:        BaseSQLServices.java
 *  Description:  Handles a ValueDomain and PickList SQL services.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDBaseSQLServices {
	private static Logger log = Logger.getLogger("convert.SQL");
	private Connection sqlConnection_;
	private GenericObjectPool connectionPool_;
	protected JDBCConnectionDescriptor _dbDesc;

	private Hashtable<String, String> tableCreateSql_ = new Hashtable<String, String>();
	private ArrayList<String> indexSql_ = new ArrayList<String>();
	private ArrayList<String> foreignKeySql_ = new ArrayList<String>();
	private ArrayList<String> dropForeignKeySql_ = new ArrayList<String>();
	private Hashtable<String, String> insertStatements_ = new Hashtable<String, String>();
	private Hashtable<String, String> updateStatements_ = new Hashtable<String, String>();

	private GenericSQLModifier gsm_;
	private SQLTableConstants stc_;
	private String tablePrefix_;

	public static final String versionString = "1.7";
	public static final String tableStructureDescription = "This is version 1.7 of the LexGrid SQL format - this is compatible with the 2009/01 LexGrid Schema";

	public VSDBaseSQLServices(Connection sqlConnection, String tablePrefix)
			throws Exception {
		sqlConnection_ = sqlConnection;
		connectionPool_ = null;
		tablePrefix_ = tablePrefix;
		gsm_ = new GenericSQLModifier(sqlConnection_);

		if (doValueDomainTablesExist()) {
			stc_ = new SQLTableConstants(getExistingValueDomainTableVersion(),
					tablePrefix_);
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
	public VSDBaseSQLServices(GenericObjectPool connectionPool,
			String tablePrefix) throws Exception {
		sqlConnection_ = null;
		connectionPool_ = connectionPool;
		tablePrefix_ = tablePrefix;

		Connection temp = getConnection();
		try {

			gsm_ = new GenericSQLModifier(temp);
		} finally {

			returnConnection(temp);
		}

		if (doValueDomainTablesExist()) {
			stc_ = new SQLTableConstants(getExistingValueDomainTableVersion(),
					tablePrefix_);
		} else {
			stc_ = new SQLTableConstants(versionString, tablePrefix_);
		}
	}

	private Connection getConnection() {
		if (sqlConnection_ != null) {
			return sqlConnection_;
		} else {
			try {
				return (Connection) connectionPool_.borrowObject();
			} catch (Exception e) {
				return null;
			}
		}
	}

	private void returnConnection(Connection connection) {
		if (connectionPool_ != null) {
			try {
				connectionPool_.returnObject(connection);
			} catch (Exception e) {
			}
		}
	}

	public SQLTableConstants getSQLTableConstants() {
		return stc_;
	}

	/**
	 * Initializes and creates value domain tables
	 * 
	 * @throws Exception
	 */
	public void createValueDomainTables() throws Exception {
		log.debug("createValueDomainTables called");

		if (doValueDomainTablesExist()) {
			log
					.debug("Value Domain tables already exist, not creating new ones");
			return;
		}

		if (tableCreateSql_.size() == 0) {
			log.debug("initing value domain table creation sql");
			initTableCreateSQL();
		}

		createVDMetaDataTable();

		Connection conn = getConnection();
		try {

			Enumeration<String> tempEnum = tableCreateSql_.keys();
			while (tempEnum.hasMoreElements()) {
				String key = (String) tempEnum.nextElement();
				log.debug("Creating " + key + " table");
				createTable(conn, gsm_.modifySQL((String) tableCreateSql_
						.get(key)), key);
			}
		} finally {
			returnConnection(conn);
		}

		createVDTableIndexes();
		createVDTableConstraints();
	}

	private void createVDTableIndexes() throws SQLException {
		log.debug("Creating table indexes");

		if (indexSql_.size() == 0) {
			log.debug("initing index creation sql");
			initIndexCreateSql();
		}
		Connection conn = getConnection();
		try {
			for (int i = 0; i < indexSql_.size(); i++) {
				createIndex(conn, gsm_.modifySQL((String) indexSql_.get(i)),
						(String) indexSql_.get(i));
			}
		} finally {
			returnConnection(conn);
		}
	}

	private void createIndex(Connection conn, String createIndexString,
			String indexName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(createIndexString);
			ps.execute();
		} catch (SQLException e) {
			// try to figure out what the databases return if the constraint
			// already exists... (mysql
			// does the errno 121 business
			if (e.toString().indexOf("already") == -1
					&& e.toString().indexOf("existing") == -1
					&& e.toString().indexOf("errno: 121") == -1
					&& e.toString().indexOf("-601") == -1
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

	private void createVDMetaDataTable() throws Exception {
		log.debug("createVDMetaDataTable called");

		Connection conn = getConnection();
		try {
			String createTable = gsm_
					.modifySQL("CREATE TABLE {IF NOT EXISTS} ^"
							+ VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA
							+ "^ (" + " ^" + SQLTableConstants.TBLCOL_VERSION
							+ "^ {limitedText}(50) NOT NULL," + " ^"
							+ SQLTableConstants.TBLCOL_DESCRIPTION
							+ "^ {limitedText}(255) default NULL"
							+ ") {TYPE} {lgTableCharSet}");

			boolean created = createTable(conn, createTable,
					VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA);

			if (created) {
				log.debug("Inserting version identifier");
				PreparedStatement tempInsert = conn
						.prepareStatement(gsm_
								.modifySQL(getInsertStatementSQL(VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA)));
				tempInsert.setString(1, versionString);
				tempInsert.setString(2, tableStructureDescription);
				tempInsert.executeUpdate();
				tempInsert.close();
			}
		} finally {
			returnConnection(conn);
		}
	}

	protected void incrementEntryId(int newEntryId) {
		log.debug("incrementing entryId to : " + newEntryId);

		PreparedStatement updateEntryIds = null;
		try {
			updateEntryIds = getKeyedUpdateStatement(VSDConstants.TBL_ENTRY_IDS);
			updateEntryIds.setInt(1, newEntryId);
			updateEntryIds.executeUpdate();
			updateEntryIds.clearParameters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (updateEntryIds != null)
					updateEntryIds.close();
			} catch (SQLException e) {
			}
		}
	}
	
	protected void insertEntryId(int newEntryId) {
		log.debug("inserting entryId : " + newEntryId + " to entryIds table");

		PreparedStatement insertEntryIds = null;
		try {
			insertEntryIds = getKeyedInsertStatement(VSDConstants.TBL_ENTRY_IDS);
			insertEntryIds.setInt(1, newEntryId);
			insertEntryIds.executeUpdate();
			insertEntryIds.clearParameters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (insertEntryIds != null)
					insertEntryIds.close();
			} catch (SQLException e) {
			}
		}
	}

	protected int getCurrentEntryId() throws LBException, Exception {

		int entryId = 0;
		PreparedStatement stmt = null;

		if (!doValueDomainTablesExist()) {
			log.debug("Value Domain tables don't exist - returning.");
			throw new LBException("Value Domain tables don't exist.");
		}

		Connection conn = getConnection();

		try {
			stmt = conn.prepareStatement("Select "
					+ SQLTableConstants.TBLCOL_ENTRYID + " from "
					+ VSDConstants.TBL_ENTRY_IDS);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
			}
			results.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				// do nothing
			}
			returnConnection(conn);
		}
		return entryId;
	}

	private void initTableCreateSQL() throws SQLException {
		log.debug("initValueDomainTableCreateSQL called");

		tableCreateSql_.put(VSDConstants.TBL_VALUE_DOMAIN,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_VALUE_DOMAIN
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VALUEDOMAINURI
						+ "^ {limitedText}(250) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VALUEDOMAINNAME
						+ "^ {limitedText}(250) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEFAULTCODINGSCHEME
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_RELEASEURI
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYSTATEID
						+ "^ {bigInt} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
						+ "^ {unlimitedText} default NULL,"
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^)," + " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_VALUEDOMAINURI + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_VD_ENTRY,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_VD_ENTRY
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYCODE
						+ "^ {limitedText}(200) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_RULEORDER
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_OPERATOR
						+ "^ {limitedText}(15) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_CODINGSCHEMEREFERENCE
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VALUEDOMAINREFERENCE
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_LEAFONLY
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_REFERENCEASSOCIATION
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_TARGETTOSOURCE
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_TRANSITIVECLOSURE
						+ "^ {boolean} default NULL," + " PRIMARY KEY  (^"
						+ SQLTableConstants.TBLCOL_ENTRYID + "^),"
						+ " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^, " + "^"
						+ SQLTableConstants.TBLCOL_ENTITYCODE + "^, " + "^"
						+ SQLTableConstants.TBLCOL_RULEORDER + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_PICK_LIST,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_PICK_LIST
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PICKLISTID
						+ "^ {limitedText} (50) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN
						+ "^ {limitedText}(250) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_COMPLETEDOMAIN
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEFAULTENTITYCODENAMESPACE
						+ "^ {limitedText} (50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEFAULTLANGUAGE
						+ "^ {limitedText} (50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEFAULTSORTORDER
						+ "^ {limitedText} (50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ISACTIVE
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_RELEASEURI
						+ "^ {limitedText} (50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYSTATEID
						+ "^ {bigInt} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
						+ "^ {unlimitedText} default NULL,"
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^)," + " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_PICKLISTID + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_PL_ENTRY,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_PL_ENTRY
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PLENTRYID
						+ "^ {limitedText}(50) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTITYCODE
						+ "^ {limitedText}(200) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYORDER
						+ "^ {bigInt} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ISDEFAULT
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYID
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ISACTIVE
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_INCLUDE
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYSTATEID
						+ "^ {bigInt} DEFAULT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PICKTEXT
						+ "^ {unlimitedText} default NULL,"
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^)," + " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^," + "^"
						+ SQLTableConstants.TBLCOL_PLENTRYID + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_MAPPING,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_MAPPING
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG
						+ "^ {limitedText}(30) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_LOCALID
						+ "^ {limitedText}(50) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_URI
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEPENDENTVALUE
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VAL1
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VAL2
						+ "^ {limitedText}(250) default NULL,"
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^)," + " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_ENTRYID + "^," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^," + "^"
						+ SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_PROPERTY,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_PROPERTY
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYNAME
						+ "^ {limitedText}(50) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ISACTIVE
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYTYPE
						+ "^ {limitedText}(15) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_LANGUAGE
						+ "^ {limitedText}(32) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ISPREFERRED
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT
						+ "^ {boolean} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYID
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_FORMAT
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYSTATEID
						+ "^ {bigInt} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_DEGREEOFFIDELITY
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_REPRESENTATIONALFORM
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYVALUE
						+ "^ {unlimitedText} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VAL1
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_VAL2
						+ "^ {limitedText}(250) default NULL,"
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID
						+ "^)," + " UNIQUE (^"
						+ SQLTableConstants.TBLCOL_ENTRYID + "^," + " ^"
						+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^," + " ^"
						+ SQLTableConstants.TBLCOL_PROPERTYNAME + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_ENTRY_TYPE,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_ENTRY_TYPE
						+ "^ (" 
						+ " ^" + SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^ {bigInt} NOT NULL," 
						+ " ^" + SQLTableConstants.TBLCOL_ENTRYTYPE + "^ {limitedText} (32) NOT NULL," 
						+ " PRIMARY KEY  (^"
						+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^),"
						+ " UNIQUE (^" + SQLTableConstants.TBLCOL_ENTRYTYPE
						+ "^," + "^"
						+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_ENTRY_STATE,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_ENTRY_STATE
						+ "^ (" + " ^" + SQLTableConstants.TBLCOL_ENTRYSTATEID
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_ENTRYTYPE
						+ "^ {limitedText}(50) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_OWNER
						+ "^ {limitedText}(250) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_STATUS
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_EFFECTIVEDATE
						+ "^ {dateTime} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_EXPIRATIONDATE
						+ "^ {dateTime} default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_REVISIONID
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PREVREVISIONID
						+ "^ {limitedText}(50) default NULL," + " ^"
						+ SQLTableConstants.TBLCOL_CHANGETYPE
						+ "^ {limitedText}(15) NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_RELATIVEORDER
						+ "^ {bigInt} NOT NULL," + " ^"
						+ SQLTableConstants.TBLCOL_PREVENTRYSTATEID
						+ "^ {bigInt} default NULL,"
						+ " PRIMARY KEY  (^"
						+ SQLTableConstants.TBLCOL_ENTRYSTATEID + "^)"
						+ ") {TYPE} {lgTableCharSet}");

		tableCreateSql_.put(VSDConstants.TBL_ENTRY_IDS,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_ENTRY_IDS
						+ "^ (" 
						+ " ^" + SQLTableConstants.TBLCOL_ENTRYID+ "^ {bigInt} NOT NULL," 
						+ " PRIMARY KEY  (^" + SQLTableConstants.TBLCOL_ENTRYID + "^)"
						+ ") {TYPE} {lgTableCharSet}");
		
		tableCreateSql_.put(VSDConstants.TBL_SYSTEM_RELEASE,
				"CREATE TABLE {IF NOT EXISTS} ^" + VSDConstants.TBL_SYSTEM_RELEASE
						+ "^ (" 
						+ " ^" + SQLTableConstants.TBLCOL_RELEASEURI + "^ {limitedText}(250) NOT NULL," 
						+ " ^" + SQLTableConstants.TBLCOL_RELEASEDATE + "^ {dateTime} NOT NULL,"
						+ " ^" + SQLTableConstants.TBLCOL_RELEASEID + "^ {limitedText}(50) default NULL,"
						+ " ^" + SQLTableConstants.TBLCOL_RELEASEAGENCY + "^ {limitedText}(250) default NULL,"
						+ " ^" + SQLTableConstants.TBLCOL_BASEDONRELEASE + "^ {limitedText}(250) default NULL,"
						+ " ^" + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + "^ {unlimitedText} default NULL,"
						+ " PRIMARY KEY  (^"
						+ SQLTableConstants.TBLCOL_RELEASEURI+ "^)"
						+ ") {TYPE} {lgTableCharSet}");
	}

	private void initIndexCreateSql() {

		indexSql_.add("CREATE INDEX ^ivd1^ ON ^" + VSDConstants.TBL_VALUE_DOMAIN
				+ "^ (^" + SQLTableConstants.TBLCOL_VALUEDOMAINURI + "^ ) ");

		indexSql_.add("CREATE INDEX ^ivd2^ ON ^" + VSDConstants.TBL_VALUE_DOMAIN
				+ "^ (^" + SQLTableConstants.TBLCOL_VALUEDOMAINNAME + "^ ) ");

		indexSql_.add("CREATE INDEX ^ivde1^ ON ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ (^" + SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^ ) ");
		
		indexSql_.add("CREATE INDEX ^ivde2^ ON ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ (^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^ ) ");
		
		indexSql_.add("CREATE INDEX ^ipl1^ ON ^" + VSDConstants.TBL_PICK_LIST
				+ "^ (^" + SQLTableConstants.TBLCOL_PICKLISTID + "^ ) ");

		indexSql_.add("CREATE INDEX ^ivpl2^ ON ^" + VSDConstants.TBL_PICK_LIST
				+ "^ (^" + SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN + "^ ) ");

		indexSql_.add("CREATE INDEX ^iple1^ ON ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ (^" + SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^ ) ");
		
		indexSql_.add("CREATE INDEX ^iple2^ ON ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ (^" + SQLTableConstants.TBLCOL_ENTITYCODE + "^ ) ");

		indexSql_.add("CREATE INDEX ^ip1^ ON ^" + VSDConstants.TBL_PROPERTY
				+ "^ (^" + SQLTableConstants.TBLCOL_ENTRYID + "^, ^" 
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^, ^"
				+ SQLTableConstants.TBLCOL_PROPERTYNAME + "^ ) ");

		indexSql_.add("CREATE INDEX ^ip2^ ON ^" + VSDConstants.TBL_PROPERTY
				+ "^ (^" + SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^ ) ");

		indexSql_.add("CREATE INDEX ^im1^ ON ^" + VSDConstants.TBL_MAPPING
				+ "^ (^" + SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^ ) ");
	}

	/**
	 * Inserts table constraints
	 * 
	 * @throws Exception
	 */
	public void createVDTableConstraints() throws Exception {
		log.debug("Creating value domain table constraints");
		if (!doValueDomainTablesExist()) {
			log.debug("Tables don't exist - returning.");
			return;
		}

		if (foreignKeySql_.size() == 0) {
			log.debug("initing the addForeignKey sql code");
			initForeigKeyAlterSQL();
		}

		Connection conn = getConnection();
		try {

			for (int i = 0; i < foreignKeySql_.size(); i++) {
				createForeignKey(conn, gsm_.modifySQL((String) foreignKeySql_
						.get(i)), (String) foreignKeySql_.get(i));
			}
		} finally {
			returnConnection(conn);
		}
	}

	private void initInsertStatements() {
		insertStatements_.put(VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA,
				"INSERT INTO " + VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA
						+ " (" + SQLTableConstants.TBLCOL_VERSION + ", "
						+ SQLTableConstants.TBLCOL_DESCRIPTION
						+ ") VALUES (?, ?)");

		// Value Domain tables
		insertStatements_.put(VSDConstants.TBL_VALUE_DOMAIN, "INSERT INTO "
				+ VSDConstants.TBL_VALUE_DOMAIN + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_VALUEDOMAINURI + ", "
				+ SQLTableConstants.TBLCOL_VALUEDOMAINNAME + ", "
				+ SQLTableConstants.TBLCOL_DEFAULTCODINGSCHEME + ", "
				+ SQLTableConstants.TBLCOL_RELEASEURI + ", "
				+ SQLTableConstants.TBLCOL_ENTRYSTATEID + ", "
				+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_VD_ENTRY, "INSERT INTO "
				+ VSDConstants.TBL_VD_ENTRY + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + ", "
				+ SQLTableConstants.TBLCOL_ENTITYCODE + ", "
				+ SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + ", "
				+ SQLTableConstants.TBLCOL_RULEORDER + ", "
				+ SQLTableConstants.TBLCOL_OPERATOR + ", "
				+ SQLTableConstants.TBLCOL_CODINGSCHEMEREFERENCE + ", "
				+ SQLTableConstants.TBLCOL_VALUEDOMAINREFERENCE + ", "
				+ SQLTableConstants.TBLCOL_LEAFONLY + ", "
				+ SQLTableConstants.TBLCOL_REFERENCEASSOCIATION + ", "
				+ SQLTableConstants.TBLCOL_TARGETTOSOURCE + ", "
				+ SQLTableConstants.TBLCOL_TRANSITIVECLOSURE
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_PICK_LIST, "INSERT INTO "
				+ VSDConstants.TBL_PICK_LIST + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_PICKLISTID + ", "
				+ SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN + ", "
				+ SQLTableConstants.TBLCOL_COMPLETEDOMAIN + ", "
				+ SQLTableConstants.TBLCOL_DEFAULTENTITYCODENAMESPACE + ", "
				+ SQLTableConstants.TBLCOL_DEFAULTLANGUAGE + ", "
				+ SQLTableConstants.TBLCOL_DEFAULTSORTORDER + ", "
				+ SQLTableConstants.TBLCOL_ISACTIVE + ", "
				+ SQLTableConstants.TBLCOL_RELEASEURI + ", "
				+ SQLTableConstants.TBLCOL_ENTRYSTATEID + ", "
				+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_PL_ENTRY, "INSERT INTO "
				+ VSDConstants.TBL_PL_ENTRY + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + ", "
				+ SQLTableConstants.TBLCOL_PLENTRYID + ", "
				+ SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + ", "
				+ SQLTableConstants.TBLCOL_ENTITYCODE + ", "
				+ SQLTableConstants.TBLCOL_ENTRYORDER + ", "
				+ SQLTableConstants.TBLCOL_ISDEFAULT + ", "
				+ SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT + ", "
				+ SQLTableConstants.TBLCOL_PROPERTYID + ", "
				+ SQLTableConstants.TBLCOL_ISACTIVE + ", "
				+ SQLTableConstants.TBLCOL_INCLUDE + ", "
				+ SQLTableConstants.TBLCOL_ENTRYSTATEID + ", "
				+ SQLTableConstants.TBLCOL_PICKTEXT
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_ENTRY_TYPE, "INSERT INTO "
				+ VSDConstants.TBL_ENTRY_TYPE + " ("
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + ", "
				+ SQLTableConstants.TBLCOL_ENTRYTYPE + ") VALUES (?, ?)");

		insertStatements_.put(VSDConstants.TBL_ENTRY_IDS, "INSERT INTO "
				+ VSDConstants.TBL_ENTRY_IDS + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ") VALUES (?)");

		insertStatements_.put(VSDConstants.TBL_MAPPING, "INSERT INTO "
				+ VSDConstants.TBL_MAPPING + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + ", "
				+ SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + ", "
				+ SQLTableConstants.TBLCOL_LOCALID + ", "
				+ SQLTableConstants.TBLCOL_URI + ", "
				+ SQLTableConstants.TBLCOL_DEPENDENTVALUE + ", "
				+ SQLTableConstants.TBLCOL_VAL1 + ", "
				+ SQLTableConstants.TBLCOL_VAL2
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_PROPERTY, "INSERT INTO "
				+ VSDConstants.TBL_PROPERTY + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID + ", "
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + ", "
				+ SQLTableConstants.TBLCOL_PROPERTYNAME + ", "
				+ SQLTableConstants.TBLCOL_ISACTIVE + ", "
				+ SQLTableConstants.TBLCOL_PROPERTYTYPE + ", "
				+ SQLTableConstants.TBLCOL_LANGUAGE + ", "
				+ SQLTableConstants.TBLCOL_ISPREFERRED + ", "
				+ SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT + ", "
				+ SQLTableConstants.TBLCOL_PROPERTYID + ", "
				+ SQLTableConstants.TBLCOL_FORMAT + ", "
				+ SQLTableConstants.TBLCOL_ENTRYSTATEID + ", "
				+ SQLTableConstants.TBLCOL_DEGREEOFFIDELITY + ", "
				+ SQLTableConstants.TBLCOL_REPRESENTATIONALFORM + ", "
				+ SQLTableConstants.TBLCOL_PROPERTYVALUE + ", "
				+ SQLTableConstants.TBLCOL_VAL1 + ", "
				+ SQLTableConstants.TBLCOL_VAL2
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		insertStatements_.put(VSDConstants.TBL_ENTRY_STATE, "INSERT INTO "
				+ VSDConstants.TBL_ENTRY_STATE + " ("
				+ SQLTableConstants.TBLCOL_ENTRYSTATEID + ", "
				+ SQLTableConstants.TBLCOL_ENTRYTYPE + ", "
				+ SQLTableConstants.TBLCOL_OWNER + ", "
				+ SQLTableConstants.TBLCOL_STATUS + ", "
				+ SQLTableConstants.TBLCOL_EFFECTIVEDATE + ", "
				+ SQLTableConstants.TBLCOL_EXPIRATIONDATE + ", "
				+ SQLTableConstants.TBLCOL_REVISIONID + ", "
				+ SQLTableConstants.TBLCOL_PREVREVISIONID + ", "
				+ SQLTableConstants.TBLCOL_CHANGETYPE + ", "
				+ SQLTableConstants.TBLCOL_RELATIVEORDER + ", "
				+ SQLTableConstants.TBLCOL_PREVENTRYSTATEID
				+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		
		insertStatements_.put(VSDConstants.TBL_ENTRY_IDS, "INSERT INTO "
				+ VSDConstants.TBL_ENTRY_IDS + " ("
				+ SQLTableConstants.TBLCOL_ENTRYID
				+ ") VALUES (?)");
		
		insertStatements_.put(VSDConstants.TBL_SYSTEM_RELEASE, "INSERT INTO "
				+ VSDConstants.TBL_SYSTEM_RELEASE + " ("
				+ SQLTableConstants.TBLCOL_RELEASEURI + ", "
				+ SQLTableConstants.TBLCOL_RELEASEDATE + ", "
				+ SQLTableConstants.TBLCOL_RELEASEID + ", "
				+ SQLTableConstants.TBLCOL_RELEASEAGENCY + ", "
				+ SQLTableConstants.TBLCOL_BASEDONRELEASE + ", "
				+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
				+ ") VALUES (?, ?, ?, ?, ?, ?)");
	}

	private void initUpdateSQL() {
		updateStatements_.put(VSDConstants.TBL_ENTRY_IDS, "UPDATE "
				+ VSDConstants.TBL_ENTRY_IDS + " SET "
				+ SQLTableConstants.TBLCOL_ENTRYID + " = ?");
	}

	public String getUpdateStatementSQL(String key) {
		if (updateStatements_.size() == 0)
			initUpdateSQL();
		return (String) updateStatements_.get(key);
	}

	/**
	 * Drops the constraints from the table
	 * @throws SQLException 
	 * 
	 * @throws Exception
	 */
	public void dropValueDomainTableConstraints() throws SQLException {
		log.debug("removing value domain table constraints");

		if (!doValueDomainTablesExist()) {
			log.debug("Value Domain tables don't exist - returning.");
			return;
		}

		if (dropForeignKeySql_.size() == 0) {
			log.debug("initing drop foreign key sql");
			initDropValueDomainTableForeignKey();
		}

		Connection conn = getConnection();

		try {
			for (int i = 0; i < dropForeignKeySql_.size(); i++) {
				PreparedStatement ps = null;
				try {
					ps = conn.prepareStatement(gsm_
							.modifySQL((String) dropForeignKeySql_.get(i)));
					ps.execute();
				} catch (SQLException e) {
					if (e.toString().indexOf("does not exist") == -1
							&& e.toString().indexOf("undefined") == -1) {
						log.error("Problem dropping the foreign key "
								+ (String) dropForeignKeySql_.get(i), e);
						throw e;
					} else {
						log.debug("The foreign constraint "
								+ (String) dropForeignKeySql_.get(i)
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
				log
						.warn("****WARNING****: - Could not remove the database constraints on your mysql database.");
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

	public String getExistingValueDomainTableVersion() throws Exception {
		PreparedStatement checkVersion = null;

		if (!doValueDomainTablesExist()) {
			log.debug("Value Domain tables don't exist - returning.");
			return null;
		}

		String result = "<1.5";

		Connection conn = getConnection();

		try {
			// can't use stc_ stuff here, because we need this data to initilize
			// it.
			checkVersion = conn.prepareStatement("Select "
					+ SQLTableConstants.TBLCOL_VERSION + " from "
					+ VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA);
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

	public boolean doValueDomainTablesExist() {
		return doesTableExist(VSDConstants.TBL_VALUE_DOMAIN);
	}

	private boolean doesTableExist(String tableName) {
		PreparedStatement query = null;

		Connection conn = getConnection();

		try {
			query = conn.prepareStatement("Select count(*) from " + tableName);
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
	 * Drop all of the tables from a LexGrid database (in the correct order)
	 * 
	 * This does drop all tables - including the optional ones.
	 * @throws SQLException 
	 * 
	 * @throws Exception
	 */
	public void dropValueDomainTables() throws SQLException {

		ArrayList<String> temp = new ArrayList<String>();
		if (doValueDomainTablesExist()) {
			dropValueDomainTableConstraints();
			
			temp.add(VSDConstants.TBL_ENTRY_STATE);
			temp.add(VSDConstants.TBL_MAPPING);
			temp.add(VSDConstants.TBL_PROPERTY);
			temp.add(VSDConstants.TBL_PL_ENTRY);
			temp.add(VSDConstants.TBL_PICK_LIST);
			temp.add(VSDConstants.TBL_VD_ENTRY);
			temp.add(VSDConstants.TBL_VALUE_DOMAIN);
			temp.add(VSDConstants.TBL_ENTRY_TYPE);
			temp.add(VSDConstants.TBL_ENTRY_IDS);
			temp.add(VSDConstants.TBL_VALUE_DOMAIN_TABLE_META_DATA);
			temp.add(VSDConstants.TBL_SYSTEM_RELEASE);
		}

		Connection conn = getConnection();

		try {
			for (int i = 0; i < temp.size(); i++) {
				dropTable(conn, (String) temp.get(i));
			}
		} finally {
			returnConnection(conn);
		}
	}

	private void dropTable(Connection conn, String tableName) {
		PreparedStatement delete = null;
		try {
			delete = conn.prepareStatement(gsm_.modifySQL("DROP TABLE "
					+ tableName + " {CASCADE} "));
			delete.executeUpdate();
			delete.close();
		} catch (SQLException e) {
			log.info(
					"Failed while dropping the table - it probably didn't exist."
							+ tableName, e);
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

	private void initForeigKeyAlterSQL() {
		log.debug("initValueDomainTableForeignKey called");

		// valueDomain FKs -begin-
		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VALUE_DOMAIN
				+ "^ ADD CONSTRAINT ^fkvd1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)");
		// valueDomain FKs -end-

		// vdEntry FKs -begin-
		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ ADD CONSTRAINT ^fkvde1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_VALUE_DOMAIN + "^ (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^)");

		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ ADD CONSTRAINT ^fkvde2^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)");
		// vdEntry FKs -end-

		// pickList FKs -begin-
		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PICK_LIST
				+ "^ ADD CONSTRAINT ^fkpl1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)ON DELETE CASCADE");
		// pickList FKs -end-

		// plEntry FKs -begin-
		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ ADD CONSTRAINT ^fkple1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_FOREIGNENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_PICK_LIST + "^ (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^)");

		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ ADD CONSTRAINT ^fkple2^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)ON DELETE CASCADE");
		// plEntry FKs -end-

		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_MAPPING
				+ "^ ADD CONSTRAINT ^fkmap1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)");

		foreignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PROPERTY
				+ "^ ADD CONSTRAINT ^fkprop1^ FOREIGN KEY (^"
				+ SQLTableConstants.TBLCOL_ENTRYID + "^) REFERENCES ^"
				+ VSDConstants.TBL_ENTRY_TYPE + "^ (^"
				+ SQLTableConstants.TBLCOL_REFERENCEENTRYID + "^)ON DELETE CASCADE");

	}

	private void initDropValueDomainTableForeignKey() {
		log.debug("initDropValueDomainTableForeignKey called");

		// pickList FKs -begin-
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PICK_LIST
				+ "^ {DROPFOREIGNKEY} ^fkpl1^");
		// pickList FKs -end-

		// plEntry FKs -start-
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ {DROPFOREIGNKEY} ^fkple1^");
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PL_ENTRY
				+ "^ {DROPFOREIGNKEY} ^fkple2^");
		// plEntry FKs -end-

		// valueDomain FKs -begin-
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VALUE_DOMAIN
				+ "^ {DROPFOREIGNKEY} ^fkvd1^");
		// valueDomain FKs -end-

		// vdEntry FKs -begin-
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ {DROPFOREIGNKEY} ^fkvde1^");
		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_VD_ENTRY
				+ "^ {DROPFOREIGNKEY} ^fkvde2^");
		// vdEntry FKs -end-

		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_MAPPING
				+ "^ {DROPFOREIGNKEY} ^fkmap1^");

		dropForeignKeySql_.add("ALTER TABLE ^" + VSDConstants.TBL_PROPERTY
				+ "^ {DROPFOREIGNKEY} ^fkprop1^");

	}

	private void createForeignKey(Connection conn, String createFKString,
			String FKName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(createFKString);
			ps.execute();
		} catch (SQLException e) {
			// try to figure out what the databases return if the constraint
			// already exists... (mysql
			// does the errno 121 business
			if (e.toString().indexOf("already") == -1
					&& e.toString().indexOf("existing") == -1
					&& e.toString().indexOf("Duplicate") == -1
					&& e.toString().indexOf("-601") == -1
					&& e.toString().indexOf("errno: 121") == -1) {
				log.error("Problem loading the foreign key " + createFKString,
						e);
				throw e;
			} else {
				log
						.debug("The foreign constraint " + FKName
								+ " already exits");
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
	private boolean createTable(Connection conn, String createTableString,
			String tableName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(createTableString);
			ps.execute();
			return true;
		} catch (SQLException e) {
			// If the error is anything other than table already exists, throw
			// an error.
			if (e.toString().indexOf("already exists") == -1
					&& e.toString().indexOf("identical") == -1
					&& e.toString().indexOf("-601") == -1
					&& e.toString().indexOf(
							"already used by an existing object") == -1
					&& e.toString().indexOf("already an object") == -1) {
				log.error("Problem creating the " + tableName
						+ " table. Syntax used=" + createTableString, e);
				throw e;
			} else {
				log.debug("The table " + tableName
						+ " already appears to exist.");
				return false;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	// Unneeded methods...
	protected String getDbTableName() {
		throw new java.lang.UnsupportedOperationException(
				"Method getDbTableName not yet implemented.");
	}

	protected ManagedObjIF findByPrimaryKeyPrim(Object key)
			throws FindException {
		throw new java.lang.UnsupportedOperationException(
				"Method findByPrimaryKeyPrim not yet implemented.");
	}

	public ManagedObjIF row2ManagedObj(ResultSet rs) throws SQLException {
		throw new java.lang.UnsupportedOperationException(
				"Method row2ManagedObj not yet implemented.");
	}

	/**
	 * Returns a specific statement based on a key
	 * 
	 * @param key
	 *            the search key
	 * @return the sql statement
	 */
	protected String getInsertStatementSQL(String key) {
		if (insertStatements_.size() == 0)
			initInsertStatements();
		String t = (String) insertStatements_.get(key);
		if (t == null) {
			throw new RuntimeException(
					"Insert statement not available for the table '" + key
							+ "'");
		} else {
			return t;
		}
	}

	protected PreparedStatement getKeyedInsertStatement(String key)
			throws Exception {
		String statement = getInsertStatementSQL(key);
		Connection insertConn = getConnection();
		return insertConn.prepareStatement(statement);
	}

	protected PreparedStatement getKeyedUpdateStatement(String key)
			throws Exception {
		String statement = getUpdateStatementSQL(key);
		Connection updateConn = getConnection();
		return updateConn.prepareStatement(statement);
	}

}