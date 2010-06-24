package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.dao.database.service.property.VersionableEventPropertyService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class VersionableEventAuthoringServiceTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource
	private VersionableEventPropertyService propertyService;

	@Test
	public void testCodingSchemeRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/csRevision/Automobiles2010_Test_CS.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
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
	
		List<DatabaseServiceEventListener> listeners = propertyService.getDatabaseServiceEventListeners();
		propertyService.getDatabaseServiceEventListeners().clear();
		
		service.loadSystemRelease(systemRelease);
		
		propertyService.setDatabaseServiceEventListeners(listeners);
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

		service.loadSystemRelease(systemRelease);
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

		service.loadSystemRelease(systemRelease);
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

		service.loadSystemRelease(systemRelease);
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

		service.loadSystemRelease(systemRelease);
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

		service.loadSystemRelease(systemRelease);
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

		service.loadSystemRelease(systemRelease);
	}
}
