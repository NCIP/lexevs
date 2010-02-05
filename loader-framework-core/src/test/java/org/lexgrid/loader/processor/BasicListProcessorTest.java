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
package org.lexgrid.loader.processor;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

import static org.junit.Assert.*;

/**
 * The Class BasicListProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BasicListProcessorTest {

	/**
	 * Test process.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcess() throws Exception{
		BasicListProcessor<String, Integer> blp = new BasicListProcessor<String, Integer>();
		
		
		blp.setDelegate(new ItemProcessor<String, Integer>(){
			
			public Integer process(String item){
				return Integer.valueOf(item);
				
			}
		
		});
		
		List<String> testList = Arrays.asList("1", "2", "3");
		
		List<Integer> processedList = blp.process(testList);
		
		assertTrue(processedList.size() == 3);
		assertTrue(processedList.get(0) == 1);
		assertTrue(processedList.get(1) == 2);
		assertTrue(processedList.get(2) == 3);
		
		
	}
}
