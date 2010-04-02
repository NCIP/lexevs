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
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.IndexCreator.EntityIndexerProgressCallback;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;

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
	
	private IndexerService indexerService;
	
	private SystemResourceService systemResourceService;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#createIndex(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void createIndex(AbsoluteCodingSchemeVersionReference reference) {
		createIndex(reference, null);
	}
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback) {
		indexCreator.index(reference, callback);
	}

	protected void optimizeIndex(String indexName) {
		try {
			indexDaoManager.getIndexerService().optimizeIndex(indexName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#updateIndexForEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(codingSchemeUri);
		reference.setCodingSchemeVersion(codingSchemeVersion);
		
		indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).updateDocumentsOfEntity(reference, entity);
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
	

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference) {
		String uri = reference.getCodingSchemeURN();
		String version = reference.getCodingSchemeVersion();
		
		String codingSchemeName;
		try {
			codingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(uri, version);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
		
		LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
		try {
			return StringUtils.isNotBlank(indexerService.getMetaData().getIndexMetaDataValue(lcs.getKey()));
		} catch (InternalErrorException e) {
			throw new RuntimeException(e);
		}
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

	public void setIndexerService(IndexerService indexerService) {
		this.indexerService = indexerService;
	}

	public IndexerService getIndexerService() {
		return indexerService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}
}
