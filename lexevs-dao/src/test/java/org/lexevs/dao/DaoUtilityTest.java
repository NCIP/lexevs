
package org.lexevs.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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
import org.lexevs.dao.database.utility.DaoUtility.Equality;

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
	
	@Test
	public void testClone() {
		Entity original = new Entity();
		original.addPresentation(new Presentation());
		original.addEntityType("type1");
		
		original.getPresentation()[0].setValue(DaoUtility.createText("some content"));
		
		Entity copy = DaoUtility.deepClone(original);
		
		assertEquals("some content", copy.getPresentation()[0].getValue().getContent());
		
		assertTrue(original.getPresentation()[0].hashCode() != 
			copy.getPresentation()[0].hashCode());
	}
	
	@Test
	public void testGetDelta() {
		Entity e1 = new Entity();
		e1.setEntityCode("e1");
		
		Entity e2 = new Entity();
		e2.setEntityCode("e2");
		
		Entity e3 = new Entity();
		e3.setEntityCode("e3");
		
		Entity e4 = new Entity();
		e4.setEntityCode("e4");
		
		Entity e5 = new Entity();
		e5.setEntityCode("e5");
		
		List<Entity> originalList = Arrays.asList(e1,e2,e3);
		List<Entity> changeList = Arrays.asList(e1,e2,e3,e4,e5);
		
		List<Entity> delta = DaoUtility.getDelta(originalList, changeList, new Equality<Entity>() {

			@Override
			public boolean equals(Entity one, Entity two) {
				return one.getEntityCode().equals(two.getEntityCode());
			}
		});
		
		assertEquals(2,delta.size());
		
		assertEquals("e4",delta.get(0).getEntityCode());
		assertEquals("e5",delta.get(1).getEntityCode());
		
	}
}