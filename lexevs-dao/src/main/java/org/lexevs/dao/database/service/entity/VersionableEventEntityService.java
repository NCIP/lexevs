package org.lexevs.dao.database.service.entity;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	public void insertEntity(String codingSchemeName, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao().
			getCodingSchemeId(codingSchemeName, version);
		
		this.getDaoManager().getEntityDao().insertEntity(codingSchemeId, entity);
	}

	public void insertEntity(String codingSchemeId, Entity entity) {
		String entityId = this.getDaoManager().getEntityDao().insertEntity(codingSchemeId, entity);
	}

	public void insertEntity(String codingSchemeId, Entity[] entities) {
		// TODO Auto-generated method stub
		
	}

	public void updateEntity(String codingSchemeName, String version,
			String enityCode, String entityCodeNamespace, Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
