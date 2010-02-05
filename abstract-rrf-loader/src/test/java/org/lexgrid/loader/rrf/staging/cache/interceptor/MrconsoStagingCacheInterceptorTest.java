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
package org.lexgrid.loader.rrf.staging.cache.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * The Class MrconsoStagingCacheInterceptorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoStagingCacheInterceptorTest {

	/**
	 * Test caching.
	 * 
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCaching() throws Throwable {
		CacheManager cacheManager = new CacheManager();
		cacheManager.addCache("testCache");
		
		MrconsoStagingCacheInterceptor interceptor = new MrconsoStagingCacheInterceptor();
		interceptor.setCache(cacheManager.getCache("testCache"));
	
		assertTrue(interceptor.getCache().getStatistics().getCacheHits() == 0);
		
		MethodInvocation mi1 = createMockMethodInvocation();
		
		interceptor.invoke(mi1);
		
		assertTrue(interceptor.getCache().getStatistics().getCacheHits() == 0);
		
		MethodInvocation mi2 = createMockMethodInvocation();
		
		interceptor.invoke(mi2);
		
		assertTrue(interceptor.getCache().getStatistics().getCacheHits() == 1);

	}
	
	/**
	 * Test construct key.
	 */
	@Test
	public void testConstructKey(){
		MrconsoStagingCacheInterceptor interceptor = new MrconsoStagingCacheInterceptor();
		String key = interceptor.getCacheKey("TestClassName", "TestMethodName", new Object[]{"TestString", new Integer(1)});
	
		assertTrue(key.equals("TestClassName.TestMethodName.TestString.1"));
	}
	
	/**
	 * Creates the mock method invocation.
	 * 
	 * @return the method invocation
	 * 
	 * @throws Throwable the throwable
	 */
	private MethodInvocation createMockMethodInvocation() throws Throwable {
		MethodInvocation mi = createMock(MethodInvocation.class);
		expect(mi.getMethod()).andReturn(TestCachingClass.class.getMethod("returnString", String.class));
		expect(mi.getArguments()).andReturn(new Object[]{"test"});
		expect(mi.getThis()).andReturn(new TestCachingClass());
		expect(mi.proceed()).andReturn("test");
		
		replay(mi);
		
		return mi;
	}
	
	/**
	 * The Class TestCachingClass.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TestCachingClass {
		
		/**
		 * Return string.
		 * 
		 * @param string the string
		 * 
		 * @return the string
		 */
		public String returnString(String string){
			return string;
		}
	}
}
	


