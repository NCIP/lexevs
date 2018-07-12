/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.lexevs.dao.index.access.entity.CommonEntityDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.access.search.SearchDao;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.index.lucene.v2010.entity.SingleTemplateDisposableLuceneCommonEntityDao;
import org.lexevs.dao.index.lucene.v2013.search.ValueSetEntityDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.util.Assert;

/**
 * The Class IndexDaoManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IndexDaoManager {

	/** The entity daos. */
	private List<EntityDao> entityDaos;
	
	/** The entity daos. */
	private List<SearchDao> searchDaos;
	
	private List<ValueSetEntityDao> valueSetEntityDaos;
	/** The entity daos. */
	private List<MetadataDao> metadataDaos;
		
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	private ConcurrentMetaData concurrentMetaData;
	
	private IndexRegistry indexRegistry;

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
	
	public SearchDao getSearchDao(){
		Assert.state(this.searchDaos.size() == 1, "Currently Search Daos are not Versionable.");
		
		return this.searchDaos.get(0);
	}
	
	public SearchDao getSearchDao(
			Set<CodingSchemeReference> codeSystemsToInclude) {
		// TODO Auto-generated method stub
		return null;
	}

	public CommonEntityDao getCommonEntityDao(List<AbsoluteCodingSchemeVersionReference> codingSchemes) {

		LuceneIndexTemplate template = 
			indexRegistry.getCommonLuceneIndexTemplate(codingSchemes);

		return new SingleTemplateDisposableLuceneCommonEntityDao(indexRegistry, template, codingSchemes);
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
			LexEvsIndexFormatVersion indexVersion = new LexEvsIndexFormatVersion();
			indexVersion.setModelFormatVersion("2010");
			return indexVersion;
			
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

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	public List<SearchDao> getSearchDaos() {
		return searchDaos;
	}

	public void setSearchDaos(List<SearchDao> searchDaos) {
		this.searchDaos = searchDaos;
	}

	public List<ValueSetEntityDao> getValueSetEntityDaos() {
		return valueSetEntityDaos;
	}

	public void setValueSetEntityDaos(List<ValueSetEntityDao> valueSetEntityDaos) {
		this.valueSetEntityDaos = valueSetEntityDaos;
	}

	public ValueSetEntityDao getValueSetEntityDao(String codingSchemeURN, String codingSchemeVersion) {
		Assert.state(this.valueSetEntityDaos.size() == 1, "Currently Search Daos are not Versionable.");
		
		return this.valueSetEntityDaos.get(0);
	}
}