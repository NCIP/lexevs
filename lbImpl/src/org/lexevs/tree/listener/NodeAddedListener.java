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
