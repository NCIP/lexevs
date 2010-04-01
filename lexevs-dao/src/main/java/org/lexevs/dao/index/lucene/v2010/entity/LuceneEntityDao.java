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
package org.lexevs.dao.index.lucene.v2010.entity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.lucene.hitcollector.BestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetFilteringBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.HitCollectorMerger;

/**
 * The Class LuceneEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityDao extends AbstractBaseIndexDao implements EntityDao {
	
	/** The supported index version2010. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2010 = LexEvsIndexFormatVersion.parseStringToVersion("2010");
	
	/** The index interface. */
	private IndexInterface indexInterface;
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	private EntityIndexer entityIndexer;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) {
		try {
			String internalCodeSystemName = systemResourceService.
				getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), 
						reference.getCodingSchemeVersion());
			
			return this.buildScoreDocs(internalCodeSystemName, reference.getCodingSchemeVersion(), combinedQuery, bitSetQueries);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Builds the score docs.
	 * 
	 * @param internalCodeSystemName the internal code system name
	 * @param internalVersionString the internal version string
	 * @param combinedQuery the combined query
	 * @param bitSetQueries the bit set queries
	 * 
	 * @return the list< score doc>
	 * 
	 * @throws Exception the exception
	 */
	protected List<ScoreDoc> buildScoreDocs(String internalCodeSystemName, String internalVersionString, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) throws Exception {

        SearchServiceInterface searcher = indexInterface.getSearcher(internalCodeSystemName, internalVersionString);

        int maxDoc = indexInterface.
            getIndexReader(internalCodeSystemName, internalVersionString).maxDoc();

        List<ScoreDoc> scoreDocs = null;

        List<BitSet> bitSets = new ArrayList<BitSet>();
        
        if(bitSetQueries != null){
            for(Query query : bitSetQueries){
                BitSetBestScoreOfEntityHitCollector bitSetCollector =
                    new BitSetBestScoreOfEntityHitCollector(
                    		indexInterface.getBoundaryDocumentIterator(
                                    internalCodeSystemName, 
                                    internalVersionString), 
                                    maxDoc);
                searcher.search(query, null, bitSetCollector);
                bitSets.add(bitSetCollector.getResult());
            }
        }
        
        if(combinedQuery.size() == 1){
            BitSetFilteringBestScoreOfEntityHitCollector collector =
                new BitSetFilteringBestScoreOfEntityHitCollector(
                        this.andBitSets(bitSets),
                        indexInterface.getBoundaryDocumentIterator(
                                internalCodeSystemName, 
                                internalVersionString), 
                                maxDoc);
            
            searcher.search(combinedQuery.get(0), null, collector);
            scoreDocs = collector.getResult();
        } else {
            HitCollectorMerger merger = new HitCollectorMerger(
            		indexInterface.getBoundaryDocumentIterator(
                            internalCodeSystemName, 
                            internalVersionString), maxDoc);
            for(Query query : combinedQuery){
                BestScoreOfEntityHitCollector collector =
                    new BitSetFilteringBestScoreOfEntityHitCollector(
                            this.andBitSets(bitSets),
                            indexInterface.getBoundaryDocumentIterator(
                                    internalCodeSystemName, 
                                    internalVersionString), 
                                    maxDoc); 

                searcher.search(query, null, collector);
                merger.addHitCollector(collector);
            }
            scoreDocs = merger.getMergedScoreDocs();
        }
        
        return scoreDocs;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getDocumentById(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, int)
	 */
	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		try {
		String internalCodeSystemName = systemResourceService.
			getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), 
					reference.getCodingSchemeVersion());
		
		return indexInterface.getIndexReader(internalCodeSystemName, reference.getCodingSchemeVersion()).document(documentId);
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference) {
		return new TermQuery(
				new Term(LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
						LuceneLoaderCode.createCodingSchemeUriVersionKey(reference.getCodingSchemeURN(), 
								reference.getCodingSchemeVersion())));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#deleteDocumentsOfCodingScheme(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void deleteDocumentsOfCodingScheme(
			AbsoluteCodingSchemeVersionReference reference) {
		try {
			String internalCodeSystemName = systemResourceService.
			getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), 
					reference.getCodingSchemeVersion());

			IndexerService indexerService = this.getIndexInterface().getBaseIndexerService();

			LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(internalCodeSystemName, 
					reference.getCodingSchemeVersion());
			String indexName = indexerService.getMetaData().getIndexMetaDataValue(lcs.getKey());

			indexerService.forceUnlockIndex(indexName);
			indexerService.openBatchRemover(indexName);

			this.removeDocumentsByField(indexName, 
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
					LuceneLoaderCode.createCodingSchemeUriVersionKey(
					reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()));

			indexerService.closeBatchRemover(indexName);
			indexerService.optimizeIndex(indexName);
			
			indexerService.getMetaData().removeIndexMetaDataValue(lcs.getKey());
			indexerService.getMetaData().rereadFile(true);
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void updateDocumentsOfEntity(
			AbsoluteCodingSchemeVersionReference reference, Entity entity) {
		String codingSchemeUri = reference.getCodingSchemeURN();
		String codingSchemeVersion = reference.getCodingSchemeVersion();
		try {
			String indexName = getIndexName(codingSchemeUri,codingSchemeVersion);

			IndexerService indexerService = this.getIndexInterface().getBaseIndexerService();
			indexerService.forceUnlockIndex(indexName);
			indexerService.openBatchRemover(indexName);
			
			
			this.removeDocumentsByField(
					indexName, 
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
					LuceneLoaderCode.
						createCodingSchemeUriVersionCodeNamespaceKey(
							codingSchemeUri, 
							codingSchemeVersion, 
							entity.getEntityCode(), 
							entity.getEntityCodeNamespace()));
			
			indexerService.closeBatchRemover(indexName);
			
			entityIndexer.indexEntity(indexName, codingSchemeUri, codingSchemeVersion, entity);
			
			String internalCodeSystemName = systemResourceService.
			 getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, 
			 	codingSchemeVersion);
			
			indexInterface.reopenIndex(internalCodeSystemName, codingSchemeVersion);
		
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void removeDocumentsByField(String indexName, String fieldName, String fieldValue) {
		IndexerService indexerService = this.getIndexInterface().getBaseIndexerService();
		try {
			indexerService.removeDocument(indexName, 
					fieldName, 
					fieldValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	protected String getIndexName(String codingSchemeUri, String codingSchemeVersion) throws Exception {
		String internalCodeSystemName = systemResourceService.
		getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, 
				codingSchemeVersion);

		IndexerService indexerService = indexInterface.getBaseIndexerService();

		LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(internalCodeSystemName, 
				codingSchemeVersion);
		return indexerService.getMetaData().getIndexMetaDataValue(lcs.getKey());
	}

	/**
	 * Sets the index interface.
	 * 
	 * @param indexInterface the new index interface
	 */
	public void setIndexInterface(IndexInterface indexInterface) {
		this.indexInterface = indexInterface;
	}

	/**
	 * Gets the index interface.
	 * 
	 * @return the index interface
	 */
	public IndexInterface getIndexInterface() {
		return indexInterface;
	}

	/**
	 * And bit sets.
	 * 
	 * @param bitSets the bit sets
	 * 
	 * @return the bit set
	 */
	private BitSet andBitSets(List<BitSet> bitSets){
		BitSet totalBitSet = null;
		for(BitSet bitSet : bitSets){
			if(totalBitSet == null){
				totalBitSet = bitSet;
			} else {
				totalBitSet.and(bitSet);
			}
		}
		return totalBitSet;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.AbstractBaseIndexDao#doGetSupportedLexEvsIndexFormatVersions()
	 */
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2010);
	}
	
	/**
	 * Sets the system resource service.
	 * 
	 * @param systemResourceService the new system resource service
	 */
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public EntityIndexer getEntityIndexer() {
		return entityIndexer;
	}

	public void setEntityIndexer(EntityIndexer entityIndexer) {
		this.entityIndexer = entityIndexer;
	}
}
