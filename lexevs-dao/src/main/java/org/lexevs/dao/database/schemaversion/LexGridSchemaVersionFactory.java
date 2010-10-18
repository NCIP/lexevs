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
package org.lexevs.dao.database.schemaversion;

import org.lexevs.dao.database.access.tablemetadata.TableMetadataDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating LexGridSchemaVersion objects.
 */
public class LexGridSchemaVersionFactory implements FactoryBean, InitializingBean{

	/** The table metadata dao. */
	private TableMetadataDao tableMetadataDao;

	/** The version. */
	private LexGridSchemaVersion version;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return version;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersion.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		version = LexGridSchemaVersion.parseStringToVersion(tableMetadataDao.getVersion());	
	}

	/**
	 * Gets the table metadata dao.
	 * 
	 * @return the table metadata dao
	 */
	public TableMetadataDao getTableMetadataDao() {
		return tableMetadataDao;
	}

	/**
	 * Sets the table metadata dao.
	 * 
	 * @param tableMetadataDao the new table metadata dao
	 */
	public void setTableMetadataDao(TableMetadataDao tableMetadataDao) {
		this.tableMetadataDao = tableMetadataDao;
	}
}