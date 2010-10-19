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
package org.lexgrid.loader.writer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.DatabaseRegistry;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

public class CodingSchemeWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private CodingSchemeWriter codingSchemeWriter;
	
	@Autowired
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	@Autowired
	private CodingSchemeService codingSchemeService;
	
	@Test
	public void testInsertCodingScheme() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("csUri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		lexEvsServiceLocator.getRegistry().addNewItem(entry);
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("csUri");
		cs.setRepresentsVersion("v1");
		
		List<CodingScheme> csList = DaoUtility.createList(CodingScheme.class, cs);
		
		codingSchemeWriter.write(csList);
		
		CodingScheme foundCs = 
			codingSchemeService.getCodingSchemeByUriAndVersion("csUri", "v1");
		
		assertEquals("csUri", foundCs.getCodingSchemeURI());
		assertEquals("v1", foundCs.getRepresentsVersion());
		
	}
}