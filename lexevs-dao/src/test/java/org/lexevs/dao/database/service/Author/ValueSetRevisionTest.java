package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.VersionableEventValueSetDefinitionService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class ValueSetRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource(name = "valueSetDefinitionService")
	private VersionableEventValueSetDefinitionService valueSetDefinitionService;

	@Test
	public void testValueSetRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/valueSetDef_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);

		ValueSetDefinition valueSetDef = valueSetDefinitionService
				.resolveValueSetDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"vdTestRelease2010Mar");

		assertNotNull(valueSetDef);
		assertNull(valueSetDef.getEntityDescription());
		assertTrue("Autos".equals(valueSetDef.getConceptDomain()));

		ValueSetDefinition valueSetDef1 = valueSetDefinitionService
				.resolveValueSetDefinitionByRevision(
						"SRITEST:AUTO:Automobiles", "vdTestRelease2010Jan");

		assertNotNull(valueSetDef1);
		assertNull(valueSetDef1.getEntityDescription());

		assertTrue(valueSetDef1.getSourceCount() == 2);
		assertTrue(valueSetDef1.getRepresentsRealmOrContextCount() == 2);
		
		ValueSetDefinition valueSetDef2 = valueSetDefinitionService
				.resolveValueSetDefinitionByRevision(
						"SRITEST:AUTO:Automobiles", "vdTestRelease2010Feb");

		assertNotNull(valueSetDef2);
		assertNotNull(valueSetDef2.getEntityDescription());
		assertTrue("test MODIFY value set definition".equals(valueSetDef2
				.getEntityDescription().getContent()));
		assertTrue(valueSetDef2.getSourceCount() == 1);
		assertTrue(valueSetDef2.getRepresentsRealmOrContextCount() == 1);
	}
}
