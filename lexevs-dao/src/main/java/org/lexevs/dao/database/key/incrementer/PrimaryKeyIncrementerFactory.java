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

import java.util.List;

import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating PrimaryKeyIncrementer objects.
 */
public class PrimaryKeyIncrementerFactory implements FactoryBean {
	
	public static KeyIncrementerThreadLocal THREAD_LOCAL  = 
		new KeyIncrementerThreadLocal();

	public static class KeyIncrementerThreadLocal extends ThreadLocal<PrimaryKeyIncrementer> {

		private PrimaryKeyIncrementer primaryKeyIncrementer;

		protected PrimaryKeyIncrementer initialValue() {
			return primaryKeyIncrementer;
		}
		
		public PrimaryKeyIncrementer get() {
			return primaryKeyIncrementer;
		}
	}

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The primary key incrementers. */
	private List<PrimaryKeyIncrementer> primaryKeyIncrementers;
	
	/** The database type. */
	private DatabaseType databaseType;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		String primaryKeyStrategy = systemVariables.getPrimaryKeyStrategy();
		
		for(PrimaryKeyIncrementer incrementer : primaryKeyIncrementers) {
			if(incrementer.getName().equals(primaryKeyStrategy) &&
					incrementer.supportsDatabases(databaseType)) {
				
				THREAD_LOCAL.primaryKeyIncrementer = incrementer;
				return incrementer;
			}
		}
		throw new RuntimeException(primaryKeyStrategy + " is not a registered Primary Key Strategy.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return PrimaryKeyIncrementer.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
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
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
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
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the primary key incrementers.
	 * 
	 * @param primaryKeyIncrementers the new primary key incrementers
	 */
	public void setPrimaryKeyIncrementers(List<PrimaryKeyIncrementer> primaryKeyIncrementers) {
		this.primaryKeyIncrementers = primaryKeyIncrementers;
	}

	/**
	 * Gets the primary key incrementers.
	 * 
	 * @return the primary key incrementers
	 */
	public List<PrimaryKeyIncrementer> getPrimaryKeyIncrementers() {
		return primaryKeyIncrementers;
	}
}
