package org.lexevs.dao.database.lazyload;

import org.lexevs.dao.database.service.DatabaseServiceManager;

public abstract class AbstractLazyLoadable {

	private DatabaseServiceManager databaseServiceManager;
	private boolean isHydrated = false;
	
	protected void hydrate() {
		if(!isHydrated) {
			doHydrate();
			isHydrated = true;
		}
	}
	
	protected abstract void doHydrate();
	
	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}	
}
