package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.test.LexEvsTreeTestBase;
import org.LexGrid.LexBIG.Impl.Extensions.tree.utility.PrintUtility;
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
