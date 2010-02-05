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
package org.LexGrid.persistence.database;

import javax.sql.DataSource;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * A factory for creating DatabaseDialect objects. Because Hibernate cannot automatically
 * detect an Oracle 11g Dialect, we have to do it here.
 */
public class DatabaseDialectFactory implements InitializingBean, FactoryBean{

	/** The dialect. */
	private Class dialect;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The max supported oracle version. */
	private static int maxSupportedOracleVersion = 10;
	
	/** The oracle default class. */
	private static Class oracleDefaultClass = Oracle10gDialect.class;
	
	private static Class db2DefaultClass = DB2Dialect.class;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		dialect = getDialectfromMetaData();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return dialect;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Class.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
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
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the dialectfrom meta data.
	 * 
	 * @return the dialectfrom meta data
	 * 
	 * @throws MetaDataAccessException the meta data access exception
	 */
	public Class getDialectfromMetaData() throws MetaDataAccessException {

		String databaseProductName =
			JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName").toString();

		if(databaseProductName.toLowerCase().contains("oracle")){
			Integer version = Integer.valueOf(JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseMajorVersion").toString());
			if(version > maxSupportedOracleVersion){	
				return oracleDefaultClass;
			}
		}
		
		if(databaseProductName.toLowerCase().contains("db2")){
			return db2DefaultClass;
		}
		return null;
	}

}
