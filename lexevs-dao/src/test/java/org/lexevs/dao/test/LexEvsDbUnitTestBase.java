package org.lexevs.dao.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-test.xml"})
@Transactional
public class LexEvsDbUnitTestBase extends DataSourceBasedDBTestCase {

	private static final String CREATE_COMMON_SCRIPT = "sql/lexevs/common-create-hsqldb.sql";
	private static final String CREATE_CODINGSCHEME_SCRIPT = "sql/lexevs/codingscheme-create-hsqldb.sql";
	
	@Resource
	protected DataSource dataSource;
	
	@Resource
	protected PrefixResolver prefixResolver;
	
	@Resource
	protected LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	@Test
	public void testConfig(){
		assertNotNull(this);
	}
	
	@Before
	public void setUp() throws Exception {

		new SimpleJdbcTemplate(dataSource).getJdbcOperations().execute("DROP SCHEMA PUBLIC CASCADE");
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_COMMON_SCRIPT), prefixResolver.resolveDefaultPrefix());
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_CODINGSCHEME_SCRIPT), prefixResolver.resolveDefaultPrefix());
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
