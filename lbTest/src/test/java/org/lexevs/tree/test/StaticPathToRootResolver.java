
package org.lexevs.tree.test;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.dao.pathtoroot.PathToRootResolver;
import org.lexevs.tree.model.LexEvsTreeNode;

public class StaticPathToRootResolver implements PathToRootResolver{

	public List<LexEvsTreeNode> getPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code, String namespace) {
		
		if(!code.equals("Chevy")){
			throw new RuntimeException("Resolver only works with 'Chevy'.");
		}
		
		LexEvsTreeNode gmNode = new LexEvsTreeNode();
		gmNode.setCode("GM");
		
		List<LexEvsTreeNode> fiveChildList = new ArrayList<LexEvsTreeNode>();
		fiveChildList.add(gmNode);
		LexEvsTreeNode fiveNode = new LexEvsTreeNode();
		fiveNode.setCode("005");

		gmNode.addPathToRootParents(fiveNode);
		
		List<LexEvsTreeNode> returnList = new ArrayList<LexEvsTreeNode>();
		returnList.add(gmNode);
		return returnList;
	}
}