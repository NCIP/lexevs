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
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialCodingSchemeRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource
	private CodingSchemeService codingSchemeService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/codingSchemeRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
		
		System.out.println();
	}

	@Test
	public void testGetRevision1CodingScheme() throws Exception {

		CodingScheme cs = codingSchemeService.resolveCodingSchemeByRevision(
				"urn:oid:11.11.0.1", "1.1", "1");

		assertNotNull(cs);
		
		assertEquals(1,cs.getLocalNameCount());
		
		assertEquals("revisionLocalName1", cs.getLocalName(0));
	}
	
	@Test
	public void testGetRevision2CodingScheme() throws Exception {

		CodingScheme cs = codingSchemeService.resolveCodingSchemeByRevision(
				"urn:oid:11.11.0.1", "1.1", "2");

		assertNotNull(cs);
		
		assertEquals(1,cs.getLocalNameCount());
		
		assertEquals("revisionLocalName2", cs.getLocalName(0));
	}
	
	@Test
	public void testGetRevision3CodingScheme() throws Exception {

		CodingScheme cs = codingSchemeService.resolveCodingSchemeByRevision(
				"urn:oid:11.11.0.1", "1.1", "3");

		assertNotNull(cs);
		
		assertEquals(1,cs.getProperties().getPropertyCount());
		
		assertEquals("test prop",cs.getProperties().getProperty(0).getValue().getContent());
	}
	
	@Test
	public void testGetRevision4CodingScheme() throws Exception {

		CodingScheme cs = codingSchemeService.resolveCodingSchemeByRevision(
				"urn:oid:11.11.0.1", "1.1", "4");

		assertNotNull(cs);
		
		assertEquals(1,cs.getLocalNameCount());
		
		assertEquals("revisionLocalName4", cs.getLocalName(0));
	}

}