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
package org.lexgrid.loader.data.association;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The Class RandomUuidKeyResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RandomUuidKeyResolverTest {

	/** The resolver. */
	private RandomUuidKeyResolver resolver;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		resolver = new RandomUuidKeyResolver();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(resolver);
	}
	
	/**
	 * Test resolve multi attributes key.
	 */
	@Test
	public void testResolveMultiAttributesKey() {
		String key = resolver.resolveMultiAttributesKey(null);
		assertTrue(StringUtils.isNotBlank(key));
	}
	
	/**
	 * Test resolve multi attributes key not null value.
	 */
	@Test
	public void testResolveMultiAttributesKeyNotNullValue() {
		String key = resolver.resolveMultiAttributesKey(new String());
		assertTrue(StringUtils.isNotBlank(key));
	}
	
	/**
	 * Test get random uuid key.
	 */
	@Test
	public void testGetRandomUuidKey() {
		String key = RandomUuidKeyResolver.getRandomUuidKey();
		assertTrue(StringUtils.isNotBlank(key));
	}
	
	/**
	 * Test resolve multi attributes key randomness.
	 */
	@Test
	public void testResolveMultiAttributesKeyRandomness() {
		Set<String> keySet = new HashSet<String>();
		
		int numberOfTries = 100;
		
		for(int i=0;i<numberOfTries;i++) {
			assertTrue(keySet.add(resolver.resolveMultiAttributesKey(null)));
		}
		
		assertTrue(keySet.size() == numberOfTries);
	}
	
	/**
	 * Test get random uuid key randomness.
	 */
	@Test
	public void testGetRandomUuidKeyRandomness() {
		Set<String> keySet = new HashSet<String>();
		
		int numberOfTries = 100;
		
		for(int i=0;i<numberOfTries;i++) {
			assertTrue(keySet.add(RandomUuidKeyResolver.getRandomUuidKey()));
		}
		
		assertTrue(keySet.size() == numberOfTries);
	}
}
