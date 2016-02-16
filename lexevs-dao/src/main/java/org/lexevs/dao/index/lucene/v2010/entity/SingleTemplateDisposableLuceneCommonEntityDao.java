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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopScoreDocCollector;
import org.lexevs.dao.index.access.entity.CommonEntityDao;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.index.lucene.AbstractBaseLuceneIndexTemplateDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class LuceneEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SingleTemplateDisposableLuceneCommonEntityDao extends AbstractBaseLuceneIndexTemplateDao implements CommonEntityDao {
    
	private static LgLoggerIF logger = LoggerFactory.getLogger();
    
	private LuceneIndexTemplate template;
	
	private List<AbsoluteCodingSchemeVersionReference> references;

	public SingleTemplateDisposableLuceneCommonEntityDao(
			IndexRegistry indexRegistry,
			LuceneIndexTemplate template,
			List<AbsoluteCodingSchemeVersionReference> references){
		this.template = template;
		this.references = references;
	}

	@Override
	public Document getDocumentById(int id) {
		return template.getDocumentById(id);
	}

	@Override
	public String getIndexName() {
		return template.getIndexName();
	}

	@Override
	public List<ScoreDoc> query(Query query) {
		
		int maxDoc = template.getMaxDoc();
		
		if (maxDoc == 0) {
			StringBuffer errorMsg = new StringBuffer("Index does not exist");
			
			if (!references.isEmpty()){
				errorMsg.append(": coding scheme: " + references.get(0).getCodingSchemeURN() + 
						" version: " +  references.get(0).getCodingSchemeVersion());
			}
			
		    logger.error(errorMsg.toString());
		    throw new RuntimeException(errorMsg.toString());
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
	}


	protected LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version) {
		return this.template;
	}

	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		throw new UnsupportedOperationException();
	}
}