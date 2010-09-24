package org.lexevs.dao.index.lucenesupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searchable;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class MultiBaseLuceneIndexTemplate extends BaseLuceneIndexTemplate {
	
	public MultiBaseLuceneIndexTemplate(){
		super();
	}
	
	public MultiBaseLuceneIndexTemplate(List<NamedDirectory> namedDirectories){
		super();
		try {
			this.setIndexSearcher(this.createIndexSearcher(namedDirectories));
			this.setIndexReader(this.createIndexReader(namedDirectories));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected MultiSearcher createIndexSearcher(List<NamedDirectory> namedDirectories) throws Exception {
		List<Searchable> searchables = new ArrayList<Searchable>();
		
		for(NamedDirectory dir : namedDirectories) {
			IndexSearcher indexSearcher = 
				this.createIndexSearcher(dir);
			
			searchables.add(indexSearcher);
		}
		return new MultiSearcher(searchables.toArray(new Searchable[searchables.size()]));
	}
	
	protected MultiReader createIndexReader(List<NamedDirectory> namedDirectories) throws Exception {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for(NamedDirectory dir : namedDirectories) {
			IndexReader reader = 
				this.createIndexReader(dir);
			
			readers.add(reader);
		}
		return new MultiReader(readers.toArray(new IndexReader[readers.size()]));
	}
}
