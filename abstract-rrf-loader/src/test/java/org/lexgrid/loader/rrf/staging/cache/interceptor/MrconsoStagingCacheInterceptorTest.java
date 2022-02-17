
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