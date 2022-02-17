
package org.lexevs.registry.setup;

import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.logging.AbstractLoggingBean;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class LexEvsSchemaInstallationSetup.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsSchemaInstallationSetup extends AbstractLoggingBean implements LexEvsDatabaseSchemaSetup {
	
	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The lex evs database operations. */
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	/** The registry dao. */
	private RegistryDao registryDao;
	
	/** The is lex grid schema installed. */
	private boolean isLexGridSchemaInstalled;
	
	private PrimaryKeyIncrementer primaryKeyIncrementer;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void setUpLexEvsDbSchema() throws Exception {
		this.getLogger().info("Checking for installed LexEVS Database Schema.");

		if(!isLexGridSchemaInstalled){
				this.getLexEvsDatabaseOperations().createAllTables();	
				this.registryDao.initRegistryMetadata();
				this.primaryKeyIncrementer.initialize();
		}		
	}

	/**
	 * Sets the lex grid schema installed.
	 * 
	 * @param isLexGridSchemaInstalled the new lex grid schema installed
	 */
	public void setLexGridSchemaInstalled(boolean isLexGridSchemaInstalled) {
		this.isLexGridSchemaInstalled = isLexGridSchemaInstalled;
	}

	/**
	 * Checks if is lex grid schema installed.
	 * 
	 * @return true, if is lex grid schema installed
	 */
	public boolean isLexGridSchemaInstalled() {
		return isLexGridSchemaInstalled;
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	/**
	 * Gets the lex evs database operations.
	 * 
	 * @return the lex evs database operations
	 */
	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	/**
	 * Sets the lex evs database operations.
	 * 
	 * @param lexEvsDatabaseOperations the new lex evs database operations
	 */
	public void setLexEvsDatabaseOperations(
			LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	/**
	 * Sets the registry dao.
	 * 
	 * @param registryDao the new registry dao
	 */
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	/**
	 * Gets the registry dao.
	 * 
	 * @return the registry dao
	 */
	public RegistryDao getRegistryDao() {
		return registryDao;
	}

	public void setPrimaryKeyIncrementer(PrimaryKeyIncrementer primaryKeyIncrementer) {
		this.primaryKeyIncrementer = primaryKeyIncrementer;
	}

	public PrimaryKeyIncrementer getPrimaryKeyIncrementer() {
		return primaryKeyIncrementer;
	}
}