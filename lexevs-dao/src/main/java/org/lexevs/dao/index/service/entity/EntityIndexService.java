package org.lexevs.dao.index.service.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

public interface EntityIndexService {
public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void removeIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQueries, List<? extends Query> individualQueries);
	
	public Document getDocumentById(AbsoluteCodingSchemeVersionReference reference, int documentId);
	
	public Query getMatchAllDocsQuery(AbsoluteCodingSchemeVersionReference reference);

}
