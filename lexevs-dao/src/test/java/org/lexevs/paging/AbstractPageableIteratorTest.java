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
package org.lexevs.paging;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class AbstractPageableIteratorTest {

	
	@Test
	public void testIterate() {
		Iterator<String> itr = getTestIterator(2);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testIterateWithBigPage() {
		Iterator<String> itr = getTestIterator(50);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testIterateWithSmallPage() {
		Iterator<String> itr = getTestIterator(1);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}

	private Iterator<String> getTestIterator(int pageSize){
		return new AbstractPageableIterator<String>(pageSize) {
			
			private String[] strings = new String[] {"1", "2", "3", 
					"4", "5", "6"};

			@Override
			protected List<String> doPage(int position, int pageSize) {
				List<String> returnList = new ArrayList<String>();
				
				for(int i=position;i<pageSize+position && i<strings.length;i++) {
					returnList.add(strings[i]);
				}
				
				return returnList;
			}
			
		};
	}
}