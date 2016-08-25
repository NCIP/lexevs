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
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao.Direction;
import org.LexGrid.LexBIG.Impl.Extensions.tree.listener.NodeAddedListenerSupport;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeIterator;
/**
 * A factory for creating ChildTreeNodeIterator objects.
 */
public class ChildTreeNodeIteratorFactory extends NodeAddedListenerSupport implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6875624940847217861L;
	
	/** The lex evs tree dao. */
	private LexEvsTreeDao lexEvsTreeDao;
	
	/** The coding scheme. */
	private String codingScheme; 
	
	/** The version or tag. */
	private CodingSchemeVersionOrTag versionOrTag; 
	
	/** The direction. */
	private Direction direction; 
	
	/** The association names. */
	private List<String> associationNames;
	
	/** The page size. */
	private int pageSize;
	
	/**
	 * Instantiates a new child tree node iterator factory.
	 * 
	 * @param lexEvsTreeDao the lex evs tree dao
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param direction the direction
	 * @param associationNames the association names
	 * @param pageSize the page size
	 */
	public ChildTreeNodeIteratorFactory(
			LexEvsTreeDao lexEvsTreeDao,
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			Direction direction, 
			List<String> associationNames, 
			int pageSize){
		this.lexEvsTreeDao = lexEvsTreeDao;
		this.codingScheme = codingScheme;
		this.versionOrTag = versionOrTag;
		this.direction = direction;
		this.associationNames = associationNames;
		this.pageSize = pageSize;
	}
	
	/**
	 * Builds the child node iterator.
	 * 
	 * @param node the node
	 * @param countOnly the count only
	 */
	public void buildChildNodeIterator(LexEvsTreeNode node, boolean countOnly){
		this.fireNodeAddedEvent(node);
		PagingChildNodeIterator itr = new PagingChildNodeIterator(
				lexEvsTreeDao,
				codingScheme, 
				versionOrTag, 
				node.getCode(), 
				node.getNamespace(), 
				direction, 
				associationNames, 
				pageSize);
		itr.setIteratorFactory(this);
		itr.setNodeAddedListener(this.getNodeAddedListener());
		node.setChildIterator(itr, countOnly);
	}
}
