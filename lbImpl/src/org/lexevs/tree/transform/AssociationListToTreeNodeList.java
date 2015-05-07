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
package org.lexevs.tree.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.lexevs.tree.model.LexEvsTreeNode;


/**
 * The Class AssociationListToTreeNodeList.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class AssociationListToTreeNodeList implements Transformer<AssociationList, List<LexEvsTreeNode>>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4791839960034941586L;

	/* (non-Javadoc)
	 * @see org.lexevs.tree.transform.Transformer#transform(java.lang.Object)
	 */
	public List<LexEvsTreeNode> transform(AssociationList input) {
		Map<String,LexEvsTreeNode> codeMap = new HashMap<String,LexEvsTreeNode>();
		return getTreeNodesForLevel(input, codeMap);
	}
	
	/**
	 * Gets the tree nodes for level.
	 * 
	 * @param level the level
	 * @param codeMap the code map
	 * 
	 * @return the tree nodes for level
	 */
	protected List<LexEvsTreeNode> getTreeNodesForLevel(AssociationList level, Map<String,LexEvsTreeNode> codeMap){
		List<LexEvsTreeNode> returnList = new ArrayList<LexEvsTreeNode>();
		if(level == null || level.getAssociation() == null){return returnList;}
		
		for(Association assoc : level.getAssociation()){
			returnList.addAll(
					getLexEvsTreeNodesFromAssociatedConcepts(assoc.getAssociatedConcepts(), codeMap));
		}
		return returnList;
	}
	
	/**
	 * Gets the lex evs tree nodes from associated concepts.
	 * 
	 * @param list the list
	 * @param codeMap the code map
	 * 
	 * @return the lex evs tree nodes from associated concepts
	 */
	protected List<LexEvsTreeNode> getLexEvsTreeNodesFromAssociatedConcepts(AssociatedConceptList list, Map<String,LexEvsTreeNode> codeMap){
		List<LexEvsTreeNode> returnList = new ArrayList<LexEvsTreeNode>();
		for(AssociatedConcept concept : list.getAssociatedConcept()){
			returnList.add(
					buildLexEvsTreeNode(concept, codeMap));
		}
		return returnList;
	}
	
	/**
	 * Builds the lex evs tree node.
	 * 
	 * @param concept the concept
	 * @param codeMap the code map
	 * 
	 * @return the lex evs tree node
	 */
	protected LexEvsTreeNode buildLexEvsTreeNode(AssociatedConcept concept, Map<String,LexEvsTreeNode> codeMap){
		if(codeMap.containsKey(concept.getCode())){
			return codeMap.get(concept.getCode());
		} else {
			LexEvsTreeNode node = new LexEvsTreeNode();
			node.setCode(concept.getCode());
			node.setEntityDescription(concept.getEntityDescription().getContent());
			node.setNamespace(concept.getCodeNamespace());

			List<LexEvsTreeNode> pathToRootParents = getTreeNodesForLevel(concept.getSourceOf(), codeMap);
			for(LexEvsTreeNode parent : pathToRootParents){
				parent.addPathToRootChildren(node);
				node.addPathToRootParents(parent);
			}

			codeMap.put(node.getCode(), node);
			return node;
		}
	}
}
