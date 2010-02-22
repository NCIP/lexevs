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
package org.lexgrid.loader.properties.impl;

import java.util.Properties;

import org.lexevs.dao.database.connection.SQLConnectionInfo;

/**
 * A factory for creating Properties objects given a SQLConnection from LexEVS
 */
public class PropertiesFactory {

	/** The PREFIX. */
	public static String PREFIX = "prefix";
	
	/** The URL. */
	public static String URL = "jdbcUrl";
	
	/** The DATABASE. */
	public static String DATABASE = "database";
	
	/** The DRIVER. */
	public static String DRIVER = "driver";
	
	/** The USERNAME. */
	public static String USERNAME = "user";
	
	/** The PASSWORD. */
	public static String PASSWORD = "password";

	/**
	 * Gets the properties.
	 * 
	 * @param connection the connection
	 * 
	 * @return the properties
	 */
	public Properties getProperties(String prefix){
		Properties props = new Properties();
		props.setProperty(PREFIX, prefix);
		return props;		
	}
}
