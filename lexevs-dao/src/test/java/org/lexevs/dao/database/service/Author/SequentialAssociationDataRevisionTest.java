
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.association.VersionableEventAssociationDataService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialAssociationDataRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource
	private VersionableEventAssociationDataService associationDataService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/associationDataRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testGetRevision1AssociationData() throws Exception {

		AssociationData data = associationDataService.resolveAssociationDataByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "1");
		
		assertNotNull(data);

		assertEquals("revision1Value", data.getAssociationDataText().getContent());
	}
	
	@Test
	public void testGetRevision2AssociationData() throws Exception {

		AssociationData data = associationDataService.resolveAssociationDataByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "2");

		assertEquals("revision2Value", data.getAssociationDataText().getContent());
	}
	
	@Test
	public void testGetRevision3AssociationData() throws Exception {

		AssociationData data1 = associationDataService.resolveAssociationDataByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "3");

		assertEquals("revision2Value", data1.getAssociationDataText().getContent());
		
		AssociationData data2 = associationDataService.resolveAssociationDataByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a3", "3");

		assertEquals("revision3Value", data2.getAssociationDataText().getContent());
	}
}