package org.lexevs.dao.database.service.entity;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.ResourceManager;

public class VersionableEntityServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private EntityService service;
	
	@Resource
	private CodingSchemeService codingSchemeservice;
	
	@Resource
	private ResourceManager resourceManager;
	
	@Test
	public void insertEntity() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		Registry registry = resourceManager.getRegistry();
		registry.addNewItem(entry);


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
	public void insertBatchEntity() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		resourceManager.getRegistry().addNewItem(entry);
		
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
