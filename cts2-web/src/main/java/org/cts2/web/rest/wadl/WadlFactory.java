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
package org.cts2.web.rest.wadl;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;

import com.sun.research.ws.wadl.Application;

/**
 * A factory for creating Wadl objects.
 */
public class WadlFactory implements ServletContextAware, FactoryBean<Wadl> {
	
	/** The servlet context. */
	private ServletContext servletContext;
	
	/** The WAD l_ path. */
	private static String WADL_PATH = "WEB-INF/application.wadl";
	
	private String wadlPath = WADL_PATH;

	/**
	 * Gets the wadl.
	 *
	 * @param wadl the wadl
	 * @return the wadl
	 */
	public Application getWadl(Resource wadl){
		try {
			JAXBContext context = JAXBContext.newInstance(Application.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			return (Application) unmarshaller.unmarshal(wadl.getInputStream());

		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Wadl getObject() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Application.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Application application =
			(Application) unmarshaller.unmarshal(this.servletContext.getResourceAsStream(wadlPath));
		
		return new Wadl(application);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return Wadl.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;	
	}

	public void setWadlPath(String wadlPath) {
		this.wadlPath = wadlPath;
	}

	public String getWadlPath() {
		return wadlPath;
	}
}
