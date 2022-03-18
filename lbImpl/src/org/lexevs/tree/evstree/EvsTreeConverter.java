
package org.lexevs.tree.evstree;

import java.io.Serializable;
import java.util.List;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Interface JsonConverter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public interface EvsTreeConverter extends Serializable {

	/**
	 * Builds the json path from root tree.
	 * 
	 * @param focusNode the focus node
	 * 
	 * @return the string
	 */
	public List<LexEvsTreeNode> buildEvsTreePathFromRootTree(LexEvsTreeNode focusNode);
	

}