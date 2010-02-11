package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	public void insertEntity(String codingSchemeName, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao().
			getCodingSchemeId(codingSchemeName, version);
		
		this.getDaoManager().getEntityDao().insertEntity(codingSchemeId, entity);
	}

	public void insertBatchEntities(String codingSchemeName, String version,
			List<? extends Entity> entities) {
		String codingSchemeId = this.getDaoManager().
		getCodingSchemeDao().
		getCodingSchemeId(codingSchemeName, version);
		
		this.getDaoManager().getEntityDao().insertBatchEntities(codingSchemeId, entities);
		
	}

	public void insertEntity(String codingSchemeId, List<? extends Entity> entities) {
		// TODO Auto-generated method stub
		
	}

	public void updateEntity(String codingSchemeName, String version,
			String enityCode, String entityCodeNamespace, Entity entity) {
		// TODO Auto-generated method stub
		
	}


	
	
}
