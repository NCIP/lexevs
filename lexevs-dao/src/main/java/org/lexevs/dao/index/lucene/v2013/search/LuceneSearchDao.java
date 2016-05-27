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
package org.lexevs.dao.index.lucene.v2013.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopScoreDocCollector;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.search.SearchDao;
import org.lexevs.dao.index.lucene.AbstractFilteringLuceneIndexTemplateDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.MultiBaseLuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class LuceneEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneSearchDao extends AbstractFilteringLuceneIndexTemplateDao implements SearchDao {
	
	/** The supported index version2013. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2013 = LexEvsIndexFormatVersion.parseStringToVersion("2013");
	
	private static LgLoggerIF logger = LoggerFactory.getLogger();

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
	public String getIndexName(String codingSchemeUri, String version) {
		return getLuceneIndexTemplate(codingSchemeUri, version).getIndexName();
	}

	@Override
	public Filter getCodingSchemeFilter(String uri, String version) {
		return null;
	}

	@Override
	public Document getById(int id) {
		return this.getLuceneIndexTemplate().getDocumentById(id);
	}
	
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int id) {
		List<AbsoluteCodingSchemeVersionReference> list = Arrays.asList(codeSystemsToInclude.
				toArray(new AbsoluteCodingSchemeVersionReference[codeSystemsToInclude.size()]));
		return this.getLuceneIndexTemplate(list).getDocumentById(id);
	}


	@Override
	public List<ScoreDoc> query(Query query) {
		try {
			LuceneIndexTemplate template = this.getLuceneIndexTemplate();
	
			int maxDoc = template.getMaxDoc();
			
			if (maxDoc == 0) {
				String errorMsg = "Index does not exist: " + template.getIndexName();
			    logger.error(errorMsg);
			    throw new RuntimeException(errorMsg);
			}
			
			final List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>();
			
						
			template.search(query, null, new Collector() {

				@Override
				public LeafCollector getLeafCollector(final LeafReaderContext context)
						throws IOException {
					return new LeafCollector() {
						
						private Scorer scorer;

						@Override
						public void collect(int docId) throws IOException {
							scoreDocs.add(new ScoreDoc(context.docBase + docId, this.scorer.score()));
						}

						@Override
						public void setScorer(Scorer scorer) throws IOException {
							this.scorer = scorer;
						}
						
					};
				}

				@Override
				public boolean needsScores() {
					return true;
				}
			
			});
			
			Collections.sort(scoreDocs, new Comparator<ScoreDoc>() {

				@Override
				public int compare(ScoreDoc o1, ScoreDoc o2) {
					if(o1.score > o2.score)return -1;
					if(o1.score < o2.score)return 1;
					return 0;
				}
				
			});
			return scoreDocs;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ScoreDoc> query(Query query,
			Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude) {
		List<AbsoluteCodingSchemeVersionReference> list = Arrays.asList(codeSystemsToInclude.
				toArray(new AbsoluteCodingSchemeVersionReference[codeSystemsToInclude.size()]));
		return getIndexRegistry().getCommonLuceneIndexTemplate(list).search(query, null);
	}

	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.AbstractBaseIndexDao#doGetSupportedLexEvsIndexFormatVersions()
	 */
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2013);
	}
	
	protected LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version) {
		return getIndexRegistry().getLuceneIndexTemplate(codingSchemeUri, version);
	}
	
	protected LuceneIndexTemplate getLuceneIndexTemplate( List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		return getIndexRegistry().getCommonLuceneIndexTemplate(codingSchemes);
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return new MultiBaseLuceneIndexTemplate(MultiBaseLuceneIndexTemplate.getNamedDirectories(ConcurrentMetaData.getInstance()));
	}

}