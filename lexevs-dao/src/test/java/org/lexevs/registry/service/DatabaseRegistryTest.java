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
package org.lexevs.registry.service;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;


/**
 * The Class DatabaseRegistryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseRegistryTest extends LexEvsDbUnitTestBase {
	
	/** The database registry. */
	@Resource
	DatabaseRegistry databaseRegistry;
	
	/**
	 * Test activate.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testActivate() throws Exception{
		
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.INACTIVE.toString());
		
		databaseRegistry.getRegistryDao().insertRegistryEntry(entry);
		
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN("uri");
		ref.setCodingSchemeVersion("v1");
		
		databaseRegistry.activate(ref);
		
		RegistryEntry updatedEntry = databaseRegistry.getCodingSchemeEntry(ref);
		
		assertEquals(CodingSchemeVersionStatus.ACTIVE.toString(), updatedEntry.getStatus());
		
	}

}
