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
package org.lexgrid.loader.setup;

import org.LexGrid.persistence.constants.PersistenceLayerConstants;
import org.LexGrid.persistence.database.DatabaseUtility;
import org.LexGrid.persistence.database.DefaultDatabaseUtility;
import org.apache.log4j.Logger;
import org.lexgrid.loader.logging.LoggerFactory;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * The Class JobRepositoryManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JobRepositoryManager extends LoggingBean implements InitializingBean, DisposableBean {

	/** The database utility. */
	private DatabaseUtility databaseUtility;
	
	/** The retry. */
	private boolean retry;
	
	/** The create script. */
	private Resource createScript;
	
	/** The drop script. */
	private Resource dropScript;
	
	private boolean dropOnClose = false;
	
	private DatabaseType databaseType;
	
	//Not needed now... just in case a subclass might...
	/** The tables. */
	protected String[] tables = {
			" BATCH_JOB_INSTANCE ",
			" BATCH_JOB_EXECUTION ",
			" BATCH_JOB_PARAMS ",
			" BATCH_STEP_EXECUTION ",
			" BATCH_STEP_EXECUTION_CONTEXT ",
			" BATCH_JOB_EXECUTION_CONTEXT ",
			" BATCH_JOB_INSTANCE\\(",
			" BATCH_JOB_EXECUTION\\(",
			" BATCH_JOB_PARAMS\\(",
			" BATCH_STEP_EXECUTION\\(",
			" BATCH_STEP_EXECUTION_CONTEX\\(",
			" BATCH_STEP_EXECUTION_SEQ",
			" BATCH_JOB_EXECUTION_SEQ",
			" BATCH_JOB_EXECUTION_SEQ",
			" BATCH_JOB_SEQ",
			};

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if(!retry){
			getLogger().info("Creating Job Repository.");
			String script = DefaultDatabaseUtility.convertResourceToString(createScript);
			databaseUtility.executeScript(insertPrefixVariable(script));
		} else {
			getLogger().info("Not Creating Job Repository for restart.");
		}
	}
	
	/**
	 * Insert prefix variable.
	 * 
	 * @param script the script
	 * 
	 * @return the string
	 */
	protected String insertPrefixVariable(String script){
		script = script.replaceAll("BATCH_", PersistenceLayerConstants.PREFIX_PLACEHOLDER);
		if(! databaseType.equals(DatabaseType.DB2)){
			script = script.replaceAll("constraint ", "constraint " + PersistenceLayerConstants.PREFIX_PLACEHOLDER);
		}
		
		return script;
	}
	
	/**
	 * Drop job repository databases.
	 * 
	 * @throws Exception the exception
	 */
	public void dropJobRepositoryDatabases() throws Exception {
		String script = DefaultDatabaseUtility.convertResourceToString(dropScript);
		databaseUtility.executeScript(insertPrefixVariable(script));
	}
	
	public void dropJobRepositoryDatabasesOnClose() throws Exception {
		dropOnClose = true;
	}

	public void destroy() throws Exception {
		if(dropOnClose){
			getLogger().info("Dropping Job Repository Databases on close of Application.");
			dropJobRepositoryDatabases();
		}	
	}

	/**
	 * Gets the database utility.
	 * 
	 * @return the database utility
	 */
	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}


	/**
	 * Sets the database utility.
	 * 
	 * @param databaseUtility the new database utility
	 */
	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
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

	/**
	 * Gets the creates the script.
	 * 
	 * @return the creates the script
	 */
	public Resource getCreateScript() {
		return createScript;
	}

	/**
	 * Sets the creates the script.
	 * 
	 * @param createScript the new creates the script
	 */
	public void setCreateScript(Resource createScript) {
		this.createScript = createScript;
	}

	/**
	 * Gets the drop script.
	 * 
	 * @return the drop script
	 */
	public Resource getDropScript() {
		return dropScript;
	}

	/**
	 * Sets the drop script.
	 * 
	 * @param dropScript the new drop script
	 */
	public void setDropScript(Resource dropScript) {
		this.dropScript = dropScript;
	}

	public boolean isDropOnClose() {
		return dropOnClose;
	}

	public void setDropOnClose(boolean dropOnClose) {
		this.dropOnClose = dropOnClose;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}
}
