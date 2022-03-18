
package org.lexgrid.loader.writer;

import org.lexevs.dao.database.service.DatabaseServiceManager;

public abstract class AbstractDatabaseServiceWriter {

	private DatabaseServiceManager databaseServiceManager;

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
	

}