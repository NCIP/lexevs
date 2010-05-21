package org.lexevs.dao.index.lucenesupport;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;

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

}