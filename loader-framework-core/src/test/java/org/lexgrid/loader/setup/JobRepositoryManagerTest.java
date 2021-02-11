
package org.lexgrid.loader.setup;

import static org.junit.Assert.assertFalse;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.dao.test.StaticPrefixResolver;
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
	
	private class TestJobRepositoryManager extends JobRepositoryManager {

		@Override
		protected boolean doJobRepositoryTablesExist() {
			return false;
		}
	}
	
	@Test
	public void testInsertOraclePrefix() throws Exception {
		TestDatabaseUtility testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new TestJobRepositoryManager();
		
		manager.setCreateScript(new ClassPathResource("schema-oracle10g.sql"));
		
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.ORACLE);
		manager.afterPropertiesSet();
		
		assertFalse(testUtil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert mysql prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertMysqlPrefix() throws Exception {
		TestDatabaseUtility testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new TestJobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-mysql.sql"));
		
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.MYSQL);
		manager.afterPropertiesSet();
		
		assertFalse(testUtil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert hsqldb prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertHsqldbPrefix() throws Exception {
		TestDatabaseUtility testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new TestJobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-hsqldb.sql"));
		
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.HSQL);
		
		manager.afterPropertiesSet();
		
		assertFalse(testUtil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert db2 prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertDb2Prefix() throws Exception {
		TestDatabaseUtility testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new TestJobRepositoryManager();

		manager.setCreateScript(new ClassPathResource("schema-db2.sql"));
		
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.DB2);
		
		manager.afterPropertiesSet();
		
		assertFalse(testUtil.script.contains(" BATCH"));
	}
	
	/**
	 * Test insert postgres prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertPostgresPrefix() throws Exception {
		TestDatabaseUtility testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		JobRepositoryManager manager = new TestJobRepositoryManager();
		manager.setCreateScript(new ClassPathResource("schema-postgresql.sql"));
		
		manager.setLexEvsDatabaseOperations(dbOps);
		
		manager.setLogger(new SpringBatchMessageDirector("test", new LoadStatus()));
		manager.setDatabaseType(DatabaseType.POSTGRES);
		
		manager.afterPropertiesSet();
		
		assertFalse(testUtil.script.contains(" BATCH"));
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
			
		}

		public void executeScript(String creationScript, String prefix)
				throws Exception {
			script = creationScript;
			
		}

		public boolean doesTableExist(String tableName) {
			// TODO Auto-generated method stub
			return false;
		}

		public void executeScript(Resource creationScript, String commonPrefix,
				String tableSetPrefix) throws Exception {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public void executeScript(String creationScript, String commonPrefix,
				String tableSetPrefix) throws Exception {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
		
	}
}