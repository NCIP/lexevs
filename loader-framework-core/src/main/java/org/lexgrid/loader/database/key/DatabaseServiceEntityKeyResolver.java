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
package org.lexgrid.loader.database.key;

import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

@Cacheable(cacheName = "DatabaseServiceEntityKeyResolver")
public class DatabaseServiceEntityKeyResolver implements EntityKeyResolver {

	public DatabaseServiceManager databaseServiceManager;

	@CacheMethod
	public String resolveKey(
			final String uri,
			final String version, 
			final String entityCode,
			final String entityCodeNamespace) throws KeyNotFoundException {
		String entityId = databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

			public String execute(DaoManager daoManager) {
				String codingSchemeId = daoManager.
					getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
				
				String entityId =
					daoManager.getEntityDao(uri, version).getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
				
				return entityId;
			}	
		});
		
		if(StringUtils.isBlank(entityId)) {
			throw new KeyNotFoundException(entityCode, entityCodeNamespace);
		}
		
		return entityId;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}
}