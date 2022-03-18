
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