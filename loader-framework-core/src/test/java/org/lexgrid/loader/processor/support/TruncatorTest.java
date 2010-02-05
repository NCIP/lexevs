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
package org.lexgrid.loader.processor.support;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.persistence.model.CodingSchemeSupportedAttrib;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttribId;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class TruncatorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TruncatorTest {

	/** The truncator. */
	private BeanReflectionTruncator truncator;
	
	/** The coding scheme supported attrib. */
	private CodingSchemeSupportedAttrib codingSchemeSupportedAttrib;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp(){
		truncator = new BeanReflectionTruncator();
		Map<String, Integer> fieldsAndLengths = new HashMap<String,Integer>();
		fieldsAndLengths.put("id.idValue", 5);
		fieldsAndLengths.put("uri", 5);
		fieldsAndLengths.put("val2", 5);
		truncator.setFieldsToTruncate(fieldsAndLengths);
		codingSchemeSupportedAttrib = new CodingSchemeSupportedAttrib(
				new CodingSchemeSupportedAttribId());
		codingSchemeSupportedAttrib.getId().setIdValue("123456789");
		codingSchemeSupportedAttrib.setUri("123456789");
		codingSchemeSupportedAttrib.setVal2("123");
	}
	
	/**
	 * Test no truncation.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNoTruncation() throws Exception {
		CodingSchemeSupportedAttrib truncated = 
			truncator.truncate(codingSchemeSupportedAttrib);
		assertTrue("Length: " + truncated.getVal2().length(),
				truncated.getVal2().length() == 3);
	}
	
	/**
	 * Test truncate one level.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTruncateOneLevel() throws Exception {
		CodingSchemeSupportedAttrib truncated = 
			truncator.truncate(codingSchemeSupportedAttrib);
		assertTrue("Length: " + truncated.getUri().length(),
				truncated.getUri().length() == 5);
	}
	
	/**
	 * Test truncate two levels.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTruncateTwoLevels() throws Exception {
		CodingSchemeSupportedAttrib truncated = 
			truncator.truncate(codingSchemeSupportedAttrib);
		assertTrue("Length: " + truncated.getId().getIdValue().length(),
				truncated.getId().getIdValue().length() == 5);
	}
	
	/**
	 * Test two truncations.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTwoTruncations() throws Exception {
		CodingSchemeSupportedAttrib truncated = 
			truncator.truncate(codingSchemeSupportedAttrib);
		assertTrue("Length: " + truncated.getId().getIdValue().length(),
				truncated.getId().getIdValue().length() == 5);
		assertTrue("Length: " + truncated.getUri().length(),
				truncated.getUri().length() == 5);
	}
}
