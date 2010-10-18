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
package org.lexevs.dao.database.type;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * The Enum DatabaseType.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public enum DatabaseType {
	
	/** The D b2. */
	DB2("DB2"),
	
	/** The HSQL. */
	HSQL("HSQL", new String[] {"HSQLDB"} ),
	
	/** The MYSQL. */
	MYSQL("MySQL"),
	
	/** The ORACLE. */
	ORACLE("Oracle"),
	
	/** The POSTGRES. */
	POSTGRES("PostgreSQL");
	
	/** The Constant nameMap. */
	private static final Map<String, DatabaseType> nameMap;

	/** The product name. */
	private final String productName;
	private final String[] aliases;

	static{
		nameMap = new HashMap<String, DatabaseType>();
		for(DatabaseType type: values()){
			nameMap.put(type.getProductName(), type);
			if(type.getAliases() != null) {
				for(String alias : type.getAliases()) {
					nameMap.put(alias, type);
				}
			}
		}
	}
	
	public static DatabaseType toDatabaseType(String databaseType) {
		for(String name : nameMap.keySet()) {
			if(name.equalsIgnoreCase(databaseType)){
				return nameMap.get(name);
			}
		}
		
		throw new RuntimeException(databaseType + " could not be found.");
	}

	/**
	 * Instantiates a new database type.
	 * 
	 * @param productName the product name
	 */
	private DatabaseType(String productName){
		this(productName, new String[0]);
	}
	
	private DatabaseType(String productName, String[] aliases){
		this.productName = productName;
		this.aliases = aliases;
	}

	/**
	 * Gets the product name.
	 * 
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}
	
	 public String[] getAliases() {
		return aliases;
	}

	/**
 	 * Gets the database type.
 	 * 
 	 * @param dataSource the data source
 	 * 
 	 * @return the database type
 	 * 
 	 * @throws LBResourceUnavailableException the LB resource unavailable exception
 	 */
 	public static DatabaseType getDatabaseType(DataSource dataSource) throws LBResourceUnavailableException {
		 String databaseProductName;
		try {
			databaseProductName = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName").toString();
		} catch (MetaDataAccessException e) {
			throw new LBResourceUnavailableException("Error fetching database information.", e);
		}

		 String commonName = JdbcUtils.commonDatabaseName(databaseProductName);
		 
		 for(String dbName : nameMap.keySet()) {
			 if(commonName.toLowerCase().contains(dbName.toLowerCase())){
				 return nameMap.get(dbName);
			 }
		 }
		 throw new LBResourceUnavailableException("The underlying database " + commonName + " is not supported by LexEVS.");
	 }
}