package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	@Transactional
	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				insertEntity(codingSchemeId, entity);
	}

	@Transactional
	public void insertBatchEntities(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).
			insertBatchEntities(codingSchemeId, entities);
		
	}

	@Transactional
	public void updateEntity(String codingSchemeUri, String version,
			String enityCode, String entityCodeNamespace, Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public Entity getEntity(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace) {
		return null;
		//return this.getDaoManager().
		//	getEntityDao(codingSchemeUri, version).g
	}


	
	
}
