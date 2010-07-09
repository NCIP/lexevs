package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.relations.Relations;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.relation.VersionableEventRelationService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialRelationsRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource
	private VersionableEventRelationService relationsService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/relationsRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}
	
	@Test
	public void testGetRevision1Entity() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"1");

		assertNotNull(relations);
		
		assertEquals("Revision 1 Description", relations.getEntityDescription().getContent());
		
		assertEquals("1", relations.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision2Entity() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"2");

		assertNotNull(relations);
		
		assertEquals("Revision 2 Description", relations.getEntityDescription().getContent());
		
		assertEquals("2", relations.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision3Entity() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"3");

		assertNotNull(relations);
		
		assertEquals("Revision 2 Description", relations.getEntityDescription().getContent());
		
		assertEquals("3", relations.getEntryState().getContainingRevision());
	}
	
	@Test
	public void testGetRevision4Entity() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"4");

		assertNotNull(relations);
		
		assertEquals("Revision 4 Description", relations.getEntityDescription().getContent());
		
		assertEquals("4", relations.getEntryState().getContainingRevision());
	}
}
