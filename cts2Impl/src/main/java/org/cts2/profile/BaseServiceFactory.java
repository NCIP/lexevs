/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.profile;

import org.apache.commons.lang.StringUtils;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A factory for creating BaseService objects.
 */
public class BaseServiceFactory implements FactoryBean<BaseService> {
	
	private static String APPLICATION_CONFIG_CONTEXT = "cts2-application-config.xml";
	
	/** The base service factory. */
	private static BaseServiceFactory baseServiceFactory;
	
	/** The base service. */
	private BaseService baseService;
	
	/** The lg config file. */
	private String lgConfigFile;

	/**
	 * Instance.
	 *
	 * @return the base service factory
	 */
	public static synchronized BaseServiceFactory instance(){
		if(baseServiceFactory == null){
			baseServiceFactory = new BaseServiceFactory();
		}
		return baseServiceFactory;
	}
	
	/**
	 * Gets the base service.
	 *
	 * @return the base service
	 */
	public synchronized BaseService getBaseService(){
		
		if(baseService == null){
			
			if(StringUtils.isNotBlank(this.lgConfigFile)){
				System.setProperty(SystemVariables.LG_CONFIG_FILE_SYSTEM_VARIABLE, this.lgConfigFile);
			}
			
			ApplicationContext appContext = new ClassPathXmlApplicationContext(APPLICATION_CONFIG_CONTEXT);
			baseService = appContext.getBean(BaseService.class);
		}
		
		return baseService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public BaseService getObject() throws Exception {
		return getBaseService();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return BaseService.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets the lg config file.
	 *
	 * @param lgConfigFile the new lg config file
	 */
	public void setLgConfigFile(String lgConfigFile) {
		this.lgConfigFile = lgConfigFile;
	}

	/**
	 * Gets the lg config file.
	 *
	 * @return the lg config file
	 */
	public String getLgConfigFile() {
		return lgConfigFile;
	}
}
