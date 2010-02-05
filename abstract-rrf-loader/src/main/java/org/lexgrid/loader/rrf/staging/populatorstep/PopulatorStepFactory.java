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
package org.lexgrid.loader.rrf.staging.populatorstep;

import java.util.Map;

import org.springframework.batch.core.Step;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating PopulatorStep objects.
 */
public class PopulatorStepFactory implements FactoryBean {

	/** The database type. */
	private DatabaseType databaseType;
	
	/** The populator steps. */
	private Map<DatabaseType,Step> populatorSteps;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		Step type = populatorSteps.get(databaseType);
		if(type == null){
			throw new RuntimeException("Could not find a Staging Table Populator Strategy for database: " + databaseType.toString());
		}
		return type;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Step.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
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
	 * Gets the populator steps.
	 * 
	 * @return the populator steps
	 */
	public Map<DatabaseType, Step> getPopulatorSteps() {
		return populatorSteps;
	}

	/**
	 * Sets the populator steps.
	 * 
	 * @param populatorSteps the populator steps
	 */
	public void setPopulatorSteps(Map<DatabaseType, Step> populatorSteps) {
		this.populatorSteps = populatorSteps;
	}
}
