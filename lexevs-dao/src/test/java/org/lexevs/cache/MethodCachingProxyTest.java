
package org.lexevs.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.dao.test.TestCacheBean;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * The Class MethodCachingProxyTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MethodCachingProxyTest extends LexEvsDbUnitTestBase {

	/** The test cache proxy. */
	@Resource 
	private MethodCachingProxy testCacheProxy;
	
	/** The test cache bean. */
	@Resource 
	private TestCacheBean testCacheBean;
	
	/**
	 * Test cache setup.
	 */
	@Test
	public void testCacheSetup(){
		assertNotNull(this.testCacheProxy);
		assertNotNull(this.testCacheBean);
	}
	
	/**
	 * Clear cache.
	 */
	@Before
	public void clearCache(){
		testCacheProxy.clearAll();
	}
	
	@Test
	public void testComplexObject(){
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN("uri");
		ref.setCodingSchemeVersion("version");
		
		testCacheBean.getValueOfCompositeObject(ref);
		
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());
		
		AbsoluteCodingSchemeVersionReference ref2 = new AbsoluteCodingSchemeVersionReference();
		ref2.setCodingSchemeURN("uri");
		ref2.setCodingSchemeVersion("version");
		
		testCacheBean.getValueOfCompositeObject(ref2);
		
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());	
	}
	
	/**
	 * Test put in cache.
	 */
	@Test
	public void testPutInCache(){
	
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
	}
	
	/**
	 * Test put twice in cache.
	 */
	@Test
	public void testPutTwiceInCache(){
		
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals(1, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
	}
	
	/**
	 * Test two different in cache.
	 */
	@Test
	public void testTwoDifferentInCache(){
		
		assertEquals("onetwo", testCacheBean.getValue("one", "two"));
		assertEquals("threefour", testCacheBean.getValue("three", "four"));
		assertEquals(2, testCacheProxy.getCaches().get("testCache").size());
		assertEquals("onetwo", testCacheProxy.getCaches().get("testCache").values().toArray()[0]);
		assertEquals("threefour", testCacheProxy.getCaches().get("testCache").values().toArray()[1]);
	}
	
	
	
	/**
	 * Test clear cache.
	 */
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
	
	/**
	 * Test clear cache.
	 */
	@Test
	public void testClearCacheWithNestedCache(){
		
		testCacheBean.testClearWithNestedCache();
		assertEquals(0, testCacheProxy.getCaches().get("testCache").size());
		
	}
	
	@Test
	public void testCloneResult(){
		CodingScheme cs = testCacheBean.getClonedResult("uri", "version");
		cs.setDefaultLanguage("test lang");
		
		CodingScheme cachedCs = testCacheBean.getClonedResult("uri", "version");
		
		assertNull(cachedCs.getDefaultLanguage());
	}
	
	@Test
	public void testCacheThreadSafety() throws InterruptedException{
		List<TestCacheThread> threads = new ArrayList<TestCacheThread>();
		for(int i=0;i<100;i++) {
			TestCacheThread thread = new TestCacheThread();
			threads.add(thread);
			thread.start();
		}
		
		Thread.sleep(1000);
		
		for(TestCacheThread thread : threads) {
			thread.run = false;
		}
	}
	
	private class TestCacheThread extends Thread {

		private boolean run = true;
		@Override
		public void run() {
			while(run) {
				String value1 = testCacheBean.getValue("1", "2");
				assertEquals("12", value1);
				
				String uid1 = UUID.randomUUID().toString();
				String uid2 = UUID.randomUUID().toString();
				
				String value2 = testCacheBean.getValue(uid1, uid2);
				assertEquals(uid1+uid2, value2);
				testCacheBean.testClear();
			}
		}
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args){
		TestCacheBean bean = new TestCacheBean();
		Class clazz = bean.getClass();
		Cacheable annotation = AnnotationUtils.findAnnotation(bean.getClass(), Cacheable.class);
		System.out.println(annotation);
	}
	

}