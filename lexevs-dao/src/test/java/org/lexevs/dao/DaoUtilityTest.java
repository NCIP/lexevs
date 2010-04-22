package org.lexevs.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.URIMap;
import org.junit.Test;
import org.lexevs.dao.database.utility.DaoUtility;

public class DaoUtilityTest {
	
	@Test
	public void testGetAllURIMaps() {
		Mappings mappings = new Mappings();
		mappings.addSupportedAssociation(new SupportedAssociation());
		mappings.addSupportedCodingScheme(new SupportedCodingScheme());

		List<URIMap> list = DaoUtility.getAllURIMappings(mappings);

		assertEquals(2, list.size());
	}
	
	@Test
	public void updateBeanTest() {
		Source original = new Source();
		original.setRole("originalRole");
		original.setContent("content");
		original.setSubRef("subref");
		
		Source updates = new Source();
		updates.setRole("changedRole");
		
		DaoUtility.updateBean(updates, original);
		
		assertEquals("changedRole", original.getRole());
		assertEquals("Known failure - fix pending.", "content", original.getContent());
		assertEquals("subref", original.getSubRef());
	}
	
	@Test
	public void updateBeanTestWithEntity() {
		Entity original = new Entity();
		original.addPresentation(new Presentation());
		original.addEntityType("type1");
		
		Entity updates = new Entity();
		updates.setEntityCode("code");

		DaoUtility.updateBean(updates, original);
		
		assertEquals(1, original.getEntityTypeCount());
		assertEquals("code", original.getEntityCode());
		assertEquals(1, original.getPresentationCount());
	}
}
