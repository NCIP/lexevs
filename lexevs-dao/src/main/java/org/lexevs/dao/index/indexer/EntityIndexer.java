package org.lexevs.dao.index.indexer;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public interface EntityIndexer {
	
	public void indexEntity(
			String indexName,
			String codingSchemeUri, 
			String codingSchemeVersion,
			Entity entity);
	
	public LexEvsIndexFormatVersion getIndexerFormatVersion();
	
	public String getCommonIndexName();
	
}
