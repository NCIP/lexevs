package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao.Direction;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder.GetChildrenSqlBuilder;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class LexEvsTreeTestBase extends TestCase{
	
	@Resource
	private GetChildrenSqlBuilder getChildrenSqlBuilder;
	
	@Resource
	protected PathToRootTreeServiceImpl pathToRootTreeServiceImpl;
	
	protected ApplicationContext ac;
	
	protected PagingChildNodeIterator iterator;
	protected PagingChildNodeIterator iteratorCountOnly;
	protected PagingChildNodeIterator iteratorNotCountOnly;
	protected LexEvsTreeDao lexEvsTreeDao;
	protected List<String> hierarchyAssocNames;
	{
		hierarchyAssocNames = new ArrayList<String>();
		hierarchyAssocNames.add("hasSubtype");
		hierarchyAssocNames.add("uses");
	}
	
	@Before
	public void setUp() throws Exception{
		ac = new FileSystemXmlApplicationContext("file:src/test/java/org/LexGrid/LexBIG/Impl/Extensions/tree/test/treeServiceContext.xml");
		pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) ac.getBean("pathToRootTreeServiceImpl");
		getChildrenSqlBuilder = (GetChildrenSqlBuilder) ac.getBean("getChildrenSqlBuilder");
//		iterator = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "A0001", null, Direction.FORWARD, hierarchyAssocNames, 5);
		getChildrenSqlBuilder.setExcludeAnonymous(false);
		lexEvsTreeDao = (LexEvsTreeDao) ac.getBean("lexEvsTreeDaoImpl");
		setUpIterator();
	}
	
	
	@After
	public void removeAutomobiles() throws Exception {
		//
	}
	
	@Test
	public void testSetUp() throws Exception {
		//
	}

	
	public boolean containsCode(List<LexEvsTreeNode> list, String code){
		for(LexEvsTreeNode node : list){
			if(node.getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsCode(Iterator<LexEvsTreeNode> itr, String code){
		while(itr.hasNext()){
			if(itr.next().getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	public void setUpIterator(){
		lexEvsTreeDao = (LexEvsTreeDao) ac.getBean("lexEvsTreeDaoImpl");
		iterator = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "A0001", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iterator.setLexEvsTreeDao(lexEvsTreeDao);
//		iterator.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		LexEvsTreeNode iteratorParentNode = lexEvsTreeDao.getNode("Automobiles", null, "A0001", null);
		iteratorParentNode.setChildIterator(iterator, true);
		iterator.initIterator(iteratorParentNode, true);
		
		iteratorCountOnly = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "005", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iteratorCountOnly.setLexEvsTreeDao(lexEvsTreeDao);
//		iteratorCountOnly.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		iteratorCountOnly.initIterator(lexEvsTreeDao.getNode("Automobiles", null, "005", null), true);
	
		iteratorNotCountOnly = new PagingChildNodeIterator(lexEvsTreeDao, "Automobiles", null, "005", null, Direction.FORWARD, hierarchyAssocNames, 5);
		iteratorNotCountOnly.setLexEvsTreeDao(lexEvsTreeDao);
//		iteratorNotCountOnly.setIteratorFactory(new ChildTreeNodeIteratorFactory(lexEvsTreeDao, "Automobiles", null, Direction.FORWARD, hierarchyAssocNames, 5));
		iteratorNotCountOnly.initIterator(lexEvsTreeDao.getNode("Automobiles", null, "005", null), true);
	}
	
	public boolean childrenContainCode(LexEvsTreeNode node, String code){
		return containsCode(node.getChildIterator(), code);
	}
	
	public boolean parentsContainCode(LexEvsTreeNode node, String code){
		return containsCode(node.getPathToRootParents(), code);
	}
	
	public LexEvsTreeNode getNode(List<LexEvsTreeNode> list, String code){
		for(LexEvsTreeNode node : list){
			if(node.getCode().equals(code)){
				return node;
			}
		}
		throw new RuntimeException("Node not found.");
	}
}
