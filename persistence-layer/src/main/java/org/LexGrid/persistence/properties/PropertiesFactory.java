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
package org.LexGrid.persistence.properties;

import java.util.Properties;

import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;

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
	public Properties getProperties(SQLConnectionInfo connection){
		Properties props = new Properties();
		props.setProperty(PREFIX, connection.prefix);
		props.setProperty(URL, connection.server);
		props.setProperty(DATABASE, connection.dbName);
		props.setProperty(DRIVER, connection.driver);
		props.setProperty(USERNAME, connection.username);
		props.setProperty(PASSWORD, connection.password);
		return props;		
	}
	
	public static SQLConnectionInfo buildSQLConnectinInfoFromProperties(Properties props){
		SQLConnectionInfo connectionInfo = new SQLConnectionInfo();
		connectionInfo.prefix = props.getProperty(PREFIX);
		connectionInfo.server = props.getProperty(URL);
		connectionInfo.dbName = props.getProperty(DATABASE);
		connectionInfo.driver = props.getProperty(DRIVER);
		connectionInfo.username = props.getProperty(USERNAME);
		connectionInfo.password = props.getProperty(PASSWORD);
		return connectionInfo;
	}
}
