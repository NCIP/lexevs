package org.lexevs.dao.database.service.codingscheme;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class VersionableEventCodingSchemeServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private VersionableEventCodingSchemeService service;
	
	@Resource
	private Registry registry;
	
	@Test
	public void insertCodingScheme() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		registry.addNewItem(entry);
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		service.insertCodingScheme(scheme);
	}
	
	@Test
	public void insertCodingSchemeWithLocalName() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		registry.addNewItem(entry);
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.addLocalName("localName");
		
		service.insertCodingScheme(scheme);
	}
}
