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
package org.lexevs.dao.database.operation;

import java.sql.Connection;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SystemVariables;
import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.core.io.Resource;

// TODO: Auto-generated Javadoc
/**
 * The Class LexEvsPersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsDatabaseOperations implements LexEvsDatabaseOperations{
	
	private DatabaseUtility databaseUtility;
	private Resource lexevsSchemaCreateScript;
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#getExistingConnectionInfo(java.lang.String, java.lang.String)
	 */
	public SQLConnectionInfo getExistingConnectionInfo(String codingScheme,
			String version) {
		return this.getConnectionInfo(codingScheme, version);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#isCodingSchemeLoaded(java.lang.String, java.lang.String)
	 */
	public boolean isCodingSchemeLoaded(String codingScheme, String version) {
		SQLConnectionInfo connection = 
			this.getConnectionInfo(codingScheme, version);
		if(connection == null){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets the connection info.
	 * 
	 * @param codingScheme the coding scheme
	 * @param version the version
	 * 
	 * @return the connection info
	 */
	protected SQLConnectionInfo getConnectionInfo(String codingScheme, String version){
		ResourceManager rm = ResourceManager.instance();
		AbsoluteCodingSchemeVersionReference acvr = 
			this.buildAbsoluteCodingSchemeVersionReference(codingScheme, version);
		return rm.getRegistry().getSQLConnectionInfoForCodeSystem(acvr);
	}

	/**
	 * Builds the absolute coding scheme version reference.
	 * 
	 * @param codingScheme the coding scheme
	 * @param version the version
	 * 
	 * @return the absolute coding scheme version reference
	 */
	private AbsoluteCodingSchemeVersionReference 
	buildAbsoluteCodingSchemeVersionReference(String codingScheme, String version){
		AbsoluteCodingSchemeVersionReference acvr = 
			new AbsoluteCodingSchemeVersionReference();
		acvr.setCodingSchemeURN(codingScheme);
		acvr.setCodingSchemeVersion(version);
		return acvr;
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#createTables(org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo)
	 */
	public void createTables(SQLConnectionInfo connectionInfo, String prefix){
		try {	
			SQLTableUtilities utils = createSQLTableUtilities(connectionInfo);
			//utils.createDefaultTables(createIndexes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void createTables(String prefix) {
		try {
			databaseUtility.executeScript(lexevsSchemaCreateScript, prefix);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public void indexTables(String codingScheme, String version){
		indexTables(this.getConnectionInfo(codingScheme, version));
	}

	public void indexTables(SQLConnectionInfo connectionInfo){
		try {	
			SQLTableUtilities utils = createSQLTableUtilities(connectionInfo);
			utils.createDefaultTableIndexes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#dropTables(org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo)
	 */
	public void dropTables(SQLConnectionInfo connectionInfo){
		try {	
			SQLTableUtilities utils = createSQLTableUtilities(connectionInfo);
			utils.dropTables();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#getNewConnectionInfoForLoad()
	 */
	public SQLConnectionInfo getNewConnectionInfoForLoad(boolean indexTables){
		ResourceManager rm = ResourceManager.instance();
		try {			
			SQLConnectionInfo connection = rm.getSQLConnectionInfoForLoad();
			//this.createTables(connection, indexTables);
			return connection;
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}		
	}

	/**
	 * Creates the sql table utilities.
	 * 
	 * @param connectionInfo the connection info
	 * 
	 * @return the sQL table utilities
	 */
	protected SQLTableUtilities createSQLTableUtilities(SQLConnectionInfo connectionInfo){
		try {
			Connection connection = createDatabaseConnection(connectionInfo);
			SQLTableUtilities utils = new SQLTableUtilities(connection, connectionInfo.prefix);
			return utils;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public SQLTableUtilities getSQLTableUtilities(String codingScheme, String version){
		return createSQLTableUtilities(getConnectionInfo(codingScheme, version));
	}
	
	public SQLTableConstants getSQLTableConstants(String codingScheme, String version){
		SQLConnectionInfo sqlConn = this.getConnectionInfo(codingScheme, version);
		try {
			return 
				new SQLTableUtilities(createDatabaseConnection(sqlConn), sqlConn.prefix)
				.getSQLTableConstants();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates the database connection.
	 * 
	 * @param connectionInfo the connection info
	 * 
	 * @return the connection
	 */
	private Connection createDatabaseConnection(SQLConnectionInfo connectionInfo){
		try {
			Connection connection = DBUtility.connectToDatabase(connectionInfo.server, 
					connectionInfo.driver, 
					connectionInfo.username, 
					connectionInfo.password);
			return connection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	public void setLexevsSchemaCreateScript(Resource lexevsSchemaCreateScript) {
		this.lexevsSchemaCreateScript = lexevsSchemaCreateScript;
	}

	public Resource getLexevsSchemaCreateScript() {
		return lexevsSchemaCreateScript;
	}
}
