/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexgrid.loader.staging;

import java.util.Map;
import java.util.Set;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * The Class DefaultStagingManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultStagingManager extends LoggingBean implements StagingManager, InitializingBean {

	/** The registered staging databases. */
	private Map<String,Resource> registeredStagingDatabases;
	
	/** The database utility. */
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	/** The retry. */
	private boolean retry;
	
	private String prefix;
	
	
	/**
	 * Gets the registered staging databases.
	 * 
	 * @return the registered staging databases
	 */
	public Map<String, Resource> getRegisteredStagingDatabases() {
		return registeredStagingDatabases;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if(!retry){
			getLogger().info("First run of load Job, initializing Staging Tables.");
			initializeStagingSupport();
		}else {
			getLogger().info("Retrying load Job, not initializing Staging Tables.");
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.staging.StagingManager#initializeStagingSupport()
	 */
	public void initializeStagingSupport() throws Exception {
		Set<String> databases = registeredStagingDatabases.keySet();
		for(String db : databases){
			createStagingDatabase(registeredStagingDatabases.get(db));
		}
	}
	
	/**
	 * Creates the staging database.
	 * 
	 * @param creationScriptPath the creation script path
	 * 
	 * @throws Exception the exception
	 */
	protected void createStagingDatabase(Resource creationScriptPath) throws Exception {
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(creationScriptPath, prefix);	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.staging.StagingManager#dropAllStagingDatabases()
	 */
	public void dropAllStagingDatabases() throws Exception {
		Set<String> databases = registeredStagingDatabases.keySet();
		
		for(String db : databases){
			lexEvsDatabaseOperations.getDatabaseUtility().dropDatabase(db);
			registeredStagingDatabases.remove(db);
		}		
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.staging.StagingManager#dropStagingDatabase(java.lang.String)
	 */
	public void dropStagingDatabase(String databaseName) throws Exception {
		lexEvsDatabaseOperations.getDatabaseUtility().dropDatabase(databaseName);
		registeredStagingDatabases.remove(databaseName);
	}
	
	
	
	/**
	 * Sets the registered staging databases.
	 * 
	 * @param registeredStagingDatabases the registered staging databases
	 */
	public void setRegisteredStagingDatabases(
			Map<String, Resource> registeredStagingDatabases) {
		this.registeredStagingDatabases = registeredStagingDatabases;
	}



	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	public void setLexEvsDatabaseOperations(
			LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	/**
	 * Checks if is retry.
	 * 
	 * @return true, if is retry
	 */
	public boolean isRetry() {
		return retry;
	}

	/**
	 * Sets the retry.
	 * 
	 * @param retry the new retry
	 */
	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}