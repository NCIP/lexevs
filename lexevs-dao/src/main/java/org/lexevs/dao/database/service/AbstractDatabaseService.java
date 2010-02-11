package org.lexevs.dao.database.service;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseService.DaoCallback;
import org.lexevs.dao.database.service.event.DatabaseServiceEventSupport;
import org.springframework.transaction.annotation.Transactional;

public class AbstractDatabaseService extends DatabaseServiceEventSupport {

	private DaoManager daoManager;
	
	@Transactional
	public void executeInDaoLayer(DaoCallback daoCallback){
		daoCallback.execute(daoManager);
	}

	protected String getCodingSchemeId(String codingSchemeUri, String codingSchemeVersion){
		return daoManager.
			getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
				getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
	}
	
	public DaoManager getDaoManager() {
		return daoManager;
	}
	
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
}
