package org.lexevs.dao.test;

import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;

@Cacheable(cacheName = "testCache")
public class TestCacheBean {
	
	@CacheMethod
	public String getValue(String arg1, String arg2){
		return arg1 + arg2;
	}
	
	public String getValueNotCachable(String arg1, String arg2){
		return arg1 + arg2;
	}
	
	@ClearCache
	public void testClear(){}
}
