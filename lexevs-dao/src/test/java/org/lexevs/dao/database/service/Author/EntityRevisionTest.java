
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.BooleanUtils;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class EntityRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource(name = "entityService")
	private VersionableEventEntityService entityService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/Automobiles2010_Test_Entity.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}

	@Test
	public void testGetNewRevisedEntity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Jan_testEntity");

		assertNotNull(entity);
		
		assertNull(entity.getEffectiveDate());
	}
	
	@Test
	public void testGetModifiedEntityRevisions() throws Exception {
		
		Entity entity1 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Mar_testEntity");

		assertNotNull(entity1);
		
		assertNotNull(entity1.getEffectiveDate());
	}
	
	@Test
	public void testGetEntityNotPartOfRevisionButInInitialLoad() throws Exception {
		
		Entity entity2 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Jan_testEntity");

		assertNotNull(entity2);
		
		assertEquals("Domestic Auto Makers",entity2
				.getEntityDescription().getContent());
	}
	
	@Test
	public void testGetModifiedEntity() throws Exception {
		Entity entity3 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Feb_testEntity");

		assertNotNull(entity3);
		
		assertTrue("Modified Domestic Auto Makers".equals(entity3
				.getEntityDescription().getContent()));
	}
	
	@Test
	public void testGetLastModifiedEntity() throws Exception {
		Entity entity4 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "005", "Automobiles",
				"testRelease2010Mar_testEntity");

		assertNotNull(entity4);
		
		assertTrue("Modified Domestic Auto Makers".equals(entity4
				.getEntityDescription().getContent()));
	}
	
	@Test
	public void testGetEntityNotPartOfRevisionButInInitialLoadTwoRevisionsIn() throws Exception {
		Entity entity5 = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "A0001", "Automobiles",
				"testRelease2010Feb_testEntity");

		assertNotNull(entity5);
		
		assertTrue("Automobile".equals(entity5
				.getEntityDescription().getContent()));

	}
	
	@Test
	public void testGetRevisedEntityWithRevisedProperty() throws Exception {
		Entity preProperyAdded = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Mar_testEntity");

		Entity postProperyAdded = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010June_testEntity");
		
		assertNotNull(preProperyAdded);
		assertNotNull(postProperyAdded);
		
		assertEquals("midas002", preProperyAdded.getEntityCode() );
		assertEquals("midas002", postProperyAdded.getEntityCode() );
		
		assertEquals(0, preProperyAdded.getDefinitionCount() );
		assertEquals(1, postProperyAdded.getDefinitionCount() );

	}
	
	@Test
	public void testDependentAndModifyEntity() throws Exception {
		Entity julyEntity = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010July_testEntity");
		
		assertNotNull(julyEntity);
		
		assertFalse(BooleanUtils.toBoolean(julyEntity.getIsAnonymous()));
		assertFalse(BooleanUtils.toBoolean(julyEntity.getIsDefined()));
		assertEquals(julyEntity.getDefinitionCount(), 2);

		Entity septEntity = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				"testRelease2010Sep_testEntity");
		
		assertNotNull(septEntity);
		
		assertTrue(septEntity.getIsAnonymous());
		assertTrue(septEntity.getIsDefined());
		
		assertEquals(septEntity.getDefinitionCount(), 3);
	}

	@Test
	public void testRemoveEntity() throws Exception {
		
		try {
			entityService.resolveEntityByRevision("urn:oid:22.22.0.2", "2.0",
					"midas001", "Automobiles", "testRelease2010Mar_testEntity");
			
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(
					"has been REMOVEd from the lexEVS system in the past."));
		}
	}
	
	@Test
	public void testNullEntity() throws Exception {
		Entity entity = entityService.resolveEntityByRevision(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles", null);

		assertNotNull(entity);
	}
	
	@Test
	public void testEntityBeforeCreation() throws Exception {
		Entity olderEntity = entityService.resolveEntityByDate(
				"urn:oid:22.22.0.2", "2.0", "midas002", "Automobiles",
				new Date(Timestamp.valueOf("2010-07-29 00:00:00").getTime()));

		assertNull(olderEntity);
	}
	
	@Test
	public void testInvalidEntity() throws Exception {
		try {
			entityService.resolveEntityByRevision("urn:oid:22.22.0.2", "2.0",
					"aaaa", "bbbb", "testRelease2010Mar_testEntity");
			
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(
					"doesn't exist in lexEVS."));
		}
	}
}