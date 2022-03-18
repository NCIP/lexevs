
package org.lexevs.dao.index.lucenesupport;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexSearcherCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexWriterCallback;

public interface LuceneIndexTemplate {

	public void addDocuments(List<Document> documents,
			Analyzer analyzer);

	public void removeDocuments(Term term);

	public void removeDocuments(Query query);

	public void search(Query query, Filter filter,
			Collector Collector);
	
	public int getMaxDoc();
	
	public Document getDocumentById(int id, StoredFieldVisitor fieldSelector);
	
	public Document getDocumentById(int id);
	
	public DocIdSet getDocIdSet(Filter filter);

	public String getIndexName();
	
	public <T> T executeInIndexReader(IndexReaderCallback<T> callback);
	
	public <T> T executeInIndexSearcher(IndexSearcherCallback<T> callback);

	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback);
	
	public List<ScoreDoc> search(final Query query, final Filter filter);


	public Query getCombinedQueryFromSchemes(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			BooleanQuery query);

	Document getDocumentById(int id, Set<String> fields);

	public void blockJoinSearch(Query query, Filter codingSchemeFilter,
			TopScoreDocCollector hitCollector);
}