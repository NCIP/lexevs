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
package org.LexGrid.LexBIG.Impl.Extensions.tree.evstree;

import java.util.List;

import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.LexGrid.LexBIG.Impl.Extensions.tree.utility.TreeUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class ChildPagingJsonConverter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ChildPagingEvsTreeConverter implements EvsTreeConverter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5665088464809753637L;

	
	/** The Constant PAGE. */
	public static final String PAGE = "page";

	/** The MA x_ children. */
	private int MAX_CHILDREN = 5;
	
	/** The SUBCHILDRE n_ levels. */
	private int SUBCHILDREN_LEVELS = 1;
	
	/** The MOR e_ childre n_ indicator. */
	private static String MORE_CHILDREN_INDICATOR = "...";
	
	public ChildPagingEvsTreeConverter(){
	    super();
	}
	
	public ChildPagingEvsTreeConverter(int maxChildren){
	        super();
	        if(maxChildren < 0){
	            MAX_CHILDREN = Integer.MAX_VALUE;
	        }
	        else{
	            MAX_CHILDREN = maxChildren;
	        }
	    }
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.json.JsonConverters#buildJsonPathFromRootTree(org.lexevs.tree.model.LexEvsTreeNode)
	 */
	public List<LexEvsTreeNode> buildEvsTreePathFromRootTree(LexEvsTreeNode focusNode){
		LexEvsTreeNode root = TreeUtility.getRoot(focusNode);
		return buildChildrenPathToRootNodes(root);
	}
	




	/**
	 * Builds the children path to root nodes.
	 * 
	 * @param focusNode the focus node
	 * 
	 * @return the jSON array
	 */
	public List<LexEvsTreeNode> buildChildrenPathToRootNodes(LexEvsTreeNode focusNode){
		try {
			return walkTreeFromRoot(focusNode).getPathToRootChildren();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Walk tree from root.
	 * 
	 * @param node the node
	 * 
	 * @return the jSON object
	 */
	private LexEvsTreeNode walkTreeFromRoot(LexEvsTreeNode node){
		LexEvsTreeNode nodeObject = new LexEvsTreeNode();

		try {
			nodeObject = buildNode(node);
			
			if(node.getPathToRootChildren() != null){	
				int children = 0;
				for(LexEvsTreeNode child : node.getPathToRootChildren()){
					children++;
					nodeObject.addPathToRootChildren(walkTreeFromRoot(child));
				} 
				
				ChildTreeNodeIterator itr = node.getChildIterator();
				while(itr.hasNext() && children < MAX_CHILDREN){
					LexEvsTreeNode child = itr.next();
					if(!knownChildrenContainsCode(node.getPathToRootChildren(), child.getCode())){
						nodeObject.addPathToRootChildren(walkTreeFromRoot(child));
						children++;
					}
				}
				
				if(children >= MAX_CHILDREN){
					nodeObject.addPathToRootChildren(buildMoreChildrenNode(node));
				}
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return nodeObject;
	}
	
	/**
	 * Builds the node.
	 * 
	 * @param node the node
	 * 
	 * @return the jSON object
	 */
	private LexEvsTreeNode buildNode(LexEvsTreeNode node){
		LexEvsTreeNode nodeObject = new LexEvsTreeNode(node);	
		return nodeObject;
		
	}
	
	/**
	 * Builds the more children node.
	 * 
	 * @param parent the parent
	 * 
	 * @return the jSON object
	 */
	private LexEvsTreeNode buildMoreChildrenNode(LexEvsTreeNode parent){
		LexEvsTreeNode nodeObject = new LexEvsTreeNode();	
	
			nodeObject.setCode(MORE_CHILDREN_INDICATOR);
			nodeObject.setEntityDescription(MORE_CHILDREN_INDICATOR);
			
			nodeObject.setExpandableStatus(ExpandableStatus.IS_NOT_EXPANDABLE);
			nodeObject.addPathToRootParents(parent);
			
		
		return nodeObject;
	}
	
	/**
	 * Known children contains code.
	 * 
	 * @param list the list
	 * @param code the code
	 * 
	 * @return true, if successful
	 */
	private boolean knownChildrenContainsCode(List<LexEvsTreeNode> list, String code){
		for(LexEvsTreeNode node : list){
			if(node.getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Expandable status to int.
	 * 
	 * @param status the status
	 * 
	 * @return the int
	 */
	public int expandableStatusToInt(ExpandableStatus status){
		if(status.equals(ExpandableStatus.IS_EXPANDABLE)){
			return 1;
		} else {
			return 0;
		}
	}
}
