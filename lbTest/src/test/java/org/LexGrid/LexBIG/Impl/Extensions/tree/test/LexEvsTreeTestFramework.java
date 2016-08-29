package org.LexGrid.LexBIG.Impl.Extensions.tree.test;



import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;


public class LexEvsTreeTestFramework extends LexBIGServiceTestCase{
	
	public PathToRootTreeServiceImpl pathToRootTreeServiceImpl;
	public LexBIGService lbs;
	public ChildTreeNodeIterator iterator;
	
	@Before
	public void setUp(){
		  lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
		  iterator = pathToRootTreeServiceImpl.getTree(AUTO_SCHEME, 
				  Constructors.createCodingSchemeVersionOrTag(null, AUTO_VERSION), "GM").getCurrentFocus().getChildIterator();
	}

	@Override
	protected String getTestID() {
		return "Tree Extension Tests";
	}

}
