package org.lexevs.tree.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.test.LexEvsTreeTestBase;
import org.junit.Test;

public class PathToRootTreeServiceImplTest extends LexEvsTreeTestBase {

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
}
