package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder.GetChildrenSqlBuilder;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/treeServiceContext.xml"})
public class LexEvsTreeTestBase {
	
	@Resource
	private GetChildrenSqlBuilder getChildrenSqlBuilder;
	
	@Resource
	protected PathToRootTreeServiceImpl pathToRootTreeServiceImpl;
	
	@Before
	public void setUp() throws Exception{
		
		getChildrenSqlBuilder.setExcludeAnonymous(false);
	}
	
//	//Users may need to provide an absolute path definition in some OS environments
//	@BeforeClass
//	public static void setSystemProp() throws Exception {
//		System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");	
//	}
	
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
