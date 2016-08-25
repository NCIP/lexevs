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
package org.lexevs.tree.dao;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Interface LexEvsTreeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public interface LexEvsTreeDao extends Serializable {
	
	/**
	 * The Enum Direction.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum Direction {/** The FORWARD. */
FORWARD, /** The BACKWARD. */
 BACKWARD};

	/**
	 * Gets the children.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param direction the direction
	 * @param associationNames the association names
	 * @param start the start
	 * @param limit the limit
	 * 
	 * @return the children
	 */
	public List<LexEvsTreeNode> getChildren(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace,
			Direction direction,
			List<String> associationNames,
			int start,
			int limit);
	
	/**
	 * Gets the children.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param direction the direction
	 * @param associationNames the association names
	 * @param knownCodes the known codes
	 * @param start the start
	 * @param limit the limit
	 * 
	 * @return the children
	 */
	public List<LexEvsTreeNode> getChildren(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace,
			Direction direction,
			List<String> associationNames,
			List<String> knownCodes,
			int start,
			int limit);
	
	/**
	 * Gets the children count.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param direction the direction
	 * @param associationNames the association names
	 * 
	 * @return the children count
	 */
	public int getChildrenCount(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace,
			Direction direction,
			List<String> associationNames);
	
	/**
	 * Gets the node.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the node
	 */
	public LexEvsTreeNode getNode(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace);

	
}