
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class VersionableEventAuthoringServiceTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void enableListeners() {
		extensionLoadingListenerRegistry.setEnableListeners(true);
	}

	@Test
	public void testCodingSchemeRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_CS.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testPropertyRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_Property.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));
		
		service.loadSystemRelease(systemRelease, true);
	}
	
		@Test
	public void testRelationRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_Relation.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}

	@Test
	public void testAssocTargetRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_AssociationTarget.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testAssocDataRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_AssociationData.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testVSDefinitionEntryRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/definitionEntry_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, null);
	}
	
	@Test
	public void testPLEntryRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/plEntry_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, null);
	}
	
	@Test
	public void testVSPropertyRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/vsplProperty_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, null);
	}
}