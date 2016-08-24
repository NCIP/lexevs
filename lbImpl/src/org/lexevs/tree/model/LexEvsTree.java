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
package org.lexevs.tree.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.dao.LexEvsTreeDao.Direction;
import org.lexevs.tree.listener.NodeAddedListener;
import org.LexGrid.annotations.LgClientSideSafe;


/**
 * The Class LexEvsTree.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class LexEvsTree implements NodeAddedListener, Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6018353585049658095L;
	
	/** The current focus. */
	private LexEvsTreeNode currentFocus;
	
	/** The code map. */
	private Map<String,LexEvsTreeNode> codeMap = new HashMap<String,LexEvsTreeNode>();
	
	/** The coding scheme. */
	private String codingScheme;
	
	/** The version or tag. */
	private CodingSchemeVersionOrTag versionOrTag;
	
	/** The association names. */
	private List<String> associationNames;
	
	/** The direction. */
	private Direction direction;

	/**
	 * Gets the current focus.
	 * 
	 * @return the current focus
	 */
	public LexEvsTreeNode getCurrentFocus() {
		return currentFocus;
	}

	/**
	 * Sets the current focus.
	 * 
	 * @param currentFocus the new current focus
	 */
	public void setCurrentFocus(LexEvsTreeNode currentFocus) {
		this.currentFocus = currentFocus;
	}
	
	/**
	 * Gets the code map.
	 * 
	 * @return the code map
	 */
	public Map<String, LexEvsTreeNode> getCodeMap() {
		return codeMap;
	}

	/**
	 * Sets the code map.
	 * 
	 * @param codeMap the code map
	 */
	public void setCodeMap(Map<String, LexEvsTreeNode> codeMap) {
		this.codeMap = codeMap;
	}

	/**
	 * Find node in tree.
	 * 
	 * @param code the code
	 * 
	 * @return the lex evs tree node
	 */
	public LexEvsTreeNode findNodeInTree(String code){
		return codeMap.get(code);
	}

	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	@LgClientSideSafe
	public String getCodingScheme() {
		return codingScheme;
	}

	/**
	 * Sets the version or tag.
	 * 
	 * @param versionOrTag the new version or tag
	 */
	public void setVersionOrTag(CodingSchemeVersionOrTag versionOrTag) {
		this.versionOrTag = versionOrTag;
	}

	/**
	 * Gets the version or tag.
	 * 
	 * @return the version or tag
	 */
	@LgClientSideSafe
	public CodingSchemeVersionOrTag getVersionOrTag() {
		return versionOrTag;
	}

	/**
	 * Gets the association names.
	 * 
	 * @return the association names
	 */
	@LgClientSideSafe
	public List<String> getAssociationNames() {
		return associationNames;
	}

	/**
	 * Sets the association names.
	 * 
	 * @param associationNames the new association names
	 */
	public void setAssociationNames(List<String> associationNames) {
		this.associationNames = associationNames;
	}

	/**
	 * Gets the direction.
	 * 
	 * @return the direction
	 */
	@LgClientSideSafe
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 * 
	 * @param direction the new direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.listener.NodeAddedListener#nodeAdded(org.lexevs.tree.model.LexEvsTreeNode)
	 */
	public void nodeAdded(LexEvsTreeNode node) {
		codeMap.put(node.getCode(), node);
	}
}
