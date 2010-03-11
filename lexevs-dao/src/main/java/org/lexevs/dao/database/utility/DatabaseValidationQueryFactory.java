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
package org.lexevs.dao.database.utility;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * A factory for creating DatabaseDialect objects. Because Hibernate cannot automatically
 * detect an Oracle 11g Dialect, we have to do it here.
 */
public class DatabaseValidationQueryFactory implements InitializingBean, FactoryBean {
	
	/** The Constant DEFAULT_VALIDATION_QUERY. */
	private static final String DEFAULT_VALIDATION_QUERY = "SELECT 1";
	
	/** The Constant ORACLE_VALIDATION_QUERY. */
	private static final String ORACLE_VALIDATION_QUERY = "SELECT sysdate from dual";
	
	/** The Constant DB2_VALIDATION_QUERY. */
	private static final String DB2_VALIDATION_QUERY = null;
	
	/** The Constant HSQLDB_VALIDATION_QUERY. */
	private static final String HSQLDB_VALIDATION_QUERY = null;
	
	/** The db url. */
	private String dbUrl;
	
	/** The validation query. */
	private String validationQuery;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		validationQuery = getValidationQuery();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return validationQuery;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}
	
	/**
	 * Gets the validation query.
	 * 
	 * @return the validation query
	 * 
	 * @throws MetaDataAccessException the meta data access exception
	 */
	public String getValidationQuery() throws MetaDataAccessException {

		if(dbUrl.toLowerCase().contains("oracle")){
			return ORACLE_VALIDATION_QUERY;
		} else if(dbUrl.toLowerCase().contains("db2")){
			return DB2_VALIDATION_QUERY;
		} else if(dbUrl.toLowerCase().contains("hsqldb")){
			return HSQLDB_VALIDATION_QUERY;
		} else {
			return DEFAULT_VALIDATION_QUERY;
		}
	}

	/**
	 * Gets the db url.
	 * 
	 * @return the db url
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * Sets the db url.
	 * 
	 * @param dbUrl the new db url
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
}
