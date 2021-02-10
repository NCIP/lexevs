
package org.lexevs.tree.json;

import java.io.Serializable;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Interface JsonConverter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public interface JsonConverter extends Serializable {

	/**
	 * Builds the json path from root tree.
	 * 
	 * @param focusNode the focus node
	 * 
	 * @return the string
	 */
	public String buildJsonPathFromRootTree(LexEvsTreeNode focusNode);
	
	/**
	 * Builds the children nodes.
	 * 
	 * @param focusNode the focus node
	 * 
	 * @return the string
	 */
	public String buildChildrenNodes(LexEvsTreeNode focusNode);
	
	/**
	 * Builds the children nodes, starting from a given 'page' of results.
	 * 
	 * @param focusNode the focus node
	 * @param page the page
	 * 
	 * @return the string
	 */
	public String buildChildrenNodes(LexEvsTreeNode focusNode, int page);

}