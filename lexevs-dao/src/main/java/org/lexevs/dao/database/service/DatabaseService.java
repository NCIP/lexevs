package org.lexevs.dao.database.service;

import org.lexevs.dao.database.access.DaoManager;

public interface DatabaseService {

	public <T> T executeInDaoLayer(DaoCallback<T> daoCallback);
	
	public interface DaoCallback<T> {
		public T execute(DaoManager daoManager);
	}
}
