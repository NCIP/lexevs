
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.VersionableEventValueSetDefinitionService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class ValueSetRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource(name = "valueSetDefinitionService")
	private VersionableEventValueSetDefinitionService valueSetDefinitionService;

	@Before
	public void loadValueSetRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/valueSetDef_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, null);
	}

	@Test
	public void testVSDefNeverRevised() throws Exception {
		ValueSetDefinition valueSetDef = valueSetDefinitionService
				.getValueSetDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"vdTestRelease2010Mar");

		assertNotNull(valueSetDef);
		assertNull(valueSetDef.getEntityDescription());
		assertTrue("Autos".equals(valueSetDef.getConceptDomain()));
	}

	@Test
	public void testVSDefNotRevisedOnGivenRevision() throws Exception {
		ValueSetDefinition valueSetDef1 = valueSetDefinitionService
				.getValueSetDefinitionByRevision("SRITEST:AUTO:Automobiles",
						"vdTestRelease2010Jan");

		assertNotNull(valueSetDef1);
		assertNull(valueSetDef1.getEntityDescription());

		assertTrue(valueSetDef1.getSourceCount() == 2);
		assertTrue(valueSetDef1.getRepresentsRealmOrContextCount() == 2);
	}

	@Test
	public void testVSDefModify() throws Exception {
		ValueSetDefinition valueSetDef2 = valueSetDefinitionService
				.getValueSetDefinitionByRevision("SRITEST:AUTO:Automobiles",
						"vdTestRelease2010Feb");

		assertNotNull(valueSetDef2);
		assertNotNull(valueSetDef2.getEntityDescription());
		assertTrue("test MODIFY value set definition".equals(valueSetDef2
				.getEntityDescription().getContent()));
		assertTrue(valueSetDef2.getSourceCount() == 1);
		assertTrue(valueSetDef2.getRepresentsRealmOrContextCount() == 1);
	}

	@Test
	public void testDummyVSDef() throws Exception {
		/* When Valueset doesn't exist. */
		try {
			valueSetDefinitionService.getValueSetDefinitionByRevision("aaaa",
					"vdTestRelease2010Feb");
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("doesn't exist in lexEVS."));
		}
	}

	@Test
	public void testRemovedVSDef() throws Exception {
		/* When valueset is removed. */
		try {
			valueSetDefinitionService.getValueSetDefinitionByRevision(
					"SRITEST:AUTO:EveryThing", "vdTestRelease2010Feb");
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(
					"has been REMOVEd from the lexEVS system in the past."));
		}
	}

	@Test
	public void testLaterThanLatestRevision() throws Exception {
		/* When revision is later than latest revision of valueset definition */
		ValueSetDefinition valueSetDef5 = valueSetDefinitionService
				.getValueSetDefinitionByRevision(
						"SRITEST:AUTO:AutomobilesNoName",
						"vdTestRelease2010Apr");

		assertNotNull(valueSetDef5);
		assertEquals(valueSetDef5.getStatus(), "inactive");
	}

	@Test
	public void testDependentChanges() throws Exception {
		/* When valueset definition has dependent changes. */
		ValueSetDefinition valueSetDef6 = valueSetDefinitionService
				.getValueSetDefinitionByRevision("SRITEST:AUTO:Automobiles",
						"vdTestRelease2010May");

		assertNotNull(valueSetDef6);
		assertEquals(valueSetDef6.getEntityDescription().getContent(),
				"test MODIFY value set definition");
		assertEquals(valueSetDef6.getDefinitionEntryCount(), 2);

		ValueSetDefinition valueSetDef7 = valueSetDefinitionService
				.getValueSetDefinitionByRevision("SRITEST:AUTO:Automobiles",
						"vdTestRelease2010June");

		assertNotNull(valueSetDef7);
		assertEquals(valueSetDef7.getEntityDescription().getContent(),
				"test MODIFY value set definition");
		assertEquals(valueSetDef7.getDefinitionEntryCount(), 3);
	}

	@Test
	public void testVSDefRevisionByDate() throws Exception {
		/* test get valueset definition by date */
		ValueSetDefinition valueSetDef8 = valueSetDefinitionService
				.getValueSetDefinitionByDate("SRITEST:AUTO:DomesticAutoMakers",
						new java.sql.Date(Timestamp.valueOf(
								"2010-07-29 00:00:00").getTime()));
		assertNull(valueSetDef8);
	}
	
	@Test
	public void testNullRevision() throws Exception {
		/* test null revisionId */
		ValueSetDefinition valueSetDef9 = valueSetDefinitionService
				.getValueSetDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers", null);
		assertNotNull(valueSetDef9);
	}
}