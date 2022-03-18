
package org.lexgrid.loader.rrf.staging;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.spring.DynamicPropertyApplicationContext;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.properties.impl.PropertiesFactory;
import org.lexgrid.loader.rrf.staging.model.CodeSabPair;
import org.lexgrid.loader.staging.DefaultStagingManager;
import org.lexgrid.loader.test.util.LoaderTestUtils;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class JdbcMrconsoStagingDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JdbcMrconsoStagingDaoTest {

	/** The ctx. */
	private ApplicationContext ctx;
	
	/** The jdbc mrconso staging dao. */
	private MrconsoStagingDao jdbcMrconsoStagingDao;
	
	/** The Constant RRF_DIRECTORY. */
	private static final String RRF_DIRECTORY = "file:src/test/resources/data/sample-air";
	
	/** The template. */
	private JdbcTemplate template;
	
	/**
	 * Clean up.
	 */
	@After
	public void cleanUp(){
		template.execute("shutdown");		
	}
	
	/**
	 * Inits the.
	 */
	@Before
	public void init() throws Exception {
		LoaderTestUtils.cleanUpAll();
		SQLConnectionInfo info = new SQLConnectionInfo();
		info.driver = "org.hsqldb.jdbcDriver";
		info.server = "jdbc:hsqldb:mem:src/test/tmp/";
		info.username = "sa";
		info.password = "";
		info.prefix = "";
		info.dbName = "";
		Properties props = new PropertiesFactory().getProperties("");
		
		props.put("sab", "LNC");
		props.put("rrfDir", RRF_DIRECTORY);
		props.put("retry", "false");
		props.put("prefix", "");
		
		ctx = new DynamicPropertyApplicationContext("abstractRrfLoaderTest.xml", props);
			
		template  = (JdbcTemplate)ctx.getBean("lexEvsJdbcTemplate");
	
		//Caching has been disabled due to performance concerns of ehcache.
		//If caching is brought back, uncomment the caching tests.
	
		jdbcMrconsoStagingDao = (MrconsoStagingDao)ctx.getBean("mrconsoStagingDao");
		//methodCacheInterceptor = (MrconsoStagingCacheInterceptor)ctx.getBean("methodCacheInterceptor");
	
		DefaultStagingManager stagingManager = new DefaultStagingManager();
		stagingManager.setLexEvsDatabaseOperations((LexEvsDatabaseOperations)ctx.getBean("defaultLexEvsDatabaseOperations"));
		
		Map<String,Resource> stagingDbs = new HashMap<String,Resource>();
		
		stagingDbs.put("mrconsoStaging", (Resource)ctx.getBean("mrconsoStagingCreateScriptFactory"));
		stagingManager.setRegisteredStagingDatabases(stagingDbs);
		stagingManager.setPrefix("");
		
		stagingManager.setLogger(new StatusTrackingLogger(){

			public LoadStatus getProcessStatus() {
				return null;
			}

			public void busy() {
				// TODO Auto-generated method stub
				
			}

			public String debug(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public void fatalAndThrowException(String message) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public void fatalAndThrowException(String message,
					Throwable sourceException) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public String info(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String warn(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String warn(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		stagingManager.afterPropertiesSet();
		stagingManager.initializeStagingSupport();
		
	}
	
	/**
	 * Test get code from aui.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCodeFromAui() throws Exception {
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE','STR','SRL','SUPPRESS','CVF')");
		
		String code = jdbcMrconsoStagingDao.getCodeFromAui("AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		
		assertTrue(code.equals("CODE"));
	
		String code2 = jdbcMrconsoStagingDao.getCodeFromAui("AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 1);
		
		assertTrue(code2.equals("CODE"));
	}

	/**
	 * Test get cui from aui and sab.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCuiFromAuiAndSab() throws Exception {
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE','STR','SRL','SUPPRESS','CVF')");
		
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		String cui = jdbcMrconsoStagingDao.getCuiFromAuiAndSab("AUI", "SAB");
		
		assertTrue(cui.equals("CUI"));
		
		String cui2 = jdbcMrconsoStagingDao.getCuiFromAuiAndSab("AUI", "SAB");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 1);
		
		assertTrue(cui2.equals("CUI"));
	}
	
	@Test
	public void testGetCodesFromCui() throws Exception {
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE1','STR','SRL','SUPPRESS','CVF')");
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE2','STR','SRL','SUPPRESS','CVF')");

		List<String> codes = jdbcMrconsoStagingDao.getCodesFromCui("CUI");
		
		assertTrue(codes.size() == 2);
		assertTrue(codes.contains("CODE1"));
		assertTrue(codes.contains("CODE2"));
	}
	
	@Test
	public void testGetCuisFromCode() throws Exception {
		template.execute("insert into mrconsoStaging values ('CUI1','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE','STR','SRL','SUPPRESS','CVF')");
		template.execute("insert into mrconsoStaging values ('CUI2','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE','STR','SRL','SUPPRESS','CVF')");

		List<String> cuis = jdbcMrconsoStagingDao.getCuisFromCode("CODE");
		
		assertTrue(cuis.size() == 2);
		assertTrue(cuis.contains("CUI1"));
		assertTrue(cuis.contains("CUI2"));
	}
	
	/**
	 * Test get code and coding scheme.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCodeAndCodingScheme() throws Exception {
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','LNC','TTY','CODE','STR','SRL','SUPPRESS','CVF')");
		
		CodeCodingSchemePair pair = jdbcMrconsoStagingDao.getCodeAndCodingScheme("CUI", "AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		
		assertTrue(pair.getCode().equals("CODE"));		
		
		CodeCodingSchemePair pair2 = jdbcMrconsoStagingDao.getCodeAndCodingScheme("CUI", "AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 1);
		
		assertTrue(pair2.getCode().equals("CODE"));
	}
	
	/**
	 * Test get code and sab.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCodeAndSab() throws Exception {
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE','STR','SRL','SUPPRESS','CVF')");
		
		CodeSabPair pair = jdbcMrconsoStagingDao.getCodeAndSab("CUI", "AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		
		assertTrue(pair.getCode().equals("CODE"));
		assertTrue(pair.getSab().equals("SAB"));
		
		CodeSabPair pair2 = jdbcMrconsoStagingDao.getCodeAndSab("CUI", "AUI");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 1);
		
		assertTrue(pair2.getCode().equals("CODE"));
		assertTrue(pair2.getSab().equals("SAB"));
	}
	
	/**
	 * Test get codes.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCodes() throws Exception {
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE1','STR','SRL','SUPPRESS','CVF')");
		template.execute("insert into mrconsoStaging values ('CUI','LAT','TS','LUI','STT','SUI','ISPREF','AUI','SAUI','SCUI','SDUI','SAB','TTY','CODE2','STR','SRL','SUPPRESS','CVF')");
		
		List<String> cui = jdbcMrconsoStagingDao.getCodes("CUI", "SAB");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 0);
		
		assertTrue(cui.size() == 2);
		
		assertTrue(cui.contains("CODE1"));
		
		assertTrue(cui.contains("CODE2"));
		
		List<String> cui2 = jdbcMrconsoStagingDao.getCodes("CUI", "SAB");
		//assertTrue(methodCacheInterceptor.getCache().getStatistics().getCacheHits() == 1);
		
		assertTrue(cui2.size() == 2);
		
		assertTrue(cui2.contains("CODE1"));
		
		assertTrue(cui2.contains("CODE2"));
	}
}