
package org.lexevs.dao.database.access.codednodegraph;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodedNodeGraphGraphingDaoTest {
	CodedNodeGraphService graphService;
	
	@Before
	public void setUp() throws Exception {
		graphService =  LexEvsServiceLocator
				.getInstance()
				.getDatabaseServiceManager()
				.getCodedNodeGraphService();
	}

	@Test
	public void test() {
		List<String> list = graphService
		.getValidAssociationsforTargetandSourceOf(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b", "C12434");
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertEquals("Gene_Product_Expressed_In_Tissue", 
				list.stream()
				.filter(x -> x.equals("Gene_Product_Expressed_In_Tissue"))
				.findAny()
				.get());
		assertEquals("subClassOf", 
				list.stream()
				.filter(x -> x.equals("subClassOf"))
				.findAny()
				.get());
		assertEquals("Procedure_Has_Target_Anatomy", 
				list.stream()
				.filter(x -> x.equals("Procedure_Has_Target_Anatomy"))
				.findAny()
				.get());
		assertFalse(list.stream()
				.anyMatch(x -> x.equals("Conceptual_Part_Of")));
	}

}