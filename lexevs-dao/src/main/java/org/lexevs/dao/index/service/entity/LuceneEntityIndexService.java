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
package org.lexevs.dao.index.service.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.IndexCreator;

/**
 * The Class LuceneEntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityIndexService implements EntityIndexService {
	
	/** The index dao manager. */
	private IndexDaoManager indexDaoManager;
	
	/** The index creator. */
	private IndexCreator indexCreator;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#createIndex(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void createIndex(AbsoluteCodingSchemeVersionReference reference) {
		indexCreator.index(reference);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#removeIndexForEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public void removeIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#updateIndexForEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Override
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference) {
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).
			getMatchAllDocsQuery(reference);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQueries, List<? extends Query> individualQueries){
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).
			query(reference, combinedQueries, individualQueries);
	}

	/**
	 * Sets the index dao manager.
	 * 
	 * @param indexDaoManager the new index dao manager
	 */
	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	/**
	 * Gets the index dao manager.
	 * 
	 * @return the index dao manager
	 */
	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#getDocumentById(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, int)
	 */
	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).getDocumentById(reference, documentId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#dropIndex(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		indexDaoManager.getEntityDao(
				reference.getCodingSchemeURN(), 
				reference.getCodingSchemeVersion()).deleteDocumentsOfCodingScheme(reference);
	}

	/**
	 * Gets the index creator.
	 * 
	 * @return the index creator
	 */
	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	/**
	 * Sets the index creator.
	 * 
	 * @param indexCreator the new index creator
	 */
	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
}
