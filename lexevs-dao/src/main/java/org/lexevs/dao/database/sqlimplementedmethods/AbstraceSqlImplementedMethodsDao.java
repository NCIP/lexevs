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
package org.lexevs.dao.database.sqlimplementedmethods;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.system.ResourceManager;

/**
 * The Class AbstraceSqlImplementedMethodsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstraceSqlImplementedMethodsDao extends AbstractBaseDao{
	
	/** The KE y_ seperator. */
	protected static String KEY_SEPERATOR = "[:]";
	
	/** The resource manager. */
	private ResourceManager resourceManager;
	
	/** The sql implemented methods dao. */
	private SQLImplementedMethodsDao sqlImplementedMethodsDao;
	
	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	/**
	 * Sets the resource manager.
	 * 
	 * @param resourceManager the new resource manager
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	/**
	 * Gets the sql implemented methods dao.
	 * 
	 * @return the sql implemented methods dao
	 */
	public SQLImplementedMethodsDao getSqlImplementedMethodsDao() {
		return sqlImplementedMethodsDao;
	}
	
	/**
	 * Sets the sql implemented methods dao.
	 * 
	 * @param sqlImplementedMethodsDao the new sql implemented methods dao
	 */
	public void setSqlImplementedMethodsDao(
			SQLImplementedMethodsDao sqlImplementedMethodsDao) {
		this.sqlImplementedMethodsDao = sqlImplementedMethodsDao;
	}
}
