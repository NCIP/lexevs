
package org.lexevs.tree.listener;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The listener interface for receiving nodeAdded events.
 * The class that is interested in processing a nodeAdded
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addNodeAddedListener<code> method. When
 * the nodeAdded event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NodeAddedEvent
 */
@Deprecated
public interface NodeAddedListener {

	/**
	 * Node added.
	 * 
	 * @param node the node
	 */
	public void nodeAdded(LexEvsTreeNode node);
}