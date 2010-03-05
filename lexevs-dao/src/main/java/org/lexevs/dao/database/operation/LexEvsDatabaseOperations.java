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

	public void createCommonTables();
	
	public void createCodingSchemeTables();
	
	public void createCodingSchemeTables(String prefix);
	
	public void dropCommonTables();
	
	public void dropTables(String codingSchemeUri, String version);
	
	public boolean isCodingSchemeLoaded(String codingSchemeUri, String version);

	public DatabaseUtility getDatabaseUtility();
	
	public DataSource getDataSource();
	
	public PlatformTransactionManager getTransactionManager();
	
	public PrefixResolver getPrefixResolver();
	
	public DatabaseType getDatabaseType();
	
	public void computeTransitiveTable(String codingSchemeName, String codingSchemeUri, String version);
	
	public void index(String codingSchemeName, String codingSchemeUri, String version);
		
	public void cleanupFailedLoad(String dbName, String prefix) throws Exception;
	
	public void reIndex(String codingSchemeUri, String version);
}


