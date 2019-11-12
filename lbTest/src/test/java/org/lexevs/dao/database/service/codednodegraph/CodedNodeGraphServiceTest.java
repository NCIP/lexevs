package org.lexevs.dao.database.service.codednodegraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodedNodeGraphServiceTest {
	
	VersionableEventCodedNodeGraphService service;

	@Before
	public void setUp() throws Exception {
		service = (VersionableEventCodedNodeGraphService) 
				LexEvsServiceLocator
				.getInstance()
				.getDatabaseServiceManager()
				.getCodedNodeGraphService();
	}

	@Test
	public void test() {
		Integer it = service.validateNodeForAssociation("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b", "subClassOf", "C12434");
		assertNotNull(it);
		assertEquals(35,it.intValue());
	}

}
