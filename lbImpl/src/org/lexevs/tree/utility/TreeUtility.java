
package org.lexevs.tree.utility;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Class TreeUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class TreeUtility {

	/**
	 * Gets the root.
	 * 
	 * @param focus the focus
	 * 
	 * @return the root
	 */
	public static LexEvsTreeNode getRoot(LexEvsTreeNode focus){
		if(focus.getPathToRootParents() == null || focus.getPathToRootParents().size() == 0){
			return focus;
		} else {
			return getRoot(focus.getPathToRootParents().get(0));
		}
	}
}