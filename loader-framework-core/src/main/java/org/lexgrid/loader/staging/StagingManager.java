
package org.lexgrid.loader.staging;

/**
 * The Interface StagingManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface StagingManager {
	
	public static String STAGING_TABLES_PREFIX = "stg";

	/**
	 * Initialize staging support.
	 * 
	 * @throws Exception the exception
	 */
	public void initializeStagingSupport() throws Exception;
	
	/**
	 * Drop staging database.
	 * 
	 * @param databaseName the database name
	 * 
	 * @throws Exception the exception
	 */
	public void dropStagingDatabase(String databaseName)throws Exception;
	
	/**
	 * Drop all staging databases.
	 * 
	 * @throws Exception the exception
	 */
	public void dropAllStagingDatabases() throws Exception;
}