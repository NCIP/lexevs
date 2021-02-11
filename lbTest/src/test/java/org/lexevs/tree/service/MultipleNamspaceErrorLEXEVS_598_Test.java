
package org.lexevs.tree.service;

import static org.junit.Assert.*;

import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.test.LexEvsTreeTestBase;
import org.lexevs.tree.utility.PrintUtility;
import org.junit.Before;
import org.junit.Test;

public class MultipleNamspaceErrorLEXEVS_598_Test extends LexEvsTreeTestBase {


	@Test
	public void testMultipleNamespace() {
	
				LexEvsTree tree = 	pathToRootTreeServiceImpl.getTree(
						"Automobiles", 
						null, 
						"DifferentNamespaceConcept", "TestForSameCodeNamespace");
				assertNotNull(tree);
				assertNotNull(tree.getCurrentFocus().getNamespace());
				try{
					PrintUtility.print(tree.getCurrentFocus());
				}
				catch(NullPointerException e){
					fail("Null value in tree node");
				}
	}
	
	@Test
	public void testMultipleNamespaceWrongNamespace() {
	
				try{
				LexEvsTree tree = 	pathToRootTreeServiceImpl.getTree(
						"Automobiles", 
						null, 
						"DifferentNamespaceConcept", "WrongNameSpace");
				fail();
				}catch(RuntimeException e){
					System.out.println(e.getMessage());
				}

	}
	

}