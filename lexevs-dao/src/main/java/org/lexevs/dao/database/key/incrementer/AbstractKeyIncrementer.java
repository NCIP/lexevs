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
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * The Class AbstractKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractKeyIncrementer implements PrimaryKeyIncrementer {

	/** The data field max value incrementer. */
	private DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#nextKey()
	 */
	@Override
	public String nextKey() {
		if(dataFieldMaxValueIncrementer == null) {
			dataFieldMaxValueIncrementer = this.createDataFieldMaxValueIncrementer();
		}
		return this.dataFieldMaxValueIncrementer.nextStringValue();
	}
	
	/**
	 * Creates the data field max value incrementer.
	 * 
	 * @return the data field max value incrementer
	 */
	protected abstract DataFieldMaxValueIncrementer createDataFieldMaxValueIncrementer();	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#supportsDatabases(org.lexevs.dao.database.type.DatabaseType)
	 */
	@Override
	public boolean supportsDatabases(DatabaseType databaseType) {
		for(DatabaseType dbType : getSupportedDatabaseTypes()){
			if(dbType.equals(databaseType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the supported database types.
	 * 
	 * @return the supported database types
	 */
	protected abstract List<DatabaseType> getSupportedDatabaseTypes();
}