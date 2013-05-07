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
package org.lexgrid.loader.processor;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.loader.processor.support.EntityResolver;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;

/**
 * The Class EntityProcessorTest.
 * 
 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
 */
public class EntityProcessorTest {
	/**
	 * Test get entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetEntityProcessed() throws Exception {
		TestEntityProcessor entityProcessor = new TestEntityProcessor();
		
		TestEntityResolver er = new TestEntityResolver();
		entityProcessor.setEntityResolver(er);
		
		CodingSchemeIdHolder<Entity> csh = entityProcessor.process(null);
		
		assertTrue(csh.getItem().getEntityCodeNamespace().equals("xyz"));
		assertTrue(csh.getItem().getEntityCode().equals("abc"));
		assertTrue(csh.getItem().getEntityDescription().getContent().equals("desc"));
		assertTrue((csh.getItem().getEntityType())[0].equals("concept"));
		assertTrue(csh.getItem().getIsActive());
		assertTrue(csh.getItem().getIsDefined());
		assertFalse(csh.getItem().getIsAnonymous());
	}

	/**
	 * The Class TestEntityProcessor.
	 * 
	 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
	 */
	private class TestEntityProcessor extends EntityProcessor{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */
		public CodingSchemeIdHolder<Entity> process(Object item) throws Exception {
			return super.process(item);
		}	
	}
	
	/**
	 * The Class TestEntityResolver.
	 * 
	 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
	 */
	private class TestEntityResolver implements EntityResolver{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */

		public String getEntityCode(Object item) {
			// TODO Auto-generated method stub
			return "abc";
		}

		public String getEntityCodeNamespace(Object item) {
			// TODO Auto-generated method stub
			return "xyz";
		}

		public String getEntityDescription(Object item) {
			// TODO Auto-generated method stub
			return "desc";
		}

		public String[] getEntityTypes(Object item) {
			// TODO Auto-generated method stub
			return (new String[]{"concept"});
		}

		public boolean getIsActive(Object item) {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean getIsAnonymous(Object item) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean getIsDefined(Object item) {
			// TODO Auto-generated method stub
			return true;
		}	
	}
}