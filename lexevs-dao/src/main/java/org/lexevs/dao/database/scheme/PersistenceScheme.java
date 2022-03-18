
package org.lexevs.dao.database.scheme;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

/**
 * The Interface PersistenceScheme.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PersistenceScheme {
	
	/**
	 * Inits the scheme.
	 */
	public void initScheme();
	
	/**
	 * Destroy scheme.
	 */
	public void destroyScheme();
	
	/**
	 * Gets the lex grid schema version.
	 * 
	 * @return the lex grid schema version
	 */
	public LexGridSchemaVersion getLexGridSchemaVersion();
	
	/**
	 * Register daos.
	 * 
	 * @param daoManager the dao manager
	 */
	public void registerDaos(DaoManager daoManager);
}