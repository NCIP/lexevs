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
package org.lexevs.locator;

import org.lexevs.cache.CacheWrappingFactory;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.operation.LexEvsIndexOperations;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Class LexEvsServiceLocator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsServiceLocator implements ApplicationContextAware, DisposableBean {
	
	private static Object MUTEX = new Object();
	
	/** The service locator. */
	private static volatile LexEvsServiceLocator serviceLocator;
	
	private static ApplicationContextCallback applicationContextCallback;

	/** The BEA n_ name. */
	private static String BEAN_NAME = "lexEvsServiceLocator";
	
	/** The CONTEX t_ file. */
	private static String CONTEXT_FILE = "lexevsDao.xml";

	/** The database service manager. */
	private DatabaseServiceManager databaseServiceManager;
	
	/** The resource manager. */
	private ResourceManager resourceManager;
	
	/** The registry. */
	private Registry registry;
	
	/** The lex evs database operations. */
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	/** The index service manager. */
	private IndexServiceManager indexServiceManager;
	
	private LexEvsIndexOperations lexEvsIndexOperations;
	
	private IndexDaoManager indexDaoManager;
	
	private CacheWrappingFactory cacheWrappingFactory;
	
	public static interface ApplicationContextCallback {
		public AbstractRefreshableApplicationContext buildApplicationContext(String xml);
	}
	
	/**
	 * Gets the single instance of LexEvsServiceLocator.
	 * 
	 * @return single instance of LexEvsServiceLocator
	 */
	public static LexEvsServiceLocator getInstance(){
		if(serviceLocator == null){
			synchronized(MUTEX){
				if(serviceLocator == null){
					AbstractRefreshableApplicationContext ctx;
					if(applicationContextCallback != null){
						ctx = applicationContextCallback.buildApplicationContext("classpath:" + CONTEXT_FILE);
						
						ctx.refresh();
					} else {
						ctx = new ClassPathXmlApplicationContext(CONTEXT_FILE);
					}
					
					ctx.registerShutdownHook();
		
					serviceLocator = (LexEvsServiceLocator) ctx.getBean(BEAN_NAME);
				}
			}
		}
		
		return serviceLocator;
	}
	
	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	@Deprecated
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
	 * Gets the registry.
	 * 
	 * @return the registry
	 */
	public Registry getRegistry() {
		return registry;
	}
	
	/**
	 * Sets the registry.
	 * 
	 * @param registry the new registry
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	/**
	 * Sets the lex evs database operations.
	 * 
	 * @param lexEvsDatabaseOperations the new lex evs database operations
	 */
	public void setLexEvsDatabaseOperations(LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	/**
	 * Gets the lex evs database operations.
	 * 
	 * @return the lex evs database operations
	 */
	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	
	/**
	 * Sets the database service manager.
	 * 
	 * @param databaseServiceManager the new database service manager
	 */
	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	/**
	 * Gets the database service manager.
	 * 
	 * @return the database service manager
	 */
	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	/**
	 * Sets the system resource service.
	 * 
	 * @param systemResourceService the new system resource service
	 */
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	/**
	 * Gets the index service manager.
	 * 
	 * @return the index service manager
	 */
	public IndexServiceManager getIndexServiceManager() {
		return indexServiceManager;
	}

	/**
	 * Sets the index service manager.
	 * 
	 * @param indexServiceManager the new index service manager
	 */
	public void setIndexServiceManager(IndexServiceManager indexServiceManager) {
		this.indexServiceManager = indexServiceManager;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
		
		if(serviceLocator == null) {
			serviceLocator = (LexEvsServiceLocator) applicationContext.getBean(BEAN_NAME);	
		}
	}

	public void setCacheWrappingFactory(CacheWrappingFactory cacheWrappingFactory) {
		this.cacheWrappingFactory = cacheWrappingFactory;
	}

	public CacheWrappingFactory getCacheWrappingFactory() {
		return cacheWrappingFactory;
	}

	public void setLexEvsIndexOperations(LexEvsIndexOperations lexEvsIndexOperations) {
		this.lexEvsIndexOperations = lexEvsIndexOperations;
	}

	public LexEvsIndexOperations getLexEvsIndexOperations() {
		return lexEvsIndexOperations;
	}
	
	public static void setApplicationContextCallback(
			ApplicationContextCallback applicationContextCallback) {
		if(serviceLocator == null){
			synchronized(MUTEX){
				if(serviceLocator == null){
					LexEvsServiceLocator.applicationContextCallback = applicationContextCallback;
				}
			}
		}
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	@Override
	public void destroy() throws Exception {
		serviceLocator = null;
		applicationContextCallback = null;
	}
}