package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class EntityRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource(name = "entityService")
	private VersionableEventEntityService entityService;

	@Test
	public void testEntityRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_Entity.xml")
				.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);

		Entity entity = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Jan_testEntity");

		assertNotNull(entity);
		
		assertNull(entity.getEffectiveDate());
		
		Entity entity1 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Mar_testEntity");

		assertNotNull(entity1);
		
		assertNotNull(entity1.getEffectiveDate());
		
		Entity entity2 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Jan_testEntity");

		assertNotNull(entity2);
		
		assertTrue("Domestic Auto Makers".equals(entity2
				.getEntityDescription().getContent()));
		
		Entity entity3 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Feb_testEntity");

		assertNotNull(entity3);
		
		assertTrue("Modified Domestic Auto Makers".equals(entity3
				.getEntityDescription().getContent()));
		
		Entity entity4 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Mar_testEntity");

		assertNotNull(entity4);
		
		assertTrue("Modified Domestic Auto Makers".equals(entity4
				.getEntityDescription().getContent()));

		Entity entity5 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "A0001", "Automobiles",
				"testRelease2010Feb_testEntity");

		assertNotNull(entity5);
		
		assertTrue("Automobile".equals(entity5
				.getEntityDescription().getContent()));

	}
}
