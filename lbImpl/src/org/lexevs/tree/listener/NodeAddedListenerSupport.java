
package org.lexevs.tree.listener;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Class NodeAddedListenerSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class NodeAddedListenerSupport {

	/** The node added listener. */
	private List<NodeAddedListener> nodeAddedListener = new ArrayList<NodeAddedListener>();

	/**
	 * Fire node added event.
	 * 
	 * @param node the node
	 */
	public void fireNodeAddedEvent(LexEvsTreeNode node){
		for(NodeAddedListener listener : nodeAddedListener){
			listener.nodeAdded(node);
		}
	}
	
	/**
	 * Adds the node added listener.
	 * 
	 * @param listener the listener
	 */
	public void addNodeAddedListener(NodeAddedListener listener){
		nodeAddedListener.add(listener);
	}

	/**
	 * Gets the node added listener.
	 * 
	 * @return the node added listener
	 */
	public List<NodeAddedListener> getNodeAddedListener() {
		return nodeAddedListener;
	}

	/**
	 * Sets the node added listener.
	 * 
	 * @param nodeAddedListener the new node added listener
	 */
	public void setNodeAddedListener(List<NodeAddedListener> nodeAddedListener) {
		this.nodeAddedListener = nodeAddedListener;
	}

}