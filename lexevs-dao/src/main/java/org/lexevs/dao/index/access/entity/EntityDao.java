package org.lexevs.dao.index.access.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

public interface EntityDao extends LexEvsIndexFormatVersionAwareDao {

	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQueries, List<? extends Query> individualQueries);

	public Document getDocumentById(AbsoluteCodingSchemeVersionReference reference, int documentId);
	
	public void deleteDocumentsOfCodingScheme(AbsoluteCodingSchemeVersionReference reference);
	
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference);
}
