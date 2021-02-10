
package org.lexevs.dao.index.service.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Interface EntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SearchIndexService {

	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity);

	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity);

	public void dropIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference);
	
	public List<ScoreDoc> query(
		Set<AbsoluteCodingSchemeVersionReference> codeSystemToInclude, 
		Query query);
	
	public Document getById(int id);
	
	public Analyzer getAnalyzer();

	public void createIndex(AbsoluteCodingSchemeVersionReference ref);

	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
			int doc);

}