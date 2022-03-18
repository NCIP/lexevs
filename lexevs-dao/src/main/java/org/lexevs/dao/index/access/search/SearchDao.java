
package org.lexevs.dao.index.access.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

/**
 * The Interface SearchDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SearchDao extends LexEvsIndexFormatVersionAwareDao {
		
	
	public String getIndexName(String codingSchemeUri, String version);

	public void deleteDocuments(String codingSchemeUri, String version, Query query);

	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer);
	
	public List<ScoreDoc> query(Query query);
	
	public Filter getCodingSchemeFilter(String uri, String version);

	public Document getById(int id);
	
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int id);

	public List<ScoreDoc> query(Query query,
			Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude);
}