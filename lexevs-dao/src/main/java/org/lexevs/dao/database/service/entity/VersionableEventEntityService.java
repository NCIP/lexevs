package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				insertEntity(codingSchemeId, entity);
	}

	public void insertBatchEntities(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		String codingSchemeId = this.getDaoManager().
		getCodingSchemeDao(codingSchemeUri, version).
		getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).
			insertBatchEntities(codingSchemeId, entities);
		
	}


	public void updateEntity(String codingSchemeUri, String version,
			String enityCode, String entityCodeNamespace, Entity entity) {
		// TODO Auto-generated method stub
		
	}


	
	
}
