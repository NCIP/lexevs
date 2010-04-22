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
package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A factory for creating LexGridSchemaCheck objects.
 */
public class LexGridSchemaCheckFactory implements FactoryBean, InitializingBean {

	/** The database type. */
	private DatabaseType databaseType;
	
	/** The is schema loaded. */
	private boolean isSchemaLoaded;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The prefix resolver. */
	private SystemVariables systemVariables;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return isSchemaLoaded;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		LexGridSchemaCheck schemaCheck = null;
		if(databaseType.equals(DatabaseType.HSQL)){
			schemaCheck = new CountBasedLexGridSchemaCheck(dataSource, systemVariables);
		}
		
		if(databaseType.equals(DatabaseType.MYSQL)){
			schemaCheck = new CountBasedLexGridSchemaCheck(dataSource, systemVariables);
		}
		
		if(databaseType.equals(DatabaseType.ORACLE)){
			schemaCheck = new CountBasedLexGridSchemaCheck(dataSource, systemVariables);
		}
		
		//TODO: Add Oracle, DB2, etc
		
		Assert.notNull(schemaCheck);
		
		isSchemaLoaded = schemaCheck.isCommonLexGridSchemaInstalled();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return boolean.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}
