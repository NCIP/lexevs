package org.lexevs.dao.index.lucenesupport;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexSearcherCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexWriterCallback;

public interface LuceneIndexTemplate {

	public void addDocuments(List<Document> documents,
			Analyzer analyzer);

	public void removeDocuments(Term term);

	public void removeDocuments(Query query);

	public void search(Query query, Filter filter,
			HitCollector hitCollector);
	
	public void optimize();
	
	public int getMaxDoc();
	
	public Document getDocumentById(int id);
	
	public DocIdSet getDocIdSet(Filter filter);

	public String getIndexName();
	
	public <T> T executeInIndexReader(IndexReaderCallback<T> callback);
	
	public <T> T executeInIndexSearcher(IndexSearcherCallback<T> callback);

	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback);
	
	public List<ScoreDoc> search(final Query query, final Filter filter);
}