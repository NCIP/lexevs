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

	protected String getCodingSchemeId(String codingSchemeName, String codingSchemeVersion){
		return daoManager.getCodingSchemeDao().getCodingSchemeId(codingSchemeName, codingSchemeVersion);
	}
	
	public DaoManager getDaoManager() {
		return daoManager;
	}
	
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
}
