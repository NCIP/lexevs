package org.LexGrid.LexBIG.example;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.Extensions.tree.utility.PrintUtility;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


public class GetTree {

	  public static void main(String[] args) {
	        LexBIGService lbsi = LexBIGServiceImpl.defaultInstance();
	        TreeService service = TreeServiceFactory.getInstance().getTreeService(lbsi);
	        LexEvsTree tree = null;
	        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
	        csvt.setVersion("TestForMultiNamespace");
	        tree = service.getTree("npo", csvt, "NPO_1607", "npo", "is_a");	            
	            LexEvsTreeNode focusNode = tree.getCurrentFocus();
	            List<LexEvsTreeNode> listForTree = service.getEvsTreeConverter().buildEvsTreePathFromRootTree(focusNode);
	            String json = service.getJsonConverter().buildJsonPathFromRootTree(focusNode);	        
	            List <LexEvsTreeNode> listOfone = new ArrayList<LexEvsTreeNode>();
	            listOfone.add(focusNode);
	            System.out.println("Printing from focus Node");
	            PrintUtility.print(focusNode);
	            System.out.println("Printing tree");
	            PrintUtility.print(listForTree);
	            System.out.println("Printing Json");
	            System.out.println(json);
	    }
}