package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.EntityDirectoryEntry;
import org.junit.Test;

public class RcrToEdeTest extends BaseDozerBeanMapperTest {
	
	@Test
	public void testGetCode(){
		ResolvedConceptReference ref = new ResolvedConceptReference();
		ref.setCode("test code");
		
		EntityDirectoryEntry mapped = 
			baseDozerBeanMapper.map(ref, EntityDirectoryEntry.class);
		
		assertEquals("test code", mapped.getLocalEntityName().getName());
		
	}
	
	@Test
	public void testGetNamespace(){
		ResolvedConceptReference ref = new ResolvedConceptReference();
		ref.setCodeNamespace("test namespace");
		
		EntityDirectoryEntry mapped = 
			baseDozerBeanMapper.map(ref, EntityDirectoryEntry.class);
		
		assertEquals("test namespace", mapped.getLocalEntityName().getNamespace());
		
	}

}
