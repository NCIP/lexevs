/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.tree.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;



/**
 * The Class NodeAddedListenerSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
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
