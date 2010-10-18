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
package org.lexevs.dao.database.access.factory;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating Dao objects.
 */
public class DaoFactory<T extends LexGridSchemaVersionAwareDao> implements FactoryBean, InitializingBean {

	/** The versionable daos. */
	private List<T> versionableDaos;
	
	/** The dao to return. */
	private T daoToReturn;
	
	/** The lex grid schema version. */
	private LexGridSchemaVersion lexGridSchemaVersion;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return daoToReturn;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersionAwareDao.class;
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
		List<T> daos = new ArrayList<T>();
		
		for(T dao : versionableDaos){
			if(dao.supportsLgSchemaVersion(lexGridSchemaVersion)){
				daos.add(dao);
			}
		}
		
		Assert.assertTrue("No matching DAO for Database Version: " +
				lexGridSchemaVersion, daos.size() > 0);	
		
		Assert.assertTrue("More than one matching DAO for: " +
				daos.get(0).getClass().getName(), daos.size() < 2);
		
		daoToReturn = daos.get(0);
	}

	/**
	 * Gets the versionable daos.
	 * 
	 * @return the versionable daos
	 */
	public List<T> getVersionableDaos() {
		return versionableDaos;
	}

	/**
	 * Sets the versionable daos.
	 * 
	 * @param versionableDaos the new versionable daos
	 */
	public void setVersionableDaos(List<T> versionableDaos) {
		this.versionableDaos = versionableDaos;
	}

	/**
	 * Sets the lex grid schema version.
	 * 
	 * @param lexGridSchemaVersion the new lex grid schema version
	 */
	public void setLexGridSchemaVersion(LexGridSchemaVersion lexGridSchemaVersion) {
		this.lexGridSchemaVersion = lexGridSchemaVersion;
	}

	/**
	 * Gets the lex grid schema version.
	 * 
	 * @return the lex grid schema version
	 */
	public LexGridSchemaVersion getLexGridSchemaVersion() {
		return lexGridSchemaVersion;
	}


}