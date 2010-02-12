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
package org.lexgrid.loader.rrf.data.association;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelRuiKeyResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelRuiKeyResolverTest {

	/** The resolver. */
	private MrrelRuiAssociationInstanceIdResolver resolver;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		resolver = new MrrelRuiAssociationInstanceIdResolver();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(resolver);
	}
	
	/**
	 * Test resolve.
	 */
	@Test
	public void testResolve() {
		Mrrel mrrel = new Mrrel();
		mrrel.setRui("testRui");
		
		assertTrue(resolver.resolveAssociationInstanceId(mrrel).equals("testRui"));
	}
}
