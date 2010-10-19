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
package org.lexgrid.loader.test.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.lexevs.system.ResourceManager;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.DataSources;

/**
 * The Class TestUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoaderTestUtils {
	
	/** The log. */
	private static Logger log = Logger.getLogger(LoaderTestUtils.class.getName());
	
	/** The Constant TMP_DIR. */
	private static final String TMP_DIR = "src/test/tmp";	
	
	/** The Constant INDEX_DIR. */
	private static final String INDEX_DIR = "src/test/resources/lbIndex";	
	
	/** The Constant REGISTRY_FILE. */
	private static final String REGISTRY_FILE = "src/test/resources/config/registry.xml";	
	
	/** The Constant LOCK_FILE. */
	private static final String LOCK_FILE = "src/test/resources/config/lock.xml";
	
	/**
	 * Delete directory.
	 * 
	 * @param path the path
	 * 
	 * @return true, if successful
	 */
	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					if(!files[i].delete()){
						return false;
					}
				}
			}			
			return path.delete();
		}
		//if it doesn't exists, consider the delete a success.
		return true;	
	}
	
	public static boolean emptyDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					if(!files[i].delete()){
						return false;
					}
				}
			}			
		}
		//if it doesn't exists, consider the delete a success.
		return true;	
	}

	public static void cleanUpDatabase(){
		shutdownnHsqldb();
		destroyConnectionPool();
		cleanTmpDirContents();
	}
	
	
	/**
	 * Clean up all.
	 */
	public static void cleanUpAll(){
		cleanIndexes();
		deleteRegistry();
		deleteLock();
	}
	
	/**
	 * Clean tmp dir.
	 */
	public static void cleanTmpDirContents(){
		File tmpDir = new File(TMP_DIR);
		if(!emptyDirectory(tmpDir)){
			tmpDir.deleteOnExit();
		}
	}
	
	/**
	 * Clean indexes.
	 */
	public static void cleanIndexes(){
		File indexDir = new File(INDEX_DIR);
		if(!deleteDirectory(indexDir)){
			indexDir.deleteOnExit();
		}
	}
	
	/**
	 * Delete registry.
	 */
	public static void deleteRegistry(){
		File registry = new File(REGISTRY_FILE);
		registry.delete();
	}
	
	/**
	 * Delete lock.
	 */
	public static void deleteLock(){
		File lock = new File(LOCK_FILE);
		lock.delete();
	}
	
	/**
	 * Shutdownn hsqldb.
	 */
	public static void shutdownnHsqldb() {	
		try {
			//String url = SystemResourceService.instance().getSystemVariables().getAutoLoadDBURL();
			//Class.forName("org.hsqldb.jdbcDriver");
			//Connection con = DriverManager.getConnection(url, "sa", "");
			//String sql = "SHUTDOWN";
			//Statement stmt = con.createStatement();
			//stmt.executeUpdate(sql);
			//stmt.close();
		} catch (Exception e) {
			log.warning("Could not close HSQL Database -- already closed. " + e.getMessage());
		}
	}	
	
	public static void destroyConnectionPool() {
		try {
			Iterator itr = C3P0Registry.allPooledDataSources().iterator();
			while(itr.hasNext()){
				DataSources.destroy((DataSource)itr.next());
			}
			
		} catch (Exception e) {
			log.warning("Connection Pool not cleaned up.");
			e.printStackTrace();
		}
	}
	
	
}