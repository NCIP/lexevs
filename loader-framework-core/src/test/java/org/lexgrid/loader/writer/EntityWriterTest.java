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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.lexgrid.loader.test.util.SupportHelpers.TestCodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
    private AuthoringService authoringService;
	
	@Autowired
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	@Autowired
	private EntityService entityService;
	
	@Test
	public void testEntity() throws Exception{
		TestCodingSchemeIdSetter test = new TestCodingSchemeIdSetter();
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(test.getCodingSchemeUri());
		entry.setResourceVersion(test.getCodingSchemeVersion());
		entry.setDbSchemaVersion("2.0");
		lexEvsServiceLocator.getRegistry().addNewItem(entry);
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(test.getCodingSchemeUri());
		cs.setCodingSchemeURI(test.getCodingSchemeUri());
		cs.setRepresentsVersion(test.getCodingSchemeVersion());
		
		authoringService.loadRevision(cs, null, true);
		
		CodingSchemeIdHolder<Entity> holder = new CodingSchemeIdHolder<Entity>();
		holder.setCodingSchemeIdSetter(new TestCodingSchemeIdSetter());
		
		Entity e = new Entity();
		e.setEntityCode("code");
		e.setEntityCodeNamespace("ns");
		
		holder.setItem(e);
		
		List<CodingSchemeIdHolder<Entity>> list = new ArrayList<CodingSchemeIdHolder<Entity>>();
		list.add(holder);
		
		entityWriter.write(list);
		
		
	}
}