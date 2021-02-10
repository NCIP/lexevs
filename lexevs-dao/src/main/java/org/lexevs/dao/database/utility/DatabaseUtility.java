
package org.lexevs.dao.database.utility;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.springframework.core.io.Resource;

/**
 * General Database utility methods.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DatabaseUtility {

	/**
	 * Drop database.
	 * 
	 * @param databaseName the database name
	 * 
	 * @throws Exception the exception
	 */
	public void dropDatabase(String databaseName) throws Exception;
	
	/**
	 * Truncate table.
	 * 
	 * @param tableName the table name
	 * 
	 * @throws Exception the exception
	 */
	public void truncateTable(String tableName) throws Exception;
	
	/**
	 * Does table exist.
	 * 
	 * @param tableName the table name
	 * 
	 * @return true, if successful
	 */
	public boolean doesTableExist(String tableName);
	
	/**
	 * Execute script.
	 * 
	 * @param creationScript the creation script
	 * 
	 * @throws Exception the exception
	 */
	public void executeScript(Resource creationScript) throws Exception;
	
	/**
	 * Execute script.
	 * 
	 * @param creationScript the creation script
	 * @param prefix the prefix
	 * 
	 * @throws Exception the exception
	 */
	public void executeScript(Resource creationScript, String commonPrefix, String tableSetPrefix) throws Exception;
	
	public void executeScript(String creationScript, String commonPrefix, String tableSetPrefix) throws Exception;
	
	public void executeScript(Resource creationScript, String tableSetPrefix) throws Exception;
	
	/**
	 * Execute script.
	 * 
	 * @param creationScript the creation script
	 * 
	 * @throws Exception the exception
	 */
	public void executeScript(String creationScript) throws Exception;
	
	public void executeScript(String creationScript, String tableSetPrefix) throws Exception;
}