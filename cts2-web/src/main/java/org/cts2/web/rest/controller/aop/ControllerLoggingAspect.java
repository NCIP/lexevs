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
package org.cts2.web.rest.controller.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.cts2.web.rest.controller.annotation.WadlRequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The Class ControllerLoggingAspect.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
public class ControllerLoggingAspect {
	
	/** The log. */
	private static Logger log = Logger.getLogger(ControllerLoggingAspect.class);

	/**
	 * Log controller request.
	 *
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("@within(org.springframework.stereotype.Controller) && " +
			"( @annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
			" @annotation(org.cts2.web.rest.controller.annotation.WadlRequestMapping) )")
	public Object logControllerRequest(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sig = (MethodSignature)pjp.getSignature();
		
		RequestMapping requestMapping = 
			sig.getMethod().getAnnotation(RequestMapping.class);
		
		if(requestMapping != null){
			this.doLogRequest(sig, requestMapping);
		}
		
		WadlRequestMapping wadlRequestMapping = 
			sig.getMethod().getAnnotation(WadlRequestMapping.class);
		
		if(wadlRequestMapping != null){
			this.doLogRequest(sig, wadlRequestMapping);
		}
		
		return pjp.proceed();	
	}
	
	/**
	 * Do log request.
	 *
	 * @param signature the signature
	 * @param requestMapping the request mapping
	 */
	protected void doLogRequest(MethodSignature signature, RequestMapping requestMapping){
		StringBuffer sb = new StringBuffer();
		sb.append("Request: ");
		sb.append(requestMapping);
		
		log.info(sb.toString());
	}
	
	/**
	 * Do log request.
	 *
	 * @param signature the signature
	 * @param requestMapping the request mapping
	 */
	protected void doLogRequest(MethodSignature signature, WadlRequestMapping requestMapping){
		StringBuffer sb = new StringBuffer();
		sb.append("Request: ");
		sb.append(requestMapping);
		
		log.info(sb.toString());
	}
}
