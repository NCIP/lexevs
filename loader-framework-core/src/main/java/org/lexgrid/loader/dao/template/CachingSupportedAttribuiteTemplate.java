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
package org.lexgrid.loader.dao.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

/**
 * The Class CachingSupportedAttribuiteTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplate extends AbstractSupportedAttributeTemplate{
	
	private DatabaseServiceManager databaseServiceManager;

	/** The attribute cache. */
	private Map<String,URIMap> attributeCache = Collections.synchronizedMap(new HashMap<String,URIMap>());

	
	/** The max cache size. */
	private int maxCacheSize = 5000;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#insert(org.LexGrid.persistence.model.CodingSchemeSupportedAttrib)
	 */
	@Override
	protected synchronized void insert(final String codingSchemeUri, final String codingSchemeVersion, final URIMap uriMap){
		String key = this.buildCacheKey(uriMap);

		if(! attributeCache.containsKey(key)){

			try {
				this.getDatabaseServiceManager().getCodingSchemeService().
					insertURIMap(codingSchemeUri, codingSchemeVersion, uriMap);
			} catch (Exception e) {
				this.getLogger().warn("Error registering Supported Attribute.", e);
			}

			attributeCache.put(key, uriMap);
		}

		if(attributeCache.size() >= maxCacheSize) {
			attributeCache.clear();
		}	
	}
	
	protected String buildCacheKey(URIMap map){
		return map.getClass().getName() +
			map.getContent() +
			map.getLocalId() +
			map.getUri();
	}
	
	protected Map<String,URIMap> getAttributeCache(){
		return this.attributeCache;
	}

	/**
	 * Gets the max cache size.
	 * 
	 * @return the max cache size
	 */
	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	/**
	 * Sets the max cache size.
	 * 
	 * @param maxCacheSize the new max cache size
	 */
	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
	
	
}
