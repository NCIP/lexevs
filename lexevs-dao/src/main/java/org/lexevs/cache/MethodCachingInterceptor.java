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
package org.lexevs.cache;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodCachingInterceptor extends AbstractMethodCachingBean<MethodInvocation> implements MethodInterceptor{

	@Override
	protected Object[] getArguments(MethodInvocation joinPoint) {
		return joinPoint.getArguments();
	}

	@Override
	protected Method getMethod(MethodInvocation joinPoint) {
		return joinPoint.getMethod();
	}

	@Override
	protected Object getTarget(MethodInvocation joinPoint) {
		return joinPoint.getThis();
	}

	@Override
	protected Object proceed(MethodInvocation joinPoint) throws Throwable {
		return joinPoint.proceed();
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		return this.doCacheMethod(methodInvocation);
	}
}