package org.lexevs.dao.database.service;

import org.lexevs.dao.database.access.DaoManager;

public interface DatabaseService {

	public void executeInDaoLayer(DaoCallback daoCallback);
	
	public interface DaoCallback {
		public Object execute(DaoManager daoManager);
	}
}
