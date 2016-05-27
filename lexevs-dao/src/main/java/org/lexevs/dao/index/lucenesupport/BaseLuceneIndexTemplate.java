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
package org.lexevs.dao.index.lucenesupport;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.logging.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class BaseLuceneIndexTemplate implements InitializingBean, DisposableBean, LuceneIndexTemplate {

	private static LgLoggerIF logger = LoggerFactory.getLogger();
	
	private NamedDirectory namedDirectory;
	private ToParentBlockJoinIndexSearcher blockJoinSearcher;
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	
	private Analyzer analyzer = LuceneLoaderCode.getAnaylzer();
	
	public BaseLuceneIndexTemplate(){
		super();
	}
	
	public BaseLuceneIndexTemplate(NamedDirectory namedDirectory){
		super();
		try {
			indexReader = namedDirectory.getIndexReader();
			indexSearcher = new IndexSearcher(indexReader);
			blockJoinSearcher = new ToParentBlockJoinIndexSearcher(indexReader);
			this.namedDirectory = namedDirectory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		indexReader = namedDirectory.getIndexReader();
		indexSearcher = namedDirectory.getIndexSearcher();
		blockJoinSearcher = namedDirectory.getBlockJoinSearcher();
		
	}

	@Override
	public String getIndexName() {
		return namedDirectory.getIndexName();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#addDocuments(java.util.List, org.apache.lucene.analysis.Analyzer)
	 */
	public void addDocuments(final List<Document> documents, final Analyzer analyzer) {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.addDocuments(documents);
				return null;
			}	
		});
	}
	
	@Deprecated
	public void optimize() {
//		throw new UnsupportedOperationException("Optimizing is an outdated indexing operation and is no longer supported");
		System.out.println("Optimizing is an outdated indexing operation and is no longer supported");
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#removeDocuments(org.apache.lucene.index.Term)
	 */
	public void removeDocuments(final Term term) {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.deleteDocuments(term);
				return null;
			}	
		});
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#removeDocuments(org.apache.lucene.search.Query)
	 */
	public void removeDocuments(final Query query) {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.deleteDocuments(query);
				return null;
			}	
		});
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Collector)
	 */
	public void search(final Query query, final Filter filter, final Collector hitCollector){
		this.doInIndexSearcher(new IndexSearcherCallback<Void>() {

			@Override
			public Void doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				indexSearcher.search(query, hitCollector);
				return null;
			}
		});	
	}
	
	public List<ScoreDoc> search(final Query query, final Filter filter){
		return this.doInIndexSearcher(new IndexSearcherCallback<List<ScoreDoc>>() {

			@Override
			public List<ScoreDoc> doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				
				List<ScoreDoc> docs = null;
				
				int maxDoc = getMaxDoc();
				
				if (maxDoc == 0) {
				    logger.error("Index does not exist.");
				    throw new RuntimeException("Index does not exist.");
				}
				
				final TopScoreDocCollector collector = TopScoreDocCollector.create(maxDoc);
				indexSearcher.search(query, collector
				);
				docs = Arrays.asList(collector.topDocs().scoreDocs);
				return docs;
			}
		}
		);	
		
	}
	
	@Override
	public void blockJoinSearch(final Query query, final Filter codingSchemeFilter,
			final TopScoreDocCollector collector) {
		this.doInBlockJoinIndexSearcher(new ToParentBlockJoinIndexSearcherCallback<Void>() {

			@Override
			public Void doInBlockJoinIndexSearcher(ToParentBlockJoinIndexSearcher indexSearcher)
					throws Exception {
				indexSearcher.search(query, collector);
				return null;
			}
		}
		);	
		
	}
	
	public List<ScoreDoc> blockJoinSearch(final Query query, final Filter filter){
		return this.doInBlockJoinIndexSearcher(new ToParentBlockJoinIndexSearcherCallback<List<ScoreDoc>>() {

			@Override
			public List<ScoreDoc> doInBlockJoinIndexSearcher(ToParentBlockJoinIndexSearcher blockJoinSearcher)
					throws Exception {
				
				List<ScoreDoc> docs = null;
				
				int maxDoc = getMaxDoc();
				
				if (maxDoc == 0) {
				    logger.error("Index does not exist.");
				    throw new RuntimeException("Index does not exist.");
				}
				
				final TopScoreDocCollector collector = TopScoreDocCollector.create(maxDoc);
				blockJoinSearcher.search(query, collector
				);
				docs = Arrays.asList(collector.topDocs().scoreDocs);
				return docs;
			}
		}
		);	
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Collector)
	 */
	
	public Document getDocumentById(final int id){
		return this.doInIndexSearcher(new IndexSearcherCallback<Document>() {

			@Override
			public Document doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
					return indexSearcher.doc(id);
			}
		});	
	}

	@Override
	public DocIdSet getDocIdSet(final Filter filter) {
		return this.doInIndexReader(new IndexReaderCallback<DocIdSet>() {

			@Override
			public DocIdSet doInIndexReader(IndexReader indexReader)
					throws Exception {
				throw new UnsupportedOperationException("getting DocIdSet not supported");
			}
		});
	}

	public int getMaxDoc(){
		return this.doInIndexReader(new IndexReaderCallback<Integer>() {

			@Override
			public Integer doInIndexReader(IndexReader indexReader)
					throws Exception {
				return indexReader.maxDoc();
			}
		});	
	}
	
	protected <T> T doInIndexReader(IndexReaderCallback<T> callback) {
		try {
			return callback.doInIndexReader(indexReader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected <T> T doInIndexSearcher(IndexSearcherCallback<T> callback) {
		try {
			return callback.doInIndexSearcher(indexSearcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected <T> T doInBlockJoinIndexSearcher(ToParentBlockJoinIndexSearcherCallback<T> toParentBlockJoinIndexSearcherCallback) {
		try {
			return toParentBlockJoinIndexSearcherCallback.doInBlockJoinIndexSearcher(blockJoinSearcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		try {
		IndexWriter writer = 
			createIndexWriter(namedDirectory);
		
		T result = callback.doInIndexWriter(writer);
		
		writer.close();
		
		namedDirectory.refresh();
		
		this.indexReader = namedDirectory.getIndexReader();
		this.indexSearcher = new IndexSearcher(indexReader);
		
		return result;
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	protected IndexWriter createIndexWriter(NamedDirectory namedDirectory) throws Exception {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = 
			new IndexWriter(namedDirectory.getDirectory(), config);
		return writer;
	}
	
	public interface IndexReaderCallback<T> {
		
		public T doInIndexReader(IndexReader indexReader) throws Exception;
	}
	
	public interface IndexSearcherCallback<T> {
		
		public T doInIndexSearcher(IndexSearcher indexSearcher) throws Exception;
	}
	
	public interface ToParentBlockJoinIndexSearcherCallback<T> {
		public T doInBlockJoinIndexSearcher(ToParentBlockJoinIndexSearcher indexSearcher) throws Exception;
	}
	
	public interface IndexWriterCallback<T> {
		
		public T doInIndexWriter(IndexWriter indexWriter) throws Exception;
	}

	@Override
	public <T> T executeInIndexReader(IndexReaderCallback<T> callback) {
		return this.doInIndexReader(callback);
	}

	@Override
	public <T> T executeInIndexSearcher(IndexSearcherCallback<T> callback) {
		return this.doInIndexSearcher(callback);
	}
	
	public <T> T executeInBlockJoinIndexSearcher(ToParentBlockJoinIndexSearcherCallback<T> callback) {
		return this.doInBlockJoinIndexSearcher(callback);
	}
	
	@Override
	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback) {
		return this.doInIndexWriter(callback);
	}

	@Override
	public void destroy() throws Exception {
//		this.indexSearcher.close();
		this.indexReader.close();
//		IndexWriter.unlock(this.namedDirectory.getDirectory());
	}

	public NamedDirectory getNamedDirectory() {
		return this.namedDirectory;
	}

	public void setNamedDirectory(NamedDirectory namedDirectory) {
		this.namedDirectory = namedDirectory;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	protected IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	protected void setIndexSearcher(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}

	protected IndexReader getIndexReader() {
		return indexReader;
	}

	protected void setIndexReader(IndexReader indexReader) {
		this.indexReader = indexReader;
	}
	
	public void finalize() throws Throwable {
		super.finalize();
		this.doFinalize();
	}
	
	protected void doFinalize() throws Throwable {
		if(this.indexReader != null) {
			this.indexReader.close();
		}
	}

	@Override
	public Document getDocumentById(final int id, final Set<String> fields) {
		return this.doInIndexReader(new IndexReaderCallback<Document>() {

			@Override
			public Document doInIndexReader(IndexReader indexReader)
					throws Exception {
				return indexReader.document(id, fields);
			}
		});	
	}


	@Override
	public Query getCombinedQueryFromSchemes(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			BooleanQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocumentById(int id, StoredFieldVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}


}