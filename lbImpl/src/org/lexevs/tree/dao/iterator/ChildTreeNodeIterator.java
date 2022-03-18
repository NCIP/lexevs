
package org.lexevs.tree.dao.iterator;

import java.io.Serializable;
import java.util.Iterator;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Interface ChildTreeNodeIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public interface ChildTreeNodeIterator extends Iterator<LexEvsTreeNode>, Serializable {

	/**
	 * Reset.
	 */
	public void reset();
	
	/**
	 * Inits the iterator.
	 * 
	 * @param parentCallback the parent callback
	 * @param countOnly the count only
	 */
	public void initIterator(LexEvsTreeNode parentCallback, boolean countOnly);
}