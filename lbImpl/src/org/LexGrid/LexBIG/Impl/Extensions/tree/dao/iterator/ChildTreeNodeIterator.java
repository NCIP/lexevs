
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator;

import java.io.Serializable;
import java.util.Iterator;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;

/**
 * The Interface ChildTreeNodeIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
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