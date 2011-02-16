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
package org.cts2.web.rest.wadl.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.cts2.web.rest.wadl.Wadl;
import org.cts2.web.rest.wadl.WadlUtils;
import org.cts2.web.rest.wadl.annotation.WadlRequestMapping;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

/**
 * The Class WadlAnnotationHanlderMapping.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class WadlAnnotationHanlderMapping extends DefaultAnnotationHandlerMapping {
	
	/** The wadl. */
	@Resource
	private Wadl wadl;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping#determineUrlsForHandlerMethods(java.lang.Class, boolean)
	 */
	@Override
	protected String[] determineUrlsForHandlerMethods(Class<?> handlerType,
			boolean hasTypeLevelMapping) {
		
		final List<String> paths = new ArrayList<String>();
		
		ReflectionUtils.doWithMethods(handlerType, new MethodCallback(){

			@Override
			public void doWith(Method method) throws IllegalArgumentException,
					IllegalAccessException {
				
				WadlRequestMapping mapping = 
					method.getAnnotation(WadlRequestMapping.class);
				
				if(mapping != null){
					com.sun.research.ws.wadl.Resource resource = 
						wadl.getResourceById(mapping.resourceId());
	
					paths.add(WadlUtils.replacePathParametersWithWildcards(resource.getPath()));
				}
			}
		});
			
		paths.addAll(Arrays.asList(super.determineUrlsForHandlerMethods(handlerType, hasTypeLevelMapping)));

		return paths.toArray(new String[paths.size()]);
	}
	
	/**
	 * Gets the wadl.
	 *
	 * @return the wadl
	 */
	public Wadl getWadl() {
		return wadl;
	}

	/**
	 * Sets the wadl.
	 *
	 * @param wadl the new wadl
	 */
	public void setWadl(Wadl wadl) {
		this.wadl = wadl;
	}
}
