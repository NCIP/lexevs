package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
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

}
