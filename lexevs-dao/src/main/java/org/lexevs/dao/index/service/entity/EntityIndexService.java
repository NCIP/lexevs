
package org.lexevs.dao.index.service.entity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.IndexCreator.EntityIndexerProgressCallback;

import java.util.List;
import java.util.Set;

/**
 * The Interface EntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityIndexService {

	/**
	 * Update index for entity.
	 *
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param entity the entity
	 */
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public String getIndexName(String codingSchemeUri, String codingSchemeVersion);
	
	public Document getDocumentById(String codingSchemeUri, String codingSchemeVersion, int id);
	 
	public Document getDocumentById(String codingSchemeUri, String codingSchemeVersion, int id,  Set<String> fields);
	
	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity);
	
	/**
	 * Creates the index.
	 * 
	 * @param reference the reference
	 */
	public void createIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback);
	/**
	 * Drop index.
	 * 
	 * @param reference the reference
	 */
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference);

	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query);
	
	public Document getDocumentFromCommonIndexById(List<AbsoluteCodingSchemeVersionReference> references, int id);
	
	public List<ScoreDoc> queryCommonIndex(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes,
			Query query);

}