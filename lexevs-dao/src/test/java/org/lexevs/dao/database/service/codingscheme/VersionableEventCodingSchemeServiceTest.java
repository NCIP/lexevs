package org.lexevs.dao.database.service.codingscheme;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;

public class VersionableEventCodingSchemeServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private VersionableEventCodingSchemeService service;
	
	@Resource
	private RegistryDao registryDao;
	
	@Test
	public void insertCodingScheme(){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		registryDao.insertCodingSchemeEntry(entry);
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		service.insertCodingScheme(scheme);
	}
	
	@Test
	public void insertCodingSchemeWithLocalName(){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		registryDao.insertCodingSchemeEntry(entry);
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.addLocalName("localName");
		
		service.insertCodingScheme(scheme);
	}
}
