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
package org.lexevs.dao.database.type;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

public enum DatabaseType {

	DERBY("Apache Derby"),
	DB2("DB2"),
	HSQL("HSQL Database Engine"),
	MYSQL("MySQL"),
	ORACLE("Oracle"),
	POSTGRES("PostgreSQL");
	
	private static final Map<String, DatabaseType> nameMap;

	private final String productName;

	static{
		nameMap = new HashMap<String, DatabaseType>();
		for(DatabaseType type: values()){
			nameMap.put(type.getProductName(), type);
		}
	}

	private DatabaseType(String productName){
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	 public static DatabaseType getDatabaseType(DataSource dataSource) throws LBResourceUnavailableException {
		 String databaseProductName;
		try {
			databaseProductName = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName").toString();
		} catch (MetaDataAccessException e) {
			throw new LBResourceUnavailableException("Error fetching database information.", e);
		}
		 
		 String commonName = JdbcUtils.commonDatabaseName(databaseProductName);
		 
		 if(nameMap.containsKey(commonName)){
			 return nameMap.get(commonName);
		 } else {
			 throw new LBResourceUnavailableException("The underlying database " + commonName + " is not supported by LexEVS.");
		 }
	 }
}
