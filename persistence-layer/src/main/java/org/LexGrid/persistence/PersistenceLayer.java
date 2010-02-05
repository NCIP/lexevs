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
package org.LexGrid.persistence;

import java.util.Properties;

import org.LexGrid.persistence.connection.LexEvsPersistenceConnectionManager;
import org.LexGrid.persistence.connection.PersistenceConnectionManager;
import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.properties.PropertiesFactory;
import org.LexGrid.persistence.spring.DynamicPropertyApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Main class for connecting to a LexEvs Database.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PersistenceLayer {
	
	/** The Constant PERSISTENCE_CTX. */
	private static final String PERSISTENCE_CTX = "persistence.xml";
	
	/** The Constant LEXBIG_SYSPROPERTY. */
	private static final String LEXBIG_SYSPROPERTY = "LG_CONFIG_FILE";
	
	/** The properties factory. */
	private PropertiesFactory propertiesFactory;
	
	/** The persistence connection manager. */
	private PersistenceConnectionManager persistenceConnectionManager;
	
	
	/**
	 * Instantiates a new persistence layer.
	 */
	public PersistenceLayer(){
		propertiesFactory = new PropertiesFactory();
		persistenceConnectionManager = new LexEvsPersistenceConnectionManager();
	}
	
	/**
	 * Connect to an existing LexEvs Database.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the lex evs dao
	 */
	public LexEvsDao connect(String uri, String version){
		Properties props = propertiesFactory.getProperties(persistenceConnectionManager.getExistingConnectionInfo(uri, version));
		ApplicationContext ctx = new DynamicPropertyApplicationContext(PERSISTENCE_CTX, props);
		return (LexEvsDao)ctx.getBean("lexEvsDao");	
	}
	
	/**
	 * Connect to an existing LexEvs Database.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param lbConfigFile the lb config file
	 * 
	 * @return the lex evs dao
	 */
	public LexEvsDao connect(String uri, String version, String lbConfigFile){
		System.setProperty(LEXBIG_SYSPROPERTY, lbConfigFile);
		Properties props = propertiesFactory.getProperties(persistenceConnectionManager.getExistingConnectionInfo(uri, version));
		ApplicationContext ctx = new DynamicPropertyApplicationContext(PERSISTENCE_CTX, props);
		return (LexEvsDao)ctx.getBean("lexEvsDao");	
	}
	
	/**
	 * Gets the jdbc dao support.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the jdbc dao support
	 */
	public JdbcTemplate getJdbcDaoSupport(String uri, String version){
		Properties props = propertiesFactory.getProperties(persistenceConnectionManager.getExistingConnectionInfo(uri, version));
		ApplicationContext ctx = new DynamicPropertyApplicationContext(PERSISTENCE_CTX, props);
		return (JdbcTemplate)ctx.getBean("lexEvsJdbcTemplate");	
	}
	
}
