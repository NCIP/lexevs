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
package org.lexevs.dao.database.operation;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.utility.DatabaseUtility;

/**
 * The Interface PersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsDatabaseOperations {

	/**
	 * Gets the new connection info for load.
	 * 
	 * @return the new connection info for load
	 */
	public SQLConnectionInfo getNewConnectionInfoForLoad(boolean indexTables);
	
	/**
	 * Gets the existing connection info.
	 * 
	 * @param codingScheme the coding scheme
	 * @param version the version
	 * 
	 * @return the existing connection info
	 */
	public SQLConnectionInfo getExistingConnectionInfo(String codingScheme, String version);

	/**
	 * Creates the tables.
	 * 
	 * @param connectionInfo the connection info
	 */
	public void createTables(SQLConnectionInfo connectionInfo, String prefix);
	
	public void createTables(String prefix);
	
	/**
	 * Drop tables.
	 * 
	 * @param connectionInfo the connection info
	 */
	public void dropTables(SQLConnectionInfo connectionInfo);
	
	/**
	 * Checks if is coding scheme loaded.
	 * 
	 * @param codingScheme the coding scheme
	 * @param version the version
	 * 
	 * @return true, if is coding scheme loaded
	 */
	public boolean isCodingSchemeLoaded(String codingScheme, String version);
	
	public void indexTables(SQLConnectionInfo connectionInfo);
	
	public void indexTables(String codingScheme, String version);
	
	public SQLTableConstants getSQLTableConstants(String codingScheme, String version);
	
	public SQLTableUtilities getSQLTableUtilities(String codingScheme, String version);
	
	public DatabaseUtility getDatabaseUtility();
}


