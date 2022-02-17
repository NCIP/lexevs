
package org.lexevs.dao.index.access.entity;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

/**
 * The Interface EntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CommonEntityDao extends LexEvsIndexFormatVersionAwareDao {
	
	public String getIndexName();
	
	public Document getDocumentById(int id);

	public List<ScoreDoc> query(Query query);
}