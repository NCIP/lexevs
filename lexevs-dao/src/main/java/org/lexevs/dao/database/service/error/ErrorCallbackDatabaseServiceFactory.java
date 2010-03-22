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
package org.lexevs.dao.database.service.error;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

/**
 * A factory for creating ErrorCallbackDatabaseService objects.
 */
public class ErrorCallbackDatabaseServiceFactory {
	
	/** The pattern. */
	String pattern = ".*insert.*";

	/**
	 * Gets the error callback database service.
	 * 
	 * @param databaseService the database service
	 * @param callback the callback
	 * 
	 * @return the error callback database service
	 */
	@SuppressWarnings("unchecked")
	public <T> T getErrorCallbackDatabaseService(T databaseService, ErrorCallbackListener callback) {

		JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();

		pointcut.setPattern(pattern);

		Advisor advisor = new DefaultPointcutAdvisor(
				pointcut, new ErrorCallbackInterceptor(callback));

		ProxyFactory pf = new ProxyFactory(databaseService);
		pf.setProxyTargetClass(true);

		pf.addAdvisor(advisor);

		Object obj = pf.getProxy();
		return (T)obj;
	}

	/**
	 * Gets the pattern.
	 * 
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern.
	 * 
	 * @param pattern the new pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
