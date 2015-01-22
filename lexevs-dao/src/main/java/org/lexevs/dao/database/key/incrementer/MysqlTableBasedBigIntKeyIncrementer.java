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
package org.lexevs.dao.database.key.incrementer;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;

/**
 * The Class MysqlTableBasedBigIntKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MysqlTableBasedBigIntKeyIncrementer 
	extends AbstractBigIntKeyIncrementer {

	private static List<DatabaseType> SUPPORTED_DATABASE_TYPES = 
		Arrays.asList(DatabaseType.MYSQL);
	
	/** The cache size. */
	private int cacheSize = 1000;
	
	private static String SEQUCENCE_TABLE_NAME_PLACEHOLDER = "@SEQUENCE_TABLE_NAME_PLACEHOLDER@";
	
	private static String SEQUCENCE_TABLE_NAME= "increment_sequence";

	private static String COLUMN_NAME = "id";

	/** The create sequence table sql. */
	private String createSequenceTableSql = 
			"create table @SEQUENCE_TABLE_NAME_PLACEHOLDER@ (" + COLUMN_NAME + " int not null) engine=MYISAM; " +
			"insert into @SEQUENCE_TABLE_NAME_PLACEHOLDER@ values(0)";
	
	/** The drop sequence table sql. */
	private String dropSequenceTableSql = "drop table " + SEQUCENCE_TABLE_NAME_PLACEHOLDER;

	/** The database utility. */
	private DatabaseUtility databaseUtility;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#createDataFieldMaxValueIncrementer()
	 */
	@Override
	protected DataFieldMaxValueIncrementer createDataFieldMaxValueIncrementer() {
		MySQLMaxValueIncrementer mySQLMaxValueIncrementer =
			new MySQLMaxValueIncrementer(this.getDataSource(), getSequenceName(), COLUMN_NAME);	
		
		mySQLMaxValueIncrementer.setCacheSize(cacheSize);
		
		return mySQLMaxValueIncrementer;
	}
	
	/**
	 * Gets the sequence name.
	 * 
	 * @return the sequence name
	 */
	private String getSequenceName() {
		return this.prefixResolver.resolveDefaultPrefix() + SEQUCENCE_TABLE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#destroy()
	 */
	@Override
	public void destroy() {
		this.dropSequence(this.getSequenceName());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#initialize()
	 */
	@Override
	public void initialize() {
		this.createSequence(this.getSequenceName());
	}

	/**
	 * Creates the sequence.
	 * 
	 * @param sequenceName the sequence name
	 */
	private void createSequence(String sequenceName) {
		this.executeSql(adjustForSequenceName(sequenceName, this.createSequenceTableSql));
	}
	
	/**
	 * Drop sequence.
	 * 
	 * @param sequenceName the sequence name
	 */
	private void dropSequence(String sequenceName) {
		this.executeSql(adjustForSequenceName(sequenceName, this.dropSequenceTableSql));
	}
	
	/**
	 * Execute sql.
	 * 
	 * @param sql the sql
	 */
	private void executeSql(String sql) {
		try {
			this.databaseUtility.executeScript(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adjust for sequence name.
	 * 
	 * @param sequenceName the sequence name
	 * @param sql the sql
	 * 
	 * @return the string
	 */
	private String adjustForSequenceName(String sequenceName, String sql) {
		return sql.replaceAll(SEQUCENCE_TABLE_NAME_PLACEHOLDER, sequenceName);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#getSupportedDatabaseTypes()
	 */
	@Override
	protected List<DatabaseType> getSupportedDatabaseTypes() {
		return SUPPORTED_DATABASE_TYPES;
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
	 * Gets the database utility.
	 * 
	 * @return the database utility
	 */
	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
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
	 * Sets the cache size.
	 * 
	 * @param cacheSize the new cache size
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	/**
	 * Gets the cache size.
	 * 
	 * @return the cache size
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}
}