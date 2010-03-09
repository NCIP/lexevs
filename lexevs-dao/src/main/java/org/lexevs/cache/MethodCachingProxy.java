package org.lexevs.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.collections.map.LRUMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.logging.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;


@Aspect
public class MethodCachingProxy implements InitializingBean {
	
	private int cacheSize = 50;
	
	private LgLoggerIF logger;
	
	private Map<String,Map<String,Object>> caches;
	

	public void afterPropertiesSet() throws Exception {
		caches = Collections.synchronizedMap(new HashMap<String,Map<String,Object>>());
	}

	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
			"@annotation(org.lexevs.cache.annotation.ClearCache)")
	public Object clearCache(ProceedingJoinPoint pjp) throws Throwable {
		logger.debug("Clearing cache.");
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(pjp.getThis().getClass(), Cacheable.class);
		Map<String,Object> cache = this.getCacheFromName(cacheableAnnotation.cacheName(), cacheableAnnotation.cacheSize());
		cache.clear();
		return pjp.proceed();
	}
	
	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
			"@annotation(org.lexevs.cache.annotation.CacheMethod)")
	public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable {
		String key = this.getKeyFromMethod(pjp.getThis().getClass().getName(),
					pjp.getSignature().getName(),
					pjp.getArgs());
		
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(pjp.getThis().getClass(), Cacheable.class);
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
	
	public Map<String,Object> getCacheFromName(String name, int cacheSize){
		if(!caches.containsKey(name)){
			caches.put(name, Collections.synchronizedMap(new LRUMap(cacheSize)));
		}
		return caches.get(name);
	}

	protected String getKeyFromMethod(String className, String signature, Object[] arguments){
		StringBuffer sb = new StringBuffer();
		sb.append(className);
		sb.append(signature);
		for(Object arg : arguments){
			sb.append(arg.toString());
		}
		return sb.toString();
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	protected Map<String, Map<String, Object>> getCaches() {
		return caches;
	}

	protected void setCaches(Map<String, Map<String, Object>> caches) {
		this.caches = caches;
	}

}
