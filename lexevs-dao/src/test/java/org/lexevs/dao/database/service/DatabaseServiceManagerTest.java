package org.lexevs.dao.database.service;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class DatabaseServiceManagerTest extends LexEvsDbUnitTestBase {

	@Resource
	private DatabaseServiceManager databaseServiceManager;
	
	@Test
	public void checkCodingSchemeService(){
		assertNotNull(this.databaseServiceManager.getCodingSchemeService());
		assertTrue(this.databaseServiceManager.getCodingSchemeService() instanceof CodingSchemeService);
	}
	
	@Test
	public void checkEntityService(){
		assertNotNull(this.databaseServiceManager.getEntityService());
		assertTrue(this.databaseServiceManager.getEntityService() instanceof EntityService);
	}
	
	@Test
	public void checkProperyService(){
		assertNotNull(this.databaseServiceManager.getPropertyService());
		assertTrue(this.databaseServiceManager.getPropertyService() instanceof PropertyService);
	}
}
