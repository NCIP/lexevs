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
package org.lexevs.dao.test;

import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;

/**
 * The Class TestCacheBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "testCache")
public class TestCacheBean {
	
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
}
