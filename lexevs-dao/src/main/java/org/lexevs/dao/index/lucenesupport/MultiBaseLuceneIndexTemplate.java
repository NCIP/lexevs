package org.lexevs.dao.index.lucenesupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searchable;
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

	protected MultiSearcher createIndexSearcher(List<NamedDirectory> namedDirectories) throws Exception {
		List<Searchable> searchables = new ArrayList<Searchable>();
		
		for(NamedDirectory dir : namedDirectories) {
			searchables.add(dir.getIndexSearcher());
		}
		return new MultiSearcher(searchables.toArray(new Searchable[searchables.size()]));
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
			
			if(! namedDirectory.getIndexReader().isOptimized()){
				writer.optimize();
			}
			
			namedDirectory.refresh();
			
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
	}

	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		throw new UnsupportedOperationException("Cannot use a Multi-template for write operations.");
	}
}
