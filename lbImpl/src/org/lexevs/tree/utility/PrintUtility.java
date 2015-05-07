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
package org.lexevs.tree.utility;

import java.util.List;

import org.lexevs.tree.model.LexEvsTreeNode;



/**
 * The Class PrintUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class PrintUtility {
	
	/**
	 * Prints the.
	 * 
	 * @param node the node
	 */
	public static void print(LexEvsTreeNode node){
		print(node, 0);	
	}
	
	/**
	 * Prints the.
	 * 
	 * @param list the list
	 */
	public static void print(List<LexEvsTreeNode> list){
		for(LexEvsTreeNode node : list){
			print(node);	
		}
	}

	/**
	 * Prints the.
	 * 
	 * @param node the node
	 * @param depth the depth
	 */
	private static void print(LexEvsTreeNode node, int depth){
		System.out.println(buildPrefix(depth) + "Code: " + node.getCode() + ", " + "Description: " + node.getEntityDescription() +  ", " + "Namespace: " + node.getNamespace() + " Hash: " + node.hashCode());
		if(node.getPathToRootParents() != null && node.getPathToRootParents().size() > 0){
			for(LexEvsTreeNode parent : node.getPathToRootParents()){
				print(parent, depth + 1);
			}
		}
	}
	
	/**
	 * Builds the prefix.
	 * 
	 * @param depth the depth
	 * 
	 * @return the string
	 */
	private static String buildPrefix(int depth){
		String prefix = "";
		for(int i=0;i<depth;i++){
			prefix = prefix + " -> ";
		}
		return prefix;
	}
}
