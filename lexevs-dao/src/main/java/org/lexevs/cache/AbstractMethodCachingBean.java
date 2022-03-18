
package org.lexevs.cache;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.cache.CacheRegistry.CacheWrapper;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.cache.annotation.ParameterKey;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/**
 * The Class MethodCachingProxy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMethodCachingBean<T> {

	private static final boolean DEFAULT_ISOLATE_CACHES_ON_CLEAR = false;

	/** The logger. */
	private LgLoggerIF logger;

	private SystemVariables systemVariables;
	
	private static String NULL_VALUE_KEY = "null";
	
	private static String NULL_VALUE_CACHE_PLACEHOLDER = "NULL_VALUE_CACHE_PLACEHOLDER";

	private CacheRegistry cacheRegistry;
	
	private boolean isolateCachesOnClear = DEFAULT_ISOLATE_CACHES_ON_CLEAR;

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
		try {
			logger.debug("Clearing cache.");

			if(isolateCachesOnClear){
				this.cacheRegistry.setInThreadCacheClearingState(true);
			}

			Object target = this.getTarget(joinPoint);

			Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(target.getClass(), Cacheable.class);

			ClearCache clearCacheAnnotation = method.getAnnotation(ClearCache.class);

			Object returnObj = this.proceed(joinPoint);
			
			clearCache(cacheableAnnotation, clearCacheAnnotation);

			return returnObj;
		} finally {
			if(isolateCachesOnClear){
				this.cacheRegistry.setInThreadCacheClearingState(false);
			}
		}
	}

	private void clearCache(
			Cacheable cacheableAnnotation,
			ClearCache clearCacheAnnotation) {
		if(clearCacheAnnotation.clearAll()) {
			this.cacheRegistry.clearAll();
		} else {

			for(String cacheName : clearCacheAnnotation.clearCaches()){
				CacheWrapper<String,Object> cache = this.getCacheFromName(cacheName, false);
				cache.clear();
			}
			
			CacheWrapper<String,Object> cache = this.getCacheFromName(cacheableAnnotation.cacheName(), false);
	
			cache.clear();
		}
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
	protected Object doCacheMethod(T joinPoint) throws Throwable {
		
		if(!CacheSessionManager.getCachingStatus()) {
			return this.proceed(joinPoint);
		}

		Method method = this.getMethod(joinPoint);

		if(method.isAnnotationPresent(CacheMethod.class)
				&&
					method.isAnnotationPresent(ClearCache.class)){
			throw new RuntimeException("Cannot both Cache method results and clear the Cache in " +
					"the same method. Please only use @CacheMethod OR @ClearCache -- not both. " +
					" This occured on method: " + method.toString());
		}
		
		Object target = this.getTarget(joinPoint);
		
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		
		String key = this.getKeyFromMethod(target.getClass().getName(),
					method.getName(),
					this.getArguments(joinPoint), 
					parameterAnnotations);
		
		Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(target.getClass(), Cacheable.class);
		CacheMethod cacheMethodAnnotation = AnnotationUtils.findAnnotation(method, CacheMethod.class);
	
		CacheWrapper<String,Object> cache = this.getCacheFromName(
				cacheableAnnotation.cacheName(), true);

		if(method.isAnnotationPresent(ClearCache.class)) {
			return this.clearCache(joinPoint, method);
		}

		Object value = cache.get(key);
		if(value != null) {
			this.logger.debug("Cache hit on: " + key);
			if(value.equals(NULL_VALUE_CACHE_PLACEHOLDER)) {
				return null;
			} else {
				return returnResult(value, cacheMethodAnnotation);
			}
		} else {
			this.logger.debug("Caching miss on: " + key);
		}

		Object result = this.proceed(joinPoint);

		if(this.isolateCachesOnClear == false || 
				(this.isolateCachesOnClear == true &&
						(this.cacheRegistry.getInThreadCacheClearingState() == null 
								|| 
								this.cacheRegistry.getInThreadCacheClearingState() == false))){
			this.logger.debug("Thread is not in @Clear state, caching can continue for key: " + key);

			if(result != null) {
				cache.put(key, result);
			} else {
				cache.put(key, NULL_VALUE_CACHE_PLACEHOLDER);
			}
		} else {
			this.logger.debug("Thread is in @Clear state, caching skipped for key: " + key);
		}

		return returnResult(result, cacheMethodAnnotation);
	}
	
	private Object returnResult(Object result, CacheMethod cacheMethodAnnotation) {
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
	
	public CacheWrapper<String,Object> getCacheFromName(String cacheName, boolean createIfNotPresent){
		return this.cacheRegistry.getCache(cacheName, createIfNotPresent);
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
			sb.append(NULL_VALUE_KEY);
		} else {
			sb.append(argument.hashCode());
		}
		return sb.toString();
	}
	
	protected Map<String, CacheWrapper<String, Object>> getCaches(){
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