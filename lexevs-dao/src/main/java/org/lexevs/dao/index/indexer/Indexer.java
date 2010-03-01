package org.lexevs.dao.index.indexer;

import org.LexGrid.concepts.Entity;

public interface Indexer {

	public void openIndex();
	
	public void closeIndex();
	
	public void indexEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
}
