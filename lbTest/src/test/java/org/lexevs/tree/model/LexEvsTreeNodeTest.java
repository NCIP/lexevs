
package org.lexevs.tree.model;

import static org.junit.Assert.assertTrue;

import org.lexevs.tree.test.LexEvsTreeTestBase;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class LexEvsTreeNodeTest extends LexEvsTreeTestBase {
	
	private LexEvsTreeNode node1;

	public void buildTree(){
		
		node1 = 
			pathToRootTreeServiceImpl.
				getTree(
						"Automobiles", 
						null, 
						"Chevy").getCurrentFocus();
	}


	@Test
	public void testTreeNodeGetCode(){
		this.buildTree();
		
		assertTrue(node1.getCode().equals("Chevy"));
	}
	
	@Test
	public void testTreeNodeGetNamespace(){
		this.buildTree();
		
		assertTrue(node1.getNamespace().equals("Automobiles"));
	}
	
	@Test
	public void testTreeNodeGetEntityDescription(){
		this.buildTree();
		
		assertTrue(node1.getEntityDescription().equals("Chevrolet"));
	}
}