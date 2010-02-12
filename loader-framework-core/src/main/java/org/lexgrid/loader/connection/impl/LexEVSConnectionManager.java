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
package org.lexgrid.loader.connection.impl;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Index.IndexLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.messaging.impl.CommandLineMessageDirector;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.operation.DefaultLexEvsDatabaseOperations;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexgrid.loader.connection.LoaderConnectionManager;

import edu.mayo.informatics.lexgrid.convert.indexer.SQLIndexer;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * The Class LexEVSConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEVSConnectionManager extends DefaultLexEvsDatabaseOperations implements LoaderConnectionManager  {
	
	private LgMessageDirectorIF messageDirector;
	
	private static String INDEX_LOADER = "IndexLoader";
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#computeTransitiveTable(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public LexEVSConnectionManager(LgMessageDirectorIF messageDirector){
		this.messageDirector = messageDirector;
		messageDirector.debug("Starting " + this.getClass().getName() 
				+ " with " + messageDirector.getClass().getName() + " MessageDirector.");
	}
	
	public LexEVSConnectionManager(){
		this.messageDirector =  new CommandLineMessageDirector();
		messageDirector.debug("Starting " + this.getClass().getName() 
				+ " with Default " + messageDirector.getClass().getName() + " MessageDirector.");
	}
	
	public void computeTransitiveTable(String codingSchemeName, String codingSchemeUri, String version) {
		try {
			SQLConnectionInfo loadConnectinoInfo = 
				this.getConnectionInfo(codingSchemeUri, version);
			
			SQLTableUtilities stu = super.createSQLTableUtilities(loadConnectinoInfo);
			stu.computeTransitivityTable(codingSchemeName, messageDirector);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#index(java.lang.String, org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo)
	 */
	public void index(String codingSchemeName, SQLConnectionInfo connectionInfo){
		SystemVariables sv = ResourceManager.instance().getSystemVariables();
		if (connectionInfo.server.indexOf("jdbc:mysql") != -1) {
			// mysql gets results in stages, has to rerun the query multiple
			// times.
			// use a much larger batch size.
			Constants.mySqlBatchSize = 50000;
		} else {
			Constants.mySqlBatchSize = 10000;
		}
		try {
			new SQLIndexer((sv.getAutoLoadSingleDBMode() ? connectionInfo.prefix : connectionInfo.dbName), sv.getAutoLoadIndexLocation(),
					connectionInfo.username, connectionInfo.password, connectionInfo.server, connectionInfo.driver, connectionInfo.prefix, 
					new String[]{codingSchemeName}, messageDirector, sv.isNormEnabled(),
					true, true, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#index(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void index(String codingSchemeName, String codingSchemeUri, String version){
		try {
			SQLConnectionInfo loadConnectinoInfo = 
				this.getConnectionInfo(codingSchemeUri, version);
			this.index(codingSchemeName, loadConnectinoInfo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void reIndex(String codingSchemeUri, String version){
		try {
			AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version);
			
			IndexLoader loader = (IndexLoader) LexBIGServiceImpl.defaultInstance()
			.getServiceManager(null).getLoader(INDEX_LOADER);
			
			loader.rebuild(ref, null, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#register(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void register(String uri, String version, String status, String serverUrl, String tag, String dbName, String prefix) throws Exception {
		ResourceManager rm = ResourceManager.instance();
		rm.getRegistry().addNewItem(uri, 
				version, 
				status, 
				serverUrl, 
				tag,
				dbName, 
				prefix);
		SQLConnectionInfo connectionInfo = 
			this.getConnectionInfo(uri, version);
		
		rm.readTerminologiesFromServer(connectionInfo);
	}
	
	/**
	 * Register.
	 * 
	 * @param connectionInfo the connection info
	 * @param status the status
	 * 
	 * @throws Exception the exception
	 */
	public void register(SQLConnectionInfo connectionInfo, String status) throws Exception {
		register(connectionInfo.urn, 
				connectionInfo.version, 
				status, 
				connectionInfo.server, 
				null,
				connectionInfo.dbName, 
				connectionInfo.prefix);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#activate(java.lang.String, java.lang.String)
	 */
	public void activate(String codingSchemeUri, String version) throws Exception {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version);
		LexBIGServiceImpl.defaultInstance().getServiceManager(null).activateCodingSchemeVersion(ref);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#deactivate(java.lang.String, java.lang.String)
	 */
	public void deactivate(String codingSchemeUri, String version) throws Exception {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version);
		LexBIGServiceImpl.defaultInstance().getServiceManager(null).deactivateCodingSchemeVersion(ref, new Date());
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#cleanupFailedLoad(java.lang.String, java.lang.String)
	 */
	public void cleanupFailedLoad(String dbName, String prefix) throws Exception {
		SystemVariables systemVariables = ResourceManager.instance().getSystemVariables();
		   try {
			CleanUpUtility.removeUnusedDatabase(systemVariables.getAutoLoadSingleDBMode() ? prefix
			           : dbName); 
		} catch (Exception e) {
			throw e;
		}	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#initLoadedScheme(java.lang.String, java.lang.String)
	 */
	public void initLoadedScheme(String codingSchemeUri, String version) throws Exception {
		refreshResourceManager(
				super.getConnectionInfo(codingSchemeUri, version));
		reloadIndexLocations();
	}
	
	/**
	 * Refresh resource manager.
	 * 
	 * @param connectionInfo the connection info
	 */
	protected void refreshResourceManager(SQLConnectionInfo connectionInfo){
		ResourceManager.instance().readTerminologiesFromServer(connectionInfo);
	}
	
	/**
	 * Reload index locations.
	 * 
	 * @throws Exception the exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	protected void reloadIndexLocations() throws Exception, UnexpectedInternalError {
		ResourceManager.instance().rereadAutoLoadIndexes();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.connection.LoaderConnectionManager#getLexEvsLoader(java.lang.String, java.lang.Class)
	 */
	public <T> T getLexEvsLoader(String loaderName, Class<T> loaderClass) throws LBException {
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		LexBIGServiceManager lbsm = lbs.getServiceManager(null);
		return (T)lbsm.getLoader(loaderName);
	}

	public LgMessageDirectorIF getMessageDirector() {
		return messageDirector;
	}

	public void setMessageDirector(LgMessageDirectorIF messageDirector) {
		this.messageDirector = messageDirector;
	}	
}
