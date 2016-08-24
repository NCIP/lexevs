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
package org.lexevs.tree.dao;

import javax.sql.DataSource;

import org.lexevs.tree.service.ApplicationContextFactory;



/**
 * A factory for creating DataSource objects.
 */
@Deprecated
public class DataSourceFactory {

	/** The instance. */
	private static DataSourceFactory instance;

	/**
	 * Gets the single instance of DataSourceFactory.
	 * 
	 * @return single instance of DataSourceFactory
	 */
	public static synchronized DataSourceFactory getInstance() {
		if (instance == null) {
			instance = new DataSourceFactory();
		}
		return instance;
	}
	
	/**
	 * Gets the lex evs tree dao.
	 * 
	 * @return the lex evs tree dao
	 */
	public DataSource getLexEvsTreeDao(){
		return (DataSource)ApplicationContextFactory.getInstance().getApplicationContext().getBean("dataSource");
	}
}
