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

import static org.junit.Assert.assertFalse;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexgrid.loader.logging.SpringBatchMessageDirector;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class JobRepositoryManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JobRepositoryManagerTest extends LoaderFrameworkCoreTestBase{

	/**
	 * Test insert oracle prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertOraclePrefix() throws Exception {
		
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(new TestDatabaseUtility()).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new JobRepositoryManager();
		
		manager.setCreateScript(new ClassPathResource("schema-oracle10g.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.ORACLE);
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
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(new TestDatabaseUtility()).anyTimes();
		EasyMock.replay(dbOps);
		
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-mysql.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.MYSQL);
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
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(new TestDatabaseUtility()).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-hsqldb.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.HSQL);
		
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
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(new TestDatabaseUtility()).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new JobRepositoryManager();

		manager.setCreateScript(new ClassPathResource("schema-db2.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.DB2);
		
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
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(new TestDatabaseUtility()).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new JobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-postgresql.sql"));
		
		TestDatabaseUtility dbutil = new TestDatabaseUtility();
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.POSTGRES);
		
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

		public void executeScript(Resource creationScript, String prefix)
				throws Exception {
			//
			
		}

		public void executeScript(String creationScript, String prefix)
				throws Exception {
			script = creationScript;
			
		}

		public boolean doesTableExist(String tableName) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
