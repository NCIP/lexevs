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

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.system.ResourceManager;
import org.springframework.core.io.Resource;

// TODO: Auto-generated Javadoc
/**
 * The Class LexEvsPersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsDatabaseOperations implements LexEvsDatabaseOperations{
	
	private DatabaseUtility databaseUtility;
	private Resource lexevsCommonSchemaCreateScript;
	private Resource lexevsCodingSchemeSchemaCreateScript;
	
	private PrefixResolver prefixResolver;
	private DataSource dataSource;
	private DatabaseType databaseType;

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#isCodingSchemeLoaded(java.lang.String, java.lang.String)
	 */
	public boolean isCodingSchemeLoaded(String codingScheme, String version) {
		return false;
	}
	
	public void createCommonTables() {
		try {
			databaseUtility.executeScript(lexevsCommonSchemaCreateScript, this.prefixResolver.resolveDefaultPrefix());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public void createCodingSchemeTables() {
		try {
			databaseUtility.executeScript(lexevsCodingSchemeSchemaCreateScript, this.prefixResolver.resolveDefaultPrefix());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public void createCodingSchemeTables(String prefix) {
		try {
			databaseUtility.executeScript(lexevsCodingSchemeSchemaCreateScript, getCombinedPrefix(prefix));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public void cleanupFailedLoad(String dbName, String prefix)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void computeTransitiveTable(String codingSchemeName,
			String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		
	}

	public void index(String codingSchemeName, SQLConnectionInfo connectionInfo) {
		// TODO Auto-generated method stub
		
	}

	public void index(String codingSchemeName, String codingSchemeUri,
			String version) {
		// TODO Auto-generated method stub
		
	}

	public void reIndex(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		
	}

	public void dropTables(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		
	}

	public void dropCommonTables() {
		// TODO Auto-generated method stub
		
	}

	protected String getCombinedPrefix(String codingSchemePrefix){
		return prefixResolver.resolveDefaultPrefix() + codingSchemePrefix;
	}
	
	public Resource getLexevsCommonSchemaCreateScript() {
		return lexevsCommonSchemaCreateScript;
	}

	public void setLexevsCommonSchemaCreateScript(
			Resource lexevsCommonSchemaCreateScript) {
		this.lexevsCommonSchemaCreateScript = lexevsCommonSchemaCreateScript;
	}

	public Resource getLexevsCodingSchemeSchemaCreateScript() {
		return lexevsCodingSchemeSchemaCreateScript;
	}

	public void setLexevsCodingSchemeSchemaCreateScript(
			Resource lexevsCodingSchemeSchemaCreateScript) {
		this.lexevsCodingSchemeSchemaCreateScript = lexevsCodingSchemeSchemaCreateScript;
	}
	
	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}
}
