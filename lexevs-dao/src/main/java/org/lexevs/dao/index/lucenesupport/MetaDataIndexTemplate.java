
package org.lexevs.dao.index.lucenesupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexWriterCallback;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

/**
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer </A>
 *
 */
public class MetaDataIndexTemplate implements LuceneIndexTemplate {


	private NamedDirectory namedDirectory;
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	
	private Analyzer analyzer = LuceneLoaderCode.getAnaylzer();
	public MetaDataIndexTemplate() {
		// TODO Auto-generated constructor stub
	}

	public MetaDataIndexTemplate(NamedDirectory namedDirectory){
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
	public void addDocuments(final List<Document> documents, final Analyzer analyzer) {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.addDocuments(documents);
				return null;
			}	
		});
	}

	@Override
	public void removeDocuments(final Term term) {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.deleteDocuments(term);
				return null;
			}	
		});
	}

	@Override
	public void removeDocuments(Query query) {
		// TODO Auto-generated method stub

	}

	@Override
	public void search(Query query, Filter filter, Collector Collector) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxDoc() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Document getDocumentById(int id, StoredFieldVisitor fieldSelector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocumentById(int id) {
		// TODO Auto-generated method stub
		return null;
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
	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScoreDoc> search(final Query query, final Filter filter){
		return this.doInIndexSearcher(new IndexSearcherCallback<List<ScoreDoc>>() {

			@Override
			public List<ScoreDoc> doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				
				final List<ScoreDoc> docs = new ArrayList<ScoreDoc>();
				
				indexSearcher.search(query, new Collector() {

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
					
				});
				
				return docs;
			}
		});	
		
	}
	
	protected <T> T doInIndexWriter(IndexWriterCallback<T> callback) {
		try {
		IndexWriter writer = 
			createIndexWriter(namedDirectory);
		
		T result = callback.doInIndexWriter(writer);
		
		writer.close();
		
		namedDirectory.refresh();
		
		this.indexReader = namedDirectory.getIndexReader();
		this.indexSearcher = new ToParentBlockJoinIndexSearcher(indexReader);
		
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
	
	protected <T> T doInIndexSearcher(IndexSearcherCallback<T> callback) {
		try {
			return callback.doInIndexSearcher(indexSearcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public interface IndexSearcherCallback<T> {
		
		public T doInIndexSearcher(IndexSearcher indexSearcher) throws Exception;
	}

	@Override
	public <T> T executeInIndexSearcher(
			org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexSearcherCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Query getCombinedQueryFromSchemes(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			BooleanQuery query) {
		// TODO Auto-generated method stub
		return null;
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