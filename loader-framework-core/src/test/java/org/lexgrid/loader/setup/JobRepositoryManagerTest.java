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
package org.lexgrid.loader.setup;

import static org.junit.Assert.*;

import org.LexGrid.persistence.constants.PersistenceLayerConstants;
import org.LexGrid.persistence.database.DatabaseUtility;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class JobRepositoryManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JobRepositoryManagerTest {

	/**
	 * Test insert oracle prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertOraclePrefix() throws Exception {
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setRetry(false);
		manager.setCreateScript(new ClassPathResource("schema-oracle10g.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setDatabaseUtility(dbutil);
		manager.afterPropertiesSet();
		
		assertFalse(dbutil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert mysql prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertMysqlPrefix() throws Exception {
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setRetry(false);
		manager.setCreateScript(new ClassPathResource("schema-mysql.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setDatabaseUtility(dbutil);
		manager.afterPropertiesSet();
		
		assertFalse(dbutil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert hsqldb prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertHsqldbPrefix() throws Exception {
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setRetry(false);
		manager.setCreateScript(new ClassPathResource("schema-hsqldb.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setDatabaseUtility(dbutil);
		manager.afterPropertiesSet();
		
		assertFalse(dbutil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert db2 prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertDb2Prefix() throws Exception {
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setRetry(false);
		manager.setCreateScript(new ClassPathResource("schema-db2.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setDatabaseUtility(dbutil);
		manager.afterPropertiesSet();
		
		assertFalse(dbutil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert postgres prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertPostgresPrefix() throws Exception {
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setRetry(false);
		manager.setCreateScript(new ClassPathResource("schema-postgresql.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setDatabaseUtility(dbutil);
		manager.afterPropertiesSet();
		
		assertFalse(dbutil.script.contains(" BATCH"));
	}
	
	/**
	 * The Class TestDatabaseUtility.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TestDatabaseUtility implements DatabaseUtility {

		/** The script. */
		public String script;
		
		/* (non-Javadoc)
		 * @see org.LexGrid.persistence.database.DatabaseUtility#dropDatabase(java.lang.String)
		 */
		public void dropDatabase(String databaseName) throws Exception {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.LexGrid.persistence.database.DatabaseUtility#executeScript(org.springframework.core.io.Resource)
		 */
		public void executeScript(Resource creationScript) throws Exception {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.LexGrid.persistence.database.DatabaseUtility#executeScript(java.lang.String)
		 */
		public void executeScript(String creationScript) throws Exception {
			script = creationScript;
			
		}

		public void truncateTable(String tableName) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	}
}
