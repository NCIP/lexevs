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
package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.springframework.aop.framework.Advised;

/**
 * A factory for creating TreeService objects.
 */
public class TreeServiceFactory implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5794966638343867430L;
	
	/** The instance. */
	private static TreeServiceFactory instance;
	
	/** The remote tree service. */
	private static TreeService remoteTreeService;
	
	/** The local tree service. */
	private static TreeService localTreeService;

	/**
	 * Gets the single instance of TreeServiceFactory.
	 * 
	 * @return single instance of TreeServiceFactory
	 */
	public static synchronized TreeServiceFactory getInstance() {
		if (instance == null) {
			instance = new TreeServiceFactory();
		}
		return instance;
	}
	
	/**
	 * Instantiates a new tree service factory.
	 */
	protected TreeServiceFactory(){};
	
	/**
	 * Gets the tree service.
	 * 
	 * @param lbs the lbs
	 * 
	 * @return the tree service
	 */
	public synchronized TreeService getTreeService(LexBIGService lbs){
		if(lbs instanceof Advised){
			if(remoteTreeService ==  null){
				PathToRootTreeServiceImpl service = (PathToRootTreeServiceImpl)getTreeServiceExtension(lbs);
				remoteTreeService = service.getSpringManagedBean();
			}
			return remoteTreeService;
		} else {
			if(localTreeService ==  null){
				PathToRootTreeServiceImpl service = (PathToRootTreeServiceImpl)getTreeServiceExtension(lbs);
				localTreeService = service.getSpringManagedBean();
			}
			return localTreeService;
		}
	}
	
	/**
	 * Gets the tree service extension.
	 * 
	 * @param lbs the lbs
	 * 
	 * @return the tree service extension
	 */
	protected TreeService getTreeServiceExtension(LexBIGService lbs){
		try {
			return (TreeService)lbs.getGenericExtension("lex-tree-utility");
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the tree service spring bean.
	 * 
	 * @return the tree service spring bean
	 */
	protected TreeService getTreeServiceSpringBean(){
		return (TreeService)ApplicationContextFactory.getInstance().getApplicationContext().getBean("pathToRootTreeServiceImpl");
	}
}
