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
package org.lexgrid.loader.rrf.processor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.processor.EntityAssnToEQualsProcessor;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class RrfEntityAssnsToEntityProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RrfEntityAssnsToEntityProcessorTest {

	/** The processor. */
	private RrfEntityAssnsToEntityProcessor processor;
	
	/**
	 * Sets the up.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		processor = new TestRrfEntityAssnsToEntityProcessor();
	}
	
	/**
	 * Test null qualifier processors.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNullQualifierProcessors() throws Exception {
		processor.setQualifierProcessors(null);
		processor.afterPropertiesSet();
		assertNotNull(processor.process(new Mrrel()));
	}
	
	/**
	 * Test empty list qualifier processors.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testEmptyListQualifierProcessors() throws Exception {
		processor.setQualifierProcessors(new ArrayList<EntityAssnToEQualsProcessor<Mrrel>>());
		processor.afterPropertiesSet();
		assertNotNull(processor.process(new Mrrel()));
	}
	
	/**
	 * Test qualifier processor with key resolver.
	 */
	@Test
	public void testQualifierProcessorWithKeyResolver() {
		EntityAssnToEQualsProcessor<Mrrel> qualProcessor = new EntityAssnToEQualsProcessor<Mrrel>();
		qualProcessor.setKeyResolver(new RandomUuidKeyResolver());
		
		List<EntityAssnToEQualsProcessor<Mrrel>> list = new ArrayList<EntityAssnToEQualsProcessor<Mrrel>>();
		list.add(qualProcessor);
		
		processor.setQualifierProcessors(list);
		
		try {
			processor.afterPropertiesSet();
		} catch (Exception e) {
			return;
		}
		fail("Didn't throw an Exception with a pre-set Key Resolver.");
	}
	
	/**
	 * The Class TestRrfEntityAssnsToEntityProcessor.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TestRrfEntityAssnsToEntityProcessor extends RrfEntityAssnsToEntityProcessor {

		/* (non-Javadoc)
		 * @see org.lexgrid.loader.rrf.processor.RrfEntityAssnsToEntityProcessor#buildEntityAssnsToEntity(org.lexgrid.loader.rrf.model.Mrrel)
		 */
		@Override
		protected EntityAssnsToEntity buildEntityAssnsToEntity(Mrrel item)
				throws Exception {
			EntityAssnsToEntity assn = new EntityAssnsToEntity();
			assn.setMultiAttributesKey("12345");
			return assn;
		}	
	}
}
