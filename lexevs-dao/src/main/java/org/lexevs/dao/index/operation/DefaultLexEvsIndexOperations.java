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
package org.lexevs.dao.index.operation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.AbstractLoggingBean;
import org.lexevs.system.model.LocalCodingScheme;
//import org.apache.lucene.index.TermEnum;

public class DefaultLexEvsIndexOperations extends AbstractLoggingBean implements LexEvsIndexOperations {
	
	private IndexCreator indexCreator;
	private IndexRegistry indexRegistry;
	private IndexDaoManager indexDaoManager;
	//private MetaData metaData;
	private ConcurrentMetaData concurrentMetaData;
	
	@Override
	public void registerCodingSchemeEntityIndex(String codingSchemeUri,
			String version) {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeUri);
		ref.setCodingSchemeVersion(version);
		
		indexCreator.index(ref, null, true);
	}
	
	@Override
	public void cleanUp(
			final List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes, boolean reindexMissing) {
		getLogger().warn("Starting Cleanup of Entity Lucene Index.");
	
//		indexRegistry.getCommonLuceneIndexTemplate().executeInIndexReader(new IndexReaderCallback<Void>() {
//
//			@Override
//			public Void doInIndexReader(IndexReader indexReader)
//					throws Exception {
//				
//				final Map<String, AbsoluteCodingSchemeVersionReference> expectedMap = 
//					getExpectedMap(expectedCodingSchemes);
//				
//				Set<String> indexSet = new HashSet<String>();
//				
//				Term term = new Term(LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD, "");
////				TermsEnum termEnum = indexReader.
//				
//				boolean hasNext = true;
////				while(hasNext && 
////						termEnum.term() != null && 
////						termEnum.term().field().equals(LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD)) {
////					String key = termEnum.term().text();
////					indexSet.add(key);
////					
////					hasNext = termEnum.next();
////				}
//				
//				Set<String> foundIndexSet = new HashSet<String>(indexSet);
//				foundIndexSet.removeAll(expectedMap.keySet());
//	
//				if(foundIndexSet.size() == 0) {
//					getLogger().warn("No extra Lucene artifacts found.");
//				} else {
//					getLogger().warn(foundIndexSet.size() + " extra Lucene artifacts found.");
//				}
//				
//				for(String additional : foundIndexSet) {
//				
//					BooleanQuery query = new BooleanQuery();
//					query.add(new TermQuery(
//										new Term(
//												LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD, additional)), Occur.MUST);
//					query.add(new TermQuery(
//							new Term(
//									"codeBoundry", "T")), Occur.MUST_NOT);
//					
//					List<ScoreDoc> scoreDocs = 
//						indexRegistry.getCommonLuceneIndexTemplate().search(
//								query, 
//								null);
//					
//					if(scoreDocs.size() == 0) {
//						getLogger().warn("No Documents to remove for for: " + additional);
//						continue;
//					}
//					
//					ScoreDoc scoreDoc = scoreDocs.get(0);
//					
//					Document document = indexReader.document(scoreDoc.doc);
//					
//					String uri = document.getField(LuceneLoaderCode.CODING_SCHEME_ID_FIELD).stringValue();
//					String version = document.getField(LuceneLoaderCode.CODING_SCHEME_VERSION_FIELD).stringValue();
//					String codingSchemeName = document.getField(LuceneLoaderCode.CODING_SCHEME_NAME_FIELD).stringValue();
//					
//					getLogger().warn("Found an extra Lucene Index for URI: " + uri + " Version: " + version + 
//						". Attempting to remove...");
//					
//					AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
//					ref.setCodingSchemeURN(uri);
//					ref.setCodingSchemeVersion(version);
//					
//					dropIndex(codingSchemeName, ref);
//					
//					getLogger().warn("Extra Lucene Index for URI: " + uri + " Version: " + version + 
//						" Successfully removed.");
//				}
//				
//				expectedMap.keySet().removeAll(indexSet);
//				
//				if(expectedMap.size() == 0) {
//					getLogger().warn("No missing Lucene artifacts found.");
//				} else {
//					getLogger().warn(expectedMap.size() + " missing Lucene artifacts found.");
//				}
//				
//				for(String key : expectedMap.keySet()) {
//					AbsoluteCodingSchemeVersionReference ref = 
//						expectedMap.get(key);
//					getLogger().warn("Re-creating missing Lucene index for URI: " +
//							ref.getCodingSchemeURN() +
//							" Version: " + ref.getCodingSchemeVersion());
//					
//					LexEvsServiceLocator.getInstance().
//						getIndexServiceManager().
//							getEntityIndexService().
//								createIndex(ref);
//				}
//				
//				
//				return null;
//			}
//			
//		});
		
//		this.cleanupSearchIndex(expectedCodingSchemes);
	}

	protected Map<String, AbsoluteCodingSchemeVersionReference> getExpectedMap(
			List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes) {
		final Map<String,AbsoluteCodingSchemeVersionReference> expectedMap = 
			new HashMap<String,AbsoluteCodingSchemeVersionReference>();
		
		for(AbsoluteCodingSchemeVersionReference ref : expectedCodingSchemes) {
			String key = LuceneLoaderCode.createCodingSchemeUriVersionKey(
					ref.getCodingSchemeURN(), 
					ref.getCodingSchemeVersion());
			expectedMap.put(key, ref);
		}
		return expectedMap;
	}
	
	public String getLexEVSIndexLocation(){
        return LexEvsServiceLocator.getInstance().getSystemResourceService().
                getSystemVariables().getAutoLoadIndexLocation();
	}
	
	
	
	protected void dropIndex(String codingSchemeName, AbsoluteCodingSchemeVersionReference reference) {
		
		this.indexRegistry.unRegisterCodingSchemeIndex(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
		
		String key = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, reference.getCodingSchemeVersion()).getKey();
		try {
			concurrentMetaData.removeIndexMetaDataValue(key);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		try {
			this.indexRegistry.destroyIndex(Utility.getIndexName(reference));
		} catch (LBParameterException e) {
			throw new RuntimeException("Problem deleting index from disk", e);
		}
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}
	
	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
		String indexLocation = getLexEVSIndexLocation();
        File indexParentFolder = new File(indexLocation);
        File[] indexes = indexParentFolder.listFiles();
        for(File index: indexes){
        	if(index.getName().equals(Utility.getIndexName(ref))){
        		return true;
        	}
        }
		return false;
	}
}