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

import javax.sql.DataSource;

import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The Interface PersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsDatabaseOperations {

	/**
	 * Creates the common tables.
	 */
	public void createCommonTables();
	
	/**
	 * Creates the coding scheme tables.
	 */
	public void createCodingSchemeTables();
	
	/**
	 * Creates the coding scheme tables.
	 * 
	 * @param prefix the prefix
	 */
	public void createCodingSchemeTables(String prefix);
	
	/**
	 * Drop common tables.
	 */
	public void dropCommonTables();
	
	/**
	 * Drop tables.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void dropTables(String codingSchemeUri, String version);
	
	/**
	 * Checks if is coding scheme loaded.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return true, if is coding scheme loaded
	 */
	public boolean isCodingSchemeLoaded(String codingSchemeUri, String version);

	/**
	 * Gets the database utility.
	 * 
	 * @return the database utility
	 */
	public DatabaseUtility getDatabaseUtility();
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource();
	
	/**
	 * Gets the transaction manager.
	 * 
	 * @return the transaction manager
	 */
	public PlatformTransactionManager getTransactionManager();
	
	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver();
	
	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType();
	
	/**
	 * Compute transitive table.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void computeTransitiveTable(String codingSchemeName, String codingSchemeUri, String version);
	
	/**
	 * Index.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void index(String codingSchemeName, String codingSchemeUri, String version);
		
	/**
	 * Cleanup failed load.
	 * 
	 * @param dbName the db name
	 * @param prefix the prefix
	 * 
	 * @throws Exception the exception
	 */
	public void cleanupFailedLoad(String dbName, String prefix) throws Exception;
	
	/**
	 * Re index.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void reIndex(String codingSchemeUri, String version);
}


