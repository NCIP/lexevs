package org.lexevs.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

public class CacheRegistry implements InitializingBean{
	
	/** The caches. */
	private Map<String,Map<String,Object>> caches;
	
	public void afterPropertiesSet() throws Exception {
		initializeCache();
	}
	
	protected void initializeCache() {
		caches = Collections.synchronizedMap(new HashMap<String,Map<String,Object>>());
	}

	public void clearAll() {
		this.initializeCache();
	}
	
	/**
	 * Gets the caches.
	 * 
	 * @return the caches
	 */
	public Map<String, Map<String, Object>> getCaches() {
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
