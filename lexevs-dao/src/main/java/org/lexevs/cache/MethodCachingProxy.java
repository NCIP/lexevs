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
package org.lexevs.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.collections.map.LRUMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ParameterKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;


/**
 * The Class MethodCachingProxy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
public class MethodCachingProxy implements InitializingBean {
	
	/** The cache size. */
	private int cacheSize = 50;
	
	/** The logger. */
	private LgLoggerIF logger;
	
	/** The caches. */
	private Map<String,Map<String,Object>> caches;
	
	private static String NULL_VALUE = "null";
	

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		initializeCache();
	}
	
	protected void initializeCache() {
		caches = Collections.synchronizedMap(new HashMap<String,Map<String,Object>>());
	}

	/**
	 * Clear cache.
	 * 
	 * @param pjp the pjp
	 * 
	 * @return the object
	 * 
	 * @throws Throwable the throwable
	 */
	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
			"@annotation(org.lexevs.cache.annotation.ClearCache)")
	public Object clearCache(ProceedingJoinPoint pjp) throws Throwable {
		logger.debug("Clearing cache.");
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(pjp.getThis().getClass(), Cacheable.class);
		Map<String,Object> cache = this.getCacheFromName(cacheableAnnotation.cacheName(), cacheableAnnotation.cacheSize());
		cache.clear();
		return pjp.proceed();
	}
	
	public void clearAll() {
		initializeCache();
	}
	
	/**
	 * Cache method.
	 * 
	 * @param pjp the pjp
	 * 
	 * @return the object
	 * 
	 * @throws Throwable the throwable
	 */
	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
			"@annotation(org.lexevs.cache.annotation.CacheMethod)")
	public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sig = (MethodSignature)pjp.getSignature();

		Annotation[][] parameterAnnotations = sig.getMethod().getParameterAnnotations();
		
		String key = this.getKeyFromMethod(pjp.getThis().getClass().getName(),
					pjp.getSignature().getName(),
					pjp.getArgs(), 
					parameterAnnotations);
		
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(pjp.getTarget().getClass(), Cacheable.class);
	
		Map<String,Object> cache = this.getCacheFromName(
				cacheableAnnotation.cacheName(),
				cacheableAnnotation.cacheSize());
		
		if(cache.containsKey(key)){
			logger.debug("Cache hit on: " + key);
			return cache.get(key);
		} else {
			logger.debug("Caching miss on: " + key);
		}

		Object result = pjp.proceed();
		cache.put(key, result);
		return result;
	}
	
	/**
	 * Gets the cache from name.
	 * 
	 * @param name the name
	 * @param cacheSize the cache size
	 * 
	 * @return the cache from name
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCacheFromName(String name, int cacheSize){
		if(!caches.containsKey(name)){
			caches.put(name, Collections.synchronizedMap(new LRUMap(cacheSize)));
		}
		return caches.get(name);
	}

	/**
	 * Gets the key from method.
	 * 
	 * @param className the class name
	 * @param signature the signature
	 * @param arguments the arguments
	 * @param parameterAnnotations 
	 * 
	 * @return the key from method
	 */
	protected String getKeyFromMethod(
			String className, 
			String signature, 
			Object[] arguments, 
			Annotation[][] parameterAnnotations){
		StringBuffer sb = new StringBuffer();
		sb.append(className);
		sb.append(signature);
		for(int i=0;i<arguments.length;i++){
			Object arg = arguments[i];
			
			Annotation[] annotations = parameterAnnotations[i];
			
			ParameterKey parameterKey = getParameterKeyAnnotation(annotations);
			
			sb.append(getArgumentKey(arg, parameterKey));
		}
		return sb.toString();
	}
	
	private ParameterKey getParameterKeyAnnotation(Annotation[] annotations) {
		List<ParameterKey> returnList = new ArrayList<ParameterKey>();
		
		for(Annotation annotation : annotations) {
			if(annotation instanceof ParameterKey) {
				returnList.add((ParameterKey)annotation);
			}
		}
		if(returnList.size() > 1) {
			throw new RuntimeException("Only one ParameterKey annotation allowed per Parameter.");
		}
		
		if(returnList.size() == 1) {
			return returnList.get(0);
		} else {
			return null;
		}
	}
	
	protected String getArgumentKey(Object argument, ParameterKey key) {
		if(key != null) {
			StringBuffer sb = new StringBuffer();
			String[] fields = key.field();
			for(String fieldName : fields) {
				Field field = 
					ReflectionUtils.findField(argument.getClass(), fieldName);
				field.setAccessible(true);
				
				Object fieldArg = ReflectionUtils.getField(field, argument);
				sb.append(getArgumentKey(fieldArg));
			}
			return sb.toString();
		} else {
			return getArgumentKey(argument);
		}
	}
	
	protected String getArgumentKey(Object argument) {
		StringBuffer sb = new StringBuffer();
		if(argument == null) {
			sb.append(NULL_VALUE);
		} else {
			sb.append(argument.hashCode());
		}
		return sb.toString();
	}

	/**
	 * Sets the cache size.
	 * 
	 * @param cacheSize the new cache size
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	/**
	 * Gets the cache size.
	 * 
	 * @return the cache size
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}

	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	/**
	 * Gets the caches.
	 * 
	 * @return the caches
	 */
	protected Map<String, Map<String, Object>> getCaches() {
		return caches;
	}

	/**
	 * Sets the caches.
	 * 
	 * @param caches the caches
	 */
	protected void setCaches(Map<String, Map<String, Object>> caches) {
		this.caches = caches;
	}

}
