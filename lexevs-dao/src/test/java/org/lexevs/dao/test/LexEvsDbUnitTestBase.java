
package org.lexevs.dao.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.cache.MethodCachingProxy;
import org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.setup.schemacheck.CountBasedLexGridSchemaCheck;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.DelegatingSystemResourceService;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class LexEvsDbUnitTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-all-in-memory-test.xml"})
public class LexEvsDbUnitTestBase extends DataSourceBasedDBTestCase {

	/** The data source. */
	@Resource
	protected DataSource dataSource;
	
	/** The prefix resolver. */
	@Resource
	protected PrefixResolver prefixResolver;
	
	/** The lex evs database operations. */
	@Resource
	protected LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	@Resource
	protected MethodCachingProxy methodCachingProxy;
	
	@Resource
	protected DatabaseType databaseType;
	
	@Resource
	protected SystemVariables systemVariables;
	
	@Resource
	protected PrimaryKeyIncrementer primaryKeyIncrementer;
	
	@Resource
	protected DelegatingSystemResourceService delegatingSystemResourceService;
	
	@Resource
	protected Registry registry;
 
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;

    @Test
    public void checkSetUp(){
    	assertNotNull(dataSource);
    	assertNotNull(prefixResolver);
    	assertNotNull(lexEvsDatabaseOperations);
    	assertNotNull(methodCachingProxy);
    }
    
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(false);
		methodCachingProxy.clearAll();
		CountBasedLexGridSchemaCheck check = new CountBasedLexGridSchemaCheck(dataSource, systemVariables);
		
		if(check.isCommonLexGridSchemaInstalled()) {
			lexEvsDatabaseOperations.dropAllTables();
			primaryKeyIncrementer.destroy();
		}	
		
		primaryKeyIncrementer.initialize();
		lexEvsDatabaseOperations.createAllTables();
	}
	
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		extensionLoadingListenerRegistry.setEnableListeners(false);
		
		if(this.databaseType.equals(DatabaseType.HSQL)) {
			new SimpleJdbcTemplate(dataSource).getJdbcOperations().execute("SHUTDOWN");
		} else {
			lexEvsDatabaseOperations.dropAllTables();
			primaryKeyIncrementer.destroy();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.dbunit.DataSourceBasedDBTestCase#getDataSource()
	 */
	@Override
	protected DataSource getDataSource() {
		return this.dataSource;
	}

	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#getDataSet()
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		return null;
	}
}