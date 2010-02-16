package org.lexevs.registry.service;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsResourceTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.DBEntry;


public class DatabaseRegistryTest extends LexEvsResourceTestBase {
	
	@Resource
	DatabaseRegistry databaseRegistry;
	
	@Test
	public void testGetNextDBIdentifier() throws Exception{
		assertEquals("baaa", databaseRegistry.getNextDBIdentifier());
	}
	
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
		
		DBEntry updatedEntry = databaseRegistry.getEntry(ref);
		
		assertEquals(CodingSchemeVersionStatus.ACTIVE.toString(), updatedEntry.status);
		
	}

}
