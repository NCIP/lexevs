package org.lexevs.dao.index.indexer;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;

public class EntityBatchingIndexCreator implements IndexCreator {
	
	private int batchSize = 10;
	
	private EntityService entityService;
	
	private LuceneLoaderCodeIndexer luceneLoaderCodeIndexer;
	
	private SystemResourceService systemResourceService;
	
	private SystemVariables systemVariables;
	
	private IndexerService indexerService;

	public void index(AbsoluteCodingSchemeVersionReference reference) {
		LuceneLoaderCodeIndexer indexer = new LuceneLoaderCodeIndexer();
		indexer.setSystemVariables(systemVariables);
		indexer.setSystemResourceService(systemResourceService);
		indexer.openIndex();
		
		int position = 0;
		 for(
				 List<? extends Entity> entities = 
				 entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position, batchSize);
				 entities.size() > 0; 
				 entities = entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position += batchSize, batchSize)) {

			 for(Entity entity : entities) {
				 indexer.indexEntity(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), entity);
			 }
		 }
		 
		 indexer.closeIndex();
	}
	
	protected void addIndexMetadata(
			AbsoluteCodingSchemeVersionReference reference, String indexName) {
		  try {
			  indexerService.getMetaData().setIndexMetaDataValue(reference.getCodingSchemeURN() + "[:]" + reference.getCodingSchemeVersion(), indexName);
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "codingScheme", reference.getCodingSchemeURN());
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "version", reference.getCodingSchemeVersion());

			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "lgModel", "2009");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "has 'Norm' fields", false + "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "has 'Double Metaphone' fields", true + "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "indexing started", "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "indexing finished", "");
		} catch (InternalErrorException e) {
			throw new RuntimeException(e);
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

	public void setLuceneLoaderCodeIndexer(LuceneLoaderCodeIndexer luceneLoaderCodeIndexer) {
		this.luceneLoaderCodeIndexer = luceneLoaderCodeIndexer;
	}

	public LuceneLoaderCodeIndexer getLuceneLoaderCodeIndexer() {
		return luceneLoaderCodeIndexer;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}
	
	public IndexerService getIndexerService() {
		return indexerService;
	}

	public void setIndexerService(IndexerService indexerService) {
		this.indexerService = indexerService;
	}
}
