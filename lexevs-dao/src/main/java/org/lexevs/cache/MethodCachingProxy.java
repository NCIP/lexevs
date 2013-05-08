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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The Class MethodCachingProxy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
public class MethodCachingProxy extends AbstractMethodCachingBean<ProceedingJoinPoint> {
	
	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
	"( @annotation(org.lexevs.cache.annotation.CacheMethod) || @annotation(org.lexevs.cache.annotation.ClearCache) )")
	public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable {
		return super.doCacheMethod(pjp);
	}

	@Override
	protected Method getMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature sig = (MethodSignature)joinPoint.getSignature();
		
		return sig.getMethod();
	}

	@Override
	protected Object[] getArguments(ProceedingJoinPoint joinPoint) {
		return joinPoint.getArgs();
	}

	@Override
	protected Object getTarget(ProceedingJoinPoint joinPoint) {
		return joinPoint.getTarget();
	}

	@Override
	protected Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
	}
}