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
package org.lexevs.dao.database.spring;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Generates a Spring Application Context. Uses a Properties Object (instead of a Properties file)
 * to set Property Placeholders.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DynamicPropertyApplicationContext extends AbstractXmlApplicationContext {

	/** The config resources. */
	private Resource[] configResources;
	
	/**
	 * Instantiates a new dynamic property application context.
	 * 
	 * @param configLocation the config location
	 * @param properties the properties
	 * 
	 * @throws BeansException the beans exception
	 */
	public DynamicPropertyApplicationContext(String configLocation, Properties properties)
			throws BeansException {
		super(null);	

		this.configResources = new Resource[1];
		
		this.configResources[0] = new ClassPathResource(configLocation);

		PropertyPlaceholderConfigurer beanConfigurer = new PropertyPlaceholderConfigurer();
		beanConfigurer.setOrder(Ordered.HIGHEST_PRECEDENCE);
		beanConfigurer.setIgnoreUnresolvablePlaceholders(true);
		beanConfigurer.setProperties(properties);

		this.addBeanFactoryPostProcessor(beanConfigurer);
		refresh();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#getConfigResources()
	 */
	public Resource[] getConfigResources() {
		return configResources;
	}
}