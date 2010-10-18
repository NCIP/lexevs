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
package edu.mayo.informatics.lexgrid.convert.directConversions.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;

public class ConceptTest {
	public Concept concept1;
	public Concept concept1_1;
	public Concept concept2;
	
	public Concept conceptA;
	public Concept conceptB;
	
	@Before
	public void setUp() throws Exception {
		concept1 = new Concept("1", "name", "description", 0);
		concept1_1 = new Concept("1", "name112", "description1212", 1);
		concept2 = new Concept("2", "name", "description", 0);
	}

	@After
	public void tearDown() throws Exception {
		concept1 = null;
		concept1_1 = null;
		concept2 = null;
		conceptA = null;
		conceptB = null;
	}

	@Test
	public final void testConceptStringStringCodingScheme() {
		CodingScheme csA = new CodingScheme();
		CodingScheme csB = new CodingScheme();
	
		conceptA = new Concept("	Purple	The color purple", "\t", csA);
		conceptB = new Concept("1	Color	Holder of colors", "\t", csB);
		
		assertEquals("conceptA code: ", null, conceptA.code);
		assertEquals("conceptA name: ", "Purple", conceptA.name);
		assertEquals("conceptA desc: ", "The color purple", conceptA.description);
		assertEquals("conceptA depth: ", 1, conceptA.depth);
		assertEquals("indentity type a: ", false, csA.isTypeB);
		
		assertEquals("conceptB code: ", "1", conceptB.code);
		assertEquals("conceptB name: ", "Color", conceptB.name);
		assertEquals("conceptB desc: ", "Holder of colors", conceptB.description);
		assertEquals("conceptB depth: ", 0, conceptB.depth);
		assertEquals("indentity type B: ", true, csB.isTypeB);
	}

	@Test
	public void testEqualsObject() {
		assertTrue("test equals true: ", concept1.equals(concept1_1));
		assertFalse("test equals false: ", concept1.equals(concept2));
	}

}