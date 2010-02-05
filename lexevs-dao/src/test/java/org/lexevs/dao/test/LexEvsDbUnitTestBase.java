package org.lexevs.dao.test;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-dbunit-test.xml"})
public class LexEvsDbUnitTestBase extends DataSourceBasedDBTestCase {

	private static final String CREATE_MAIN_SCRIPT = "sql/lexevs/lexevs-main-create-hsqldb.sql";
	private static final String CREATE_REGISTSRY_SCRIPT = "sql/lexevs/lexevs-registry-create-hsqldb.sql";
	
	@Resource
	protected DataSource dataSource;
	
	@Resource
	protected PrefixResolver prefixResolver;
	
	@Resource
	protected DatabaseUtility databaseUtility;
	
	@Test
	public void testConfig(){
		assertNotNull(this);
	}
	
	@Before
	public void setUp() throws Exception {
		databaseUtility.executeScript(new ClassPathResource(CREATE_MAIN_SCRIPT), prefixResolver.resolvePrefix());
		databaseUtility.executeScript(new ClassPathResource(CREATE_REGISTSRY_SCRIPT), prefixResolver.resolvePrefix());
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		new SimpleJdbcTemplate(dataSource).getJdbcOperations().execute("SHUTDOWN");
	}
	
	@Override
	protected DataSource getDataSource() {
		return this.dataSource;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return null;
	}
}
