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

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
//import org.apache.lucene.search.MultiSearcher;
//import org.apache.lucene.search.Searchable;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class MultiBaseLuceneIndexTemplate extends BaseLuceneIndexTemplate {
	
	private List<NamedDirectory> namedDirectories;
	
	public MultiBaseLuceneIndexTemplate(){
		super();
	}
	
	public MultiBaseLuceneIndexTemplate(List<NamedDirectory> namedDirectories){
		super();
		try {
			this.setIndexSearcher(this.createIndexSearcher(namedDirectories));
			this.setIndexReader(this.createIndexReader(namedDirectories));
			this.namedDirectories = namedDirectories;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ToParentBlockJoinIndexSearcher createIndexSearcher(
			List<NamedDirectory> namedDirectories2) {
		// TODO Auto-generated method stub
		return null;
	}

	protected MultiReader createReader(IndexReader[] namedDirectories) throws Exception {
//		List<Searchable> searchables = new ArrayList<Searchable>();
		
//		for(NamedDirectory dir : namedDirectories) {
//			searchables.add(dir.getIndexSearcher());
//		}
		return new MultiReader(namedDirectories);
	}
	
	protected MultiReader createIndexReader(List<NamedDirectory> namedDirectories) throws Exception {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for(NamedDirectory dir : namedDirectories) {
			readers.add(dir.getIndexReader());
		}
		return new MultiReader(readers.toArray(new IndexReader[readers.size()]));
	}

	@Override
	public void optimize() {
		for(NamedDirectory namedDirectory : this.namedDirectories) {
		try {
			IndexWriter writer = 
				createIndexWriter(namedDirectory);
			
//			if(! namedDirectory.getIndexReader().isOptimized()){
//				writer.optimize();
//			}
			
			namedDirectory.refresh();
			
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
	}

	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
	}
	
	protected void doFinalize() throws Throwable {
		//no-op -- this is a prototype-scoped class, don't close underlying Lucene
		//resources on finalize
	}
}