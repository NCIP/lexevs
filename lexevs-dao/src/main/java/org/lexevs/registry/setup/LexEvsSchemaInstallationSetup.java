/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.registry.setup;

import org.lexevs.dao.database.access.registry.RegistryDao;
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

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void setUpLexEvsDbSchema() throws Exception {
		this.getLogger().info("Checking for installed LexEVS Database Schema.");

		if(!isLexGridSchemaInstalled){
				this.getLexEvsDatabaseOperations().createAllTables();	
				this.registryDao.initRegistryMetadata();
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
}
