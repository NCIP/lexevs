package org.lexevs.dao.index.lucenesupport;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class BaseLuceneIndexTemplate implements InitializingBean, DisposableBean, LuceneIndexTemplate {

	private NamedDirectory namedDirectory;
	
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	
	private Analyzer analyzer = LuceneLoaderCode.getAnaylzer();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		indexSearcher = new IndexSearcher(namedDirectory.getDirectory());
		indexReader = IndexReader.open(namedDirectory.getDirectory(), true);
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
				for(Document doc : documents) {
					indexWriter.addDocument(doc, analyzer);
				}
				return null;
			}	
		});
	}
	
	public void optimize() {
		this.doInIndexWriter(new IndexWriterCallback<Void>() {

			@Override
			public Void doInIndexWriter(IndexWriter indexWriter) throws Exception {
				indexWriter.optimize();
				return null;
			}	
		});
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
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.HitCollector)
	 */
	public void search(final Query query, final Filter filter, final HitCollector hitCollector){
		this.doInIndexSearcher(new IndexSearcherCallback<Void>() {

			@Override
			public Void doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				indexSearcher.search(query, filter, hitCollector);
				return null;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.HitCollector)
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
				return filter.getDocIdSet(indexReader);
			}
		});
	}

	public int getMaxDoc(){
		return this.doInIndexSearcher(new IndexSearcherCallback<Integer>() {

			@Override
			public Integer doInIndexSearcher(IndexSearcher indexSearcher)
					throws Exception {
				return indexSearcher.maxDoc();
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
			new IndexWriter(namedDirectory.getDirectory(), analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
		
		writer.setMergeFactor(20);
		writer.setRAMBufferSizeMB(500);
		
		T result = callback.doInIndexWriter(writer);
		
		writer.close();
		
		indexReader = indexReader.reopen();
		indexSearcher = new IndexSearcher(indexReader);
		
		return result;
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
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
	public void destroy() throws Exception {
		this.indexSearcher.close();
		this.indexReader.close();
		IndexWriter.unlock(this.namedDirectory.getDirectory());
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
}
