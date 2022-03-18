
package org.lexgrid.loader.rrf.staging.cache.interceptor;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class MrconsoStagingCacheInterceptor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoStagingCacheInterceptor implements MethodInterceptor {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(MrconsoStagingCacheInterceptor.class);

	 /** The cache. */
 	private Cache cache;

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Exception {
		 String callingClassName = invocation.getThis().getClass().getName();
		 String methodName  = invocation.getMethod().getName();
		
		 String cacheKey = getCacheKey(callingClassName, methodName, invocation.getArguments());

		 Element element = cache.get(cacheKey);
		 if (element == null) {
			 log.debug("calling intercepted method");
			 Object result = null;
			try {
				result = invocation.proceed();
			} catch (Throwable e) {
				throw new Exception(e);
			}

			 //cache method result
			 log.debug("caching result");
			 element = new Element(cacheKey, (Serializable) result);
			 cache.put(element);
		 } else {
			 log.debug("cache hit");
		 }
		 
		 return element.getValue();
	 }

	/**
	 * Gets the cache key.
	 * 
	 * @param callingClassName the calling class name
	 * @param methodName the method name
	 * @param args the args
	 * 
	 * @return the cache key
	 */
	protected String getCacheKey(String callingClassName, String methodName, Object[] args){
		 StringBuffer sb = new StringBuffer();
		 sb.append(callingClassName);
		 sb.append(".");
		 sb.append(methodName);
		for(Object obj : args){
			sb.append(".")
	          .append(obj);
		}
		return sb.toString();
	}

	/**
	 * Gets the cache.
	 * 
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}


	/**
	 * Sets the cache.
	 * 
	 * @param cache the new cache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
}