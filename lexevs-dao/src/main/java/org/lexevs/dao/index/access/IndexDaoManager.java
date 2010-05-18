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
package org.lexevs.dao.index.access;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.util.Assert;

import edu.mayo.informatics.indexer.api.IndexerService;

/**
 * The Class IndexDaoManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IndexDaoManager {

	/** The entity daos. */
	private List<EntityDao> entityDaos;
	
	/** The entity daos. */
	private List<MetadataDao> metadataDaos;
	
	/** The indexer service. */
	private IndexerService indexerService;
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;

	/**
	 * Gets the entity dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the entity dao
	 */
	public EntityDao getEntityDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getEntityDaos());
	}
	
	public MetadataDao getMetadataDao(){
		Assert.state(this.metadataDaos.size() == 1, "Currently Metadata Daos are not Versionable.");
		
		return this.metadataDaos.get(0);
	}
	
	/**
	 * Do get dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param daos the daos
	 * 
	 * @return the t
	 */
	protected <T extends LexEvsIndexFormatVersionAwareDao> T doGetDao(String codingSchemeUri, String version, List<T> daos){
		Assert.notEmpty(daos, "No DAOs have been registered for the requested type.");	
		return getCorrectDaoForIndexVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	/**
	 * Gets the lex grid schema version.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the lex grid schema version
	 */
	protected LexEvsIndexFormatVersion getLexGridSchemaVersion(String uri, String version){
		try {
			String codingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(uri, version);
			
			LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
			String indexName = indexerService.getMetaData().getIndexMetaDataValue(lcs.getKey());
	
			String indexVersion = indexerService.getMetaData().getIndexMetaDataValue(indexName, "lgModel");
			
			return LexEvsIndexFormatVersion.parseStringToVersion(indexVersion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the correct dao for index version.
	 * 
	 * @param possibleDaos the possible daos
	 * @param indexVersion the index version
	 * 
	 * @return the correct dao for index version
	 */
	protected <T extends LexEvsIndexFormatVersionAwareDao> T getCorrectDaoForIndexVersion(List<T> possibleDaos, LexEvsIndexFormatVersion indexVersion) {
		List<T> foundDaos = new ArrayList<T>();
		
		for(T dao : possibleDaos){
			if(dao.supportsLexEvsIndexFormatVersion(indexVersion)){
				foundDaos.add(dao);
			}
		}
		
		Assert.state(foundDaos.size() > 0, "No matching DAO for Index Version: " +
				indexVersion);	
		
		Assert.state(foundDaos.size() < 2, "More than one matching DAO for: " +
				foundDaos.get(0).getClass().getName());
		
		return foundDaos.get(0);
	}


	/**
	 * Sets the entity daos.
	 * 
	 * @param entityDaos the new entity daos
	 */
	public void setEntityDaos(List<EntityDao> entityDaos) {
		this.entityDaos = entityDaos;
	}


	/**
	 * Gets the entity daos.
	 * 
	 * @return the entity daos
	 */
	public List<EntityDao> getEntityDaos() {
		return entityDaos;
	}

	/**
	 * Sets the indexer service.
	 * 
	 * @param indexerService the new indexer service
	 */
	public void setIndexerService(IndexerService indexerService) {
		this.indexerService = indexerService;
	}

	/**
	 * Gets the indexer service.
	 * 
	 * @return the indexer service
	 */
	public IndexerService getIndexerService() {
		return indexerService;
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

	public void setMetadataDaos(List<MetadataDao> metadataDaos) {
		this.metadataDaos = metadataDaos;
	}

	public List<MetadataDao> getMetadataDaos() {
		return metadataDaos;
	}
}
