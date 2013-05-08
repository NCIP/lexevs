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
package org.lexevs.registry.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.registry.xmltransfer.RegistryXmlToDatabaseTransfer;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class DatabaseRegistryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseRegistryTest extends LexEvsDbUnitTestBase {
	
	/** The database registry. */
	@Resource
	DatabaseRegistry databaseRegistry;
	
	@Test
	public void testContainsNonCodingSchemeResource() throws Exception {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setResourceType(ResourceType.NCI_HISTORY);
		
		databaseRegistry.addNewItem(entry);
		
		assertTrue(databaseRegistry.containsNonCodingSchemeEntry("uri"));
	}
	
	@Test
	public void testContainsNonCodingSchemeResourceInvalid() throws Exception {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("uri");
		entry.setResourceVersion("v1");
		entry.setResourceType(ResourceType.NCI_HISTORY);
		
		databaseRegistry.addNewItem(entry);
		
		assertFalse(databaseRegistry.containsNonCodingSchemeEntry("INVALID"));
	}
	
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
	
	@Test
	public void testMigration() throws Exception{
		
		TestingRegistryXmlToDatabaseTransfer transfer = new TestingRegistryXmlToDatabaseTransfer();
		Registry dbRegistry = EasyMock.createNiceMock(Registry.class);
		Registry xmlRegistry = EasyMock.createNiceMock(Registry.class);
		
		LgLoggerIF logger = EasyMock.createNiceMock(LgLoggerIF.class);
		
		RegistryEntry entry = new RegistryEntry();
		List<RegistryEntry> entries = new ArrayList<RegistryEntry>();
		entries.add(entry);
		
		EasyMock.expect(xmlRegistry.getAllRegistryEntries()).andReturn(entries);

		transfer.setDatabaseRegistry(dbRegistry);
		transfer.setLogger(logger);
	
		SystemVariables vars = EasyMock.createNiceMock(SystemVariables.class);
		EasyMock.expect(vars.isMigrateOnStartupEnabled()).andReturn(true);
		
		transfer.setSystemVariables(vars);
		transfer.setXmlRegistry(xmlRegistry);
		
		EasyMock.replay(logger,dbRegistry,xmlRegistry,vars);
		

		transfer.afterPropertiesSet();
		
		assertTrue(transfer.fileDeleted);
	}
	
	public static class TestingRegistryXmlToDatabaseTransfer extends RegistryXmlToDatabaseTransfer {

		private boolean fileDeleted = false;
		@Override
		protected void deleteRegistryXmlFile() {
			fileDeleted = true;
		}
	}

}