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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MultiBaseLuceneIndexTemplate extends BaseLuceneIndexTemplate implements InitializingBean, DisposableBean {
	
	private ConcurrentMetaData metaDirectories;
	private List<NamedDirectory> namedDirectories;
	private IndexReader indexReader;
	
	private ToParentBlockJoinIndexSearcher indexSearcher;

	
	public MultiBaseLuceneIndexTemplate(){
		super();
	}
	
	public MultiBaseLuceneIndexTemplate(List<NamedDirectory> namedDirectories){
		super();
		try {
			this.setIndexReader(this.createIndexReader(namedDirectories));
			this.namedDirectories = namedDirectories;
			this.setIndexSearcher(this.createIndexSearcher(indexReader));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<NamedDirectory> getNamedDirectories(
			ConcurrentMetaData metaDirectories) {
		List<NamedDirectory> directories = new ArrayList<NamedDirectory>();
		for(CodingSchemeMetaData csmd : metaDirectories.getCodingSchemeList()){
			directories.add(csmd.getDirectory());
		}
		return directories;
	}

	private ToParentBlockJoinIndexSearcher createIndexSearcher(IndexReader reader) {
		return new ToParentBlockJoinIndexSearcher(reader);
	}

	protected MultiReader createReader(IndexReader[] namedDirectories) throws Exception {
		return new MultiReader(namedDirectories);
	}
	
	protected MultiReader createIndexReader(List<NamedDirectory> namedDirectories) throws Exception {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for(NamedDirectory dir : namedDirectories) {
			readers.add(dir.getIndexReader());
		}
		return new MultiReader(readers.toArray(new IndexReader[readers.size()]));
	}

	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
	}
	
//	protected void doFinalize() throws Throwable {
//		throw new UnsupportedOperationException("Closing this Multi Index Reader would close all readers");
//	}

	public ConcurrentMetaData getMetaDirectories() {
		return metaDirectories;
	}

	public void setMetaDirectories(ConcurrentMetaData metaDirectories) {
		this.metaDirectories = metaDirectories;
	}

	public List<NamedDirectory> getNamedDirectories() {
		return namedDirectories;
	}

	public void setNamedDirectories(List<NamedDirectory> namedDirectories) {
		this.namedDirectories = namedDirectories;
	}


	public void addDocuments(List<Document> documents, Analyzer analyzer) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
		
	}

	@Override
	public void removeDocuments(Term term) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
		
	}

	@Override
	public void removeDocuments(Query query) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
	}



	@Override
	public int getMaxDoc() {
	return indexReader.maxDoc();
	}

	@Override
	public Document getDocumentById(int id, StoredFieldVisitor fieldSelector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocumentById(int id) {
	   try {
		return indexReader.document(id);
	} catch (IOException e) {
		throw new RuntimeException("Failed reading document id in Multi Reader", e);
	}
	}

	@Override
	public DocIdSet getDocIdSet(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIndexName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public <T> T executeInIndexReader(IndexReaderCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T executeInIndexSearcher(IndexSearcherCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	protected <T> T doInIndexSearcher(IndexSearcherCallback<T> callback) {
		try {
			return callback.doInIndexSearcher(this.indexSearcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ScoreDoc> search(final Query query, Filter filter) {
		return this.doInIndexSearcher(new IndexSearcherCallback<List<ScoreDoc>>() {

			@Override
			public List<ScoreDoc> doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				
				List<ScoreDoc> docs = null;
				final TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxDoc());
				indexSearcher.search(query, collector
				);
				docs = Arrays.asList(collector.topDocs().scoreDocs);
				return docs;
			}
		}
		);	
	}

	@Override
	public void search(final Query query, final Filter filter, final Collector Collector) {
		this.doInIndexSearcher(new IndexSearcherCallback<Void>() {

			@Override
			public Void doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				indexSearcher.search(query, Collector);
				return null;
			}
		});	
		
	}


	@Override
	public void destroy() throws Exception {
		this.indexReader.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ToParentBlockJoinIndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	public void setIndexSearcher(ToParentBlockJoinIndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}

	public IndexReader getIndexReader() {
		return indexReader;
	}

	public void setIndexReader(IndexReader indexReader) {
		this.indexReader = indexReader;
	}

	@Override
	public Query getCombinedQueryFromSchemes(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			BooleanQuery query) {
		throw new UnsupportedOperationException(
				"no longer supported in multi-index environment");
	}

	@Override
	public Document getDocumentById(int id, Set<String> fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blockJoinSearch(Query query, Filter codingSchemeFilter,
			TopScoreDocCollector hitCollector) {
		// TODO Auto-generated method stub
		
	}
		
}