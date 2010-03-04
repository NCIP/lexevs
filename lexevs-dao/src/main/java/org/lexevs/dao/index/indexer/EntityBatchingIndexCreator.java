package org.lexevs.dao.index.indexer;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.IndexerService;

public class EntityBatchingIndexCreator implements IndexCreator {
	
	private int batchSize = 10;
	
	private EntityService entityService;
	
	private SystemResourceService systemResourceService;
	
	private SystemVariables systemVariables;

	private IndexInterface indexInterface;

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
		 
		 String indexName = indexer.getIndexName();
		 String indexVersion = indexer.getCurrentIndexVersion();
		 
		 indexer.closeIndex();
		 addIndexMetadata(reference, indexName, indexVersion);

		 try {
			 indexInterface.initCodingSchemes();
		 } catch (LBInvocationException e) {
			 throw new RuntimeException(e);
		 }
	}
	
	protected void addIndexMetadata(
			AbsoluteCodingSchemeVersionReference reference, String indexName, String indexVersion) {
		  try {	  
			  IndexerService indexerService = this.indexInterface.getBaseIndexerService();
			  
			  String codingSchemeName = 
				  systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
			  
			  indexerService.getMetaData().setIndexMetaDataValue(codingSchemeName + "[:]" + reference.getCodingSchemeVersion(), indexName);

			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "lgModel", indexVersion);
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "has 'Norm' fields", false + "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "has 'Double Metaphone' fields", true + "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "indexing started", "");
			  indexerService.getMetaData().setIndexMetaDataValue(indexName, "indexing finished", "");
			  
			  indexerService.getMetaData().rereadFile(true);
		} catch (Exception e) {
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

	public void setIndexInterface(IndexInterface indexInterface) {
		this.indexInterface = indexInterface;
	}

	public IndexInterface getIndexInterface() {
		return indexInterface;
	}
}
