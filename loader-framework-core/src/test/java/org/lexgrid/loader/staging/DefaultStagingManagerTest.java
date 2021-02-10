
package org.lexgrid.loader.staging;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.dao.test.StaticPrefixResolver;
import org.lexgrid.loader.listener.CleanupListener;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

public class DefaultStagingManagerTest extends LoaderFrameworkCoreTestBase
{
	private DefaultStagingManager dsm = null;
	private TestDatabaseUtility testUtil = null;
	@Test
	public void testCreateStagingDatabase() throws Exception
	{
		testUtil = new TestDatabaseUtility();
		LexEvsDatabaseOperations dbOps = EasyMock.createMock(LexEvsDatabaseOperations.class);
		EasyMock.expect(dbOps.getDatabaseUtility()).andReturn(testUtil).anyTimes();
		EasyMock.expect(dbOps.getPrefixResolver()).andReturn(new StaticPrefixResolver("")).anyTimes();
		EasyMock.replay(dbOps);
		
		Resource mr = EasyMock.createMock(Resource.class);
		
		dsm = new DefaultStagingManager();
		Map<String, Resource> mp = new HashMap<String, Resource>();
		mp.put("testrdb", mr);
		dsm.setRegisteredStagingDatabases(mp);
		
		StatusTrackingLogger logger = EasyMock.createMock(StatusTrackingLogger.class);
		dsm.setLogger(logger);
		dsm.setLexEvsDatabaseOperations(dbOps);
		
		dsm.afterPropertiesSet();
		
		assertTrue(testUtil.rc == mr);
	}
	
	@Test
	public void testDropStagingDatabase() throws Exception
	{
		testCreateStagingDatabase();
		
		dsm.dropStagingDatabase("testrdb");
		
		assertTrue(testUtil.rc == null);
		assertTrue(dsm.getRegisteredStagingDatabases().size() == 0);
	}
	
	@Test
	public void testDropAllStagingDatabases() throws Exception
	{
		testCreateStagingDatabase();
		
		dsm.dropAllStagingDatabases();
		
		assertTrue(testUtil.rc == null);
		assertTrue(dsm.getRegisteredStagingDatabases().size() == 0);
	}

/**
	 * The Class TestDatabaseUtility.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
private class TestDatabaseUtility implements DatabaseUtility {

		/** The script. */
		public String script;
		public Resource rc;
		
		/* (non-Javadoc)
		 * @see org.LexGrid.persistence.database.DatabaseUtility#dropDatabase(java.lang.String)
		 */
		public void dropDatabase(String databaseName) throws Exception {
			// TODO Auto-generated method stub
			
			rc = null;
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
			rc = creationScript;
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