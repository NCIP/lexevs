package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class TestGetTree extends TestCase{
	
	public PathToRootTreeServiceImpl pathToRootTreeServiceImpl;
	public LexBIGService lbs;
	public ChildTreeNodeIterator iterator;


	@Test
	public void testGetTreeWithMultipleHierarchyAsscNames() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
		  iterator = pathToRootTreeServiceImpl.getTree("urn:oid:11.11.0.1", 
				  null, "005").getCurrentFocus().getChildIterator();
		  assertNotNull(iterator.next());
	}

	@Test
	public void testGetTreeWithMultipleHierarchyAsscDirectionNames() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
		  iterator = pathToRootTreeServiceImpl.getTree("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				  Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"), "Patient").getCurrentFocus().getChildIterator();
		  assertNotNull(iterator.next());
	}
	
	@Test
	public void testGetTreeWithMultipleHierarchyAsscNamesPatient() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
	        LexEvsTree tree = pathToRootTreeServiceImpl.getTree(
	        		"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
	                Constructors.createCodingSchemeVersionOrTagFromVersion("0.2.0"), "C12434");
	        LexEvsTreeNode node = tree.getCodeMap().get("C12434");
	       assertEquals(ExpandableStatus.IS_NOT_EXPANDABLE,  node.getExpandableStatus());
	}
}
