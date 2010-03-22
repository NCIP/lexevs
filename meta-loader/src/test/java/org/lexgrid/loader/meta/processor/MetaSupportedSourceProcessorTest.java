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
package org.lexgrid.loader.meta.processor;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.test.util.SupportHelpers.TestCodingSchemeIdSetter;

/**
 * The Class MetaSupportedSourceProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaSupportedSourceProcessorTest {

	/** The processor. */
	private MetaSupportedSourceProcessor processor;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		processor = new MetaSupportedSourceProcessor();
		processor.setCodingSchemeIdSetter(new TestCodingSchemeIdSetter.TestCodingSchemeNameSetter());
		Map<String,String> isoMap = new HashMap<String,String>();
		isoMap.put("testCs", "1.2.3.4.5");
		processor.setIsoMap(isoMap);
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(processor);
	}
	
	/**
	 * Test add supported source.
	 */
	@Test
	public void testAddSupportedSource() {
		CodingSchemeIdSetter setter = new TestCodingSchemeIdSetter.TestCodingSchemeNameSetter();
		SupportedAttributeTemplate template = createMock(SupportedAttributeTemplate.class);
		
		template.addSupportedSource(setter.getCodingSchemeName(), 
				"testCs", 
				"1.2.3.4.5", 
				null, 
				null);
		
		replay(template);
		
		EntityPropertyMultiAttrib source = new EntityPropertyMultiAttrib(new EntityPropertyMultiAttribId());
		source.getId().setAttributeValue("testCs");
		processor.registerSupportedAttributes(template, source);
	}
}
