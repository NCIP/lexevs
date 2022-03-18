
package org.lexgrid.loader.setup;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lexevs.dao.database.setup.script.ScriptFactory;
import org.lexevs.dao.database.setup.script.ScriptFactory.ScriptType;
import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.core.io.Resource;

/**
 * The Class ScriptFactoryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ScriptFactoryTest {

	/**
	 * Test get mysql create script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetMysqlCreateScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.MYSQL);
		sf.setScriptType(ScriptType.create);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-mysql.sql"));
	}
	
	/**
	 * Test get mysql drop script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetMysqlDropScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.MYSQL);
		sf.setScriptType(ScriptType.drop);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-drop-mysql.sql"));
	}
	
	/**
	 * Test get oracle create script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetOracleCreateScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.ORACLE);
		sf.setScriptType(ScriptType.create);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-oracle10g.sql"));
	}
	
	/**
	 * Test get oracle drop script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetOracleDropScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.ORACLE);
		sf.setScriptType(ScriptType.drop);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-drop-oracle10g.sql"));
	}
	
	/**
	 * Test get hsql create script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetHsqlCreateScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.HSQL);
		sf.setScriptType(ScriptType.create);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-hsqldb.sql"));
	}
	
	/**
	 * Test get hsql drop script.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetHsqlDropScript() throws Exception {
		ScriptFactory sf = new ScriptFactory();
		sf.setDatabaseType(DatabaseType.HSQL);
		sf.setScriptType(ScriptType.drop);
		sf.afterPropertiesSet();
		Resource resource = (Resource)sf.getObject();
		assertTrue(resource.getFilename().equals("schema-drop-hsqldb.sql"));
	}
}