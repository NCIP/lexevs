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
package org.lexevs.dao.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.cache.MethodCachingProxy;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class LexEvsDbUnitTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-test.xml"})
public class LexEvsDbUnitTestBase extends DataSourceBasedDBTestCase {

	/** The Constant CREATE_COMMON_SCRIPT. */
//	private static final String CREATE_COMMON_SCRIPT = "sql/lexevs/common-create-hsqldb.sql";
	private static final String CREATE_COMMON_SCRIPT = "sql/lexevs/common-create-mysql.sql";
	
	/** The Constant CREATE_CODINGSCHEME_SCRIPT. */
//	private static final String CREATE_CODINGSCHEME_SCRIPT = "sql/lexevs/codingscheme-create-hsqldb.sql";
	private static final String CREATE_CODINGSCHEME_SCRIPT = "sql/lexevs/codingscheme-create-mysql.sql";
	
	/** The Constant CREATE_VD_PICKLIST_SCRIPT. */
//	private static final String CREATE_VD_PICKLIST_SCRIPT = "sql/lexevs/valuesets-create-hsqldb.sql";
	
//	private static final String CREATE_CODINGSCHEME_HISTORY_SCRIPT = "sql/lexevs/codingschemehistory-create-hsqldb.sql";
	
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
	
    @BeforeClass
    public static void setSystemProp() {
        System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");
    }
	
	/**
	 * Test set up.
	 */
	@Test
	/*public void testSetUp() {
		assertTrue(true);
	}*/
	
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		/*methodCachingProxy.clearAll();
		
		String prefix = prefixResolver.resolveDefaultPrefix();
		JdbcOperations jdbcOps = new SimpleJdbcTemplate(dataSource).getJdbcOperations();
		
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "csSupportedAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "csMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "propertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "propertyLinks");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "property");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityType");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnQuals");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnsToEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnsToEntityTr");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnsToData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "associationPredicate");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "associationEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "relation");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "codingScheme");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entryState");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "revision");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "systemRelease");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "registryMetaData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "registry");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_associationEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_codingScheme");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_csMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entityAssnQuals");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entityAssnsToData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entityAssnsToEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_property");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_propertyLinks");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_propertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_relation");		
		jdbcOps.execute("DROP TABLE IF EXISTS vsdEntry");
		jdbcOps.execute("DROP TABLE IF EXISTS valueSetDefinition");
		jdbcOps.execute("DROP TABLE IF EXISTS vsMultiAttrib");		
		jdbcOps.execute("DROP TABLE IF EXISTS vsPLEntry");
		jdbcOps.execute("DROP TABLE IF EXISTS vsPickList");
		jdbcOps.execute("DROP TABLE IF EXISTS vsPropertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS vsProperty");
		jdbcOps.execute("DROP TABLE IF EXISTS vsEntryState");
		jdbcOps.execute("DROP TABLE IF EXISTS vsSupportedAttrib");
		
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_COMMON_SCRIPT), prefix, prefix);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_CODINGSCHEME_SCRIPT), prefix, prefix);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_VD_PICKLIST_SCRIPT), prefix, prefix);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(new ClassPathResource(CREATE_CODINGSCHEME_HISTORY_SCRIPT), prefix, prefix);*/
	}
	
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		/*super.tearDown();
		new SimpleJdbcTemplate(dataSource).getJdbcOperations().execute("SHUTDOWN");*/
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
