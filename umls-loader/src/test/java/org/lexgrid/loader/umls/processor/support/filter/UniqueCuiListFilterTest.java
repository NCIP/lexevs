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
package org.lexgrid.loader.umls.processor.support.filter;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UniqueCuiListFilterTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UniqueCuiListFilterTest {

	/**
	 * Test filter by cui.
	 */
	@Test
	public void testFilterByCui(){
		UniqueCuiListFilter filter = new UniqueCuiListFilter();	
		
		Mrconso mrconso1 = new Mrconso();
		mrconso1.setCui("1");
		
		Mrconso mrconso2 = new Mrconso();
		mrconso2.setCui("2");
		
		Mrconso mrconso3 = new Mrconso();
		mrconso3.setCui("1");
		
		List<Mrconso> filteredList = filter.filter(Arrays.asList(new Mrconso[]{mrconso1, mrconso2, mrconso3}));
		
		Assert.assertTrue(filteredList.size() == 2);
			
		boolean foundOne = false;
		boolean foundTwo = false;
		for(Mrconso item : filteredList){
			if(item.getCui().equals("1")){
				foundOne = true;
			}
			if(item.getCui().equals("2")){
				foundTwo = true;
			}
		}
		
		Assert.assertTrue(foundOne);
		
		Assert.assertTrue(foundTwo);		
	}
}