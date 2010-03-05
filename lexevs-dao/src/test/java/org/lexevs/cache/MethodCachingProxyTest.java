package org.lexevs.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.dao.test.TestCacheBean;
import org.springframework.core.annotation.AnnotationUtils;

public class MethodCachingProxyTest extends LexEvsDbUnitTestBase {

	@Resource 
	private MethodCachingProxy testCacheProxy;
	
	@Resource 
	private TestCacheBean testCacheBean;
	
	@Test
	public void testCacheSetup(){
		assertNotNull(this.testCacheProxy);
		assertNotNull(this.testCacheBean);
	}
	
	@Before
	public void clearCache(){
		testCacheProxy.getCaches().clear();
	}
	
	@Test
	public void testPutInCache(){
	
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
	}
	
	@Test
	public void testPutTwiceInCache(){
		
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
	}
	
	@Test
	public void testTwoDifferentInCache(){
		
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals("threefour", testCacheBean.getValue("three", "four"));
		assertEquals(2, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
		assertEquals("threefour", testCacheProxy.getCaches().get("testCache").values().toArray()[1]);
	}
	
	@Test
	public void testUnCachableMethod(){
		
		assertEquals("onetwo", testCacheBean.getValueNotCachable("one", "two"));
		
		assertNull(testCacheProxy.getCaches().get("testCache"));
	}
	
	@Test
	public void testClearCache(){
		
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals("threefour", testCacheBean.getValue("three", "four"));
		assertEquals(2, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
		assertEquals("threefour", testCacheProxy.getCaches().get("testCache").values().toArray()[1]);
		
		testCacheBean.testClear();
		assertEquals(0, testCacheProxy.getCaches().get("testCache").size());
		
	}
	
	public static void main(String[] args){
		TestCacheBean bean = new TestCacheBean();
		Class clazz = bean.getClass();
		Cacheable annotation = AnnotationUtils.findAnnotation(bean.getClass(), Cacheable.class);
		System.out.println(annotation);
	}
	

}
