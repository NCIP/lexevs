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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermsFilter;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.service.SystemResourceService;

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
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	private LuceneIndexTemplate luceneIndexTemplate;
	
	private Map<String,Filter> codingSchemeFilterMap = new HashMap<String,Filter>();
	private Map<String,Filter> boundaryDocFilterMap = new HashMap<String,Filter>();

	@Override
	public void addDocuments(String codingSchemeUri, String version,
			List<Document> documents, Analyzer analyzer) {
		luceneIndexTemplate.addDocuments(documents, analyzer);
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Query query) {
		luceneIndexTemplate.removeDocuments(query);
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Term term) {
		luceneIndexTemplate.removeDocuments(term);
	}

	
	@Override
	public String getIndexName(String codingSchemeUri, String version) {
		return luceneIndexTemplate.getIndexName();
	}

	@Override
	public void optimizeIndex(String codingSchemeUri, String version) {
		luceneIndexTemplate.optimize();
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(String codingSchemeUri, String version, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) {
		try {
			
			Filter codingSchemeFilter = getCodingSchemeFilterForCodingScheme(codingSchemeUri, version);
			Filter boundaryDocSchemeFilter = getBoundaryDocFilterForCodingScheme(codingSchemeUri, version);
			
			return this.buildScoreDocs(boundaryDocSchemeFilter, codingSchemeFilter, combinedQuery, bitSetQueries);
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
	protected List<ScoreDoc> buildScoreDocs(
			Filter boundaryDocFilter,
			Filter codingSchemeFilter,
			List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) throws Exception {

        int maxDoc = luceneIndexTemplate.getMaxDoc();

        List<ScoreDoc> scoreDocs = null;
        
        DocIdSet boundaryDocIds = this.luceneIndexTemplate.getDocIdSet(boundaryDocFilter);

        List<BitSet> bitSets = new ArrayList<BitSet>();
        
        if(bitSetQueries != null){
            for(Query query : bitSetQueries){
                BitSetBestScoreOfEntityHitCollector bitSetCollector =
                    new BitSetBestScoreOfEntityHitCollector(
                    		boundaryDocIds.iterator(), 
                                    maxDoc);
                luceneIndexTemplate.search(query, codingSchemeFilter, bitSetCollector);
                bitSets.add(bitSetCollector.getResult());
            }
        }
        
        if(combinedQuery.size() == 1){
            BitSetFilteringBestScoreOfEntityHitCollector collector =
                new BitSetFilteringBestScoreOfEntityHitCollector(
                        this.andBitSets(bitSets),
                        boundaryDocIds.iterator(), 
                                maxDoc);
            
            luceneIndexTemplate.search(combinedQuery.get(0), codingSchemeFilter, collector);
            scoreDocs = collector.getResult();
        } else {
            HitCollectorMerger merger = new HitCollectorMerger(
            		boundaryDocIds.iterator(), 
            		maxDoc);
            for(Query query : combinedQuery){
                BestScoreOfEntityHitCollector collector =
                    new BitSetFilteringBestScoreOfEntityHitCollector(
                            this.andBitSets(bitSets),
                            boundaryDocIds.iterator(), 
                                    maxDoc); 

                luceneIndexTemplate.search(query, codingSchemeFilter, collector);
                merger.addHitCollector(collector);
            }
            scoreDocs = merger.getMergedScoreDocs();
        }
        
        return scoreDocs;
	}
	
	protected Filter getBoundaryDocFilterForCodingScheme(String codingSchemeUri, String codingSchemeVersion) {
		String key = getFilterMapKey(codingSchemeUri, codingSchemeUri);
		if(!this.boundaryDocFilterMap.containsKey(key)) {
			TermsFilter filter = new TermsFilter();
			filter.addTerm(new Term("codeBoundry", "T"));
			boundaryDocFilterMap.put(key, new CachingWrapperFilter(filter));
		}
		return boundaryDocFilterMap.get(key);
	}
	
	protected Filter getCodingSchemeFilterForCodingScheme(String codingSchemeUri, String codingSchemeVersion) {
		String key = getFilterMapKey(codingSchemeUri, codingSchemeUri);
		if(!this.codingSchemeFilterMap.containsKey(key)) {
			Term term = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
					LuceneLoaderCode.createCodingSchemeUriVersionKey(
							codingSchemeUri, codingSchemeVersion));
			TermsFilter filter = new TermsFilter();
			filter.addTerm(term);
			codingSchemeFilterMap.put(key, new CachingWrapperFilter(filter));
		}
		return codingSchemeFilterMap.get(key);
	}
	
	private String getFilterMapKey(String codingSchemeUri, String codingSchemeVersion) {
		return Integer.toString(codingSchemeUri.hashCode())
			+ Integer.toString(codingSchemeVersion.hashCode());
	}
	
	@Override
	public Document getDocumentById(String codingSchemeUri, String version,
			int id) {
		return luceneIndexTemplate.getDocumentById(id);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public Query getMatchAllDocsQuery(
			String codingSchemeUri, String version) {
		return new TermQuery(
				new Term(LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
						LuceneLoaderCode.createCodingSchemeUriVersionKey(codingSchemeUri, 
								version)));
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

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}
}
