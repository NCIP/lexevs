
package org.LexGrid.LexBIG.Impl.Extensions.tree.utility;

import java.util.List;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;

/**
 * The Class PrintUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrintUtility {
	
	/**
	 * Prints the.
	 * 
	 * @param node the node
	 */
	public static void print(LexEvsTreeNode node){
		print(node, 0);	
	}
	
	/**
	 * Prints the.
	 * 
	 * @param list the list
	 */
	public static void print(List<LexEvsTreeNode> list){
		for(LexEvsTreeNode node : list){
			print(node);	
		}
	}

	/**
	 * Prints the.
	 * 
	 * @param node the node
	 * @param depth the depth
	 */
	private static void print(LexEvsTreeNode node, int depth){
		System.out.println(buildPrefix(depth) + "Code: " + node.getCode() + ", " + "Description: " + node.getEntityDescription() +  ", " + "Namespace: " + node.getNamespace() + " Hash: " + node.hashCode());
		if(node.getPathToRootParents() != null && node.getPathToRootParents().size() > 0){
			for(LexEvsTreeNode parent : node.getPathToRootParents()){
				print(parent, depth + 1);
			}
		}
	}
	
	/**
	 * Builds the prefix.
	 * 
	 * @param depth the depth
	 * 
	 * @return the string
	 */
	private static String buildPrefix(int depth){
		String prefix = "";
		for(int i=0;i<depth;i++){
			prefix = prefix + " -> ";
		}
		return prefix;
	}
}