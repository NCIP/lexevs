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
import org.lexgrid.loader.connection.LoaderConnectionManager;
import org.lexgrid.loader.connection.impl.LexEVSConnectionManager;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;

/**
 * A factory for creating DefaultLexEVSProperties objects.
 */
public class DefaultLexEVSPropertiesFactory extends PropertiesFactory implements ConnectionPropertiesFactory {
	
	/** The connection manager. */
	private LoaderConnectionManager connectionManager = new LexEVSConnectionManager();

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.properties.ConnectionPropertiesFactory#getPropertiesForNewLoad()
	 */
	public Properties getPropertiesForNewLoad(boolean indexTables) {		
		SQLConnectionInfo connection = 
			connectionManager.getNewConnectionInfoForLoad(indexTables);
		return getProperties(connection);		
	}
		
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.properties.ConnectionPropertiesFactory#getPropertiesForExistingLoad(java.lang.String, java.lang.String)
	 */
	public Properties getPropertiesForExistingLoad(String codingScheme, String version) {		
		SQLConnectionInfo connection = 
			connectionManager.getExistingConnectionInfo(codingScheme, version);
		return getProperties(connection);
	}
		
	/**
	 * Gets the connection manager.
	 * 
	 * @return the connection manager
	 */
	public LoaderConnectionManager getConnectionManager() {
		return connectionManager;
	}

	/**
	 * Sets the connection manager.
	 * 
	 * @param connectionManager the new connection manager
	 */
	public void setConnectionManager(LoaderConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
}
