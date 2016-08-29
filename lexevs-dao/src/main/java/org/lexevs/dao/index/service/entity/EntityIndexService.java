/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.index.service.entity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.IndexCreator.EntityIndexerProgressCallback;

import java.util.List;
import java.util.Set;

/**
 * The Interface EntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityIndexService {

	/**
	 * Update index for entity.
	 *
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param entity the entity
	 */
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public String getIndexName(String codingSchemeUri, String codingSchemeVersion);
	
	public Document getDocumentById(String codingSchemeUri, String codingSchemeVersion, int id);
	 
	public Document getDocumentById(String codingSchemeUri, String codingSchemeVersion, int id,  Set<String> fields);
	
	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity);
	
	/**
	 * Creates the index.
	 * 
	 * @param reference the reference
	 */
	public void createIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback);
	/**
	 * Drop index.
	 * 
	 * @param reference the reference
	 */
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference);

	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query);
	
	public Document getDocumentFromCommonIndexById(List<AbsoluteCodingSchemeVersionReference> references, int id);
	
	public List<ScoreDoc> queryCommonIndex(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			Query query);

}