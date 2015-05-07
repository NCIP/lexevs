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
package org.lexevs.tree.service;

import java.io.InputStream;
import java.io.Serializable;

import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

/**
 * A factory for creating ApplicationContext objects.
 */
@Deprecated
public class ApplicationContextFactory implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5794966638343867430L;
	
	/** The instance. */
	private static ApplicationContextFactory instance;
	
	/** The context. */
	private ApplicationContext context;

	/**
	 * Gets the single instance of ApplicationContextFactory.
	 * 
	 * @return single instance of ApplicationContextFactory
	 */
	public static synchronized ApplicationContextFactory getInstance() {
		if (instance == null) {
			instance = new ApplicationContextFactory();
		}
		return instance;
	}

	/**
	 * Instantiates a new application context factory.
	 */
	protected ApplicationContextFactory(){
		GenericApplicationContext ctx = new GenericApplicationContext();
		ctx.setClassLoader(MyClassLoader.instance());
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		InputStream stream = MyClassLoader.instance().getResourceAsStream("deptreeServiceContext.xml");
		xmlReader.loadBeanDefinitions(new InputStreamResource(stream));
		ctx.refresh();

		this.context = ctx;
	}

	/**
	 * Gets the application context.
	 * 
	 * @return the application context
	 */
	public ApplicationContext getApplicationContext(){
		return this.context;
	}
}
