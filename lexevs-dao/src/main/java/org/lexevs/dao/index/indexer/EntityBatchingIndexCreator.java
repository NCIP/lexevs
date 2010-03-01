package org.lexevs.dao.index.indexer;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;

public class EntityBatchingIndexCreator implements IndexCreator {
	
	private int batchSize = 10;
	
	private EntityService entityService;
	
	private Indexer indexer;

	public void index(AbsoluteCodingSchemeVersionReference reference) {
		indexer.openIndex();
		int position = 0;
		 for(
				 List<Entity> entities = 
				 entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position, batchSize);
				 entities.size() > 0; 
				 entities = entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position += batchSize, batchSize)) {

			 indexEntityBatch(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), entities);
		 }
		 indexer.closeIndex();
	}
	
	protected void indexEntityBatch(String codingSchemeUri, String codingScheme, List<Entity> entities) {
		for(Entity entity : entities) {
			indexer.indexEntity(codingSchemeUri, codingScheme, entity);
		}
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public EntityService getEntityService() {
		return entityService;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public void setIndexer(Indexer indexer) {
		this.indexer = indexer;
	}

	public Indexer getIndexer() {
		return indexer;
	}

	
}
