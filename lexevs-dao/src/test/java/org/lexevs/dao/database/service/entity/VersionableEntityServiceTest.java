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
package org.lexevs.dao.database.service.entity;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

/**
 * The Class VersionableEntityServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEntityServiceTest extends LexEvsDbUnitTestBase {

	/** The service. */
	@Resource
	private VersionableEventEntityService service;
	
	/** The coding schemeservice. */
	@Resource
	private CodingSchemeService codingSchemeservice;

	
	/**
	 * Insert entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertEntity() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		codingSchemeservice.insertCodingScheme(scheme);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		
		service.insertEntity("uri", "v1", entity);
	}
	
	@Test
	public void updateEntity() throws Exception{
		List<DatabaseServiceEventListener> listeners = service.getDatabaseServiceEventListeners();
		service.getDatabaseServiceEventListeners().clear();

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		codingSchemeservice.insertCodingScheme(scheme);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		entity.setIsDefined(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("pre-update");
		entity.setEntityDescription(ed);
		
		service.insertEntity("uri", "v1", entity);
		
		entity.getEntityDescription().setContent("post-update");
		
		service.updateEntity("uri", "v1", entity);

		Entity moddedEntity = service.getEntity("uri", "v1", "c1", "ns");
		
		Assert.assertEquals("post-update", moddedEntity.getEntityDescription().getContent());
		
		service.setDatabaseServiceEventListeners(listeners);
	}
	
	/**
	 * Insert batch entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertBatchEntity() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		codingSchemeservice.insertCodingScheme(scheme);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		
		service.insertEntity("uri", "v1", entity);
	}
}
