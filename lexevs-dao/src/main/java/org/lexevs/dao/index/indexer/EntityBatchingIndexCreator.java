/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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

/**
 * The Class EntityBatchingIndexCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityBatchingIndexCreator implements IndexCreator {
	
	/** The batch size. */
	private int batchSize = 10;
	
	/** The entity service. */
	private EntityService entityService;
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	/** The system variables. */
	private SystemVariables systemVariables;

	/** The index interface. */
	private IndexInterface indexInterface;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexer.IndexCreator#index(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
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
	
	/**
	 * Adds the index metadata.
	 * 
	 * @param reference the reference
	 * @param indexName the index name
	 * @param indexVersion the index version
	 */
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

	/**
	 * Sets the entity service.
	 * 
	 * @param entityService the new entity service
	 */
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	/**
	 * Gets the entity service.
	 * 
	 * @return the entity service
	 */
	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 * Gets the batch size.
	 * 
	 * @return the batch size
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Sets the batch size.
	 * 
	 * @param batchSize the new batch size
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * Sets the system resource service.
	 * 
	 * @param systemResourceService the new system resource service
	 */
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	/**
	 * Sets the index interface.
	 * 
	 * @param indexInterface the new index interface
	 */
	public void setIndexInterface(IndexInterface indexInterface) {
		this.indexInterface = indexInterface;
	}

	/**
	 * Gets the index interface.
	 * 
	 * @return the index interface
	 */
	public IndexInterface getIndexInterface() {
		return indexInterface;
	}
}
