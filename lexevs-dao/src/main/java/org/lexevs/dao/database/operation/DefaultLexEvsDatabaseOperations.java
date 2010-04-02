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
package org.lexevs.dao.database.operation;

import javax.sql.DataSource;

import org.lexevs.dao.database.operation.transitivity.TransitivityBuilder;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The Class LexEvsPersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsDatabaseOperations implements LexEvsDatabaseOperations{
	
	/** The database utility. */
	private DatabaseUtility databaseUtility;
	
	private IndexServiceManager indexServiceManager;
	
	/** The lexevs common schema create script. */
	private Resource lexevsCommonSchemaCreateScript;
	
	/** The lexevs coding scheme schema create script. */
	private Resource lexevsCodingSchemeSchemaCreateScript;
	
	/** The lexevs value sets schema create script. */
	private Resource lexevsValueSetsSchemaCreateScript;
	
	/** The lexevs coding scheme schema create script. */
	private Resource lexevsHistoryCreateScript;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The transaction manager. */
	private PlatformTransactionManager transactionManager;
	
	/** The database type. */
	private DatabaseType databaseType;
	
	private TransitivityBuilder transitivityBuilder;

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#isCodingSchemeLoaded(java.lang.String, java.lang.String)
	 */
	public boolean isCodingSchemeLoaded(String codingScheme, String version) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCommonTables()
	 */
	public void createCommonTables() {
		try {
			databaseUtility.executeScript(lexevsCommonSchemaCreateScript, this.prefixResolver.resolveDefaultPrefix());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCodingSchemeTables()
	 */
	public void createCodingSchemeTables() {
		try {
			databaseUtility.executeScript(lexevsCodingSchemeSchemaCreateScript, this.prefixResolver.resolveDefaultPrefix());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCodingSchemeTables(java.lang.String)
	 */
	public void createCodingSchemeTables(String prefix) {
		try {
			databaseUtility.executeScript(lexevsCodingSchemeSchemaCreateScript, getCombinedPrefix(prefix));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public void createValueSetsTables() {
		try {
			databaseUtility.executeScript(lexevsValueSetsSchemaCreateScript, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public void createHistoryTables() {
		try {
			databaseUtility.executeScript(lexevsHistoryCreateScript, this.prefixResolver.resolveDefaultPrefix());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#cleanupFailedLoad(java.lang.String, java.lang.String)
	 */
	public void cleanupFailedLoad(String dbName, String prefix)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#computeTransitiveTable(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void computeTransitiveTable(String codingSchemeUri,
			String codingSchemeVersion) {
		transitivityBuilder.computeTransitivityTable(codingSchemeUri, codingSchemeVersion);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#dropTables(java.lang.String, java.lang.String)
	 */
	public void dropTables(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#dropCommonTables()
	 */
	public void dropCommonTables() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the combined prefix.
	 * 
	 * @param codingSchemePrefix the coding scheme prefix
	 * 
	 * @return the combined prefix
	 */
	protected String getCombinedPrefix(String codingSchemePrefix){
		return prefixResolver.resolveDefaultPrefix() + codingSchemePrefix;
	}
	
	/**
	 * Gets the lexevs common schema create script.
	 * 
	 * @return the lexevs common schema create script
	 */
	public Resource getLexevsCommonSchemaCreateScript() {
		return lexevsCommonSchemaCreateScript;
	}

	/**
	 * Sets the lexevs common schema create script.
	 * 
	 * @param lexevsCommonSchemaCreateScript the new lexevs common schema create script
	 */
	public void setLexevsCommonSchemaCreateScript(
			Resource lexevsCommonSchemaCreateScript) {
		this.lexevsCommonSchemaCreateScript = lexevsCommonSchemaCreateScript;
	}

	/**
	 * Gets the lexevs coding scheme schema create script.
	 * 
	 * @return the lexevs coding scheme schema create script
	 */
	public Resource getLexevsCodingSchemeSchemaCreateScript() {
		return lexevsCodingSchemeSchemaCreateScript;
	}

	/**
	 * Sets the lexevs coding scheme schema create script.
	 * 
	 * @param lexevsCodingSchemeSchemaCreateScript the new lexevs coding scheme schema create script
	 */
	public void setLexevsCodingSchemeSchemaCreateScript(
			Resource lexevsCodingSchemeSchemaCreateScript) {
		this.lexevsCodingSchemeSchemaCreateScript = lexevsCodingSchemeSchemaCreateScript;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDatabaseUtility()
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getPrefixResolver()
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDataSource()
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDatabaseType()
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
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
	 * Sets the transaction manager.
	 * 
	 * @param transactionManager the new transaction manager
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getTransactionManager()
	 */
	public  PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransitivityBuilder(TransitivityBuilder transitivityBuilder) {
		this.transitivityBuilder = transitivityBuilder;
	}

	public TransitivityBuilder getTransitivityBuilder() {
		return transitivityBuilder;
	}

	public void setLexevsHistoryCreateScript(Resource lexevsHistoryCreateScript) {
		this.lexevsHistoryCreateScript = lexevsHistoryCreateScript;
	}

	public Resource getLexevsHistoryCreateScript() {
		return lexevsHistoryCreateScript;
	}

	public void setIndexServiceManager(IndexServiceManager indexServiceManager) {
		this.indexServiceManager = indexServiceManager;
	}

	public IndexServiceManager getIndexServiceManager() {
		return indexServiceManager;
	}

	public void setLexevsValueSetsSchemaCreateScript(
			Resource lexevsValueSetsSchemaCreateScript) {
		this.lexevsValueSetsSchemaCreateScript = lexevsValueSetsSchemaCreateScript;
	}

	public Resource getLexevsValueSetsSchemaCreateScript() {
		return lexevsValueSetsSchemaCreateScript;
	}
}
