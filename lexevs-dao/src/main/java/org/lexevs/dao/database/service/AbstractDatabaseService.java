package org.lexevs.dao.database.service;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.event.DatabaseServiceEventSupport;

public class AbstractDatabaseService extends DatabaseServiceEventSupport {

	private DaoManager daoManager;
	
	public DaoManager getDaoManager() {
		return daoManager;
	}
	
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
}
