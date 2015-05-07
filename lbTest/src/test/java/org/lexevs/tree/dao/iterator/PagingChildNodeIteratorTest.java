package org.lexevs.tree.dao.iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lexevs.tree.dao.LexEvsTreeDao;
import org.lexevs.tree.dao.LexEvsTreeDao.Direction;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIteratorFactory;
import org.lexevs.tree.dao.iterator.PagingChildNodeIterator;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.test.LexEvsTreeTestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PagingChildNodeIteratorTest extends LexEvsTreeTestBase{

//	private PagingChildNodeIterator iterator;
//	private PagingChildNodeIterator iteratorCountOnly;
//	private PagingChildNodeIterator iteratorNotCountOnly;
//	
//	@Autowired
//	@Qualifier("proxy")
//	private LexEvsTreeDao lexEvsTreeDao;
//	
//	private List<String> hierarchyAssocNames;
//	{
//		hierarchyAssocNames = new ArrayList<String>();
//		hierarchyAssocNames.add("hasSubtype");
//		hierarchyAssocNames.add("uses");
//	}
	
	@Before
	public void setUpIterator(){
		lexEvsTreeDao = (LexEvsTreeDao) ac.getBean("lexEvsTreeDaoImpl");
		iterator = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "A0001", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iterator.setLexEvsTreeDao(lexEvsTreeDao);
		iterator.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		LexEvsTreeNode iteratorParentNode = lexEvsTreeDao.getNode("Automobiles", null, "A0001", null);
		iteratorParentNode.setChildIterator(iterator, true);
		iterator.initIterator(iteratorParentNode, true);
		
		iteratorCountOnly = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "005", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iteratorCountOnly.setLexEvsTreeDao(lexEvsTreeDao);
		iteratorCountOnly.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		iteratorCountOnly.initIterator(lexEvsTreeDao.getNode("Automobiles", null, "005", null), true);
	
		iteratorNotCountOnly = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "005", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iteratorNotCountOnly.setLexEvsTreeDao(lexEvsTreeDao);
		iteratorNotCountOnly.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		iteratorNotCountOnly.initIterator(lexEvsTreeDao.getNode("Automobiles", null, "005", null), true);
	}

	@Test
	public void testHasNext(){
		iterator.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		assertTrue(iterator.hasNext());
	}
	
	@Test
	public void testIterateThrough(){
		iterator.setPageSize(1000);
		
		int counter = 0;
		while(iterator.hasNext()){
			iterator.next();
			counter++;
		}
		assertTrue("Size: " + counter, counter == 5);
	}
	
	@Test
	public void testIterateThroughLimitOne(){
		iterator.setPageSize(1);
		
		int counter = 0;
		while(iterator.hasNext()){
			iterator.next();
			counter++;
		}
		assertTrue("Size: " + counter, counter == 5);
	}
	
	@Test
	public void testIterateThroughLimitTwo(){
		iterator.setPageSize(2);
		
		int counter = 0;
		while(iterator.hasNext()){
			iterator.next();
			counter++;
		}
		assertTrue("Size: " + counter, counter == 5);
	}
	
	@Test
	public void testIterateWithPopulatedCache(){
		iterator.setPageSize(1000);
		
		int counter = 0;
		while(iterator.hasNext()){
			iterator.next();
			counter++;
		}
		assertTrue("Size: " + counter, counter == 5);
	}

	@Test
	public void testIteratedChild() throws Exception{
		Thread.sleep(1000);
		iteratorCountOnly.setPageSize(1000);
		
		Map<String,LexEvsTreeNode> nodes = getTreeNodes(getTreeNodes(iteratorNotCountOnly).get("GM").getChildIterator());

		LexEvsTreeNode twoLevelsChild73 = nodes.get("73");
		LexEvsTreeNode twoLevelsChildChevy = nodes.get("Chevy");
		
		assertTrue(twoLevelsChild73.getCode().equals("73"));
		assertTrue(twoLevelsChild73.getExpandableStatus().equals(ExpandableStatus.IS_NOT_EXPANDABLE));
		
		assertTrue(twoLevelsChildChevy.getCode().equals("Chevy"));
		assertTrue(twoLevelsChildChevy.getExpandableStatus().equals(ExpandableStatus.IS_NOT_EXPANDABLE));
	}
	
	@Test
	public void testIteratedChildNotCountOnly(){
		iteratorNotCountOnly.setPageSize(1000);
		
		Map<String,LexEvsTreeNode> nodes = getTreeNodes(getTreeNodes(iteratorNotCountOnly).get("GM").getChildIterator());
		
		LexEvsTreeNode twoLevelsChild73 = nodes.get("73");
		LexEvsTreeNode twoLevelsChildChevy = nodes.get("Chevy");
		
		assertTrue(twoLevelsChild73.getCode().equals("73"));
		assertTrue(twoLevelsChild73.getExpandableStatus().equals(ExpandableStatus.IS_NOT_EXPANDABLE));
		
		assertTrue(twoLevelsChildChevy.getCode().equals("Chevy"));
		assertTrue(twoLevelsChildChevy.getExpandableStatus().equals(ExpandableStatus.IS_NOT_EXPANDABLE));

		assertFalse(iteratorNotCountOnly.hasNext());
	}
	
	private Map<String,LexEvsTreeNode> getTreeNodes(Iterator<LexEvsTreeNode> itr){
		Map<String,LexEvsTreeNode> returnMap = new  HashMap<String,LexEvsTreeNode>();
		
		while(itr.hasNext()){
			LexEvsTreeNode node = itr.next();
			returnMap.put(node.getCode(), node);
		}
		
		return returnMap;
	}

	@Test
	public void testIteratedChildParent(){
		iterator.setPageSize(1000);
		
		LexEvsTreeNode node = iterator.next();

		LexEvsTreeNode parent = node.getPathToRootParents().get(0);
		
		assertTrue(parent.getCode().equals("A0001"));
		
		assertTrue(parent.getChildIterator() != null);
		
		assertTrue(parent.getChildIterator().hasNext());
	}
	
}
