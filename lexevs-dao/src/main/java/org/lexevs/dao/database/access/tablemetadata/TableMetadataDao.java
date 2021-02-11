
package org.lexevs.dao.database.access.tablemetadata;

/**
 * The Interface TableMetadataDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface TableMetadataDao {

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion();
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription();
}