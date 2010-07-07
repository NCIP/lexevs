package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Date;

import javax.annotation.Resource;

import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.VersionableEventPickListDefinitionService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class PickListRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource(name = "pickListDefinitionService")
	private VersionableEventPickListDefinitionService pickListDefinitionService;

	@Test
	public void testPickListDefinitionRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/pickListDef_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);

		PickListDefinition plDefinition = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:FA:MicrobialStructureOntology",
						"picklistTestRelease2010Mar", 0);

		assertNotNull(plDefinition);

		PickListDefinition plDefinition1 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Jan", 0);

		assertNotNull(plDefinition1);
		assertTrue("DomesticAutoMakers".equals(plDefinition1
				.getEntityDescription().getContent()));

		PickListDefinition plDefinition2 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Feb", 0);

		assertNotNull(plDefinition2);
		assertTrue("desc-mod".equals(plDefinition2.getEntityDescription()
				.getContent()));

		PickListDefinition plDefinition3 = pickListDefinitionService
				.resolvePickListDefinitionByDate(
						"SRITEST:AUTO:DomesticAutoMakers", new Date(System
								.currentTimeMillis()), 0);

		assertNotNull(plDefinition3);
	}
}
