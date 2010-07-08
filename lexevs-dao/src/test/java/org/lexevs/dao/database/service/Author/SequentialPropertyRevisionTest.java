package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialPropertyRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource
	private VersionableEventEntityService entityService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/propertyRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}
	
	@Test
	public void testFirstEntity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				null);

		assertNotNull(entity);
		
		assertEquals(0,entity.getPropertyCount());
	}
	
	@Test
	public void testGetRevision1Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"1");
		
		assertNotNull(entity);
		
		assertEquals(1,entity.getPropertyCount());
		
		assertEquals("revision1Value",entity.getProperty(0).getValue().getContent());
	}
	
	@Test
	public void testGetRevision2Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"2");
		
		assertNotNull(entity);
		
		assertEquals(1,entity.getPropertyCount());
		
		assertEquals("revision2Value",entity.getProperty(0).getValue().getContent());
	}
	
	@Test
	public void testGetRevision3Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"3");

		assertNotNull(entity);
		
		assertEquals(2,entity.getPropertyCount());
		
		List<String> propertyValues = new ArrayList<String>();
		propertyValues.add(entity.getProperty(0).getValue().getContent());
		propertyValues.add(entity.getProperty(1).getValue().getContent());
		
		assertTrue(propertyValues.contains("revision2Value"));
		assertTrue(propertyValues.contains("revision3Value"));
	}
	
	@Test
	public void testGetRevision4Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"4");

		assertNotNull(entity);
		
		assertEquals(1,entity.getPropertyCount());
		
		assertEquals("revision3Value",entity.getProperty(0).getValue().getContent());
	}
	
	@Test
	public void testGetRevision5Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"5");

		assertNotNull(entity);
		
		assertEquals(2,entity.getPropertyCount());
		
		List<String> propertyValues = new ArrayList<String>();
		propertyValues.add(entity.getProperty(0).getValue().getContent());
		propertyValues.add(entity.getProperty(1).getValue().getContent());
		
		assertTrue(propertyValues.contains("revision3Value"));
		assertTrue(propertyValues.contains("revision5Value"));
	}
}
