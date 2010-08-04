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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.cache.annotation.ParameterKey;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;


/**
 * The Class MethodCachingProxy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMethodCachingBean<T> {

	/** The logger. */
	private LgLoggerIF logger;

	private SystemVariables systemVariables;
	
	private static String NULL_VALUE = "null";

	private CacheRegistry cacheRegistry;
	
	/**
	 * Clear cache.
	 * 
	 * @param pjp the pjp
	 * 
	 * @return the object
	 * 
	 * @throws Throwable the throwable
	 */
	protected Object clearCache(T joinPoint, Method method) throws Throwable {
		logger.debug("Clearing cache.");
		
		Object target = this.getTarget(joinPoint);
		
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(target.getClass(), Cacheable.class);
		
		ClearCache clearCacheAnnotation = method.getAnnotation(ClearCache.class);
		
		Object returnObj = this.proceed(joinPoint);
		
		if(clearCacheAnnotation.clearAll()) {
			this.cacheRegistry.clearAll();
		} else {
			Map<String,Object> cache = this.getCacheFromName(cacheableAnnotation.cacheName(), cacheableAnnotation.cacheSize());
	
			cache.clear();
		}
		
		return returnObj;
	}
	
	public void clearAll() {
		cacheRegistry.clearAll();
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
	protected synchronized Object doCacheMethod(T joinPoint) throws Throwable {
		
		Method method = this.getMethod(joinPoint);
		
		if(method.isAnnotationPresent(ClearCache.class)) {
			return this.clearCache(joinPoint, method);
		}

		Object target = this.getTarget(joinPoint);
		
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		
		String key = this.getKeyFromMethod(target.getClass().getName(),
					method.getName(),
					this.getArguments(joinPoint), 
					parameterAnnotations);
		
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(target.getClass(), Cacheable.class);
		CacheMethod cacheMethodAnnotation = AnnotationUtils.findAnnotation(method, CacheMethod.class);
	
		Map<String,Object> cache = this.getCacheFromName(
				cacheableAnnotation.cacheName(),
				cacheableAnnotation.cacheSize());
		
		if(cache.containsKey(key)){
			logger.debug("Cache hit on: " + key);
			Object obj = cache.get(key);
			return obj;
		} else {
			logger.debug("Caching miss on: " + key);
		}

		Object result = this.proceed(joinPoint);
		cache.put(key, result);

		if(result != null && 
				cacheMethodAnnotation.cloneResult() && 
				ClassUtils.isAssignable(result.getClass(), Serializable.class)) {
			return DaoUtility.deepClone((Serializable)result);
		} else {
			return result;
		}
	}
	
	protected abstract Method getMethod(T joinPoint);

	protected abstract Object getTarget(T joinPoint);

	protected abstract Object proceed(T joinPoint) throws Throwable;

	protected abstract Object[] getArguments(T joinPoint);

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
		if(!cacheRegistry.getCaches().containsKey(name)){
			cacheRegistry.getCaches().put(name, Collections.synchronizedMap(new LRUMap(cacheSize)));
		}
		return cacheRegistry.getCaches().get(name);
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
	
	protected Map<String, Map<String, Object>> getCaches(){
		return this.cacheRegistry.getCaches();
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

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public void setCacheRegistry(CacheRegistry cacheRegistry) {
		this.cacheRegistry = cacheRegistry;
	}

	public CacheRegistry getCacheRegistry() {
		return cacheRegistry;
	}
}
