/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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