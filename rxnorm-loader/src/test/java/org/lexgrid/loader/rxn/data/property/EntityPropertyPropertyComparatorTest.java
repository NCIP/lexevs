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
package org.lexgrid.loader.rxn.data.property;

import static org.junit.Assert.assertTrue;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.junit.Test;
import org.lexgrid.loader.rrf.data.property.MrrankUtility;
import org.lexgrid.loader.rrf.model.Mrrank;
import org.lexgrid.loader.rxn.data.property.EntityPropertyPropertyComparator;

/**
 * The Class EntityPropertyPropertyComparatorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyPropertyComparatorTest {

	/**
	 * Test compare by same language.
	 */
	@Test
	public void testCompareBySameLanguage(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();

		prop1.setLanguage("ENG");
		
		prop1.setPropertyId("id1");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
        Text text1 = new Text();
        text1.setContent("test");
		prop1.setValue(text1);
		prop1.setIsPreferred(false);
		
		Presentation prop2 = new Presentation();

		prop2.setLanguage("ENG");
		prop2.setPropertyId("id2");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
        Text text2 = new Text();
        text2.setContent("test");
		prop2.setValue(text2);
		prop2.setIsPreferred(false);
		
		
		
		assertTrue(comparator.compare(prop1, prop2) == 0);
	}
	
	/**
	 * Test compare by different language.
	 */
	@Test
	public void testCompareByDifferentLanguage(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();
		prop1.setLanguage("ENG");
		prop1.setPropertyId("id3");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
        Text text1 = new Text();
        text1.setContent("test");
		prop1.setValue(text1);
		prop1.setIsPreferred(false);
		
		Presentation prop2 = new Presentation();

		prop2.setLanguage("FR");
		prop2.setPropertyId("id3");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
        Text text3 = new Text();
        text3.setContent("test");
		prop2.setValue(text3);
		prop2.setIsPreferred(false);
		
	
		assertTrue(comparator.compare(prop1, prop2) == 1);
	}
	
	/**
	 * Test compare by rank.
	 */
	@Test
	public void testCompareByRank(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();

		prop1.setLanguage("ENG");
		prop1.setPropertyId("id4");
		prop1.setRepresentationalForm("SSN");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
        Text text4 = new Text();
        text4.setContent("test");
		prop1.setValue(text4);
		prop1.setIsPreferred(false);
		
		Presentation prop2 = new Presentation();

		prop2.setLanguage("ENG");
		prop2.setPropertyId("id5");
		prop2.setRepresentationalForm("VSY");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
        Text text5 = new Text();
        text5.setContent("test");
		prop2.setValue(text5);
		prop2.setIsPreferred(false);
			
		comparator.setMrrankUtility(new MrrankTestUtility());
//		comparator.setSab("SRC");
	
		assertTrue(comparator.compare(prop1, prop2) == 1);
	}
	
	/**
	 * The Class MrrankTestUtility.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class MrrankTestUtility implements MrrankUtility {

		/* (non-Javadoc)
		 * @see org.lexgrid.loader.rrf.data.property.MrrankUtility#getRank(java.lang.String, java.lang.String)
		 */
		public int getRank(String sab, String tty) {
			if(sab.equals("SRC") && tty.equals("SSN")){
				return 1;
			} else if(sab.equals("SRC") && tty.equals("VSY")){
				return 2;
			}
			throw new RuntimeException("Test MRRANK utility cannot process this sab and tty.");
		}

		public Mrrank getMrrank(String sab, String tty) {
			return null;
		}
	}
	

}
