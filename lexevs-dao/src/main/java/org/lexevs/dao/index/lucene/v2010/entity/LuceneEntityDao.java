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
package org.lexevs.dao.index.lucene.v2010.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucene.AbstractBaseLuceneIndexTemplateDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.custom.NonScoringTermQuery;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import edu.mayo.informatics.indexer.lucene.hitcollector.BestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetFilteringBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.HitCollectorMerger;

/**
 * The Class LuceneEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityDao extends AbstractBaseLuceneIndexTemplateDao implements EntityDao {
	
	public enum BitSetOp {AND, OR};
	
	/** The supported index version2010. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2010 = LexEvsIndexFormatVersion.parseStringToVersion("2010");

	private LuceneIndexTemplate luceneIndexTemplate;

	@Override
	public void addDocuments(String codingSchemeUri, String version,
			List<Document> documents, Analyzer analyzer) {
		getLuceneIndexTemplate(codingSchemeUri, version).addDocuments(documents, analyzer);
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Query query) {
		getLuceneIndexTemplate(codingSchemeUri, version).removeDocuments(query);
	}

	@Override
	public Filter getCodingSchemeFilter(String uri, String version) {
		return this.getCodingSchemeFilterForCodingScheme(uri, version);
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Term term) {
		getLuceneIndexTemplate(codingSchemeUri, version).removeDocuments(term);
	}

	
	@Override
	public String getIndexName(String codingSchemeUri, String version) {
		return getLuceneIndexTemplate(codingSchemeUri, version).getIndexName();
	}

	@Override
	public void optimizeIndex(String codingSchemeUri, String version) {
		getLuceneIndexTemplate(codingSchemeUri, version).optimize();
	}

	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query) {
		try {
			LuceneIndexTemplate template = getLuceneIndexTemplate(codingSchemeUri, version);

			Filter codingSchemeFilter = getCodingSchemeFilterForCodingScheme(codingSchemeUri, version);

			int maxDoc = template.getMaxDoc();

			Filter boundaryDocFilter = this.getBoundaryDocFilterForCodingScheme(codingSchemeUri, version);

			DocIdSet boundaryDocIds = template.getDocIdSet(boundaryDocFilter);

			BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(boundaryDocIds.iterator(), maxDoc);

			template.search(query, codingSchemeFilter, hitCollector);

			return hitCollector.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ScoreDoc> query(Query query) {
		try {
			LuceneIndexTemplate template = this.getCommonLuceneIndexTemplate();
			
			int maxDoc = template.getMaxDoc();

			DocIdSet boundaryDocIds = template.getDocIdSet(this.createBoundaryDocFilter());

			BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(boundaryDocIds.iterator(), maxDoc);
	
			template.search(query, null, hitCollector);

			return hitCollector.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ScoreDoc> query(List<AbsoluteCodingSchemeVersionReference> codingSchemes, Query query) {
		try {
			LuceneIndexTemplate template = this.getCommonLuceneIndexTemplate();
			
			int maxDoc = template.getMaxDoc();

			Filter boundaryDocFilter = this.getBoundaryDocFilterForCodingScheme(codingSchemes);

			DocIdSet boundaryDocIds = template.getDocIdSet(boundaryDocFilter);

			BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(boundaryDocIds.iterator(), maxDoc);

			Filter codingSchemeFilter = getCodingSchemeFilterForCodingScheme(codingSchemes);
			
			template.search(query, codingSchemeFilter, hitCollector);

			return hitCollector.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected LuceneIndexTemplate getCommonLuceneIndexTemplate() {
		return this.luceneIndexTemplate;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(String codingSchemeUri, String version, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) {
		try {
			
			Filter codingSchemeFilter = getCodingSchemeFilterForCodingScheme(codingSchemeUri, version);
			Filter boundaryDocSchemeFilter = getBoundaryDocFilterForCodingScheme(codingSchemeUri, version);
			
			return this.buildScoreDocs(
					getLuceneIndexTemplate(codingSchemeUri, version),
					boundaryDocSchemeFilter, codingSchemeFilter, combinedQuery, bitSetQueries);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Filter getBoundaryDocsHitAsAWholeFilter(
			String codingSchemeUri, 
			String version, 
			Query query) {
		Filter boundaryDocFilter = 
			this.getBoundaryDocFilterForCodingScheme(codingSchemeUri, version);
		
		Filter codingSchemeFilter = 
			this.getCodingSchemeFilter(codingSchemeUri, version);

		LuceneIndexTemplate template = getLuceneIndexTemplate(codingSchemeUri, version);

		DocIdSet boundaryDocIds = template.getDocIdSet(boundaryDocFilter);
		DocIdSetIterator boundaryItr = boundaryDocIds.iterator();

		DocIdSet queryDocIds = template.getDocIdSet(
				new QueryWrapperFilter(new FilteredQuery(query, codingSchemeFilter)));
		
		DocIdSetIterator queryItr = queryDocIds.iterator();

		BitSet bitSet = new BitSet();

		try {
			while(boundaryItr.next()) {
				bitSet.set(boundaryItr.doc());
			}

			while(queryItr.next()) {
				int docId = queryItr.doc();
				int startDoc = getPreviousSetBit(bitSet,docId);
				int endDoc = bitSet.nextSetBit(docId);      
				
				if(endDoc == -1) {
					endDoc = template.getMaxDoc();
				}

				bitSet.set(startDoc, endDoc);
			}

			return new BitSetFilter(bitSet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static class BitSetFilter extends Filter {

		private static final long serialVersionUID = 100126269349636772L;
		private BitSet bitSet;
		
		private BitSetFilter(BitSet bitSet){
			this.bitSet = bitSet;
		}
		
		@Override
		public BitSet bits(IndexReader reader) throws IOException {
			return this.bitSet;
		}
	}
	
	private int getPreviousSetBit(BitSet bitSet, int index) {
		for(int i=index;i>=0;i--) {
			if(bitSet.get(i)) {
				return i;
			}
		}
		return 0;
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
			LuceneIndexTemplate template,
			Filter boundaryDocFilter,
			Filter codingSchemeFilter,
			List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) throws Exception {

        int maxDoc = template.getMaxDoc();

        List<ScoreDoc> scoreDocs = null;
        
        DocIdSet boundaryDocIds = template.getDocIdSet(boundaryDocFilter);

        List<BitSet> bitSets = new ArrayList<BitSet>();
        
        if(bitSetQueries != null){
            for(Query query : bitSetQueries){
                BitSetBestScoreOfEntityHitCollector bitSetCollector =
                    new BitSetBestScoreOfEntityHitCollector(
                    		boundaryDocIds.iterator(), 
                                    maxDoc);
                template.search(query, codingSchemeFilter, bitSetCollector);
                bitSets.add(bitSetCollector.getResult());
            }
        }
        
        if(combinedQuery.size() == 1){
            BitSetFilteringBestScoreOfEntityHitCollector collector =
                new BitSetFilteringBestScoreOfEntityHitCollector(
                        this.getBitSet(bitSets, BitSetOp.AND),
                        boundaryDocIds.iterator(), 
                                maxDoc);
            
            template.search(combinedQuery.get(0), codingSchemeFilter, collector);
            scoreDocs = collector.getResult();
        } else {
            HitCollectorMerger merger = new HitCollectorMerger(
            		boundaryDocIds.iterator(), 
            		maxDoc);
            for(Query query : combinedQuery){
                BestScoreOfEntityHitCollector collector =
                    new BitSetFilteringBestScoreOfEntityHitCollector(
                            this.getBitSet(bitSets, BitSetOp.AND),
                            boundaryDocIds.iterator(), 
                                    maxDoc); 

                template.search(query, codingSchemeFilter, collector);
                merger.addHitCollector(collector);
            }
            scoreDocs = merger.getMergedScoreDocs();
        }
        
        return scoreDocs;
	}
	
	@Override
	public Document getDocumentById(String codingSchemeUri, String version,
			int id) {
		return getDocumentById(codingSchemeUri, version, id, null);
	}
	
	@Override
	public Document getDocumentById(String codingSchemeUri, String version,
			int id, FieldSelector fieldSelector) {
		return getLuceneIndexTemplate(codingSchemeUri, version).getDocumentById(id, fieldSelector);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public Query getMatchAllDocsQuery(
			String codingSchemeUri, String version) {
		TermQuery query = new NonScoringTermQuery(
						new Term(
						LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
						LuceneLoaderCode.createCodingSchemeUriVersionKey(codingSchemeUri, version)));

		return query;
	}

	/**
	 * And bit sets.
	 * 
	 * @param bitSets the bit sets
	 * 
	 * @return the bit set
	 */
	private BitSet getBitSet(List<BitSet> bitSets, BitSetOp op){
		BitSet totalBitSet = null;
		for(BitSet bitSet : bitSets){
			if(totalBitSet == null){
				totalBitSet = bitSet;
			} else {
				switch (op) {
					case AND: {
						totalBitSet.and(bitSet);
						continue;
					}
					case OR: {
						totalBitSet.or(bitSet);
						continue;
					}
				}
			}
		}
		return totalBitSet;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.AbstractBaseIndexDao#doGetSupportedLexEvsIndexFormatVersions()
	 */
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(
				LexEvsIndexFormatVersion.class, 
				supportedIndexVersion2010);
	}
	
	@Override
	protected LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version) {
		return this.getIndexRegistry().getLuceneIndexTemplate(codingSchemeUri, version);
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}
}