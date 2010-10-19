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
package org.lexgrid.loader.meta.processor.support;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrrel;

import static org.junit.Assert.*;

/**
 * The Class SelfReferencingAssocQualResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SelfReferencingAssocQualResolverTest {

	/** The resolver. */
	private SelfReferencingAssocQualResolver resolver;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		resolver = new SelfReferencingAssocQualResolver();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(resolver);
	}
	
	/**
	 * Test process.
	 */
	@Test
	public void testProcess() {
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1("cui");
		mrrel.setCui2("cui");
		
		assertTrue(resolver.toProcess(mrrel));
	}
	
	/**
	 * Test dont process.
	 */
	@Test
	public void testDontProcess() {
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1("cui1");
		mrrel.setCui2("cui2");
		
		assertFalse(resolver.toProcess(mrrel));
	}	
}