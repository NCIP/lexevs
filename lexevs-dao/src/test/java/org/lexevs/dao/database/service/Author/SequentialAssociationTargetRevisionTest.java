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

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.association.VersionableEventAssociationTargetService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialAssociationTargetRevisionTest extends BaseRevisionTest {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	@Resource
	private VersionableEventAssociationTargetService associationTargetService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/associationTargetRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testGetRevision1AssociationSource() throws Exception {

		AssociationTarget target = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "1");

		assertEquals("rev1TargetCode", target.getTargetEntityCode());
		assertEquals("rev1TargetCodeNamespace", target.getTargetEntityCodeNamespace());
	}
	
	@Test
	public void testGetRevision2AssociationSource() throws Exception {

		AssociationTarget target = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "2");

		assertEquals("rev2TargetCode", target.getTargetEntityCode());
		assertEquals("rev2TargetCodeNamespace", target.getTargetEntityCodeNamespace());
	}
	
	@Test
	public void testGetRevision3AssociationSource() throws Exception {

		AssociationTarget target1 = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a1", "3");

		assertEquals("rev2TargetCode", target1.getTargetEntityCode());
		assertEquals("rev2TargetCodeNamespace", target1.getTargetEntityCodeNamespace());
		
		AssociationTarget target2 = associationTargetService.resolveAssociationTargetByRevision(
				"testUri", "1.0", "testRelationsName", "testAssocPredicate",
				"a3", "3");

		assertEquals("rev3TargetCode", target2.getTargetEntityCode());
		assertEquals("rev3TargetCodeNamespace", target2.getTargetEntityCodeNamespace());
	}
}