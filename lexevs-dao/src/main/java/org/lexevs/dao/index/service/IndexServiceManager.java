
package org.lexevs.dao.index.service;

import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.index.service.metadata.MetadataIndexService;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;

/**
 * The Class IndexServiceManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IndexServiceManager {

	/** The entity index service. */
	private EntityIndexService entityIndexService;
	
	/** The entity index service. */
	private MetadataIndexService metadataIndexService;
	
	private SearchIndexService searchIndexService;
	
	private SourceAssertedValueSetSearchIndexService assertedValueSetIndexService;

	/**
	 * Sets the entity index service.
	 * 
	 * @param entityIndexService the new entity index service
	 */
	public void setEntityIndexService(EntityIndexService entityIndexService) {
		this.entityIndexService = entityIndexService;
	}

	/**
	 * Gets the entity index service.
	 * 
	 * @return the entity index service
	 */
	public EntityIndexService getEntityIndexService() {
		return entityIndexService;
	}

	public void setMetadataIndexService(MetadataIndexService metadataIndexService) {
		this.metadataIndexService = metadataIndexService;
	}

	public MetadataIndexService getMetadataIndexService() {
		return metadataIndexService;
	}

	public SearchIndexService getSearchIndexService() {
		return searchIndexService;
	}

	public void setSearchIndexService(SearchIndexService searchIndexService) {
		this.searchIndexService = searchIndexService;
	}

	public SourceAssertedValueSetSearchIndexService getAssertedValueSetIndexService() {
		return assertedValueSetIndexService;
	}

	public void setAssertedValueSetIndexService(SourceAssertedValueSetSearchIndexService assertedValueSetIndexService) {
		this.assertedValueSetIndexService = assertedValueSetIndexService;
	}
}