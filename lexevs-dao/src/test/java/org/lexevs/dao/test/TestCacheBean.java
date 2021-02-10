
package org.lexevs.dao.test;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.cache.CacheWrappingFactory;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.cache.annotation.ParameterKey;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class TestCacheBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "testCache")
public class TestCacheBean {
	
	@Resource
	private CacheWrappingFactory cacheWrappingFactory;
	
	
	/**
	 * Gets the value.
	 * 
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * 
	 * @return the value
	 */
	@CacheMethod
	public String getValue(
			String arg1, 
			String arg2){
		return arg1 + arg2;
	}
	
	@CacheMethod
	public String getValueOfCompositeObject(
			@ParameterKey(field = { "_codingSchemeURN" , "_codingSchemeVersion"}) 
			AbsoluteCodingSchemeVersionReference ref){
		return ref.getCodingSchemeURN() + ref.getCodingSchemeVersion();
	}
	
	@CacheMethod(cloneResult=true)
	public CodingScheme getClonedResult(String uri, String version){
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeURI(uri);
		cs.setRepresentsVersion(version);
		return cs;
	}
	
	/**
	 * Gets the value not cachable.
	 * 
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * 
	 * @return the value not cachable
	 */
	public String getValueNotCachable(String arg1, String arg2){
		return arg1 + arg2;
	}
	
	/**
	 * Test clear.
	 */
	@ClearCache
	public void testClear(){}
	
	/**
	 * Test clear.
	 */
	@ClearCache
	public void testClearWithNestedCache(){
		if(this.cacheWrappingFactory == null){
			this.cacheWrappingFactory = LexEvsServiceLocator.getInstance().getCacheWrappingFactory();
		}
		TestNestedCacheBean bean = cacheWrappingFactory.wrapForCaching(new TestNestedCacheBean());
		bean.getNestedValue("1", "2");
	}
	
	@Cacheable(cacheName = "testCache")
	public static class TestNestedCacheBean {
		
		@CacheMethod
		public String getNestedValue(
				String arg1, 
				String arg2){
			return arg1 + arg2;
		}
	}
}