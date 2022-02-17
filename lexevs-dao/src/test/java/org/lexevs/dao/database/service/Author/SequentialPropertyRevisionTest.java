
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.util.TestUtils;

public class SequentialPropertyRevisionTest extends BaseRevisionTest {

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

		service.loadSystemRelease(systemRelease, true);
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
		
		Property prop = TestUtils.getPropertyWithId(entity, "2");
		assertEquals(0,prop.getPropertyQualifierCount());
		
		
		assertTrue(TestUtils.entityContainsPropertyWithValue(entity,"revision2Value"));
		assertTrue(TestUtils.entityContainsPropertyWithValue(entity,"revision3Value"));
	}
	
	@Test
	public void testGetRevision4Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"4");

		assertNotNull(entity);
		
		assertEquals(2,entity.getPropertyCount());
		
		assertTrue(TestUtils.entityContainsPropertyWithValue(entity,"revision2Value"));
		assertTrue(TestUtils.entityContainsPropertyWithValue(entity,"revision3Value"));
	}
	
	@Test
	public void testGetRevision5Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"5");

		assertNotNull(entity);
		
		assertEquals(3,entity.getPropertyCount());

		assertTrue(TestUtils.entityContainsPropertyWithId(entity, "1"));
		assertTrue(TestUtils.entityContainsPropertyWithId(entity,"2"));
		assertTrue(TestUtils.entityContainsPropertyWithId(entity,"5"));
	}
	
	@Test
	public void testGetRevision6Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"6");

		assertNotNull(entity);
		
		Property prop = TestUtils.getPropertyWithId(entity, "2");
		
		assertEquals(1,prop.getPropertyQualifierCount());
		assertEquals("TestQual",prop.getPropertyQualifier(0).getPropertyQualifierName());
		assertEquals("TestQualValueR6",prop.getPropertyQualifier(0).getValue().getContent());
		
		assertEquals(3,entity.getPropertyCount());	
	}
	
	@Test
	public void testGetRevision7Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"7");

		assertNotNull(entity);
		
		Property prop = TestUtils.getPropertyWithId(entity, "2");
		
		assertEquals(2,prop.getPropertyQualifierCount());
	}
	
	@Test
	public void testGetRevision8Entity() throws Exception {

		Entity entity = entityService.resolveEntityByRevision(
				"testUri", "1.0", "testEntity", "test",
				"8");

		assertNotNull(entity);
		
		Property prop = TestUtils.getPropertyWithId(entity, "2");
		
		assertEquals(3,prop.getPropertyQualifierCount());
	}
	
	
}