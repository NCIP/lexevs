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