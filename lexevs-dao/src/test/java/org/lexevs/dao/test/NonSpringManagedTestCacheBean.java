
package org.lexevs.dao.test;

import org.lexevs.cache.annotation.Cacheable;

/**
 * The Class TestCacheBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "testCache")
public class NonSpringManagedTestCacheBean extends TestCacheBean {
	
}