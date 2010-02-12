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
package org.lexgrid.loader.connection;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;

/**
 * The Interface LoaderConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LoaderConnectionManager extends LexEvsDatabaseOperations {

	/**
	 * Compute transitive table.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void computeTransitiveTable(String codingSchemeName, String codingSchemeUri, String version);
	
	/**
	 * Index.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param connectionInfo the connection info
	 */
	public void index(String codingSchemeName, SQLConnectionInfo connectionInfo);
	
	/**
	 * Index.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void index(String codingSchemeName, String codingSchemeUri, String version);
	
	/**
	 * Register.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param status the status
	 * @param serverUrl the server url
	 * @param tag the tag
	 * @param dbName the db name
	 * @param prefix the prefix
	 * 
	 * @throws Exception the exception
	 */
	public void register(String uri, String version, String status, String serverUrl, String tag, String dbName, String prefix)  throws Exception;
	
	/**
	 * Activate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @throws Exception the exception
	 */
	public void activate(String codingSchemeUri, String version) throws Exception;
	
	/**
	 * Deactivate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @throws Exception the exception
	 */
	public void deactivate(String codingSchemeUri, String version) throws Exception;
	
	/**
	 * Cleanup failed load.
	 * 
	 * @param dbName the db name
	 * @param prefix the prefix
	 * 
	 * @throws Exception the exception
	 */
	public void cleanupFailedLoad(String dbName, String prefix) throws Exception;
	
	/**
	 * Inits the loaded scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @throws Exception the exception
	 */
	public void initLoadedScheme(String codingSchemeUri, String version) throws Exception;

	/**
	 * Gets the lex evs loader.
	 * 
	 * @param loaderName the loader name
	 * @param loaderClass the loader class
	 * 
	 * @return the lex evs loader
	 * 
	 * @throws LBException the LB exception
	 */
	public <T> T getLexEvsLoader(String loaderName, Class<T> loaderClass) throws LBException;
	
	public void reIndex(String codingSchemeUri, String version);
}
