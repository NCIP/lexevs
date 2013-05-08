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
package org.lexgrid.loader.dao.template;

import static org.junit.Assert.assertTrue;

import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.Test;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CachedSupportedAttributeSupportTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplateTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private CachingSupportedAttribuiteTemplate cachingSupportedAttribuiteTemplate;
	/**
	 * Test insert cached.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertCached() throws Exception {
		SupportedCodingScheme cs = new SupportedCodingScheme();
		
		cs.setContent("content");
		cs.setIsImported(false);
		cs.setLocalId("locaId");
		
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs);
		
		assertTrue(cachingSupportedAttribuiteTemplate.getAttributeCache().size() == 1);
	}
	
	@Test
	public void testInsertTwoCached() throws Exception {
		SupportedCodingScheme cs = new SupportedCodingScheme();
		
		cs.setContent("content");
		cs.setIsImported(false);
		cs.setLocalId("locaId");
		
		SupportedCodingScheme cs2 = new SupportedCodingScheme();
		
		cs2.setContent("content");
		cs2.setIsImported(false);
		cs2.setLocalId("locaId");
		
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs);
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs2);
		
		assertTrue(cachingSupportedAttribuiteTemplate.getAttributeCache().size() == 1);
	}
}