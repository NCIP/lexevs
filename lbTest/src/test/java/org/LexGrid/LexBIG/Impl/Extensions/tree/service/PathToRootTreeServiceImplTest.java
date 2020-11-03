package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.test.LexEvsTreeTestFramework;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class PathToRootTreeServiceImplTest extends LexEvsTreeTestFramework{

	@Test
	public void testGetSubConcepts(){
		LexEvsTreeNode fiveNode = 
			pathToRootTreeServiceImpl.
				getSubConcepts(
						"Automobiles", 
						null, 
						"005");
		
		assertTrue(fiveNode.getCode().equals("005"));
		
		int count = 0;
		
		Iterator<LexEvsTreeNode> itr = fiveNode.getChildIterator();
		
		while(itr.hasNext()){
			itr.next();
			count++;
		}
		assertTrue("Size: " + count, count == 3);
	}
	
	@Test
	public void testWalkTreeGetSubConcepts(){
		LexEvsTreeNode fiveNode = 
			pathToRootTreeServiceImpl.
				getSubConcepts(
						"Automobiles", 
						null, 
						"005");
		
		assertTrue(fiveNode.getCode().equals("005"));
		
		Iterator<LexEvsTreeNode> itr = fiveNode.getChildIterator();
		
		List<String> children = new ArrayList<String>();
		
		while(itr.hasNext()){
			children.add(itr.next().getCode());
		}
		
		assertTrue(children.contains("Ford"));
		assertTrue(children.contains("GM"));
		assertTrue(children.contains("A"));
	}
	
	@Test
	public void testSubConceptsMultiHierarchy(){
		LexEvsTreeNode fiveNode = 
			pathToRootTreeServiceImpl.
			getSubConcepts(
					OWL2_SNIPPET_INDIVIDUAL_URN, 
					Constructors.createCodingSchemeVersionOrTagFromVersion(
							OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), "BFO_0000001", "obo", null);
		
		assertTrue(fiveNode.getCode().equals("BFO_0000001"));
		assertTrue(fiveNode.getExpandableStatus().name().equals("IS_EXPANDABLE"));

	}
}
