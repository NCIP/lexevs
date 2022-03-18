
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class CodingSchemeRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource
	private CodingSchemeService codingSchemeService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/Automobiles2010_Test_CS.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testModifiedCodingScheme() throws Exception {

		CodingScheme cs = codingSchemeService.resolveCodingSchemeByRevision("urn:oid:11.11.0.1", "1.1", "testRelease2010Feb");	

		assertNotNull(cs);
		
		assertEquals(3,cs.getLocalNameCount());
		
		assertTrue(cs.getLocalNameAsReference().contains("11.11.0.1"));
		assertTrue(cs.getLocalNameAsReference().contains("Automobiles"));
		assertTrue(cs.getLocalNameAsReference().contains("fourWheeledVehicle"));
		
		assertEquals(1,cs.getSourceCount());
		assertEquals("temp", cs.getSource(0).getRole());
		assertEquals("milkeyway", cs.getSource(0).getSubRef());
		assertEquals("testAuto", cs.getSource(0).getContent());	
	}
}