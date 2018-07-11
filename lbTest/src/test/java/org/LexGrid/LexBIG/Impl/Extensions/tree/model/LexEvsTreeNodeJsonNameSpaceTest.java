package org.LexGrid.LexBIG.Impl.Extensions.tree.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.Extensions.tree.test.LexEvsTreeTestBase;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class LexEvsTreeNodeJsonNameSpaceTest extends LexEvsTreeTestBase {
	
	private LexEvsTree tree;
	private LexEvsTree treeWithAnonymous;
	private CodingSchemeVersionOrTag csvt;
	private LexBIGService lbsi;
	private TreeService service;
	private LexEvsTreeNode focusNode;
	
	public void buildTree(){
		lbsi = LexBIGServiceImpl.defaultInstance();
		service = TreeServiceFactory.getInstance().getTreeService(lbsi);
				
		csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("TestForMultiNamespace");
		
		tree = pathToRootTreeServiceImpl.getTree("npo", csvt, "NPO_1607", "npo");	
	}
	
	public void buildTreeWithAnonymousNode(){
		lbsi = LexBIGServiceImpl.defaultInstance();
		service = TreeServiceFactory.getInstance().getTreeService(lbsi);
				
		csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("TestForMultiNamespace");
		
		treeWithAnonymous = pathToRootTreeServiceImpl.getTree("npo", csvt, "NPO_1923", "npo");	
	}

	@Test
	public void testJsonNameSpaceExists(){
		this.buildTree();

		focusNode = tree.getCurrentFocus();
		
		String json = service.getJsonConverter().buildJsonPathFromRootTree(focusNode);	    
    
		// verify that the JSON namespace npo is present 
        Pattern pattern = Pattern.compile("\"ontology_node_ns\":\"npo\""); 
        Matcher matcher = pattern.matcher(json);
        int namespaceCount = 0;
        while (matcher.find()) namespaceCount++;
        
        assertTrue(namespaceCount == 5);
        
        // verify that the JSON namespace span is present
        pattern = Pattern.compile("\"ontology_node_ns\":\"span\""); 
        matcher = pattern.matcher(json);
        namespaceCount = 0;
        while (matcher.find()) namespaceCount++;
        
        assertTrue(namespaceCount == 9);
	}
	
	@Test
	public void testJsonNoAnymousNodes(){
		this.buildTreeWithAnonymousNode();

		focusNode = treeWithAnonymous.getCurrentFocus();
		
		String json = service.getJsonConverter().buildJsonPathFromRootTree(focusNode);	    
				
		// verify that the JSON namespace npo is present 
        Pattern pattern = Pattern.compile("\"npo\":\"NPO_1489 or npo\""); 
        Matcher matcher = pattern.matcher(json);
        int anonymousCount = 0;
        while (matcher.find()) anonymousCount++;
        
        assertTrue(anonymousCount == 0);
	}
	
}
