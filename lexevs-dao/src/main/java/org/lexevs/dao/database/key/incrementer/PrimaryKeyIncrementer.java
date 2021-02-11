
package org.lexevs.dao.database.key.incrementer;

import org.lexevs.dao.database.type.DatabaseType;

/**
 * The Interface PrimaryKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PrimaryKeyIncrementer {
	
	/**
	 * The Enum KeyType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum KeyType {
		INT,
		BIGINT,
		VARCHAR}
	
	/**
	 * Next key.
	 * 
	 * @return the string
	 */
	public String nextKey();
	
	/**
	 * Value of.
	 * 
	 * @param key the key
	 * 
	 * @return the object
	 */
	public Object valueOf(String key);
	
	/**
	 * Gets the key type.
	 * 
	 * @return the key type
	 */
	public KeyType getKeyType();
	
	/**
	 * String value.
	 * 
	 * @param key the key
	 * 
	 * @return the string
	 */
	public String stringValue(Object key);

	/**
	 * Gets the key length.
	 * 
	 * @return the key length
	 */
	public int getKeyLength();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Initialize.
	 */
	public void initialize();
	
	/**
	 * Destroy.
	 */
	public void destroy();
	
	/**
	 * Supports databases.
	 * 
	 * @param databaseType the database type
	 * 
	 * @return true, if successful
	 */
	public boolean supportsDatabases(DatabaseType databaseType);
}