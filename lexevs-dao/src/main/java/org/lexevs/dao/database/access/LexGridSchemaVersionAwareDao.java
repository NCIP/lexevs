
package org.lexevs.dao.database.access;

import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

/**
 * The Interface LexGridSchemaVersionAwareDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexGridSchemaVersionAwareDao {

	/**
	 * Supports lg schema version.
	 * 
	 * @param version the version
	 * 
	 * @return true, if successful
	 */
	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version);
	
	/**
	 * Execute in transaction.
	 * 
	 * @param callback the callback
	 * 
	 * @return the t
	 */
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback);
	
	/**
	 * The Interface IndividualDaoCallback.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public interface IndividualDaoCallback<T> {
		
		/**
		 * Execute.
		 * 
		 * @return the t
		 */
		public T execute();
	}
}