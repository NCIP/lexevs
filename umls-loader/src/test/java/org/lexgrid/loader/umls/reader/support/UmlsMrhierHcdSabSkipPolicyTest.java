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
package org.lexgrid.loader.umls.reader.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrhier;

/**
 * The Class UmlsMrhierHcdSabSkipPolicyTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrhierHcdSabSkipPolicyTest {

	/**
	 * Test skip.
	 */
	@Test
	public void testSkip(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("NOT-THE-SAB");
		
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test skip no hcd.
	 */
	@Test
	public void testSkipNoHcd(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("SAB");
		
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test skip hcd wrong sab.
	 */
	@Test
	public void testSkipHcdWrongSab(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("NOT-THE-SAB");
		mrhier.setHcd("SOME-HCD");
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test dont skip.
	 */
	@Test
	public void testDontSkip(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("SAB");
		mrhier.setHcd("SOME-HCD");
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertFalse(policy.toSkip(mrhier));
	}
}
