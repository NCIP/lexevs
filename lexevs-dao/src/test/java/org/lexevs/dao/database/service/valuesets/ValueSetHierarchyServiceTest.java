package org.lexevs.dao.database.service.valuesets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.locator.LexEvsServiceLocator;

public class ValueSetHierarchyServiceTest {
	ValueSetHierarchyServiceImpl service;
	@Before
	public void setUp() throws Exception {
		service = (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
//		service.init("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl",  "0.1.5", "subClassOf", "Contributing_Source","Publish_Value_Set", "C54443");
		
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
		Map<String, LexEVSTreeItem> map = service.getHierarchyValueSetRoots(
				service.root_code);
		assertTrue(map.size() > 0);
		assertTrue(map.get(ValueSetHierarchyService.ROOT)._assocToChildMap.size() > 0);
		Map<String, List<LexEVSTreeItem>> items  = map.get(ValueSetHierarchyService.ROOT)._assocToChildMap;
		
		items.forEach((y,z) -> z.forEach(x->System.out.println(x._code + ": " + x._text )));
	}
	
	@Test
	public void testBuildTree() throws LBException{
		long startNano = System.currentTimeMillis();
		Map<String, LexEVSTreeItem> items  = service.getSourceValueSetTree(null, "17.02d");
		long endNano = System.currentTimeMillis();
		System.out.println("Performance output milli sec: " + (endNano - startNano));
		LexEVSTreeItem item = items.get(ValueSetHierarchyServiceImpl.ROOT);
		assertTrue(items.size() > 0);
		int tabCounter = 0;
		printTree(item._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A), tabCounter);		
	}
	
//	@Test
//	public void testBuildTree() throws LBException{
//		Map<String, LexEVSTreeItem> items  = service.getSourceValueSetTree("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
//		LexEVSTreeItem item = items.get(ValueSetHierarchyServiceImpl.ROOT);
//		assertTrue(items.size() > 0);
//		int tabCounter = 0;
//		printTree(item._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A), tabCounter);
//		
//	}
	
	private void printTree(List<LexEVSTreeItem> items, int counter){
		if(items == null || items.isEmpty()){return;}
		counter = counter + 5;
		for(LexEVSTreeItem x : items){
			System.out.println(String.format(
					"%1$" + (counter + x._text.length()) + "s",  
					x._text));
			List<LexEVSTreeItem> list = x._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A);
			printTree(list, counter);
		}

	 
	}


}
