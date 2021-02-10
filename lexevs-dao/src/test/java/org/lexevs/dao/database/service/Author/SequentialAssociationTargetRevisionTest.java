
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.association.VersionableEventAssociationTargetService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialAssociationTargetRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource
	private VersionableEventAssociationTargetService associationTargetService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/associationTargetRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testGetRevision1AssociationSource() throws Exception {

		AssociationTarget target = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "1");

		assertEquals("rev1TargetCode", target.getTargetEntityCode());
		assertEquals("rev1TargetCodeNamespace", target.getTargetEntityCodeNamespace());
	}
	
	@Test
	public void testGetRevision2AssociationSource() throws Exception {

		AssociationTarget target = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "2");

		assertEquals("rev2TargetCode", target.getTargetEntityCode());
		assertEquals("rev2TargetCodeNamespace", target.getTargetEntityCodeNamespace());
	}
	
	@Test
	public void testGetRevision3AssociationSource() throws Exception {

		AssociationTarget target1 = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "3");

		assertEquals("rev2TargetCode", target1.getTargetEntityCode());
		assertEquals("rev2TargetCodeNamespace", target1.getTargetEntityCodeNamespace());
		
		AssociationTarget target2 = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a3", "3");

		assertEquals("rev3TargetCode", target2.getTargetEntityCode());
		assertEquals("rev3TargetCodeNamespace", target2.getTargetEntityCodeNamespace());
	}
}