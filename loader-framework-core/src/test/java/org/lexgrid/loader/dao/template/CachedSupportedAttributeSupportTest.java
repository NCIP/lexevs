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
package org.lexgrid.loader.dao.template;

import org.LexGrid.persistence.model.CodingSchemeSupportedAttrib;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttribId;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The Class CachedSupportedAttributeSupportTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachedSupportedAttributeSupportTest extends SupportedAttributeSupportTestBase {

	/**
	 * Test insert cached.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertCached() throws Exception {
		CodingSchemeSupportedAttrib attrib = new CodingSchemeSupportedAttrib();
		CodingSchemeSupportedAttribId id = new CodingSchemeSupportedAttribId();
		attrib.setId(id);
		
		assertTrue(getLexEvsDao().query(attrib).size() == 0);
		
		CachingSupportedAttribuiteTemplate cachedTemplate = (CachingSupportedAttribuiteTemplate)getTemplate();
		assertTrue(cachedTemplate.getCache().size() == 0);
		
		cachedTemplate.addSupportedAssociation("testCsName", "testId", "testUri", "testContent");
		
		assertTrue(cachedTemplate.getCache().size() == 1);
		
		assertTrue(getLexEvsDao().query(attrib).size() == 1);
		
		cachedTemplate.addSupportedAssociation("testCsName", "testId", "testUri", "testContent");
		
		assertTrue(cachedTemplate.getCache().size() == 1);
		
		assertTrue(getLexEvsDao().query(attrib).size() == 1);
		
	}
}
