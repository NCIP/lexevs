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
package org.lexgrid.loader.processor;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.sql.DatabaseMetaData;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class HighestRankingListProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HighestRankingListProcessorTest {

	/**
	 * Test get highest rank.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetHighestRank() throws Exception {
		HighestRankingListProcessor processor = new HighestRankingListProcessor();
		
		TestSortingListProcessor p = new TestSortingListProcessor();
		p.setDelegate(new ItemProcessor(){

			public Object process(Object item) throws Exception {	
				return item;
			}			
		});
		
		processor.setSortingListProcessor(p);		
		
		String val = (String)processor.process(Arrays.asList(new String[]{"one", "two"}));
	
		assertTrue(val.equals("one"));
	}
	
	/**
	 * The Class TestSortingListProcessor.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TestSortingListProcessor extends SortingListProcessor{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */
		public Object process(Object item) throws Exception {
			return "one";
		}	
	}
}