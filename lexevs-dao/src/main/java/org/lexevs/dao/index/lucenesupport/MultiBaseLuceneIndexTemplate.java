package org.lexevs.dao.index.lucenesupport;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

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

	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		for(NamedDirectory namedDirectory : this.namedDirectories) {
			try {
				IndexWriter writer = 
					new IndexWriter(namedDirectory.getDirectory(), getAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);

				writer.setMergeFactor(20);
				writer.setRAMBufferSizeMB(500);

				T result = callback.doInIndexWriter(writer);
				
				Assert.assertNull(result);

				writer.close();

				namedDirectory.refresh();

			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
		
		return null;
	}
}
