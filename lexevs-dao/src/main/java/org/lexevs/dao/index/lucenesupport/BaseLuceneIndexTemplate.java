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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.Utility;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class BaseLuceneIndexTemplate implements DisposableBean, LuceneIndexTemplate {

	private NamedDirectory namedDirectory;
	
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	
	private Analyzer analyzer = LuceneLoaderCode.getAnaylzer();
//	private Analyzer analyzer = Utility.getAnalyzer();
	
	public BaseLuceneIndexTemplate(){
		super();
	}
	
	public BaseLuceneIndexTemplate(NamedDirectory namedDirectory){
		super();
		try {
			indexReader = namedDirectory.getIndexReader();
			indexSearcher = new IndexSearcher(indexReader);
			this.namedDirectory = namedDirectory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
				indexSearcher.search(query, filter, hitCollector);
				return null;
			}
		});	
	}
	
	public List<ScoreDoc> search(final Query query, final Filter filter){
		return this.doInIndexSearcher(new IndexSearcherCallback<List<ScoreDoc>>() {

			@Override
			public List<ScoreDoc> doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				
				final List<ScoreDoc> docs = new ArrayList<ScoreDoc>();
				
				indexSearcher.search(query, filter, new Collector() {

					@Override
					public LeafCollector getLeafCollector(LeafReaderContext arg0)
							throws IOException {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public boolean needsScores() {
						// TODO Auto-generated method stub
						return false;
					}
					
//					public void collect(int doc, float score) {
//						ScoreDoc scoreDoc = new ScoreDoc(doc, score);
//						docs.add(scoreDoc);
//					}
				});
				
				return docs;
			}
		});	
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Collector)
	 */
//	public Document getDocumentById(final int id){
//		return this.getDocumentById(id, null);
//	}
	
	public Document getDocumentById(final int id){
		return this.doInIndexSearcher(new IndexSearcherCallback<Document>() {

			@Override
			public Document doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
//				if(fieldSelector != null) {
//					return indexSearcher.doc(id);
//				} else {
					return indexSearcher.doc(id);
//				}
			}
		});	
	}

	@Override
	public DocIdSet getDocIdSet(final Filter filter) {
		return this.doInIndexReader(new IndexReaderCallback<DocIdSet>() {

			@Override
			public DocIdSet doInIndexReader(IndexReader indexReader)
					throws Exception {
	//			return filter.getDocIdSet(indexReader.getContext());
				return null;
			}
		});
	}

	public int getMaxDoc(){
		return this.doInIndexSearcher(new IndexSearcherCallback<Integer>() {

			@Override
			public Integer doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
	//			return indexSearcher.maxDoc();
				return null;
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
					//, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
		
//		writer.setMergeFactor(20);
//		writer.setRAMBufferSizeMB(500);
		
		return writer;
	}
	
	public interface IndexReaderCallback<T> {
		
		public T doInIndexReader(IndexReader indexReader) throws Exception;
	}
	
	public interface IndexSearcherCallback<T> {
		
		public T doInIndexSearcher(IndexSearcher indexSearcher) throws Exception;
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
//		if(this.indexSearcher != null) {
//			this.indexSearcher.close();
//		}
	}

	@Override
	public Document getDocumentById(int id, StoredFieldVisitor fieldSelector) {
		// TODO Auto-generated method stub
		return null;
	}
}