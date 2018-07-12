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