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
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.setup.schemacheck.CountBasedLexGridSchemaCheck;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.system.constants.SystemVariables;
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
	
    @BeforeClass
    public static void setSystemProp() {
        System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");
    }
    
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
		}	
		
		lexEvsDatabaseOperations.createAllTables();
	}
	
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		if(this.databaseType.equals(DatabaseType.HSQL)) {
			new SimpleJdbcTemplate(dataSource).getJdbcOperations().execute("SHUTDOWN");
		} else {
			lexEvsDatabaseOperations.dropAllTables();
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
