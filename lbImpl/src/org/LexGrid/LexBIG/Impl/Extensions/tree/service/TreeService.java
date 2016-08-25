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
package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Impl.Extensions.tree.evstree.EvsTreeConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;


/**
 * The Interface TreeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface TreeService extends GenericExtension {

	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			String hierarchyId);
	
	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace);
	
	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			String hierarchyId);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code);
	
	/**
	 * Gets the json converter.
	 * 
	 * @return the json converter
	 */
	public JsonConverter getJsonConverter();
	
	/**
	 * Gets the evsTree converter.
	 * 
	 * @return the evsTree converter
	 */
	public EvsTreeConverter getEvsTreeConverter();
}