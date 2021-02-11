
package org.lexevs.dao.database.lazyload;

import org.lexevs.dao.database.service.DatabaseServiceManager;

/**
 * The Class AbstractLazyLoadable.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLazyLoadable {

	/** The database service manager. */
	private DatabaseServiceManager databaseServiceManager;
	
	/** The is hydrated. */
	private boolean isHydrated = false;
	
	/**
	 * Hydrate.
	 */
	protected void hydrate() {
		if(!isHydrated) {
			doHydrate();
			isHydrated = true;
		}
	}
	
	/**
	 * Do hydrate.
	 */
	protected abstract void doHydrate();
	
	/**
	 * Gets the database service manager.
	 * 
	 * @return the database service manager
	 */
	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
	
	/**
	 * Sets the database service manager.
	 * 
	 * @param databaseServiceManager the new database service manager
	 */
	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}	
}