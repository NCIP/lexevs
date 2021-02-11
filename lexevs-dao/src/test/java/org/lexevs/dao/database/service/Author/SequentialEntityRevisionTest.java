
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialEntityRevisionTest extends BaseRevisionTest {

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
		"src/test/resources/csRevision/entityRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testGetRevision1Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"1");

		assertNotNull(entity);
		
		assertEquals("Revision 1 Description", entity.getEntityDescription().getContent());
		
		assertEquals("1", entity.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision2Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"2");

		assertNotNull(entity);
		
		assertEquals("Revision 2 Description", entity.getEntityDescription().getContent());
		
		assertEquals("2", entity.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision3Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"3");

		assertNotNull(entity);
		
		assertEquals("Revision 2 Description", entity.getEntityDescription().getContent());
		
		assertEquals("2", entity.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision4Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"4");

		assertNotNull(entity);
		
		assertEquals("Revision 4 Description", entity.getEntityDescription().getContent());
		
		assertEquals("4", entity.getEntryState().getContainingRevision());
	}
}