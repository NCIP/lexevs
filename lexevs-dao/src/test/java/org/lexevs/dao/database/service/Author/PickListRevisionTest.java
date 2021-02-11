
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.VersionableEventPickListDefinitionService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class PickListRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource(name = "pickListDefinitionService")
	private VersionableEventPickListDefinitionService pickListDefinitionService;

	@Before
	public void testPickListDefinitionRevisions() throws Exception {

		URI sourceURI = new File(
				"src/test/resources/vsplRevision/pickListDef_test.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, null);
	}

	@Test
	public void testNeverRevisedPickListDefinition() throws Exception {
		PickListDefinition plDefinition = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:FA:MicrobialStructureOntology",
						"picklistTestRelease2010Mar", 0);

		assertNotNull(plDefinition);
		assertTrue(plDefinition.getOwner().equals(
				"Owner for FA:MicrobialStructireOntology"));
		assertEquals(plDefinition.getPickListEntryNodeCount(), 9);
	}

	@Test
	public void testInvalidPickListDefinition() throws Exception {

		try {
			pickListDefinitionService.resolvePickListDefinitionByRevision(
					"aaa", "picklistTestRelease2010Mar", 0);
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("doesn't exist in lexEVS."));
		}
	}

	@Test
	public void testPickListDefinitionNotRevisedInGivenRevision()
			throws Exception {
		PickListDefinition plDefinition1 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Jan", 0);

		assertNotNull(plDefinition1);
		assertEquals("DomesticAutoMakers", plDefinition1
				.getEntityDescription().getContent());
	}

	@Test
	public void testPickListDefinitionRevision() throws Exception {
		PickListDefinition plDefinition2 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Feb", 0);

		assertNotNull(plDefinition2);
		assertTrue("desc-mod".equals(plDefinition2.getEntityDescription()
				.getContent()));
	}

	@Test
	public void testLaterThanLatestRevisionForPickListDefinition()
			throws Exception {
		PickListDefinition plDefinition2 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:FA:hyphaLeafOnly",
						"picklistTestRelease2010Apr", 0);

		assertEquals(plDefinition2.getEffectiveDate(), new java.sql.Date(
				Timestamp.valueOf("2010-01-01 01:20:30").getTime()));
	}

	@Test
	public void testRemovedPickListDefinition() throws Exception {
		try {
			pickListDefinitionService.resolvePickListDefinitionByRevision(
					"SRITEST:AUTO:DomasticLeafOnly",
					"picklistTestRelease2010Mar", 0);
			fail("Exception expected, didn't occur.");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(
					"has been REMOVEd from the lexEVS system in the past."));
		}
	}

	@Test
	public void testOlderThanOldestRevisionForPickListDefinition()
			throws Exception {

		PickListDefinition plDefinition = pickListDefinitionService
				.resolvePickListDefinitionByDate(
						"SRITEST:FA:MicrobialStructureOntologyMinusMCell",
						new java.sql.Date(Timestamp.valueOf(
								"2010-07-29 00:00:00").getTime()), 0);

		assertNull(plDefinition);
	}

	@Test
	public void testDependentChanges() throws Exception {

		PickListDefinition plDefinition = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Apr", 0);

		assertEquals(plDefinition.getPickListEntryNodeCount(), 4);

		PickListDefinition plDefinition1 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010May", 0);
		
		assertNotNull(plDefinition1);
		assertEquals(plDefinition1.getPickListEntryNodeCount(), 5);

		PickListDefinition plDefinition2 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010Jun", 0);

		assertNotNull(plDefinition2);
		assertEquals(plDefinition2.getPickListEntryNodeCount(), 6);
		assertTrue(plDefinition2.getDefaultEntityCodeNamespace().equals(
				"Automobiles-mod"));

		PickListDefinition plDefinition3 = pickListDefinitionService
				.resolvePickListDefinitionByRevision(
						"SRITEST:AUTO:DomesticAutoMakers",
						"picklistTestRelease2010July", 0);

		assertNotNull(plDefinition3);
		assertEquals(plDefinition3.getPickListEntryNodeCount(), 6);
		assertTrue(plDefinition3.getDefaultEntityCodeNamespace().equals(
				"sampleCodingScheme"));
	}
}