package org.lexevs.dao.database.service.valuesets;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.locator.LexEvsServiceLocator;

import gov.nih.nci.evs.browser.utils.TreeItem;

public class ValueSetHierarchyServiceTest {
	ValueSetHierarchyServiceImpl service;
	@Before
	public void setUp() throws Exception {
		service = (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
		service.init();
	}

	@Test
	public void testGetSchemeUid() {
		String uid = service.getSchemeUid("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertNotNull(uid);
		String uid2 = service.getSchemeUid("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", null);
		assertNotNull(uid2);
	}
	
	@Test
	public void testGetPredicateUid() {
		service.schemeUID = service.getSchemeUid("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		service.association = "subClassOf";
		String uid = service.getPredicateUid();
		assertNotNull(uid);
	}
	
	@Test
	public void testNodeReduction() {
		List<VSHierarchyNode> nodes = new ArrayList<VSHierarchyNode>();

		VSHierarchyNode node = new VSHierarchyNode();
		VSHierarchyNode node2 = new VSHierarchyNode();
		VSHierarchyNode node3 = new VSHierarchyNode();
		VSHierarchyNode node4 = new VSHierarchyNode();
		VSHierarchyNode node5 = new VSHierarchyNode();

		node.setDescription("apples");
		node2.setDescription("oranges");
		node3.setDescription("apples");
		node4.setDescription("oranges");
		node5.setDescription("banana");

		node.setEntityCode("C1");
		node2.setEntityCode("C2");
		node3.setEntityCode("C1");
		node4.setEntityCode("C2");
		node5.setEntityCode("C3");

		node.setSource("CDISC");
		node2.setSource("FDA");

		nodes.add(node);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		List<VSHierarchyNode> complete = new ValueSetHierarchyServiceImpl().collectReducedNodes(nodes);

		complete.stream()
				.forEach(x -> System.out.println(
						x.getDescription() + ": " + x.getEntityCode() + ": " + x.getSource()));
		assertTrue(complete.contains(node));
		assertTrue(complete.contains(node2));
		assertTrue(complete.contains(node5));
	}
	
	@Test 
	public void testGetHierarchyRoots() throws LBException{
		Map<String, TreeItem> map = service.getHierarchyValueSetRoots(service.scheme, "17.02d", service.association, service.sourceDesignation, service.publishName, service.root_code);
		assertTrue(map.size() > 0);
		assertTrue(map.get(service.ROOT)._assocToChildMap.size() > 0);
		Map<String, List<TreeItem>> items  = map.get(service.ROOT)._assocToChildMap;
		
		items.forEach((y,z) -> z.forEach(x->System.out.println(x._code + ": " + x._text )));
	}


}
