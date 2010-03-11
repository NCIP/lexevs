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
package org.lexevs.dao.database.service;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

/**
 * The Class DatabaseServiceManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceManagerTest extends LexEvsDbUnitTestBase {

	/** The database service manager. */
	@Resource
	private DatabaseServiceManager databaseServiceManager;
	
	/**
	 * Check coding scheme service.
	 */
	@Test
	public void checkCodingSchemeService(){
		assertNotNull(this.databaseServiceManager.getCodingSchemeService());
		assertTrue(this.databaseServiceManager.getCodingSchemeService() instanceof CodingSchemeService);
	}
	
	/**
	 * Check entity service.
	 */
	@Test
	public void checkEntityService(){
		assertNotNull(this.databaseServiceManager.getEntityService());
		assertTrue(this.databaseServiceManager.getEntityService() instanceof EntityService);
	}
	
	/**
	 * Check propery service.
	 */
	@Test
	public void checkProperyService(){
		assertNotNull(this.databaseServiceManager.getPropertyService());
		assertTrue(this.databaseServiceManager.getPropertyService() instanceof PropertyService);
	}
}
