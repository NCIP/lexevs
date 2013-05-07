/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.prefix;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.registry.service.Registry;

/**
 * The Class CyclingCharDbPrefixGeneratorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CyclingCharDbPrefixGeneratorTest {

	/**
	 * Test adjust length chop.
	 */
	@Resource(name="databaseRegistry")
	private Registry registry;
	
	@Test
	public void testAdjustLengthChop(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		char[] chars = generator.adjustLength("aasdffdsaer".toCharArray());

		assertEquals(3, chars.length);
		assertArrayEquals("aas".toCharArray(), chars);
	}
	
	/**
	 * Test is in cycle one char true.
	 */
	@Test
	public void testIsInCycleOneCharTrue(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(1);
		
		assertTrue(generator.isInCycle(new char[]{'Z'}));
	}
	
	/**
	 * Test is in cycle one char false.
	 */
	@Test
	public void testIsInCycleOneCharFalse(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		assertFalse(generator.isInCycle(new char[]{'E'}));
	}
	
	/**
	 * Test is in cycle multiple chars false.
	 */
	@Test
	public void testIsInCycleMultipleCharsFalse(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(10);
		
		assertFalse(generator.isInCycle("EGHHYTVVHH".toCharArray()));
	}
	
	/**
	 * Test generate starting cycling prefix.
	 */
	@Test
	public void testGenerateStartingCyclingPrefix(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		char[] chars = generator.generateStartingCyclingPrefix();

		assertEquals(3, chars.length);
		assertArrayEquals("AAZ".toCharArray(), chars);
	}
	
	/**
	 * Test adjust length add.
	 */
	@Test
	public void testAdjustLengthAdd(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(5);
		
		char[] chars = generator.adjustLength("ff".toCharArray());

		assertEquals(5, chars.length);
		assertArrayEquals("ffAAA".toCharArray(), chars);
	}

	/**
	 * Test too small prefix.
	 * 
	 * @throws Exception the exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testTooSmallPrefix() throws Exception {

		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		
		generator.setPrefixLengthLimit(1);
		
		generator.afterPropertiesSet();
	}
}