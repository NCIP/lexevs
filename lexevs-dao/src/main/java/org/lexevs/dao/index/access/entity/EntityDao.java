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
package org.lexevs.dao.index.access.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

/**
 * The Interface EntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityDao extends LexEvsIndexFormatVersionAwareDao {

	/**
	 * Query.
	 * 
	 * @param reference the reference
	 * @param combinedQueries the combined queries
	 * @param individualQueries the individual queries
	 * 
	 * @return the list< score doc>
	 */
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQueries, List<? extends Query> individualQueries);

	/**
	 * Gets the document by id.
	 * 
	 * @param reference the reference
	 * @param documentId the document id
	 * 
	 * @return the document by id
	 */
	public Document getDocumentById(AbsoluteCodingSchemeVersionReference reference, int documentId);
	
	/**
	 * Delete documents of coding scheme.
	 * 
	 * @param reference the reference
	 */
	public void deleteDocumentsOfCodingScheme(AbsoluteCodingSchemeVersionReference reference);
	
	public void updateDocumentsOfEntity(AbsoluteCodingSchemeVersionReference reference, Entity entity);
	
	public void deleteDocumentsOfEntity(AbsoluteCodingSchemeVersionReference reference, Entity entity);
	
	public void addEntityToIndex(AbsoluteCodingSchemeVersionReference reference, Entity entity);
	
	/**
	 * Gets the match all docs query.
	 * 
	 * @param reference the reference
	 * 
	 * @return the match all docs query
	 */
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference);
}
