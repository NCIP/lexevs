
package org.lexevs.dao.index.access.entity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

import java.util.List;
import java.util.Set;

/**
 * The Interface EntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityDao extends LexEvsIndexFormatVersionAwareDao {
	
	public String getIndexName(String codingSchemeUri, String version);

	public void deleteDocuments(String codingSchemeUri, String version, Term term);
	
	public void deleteDocuments(String codingSchemeUri, String version, Query query);

	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer);
	
	public Document getDocumentById(String codingSchemeUri, String version, int id);
	
	public Document getDocumentById(String codingSchemeUri, String version,
			int id, Set<String> field);
	
	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query);

}