package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.EntityDirectoryEntry;
import org.junit.Before;
import org.junit.Test;

public class ResolvedConceptReferenceToEntityDirectoryEntryTest extends BaseDozerBeanMapperTest {
	private ResolvedConceptReference ref;
	private EntityDirectoryEntry mapped;
	
	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCode("test code");
		ref.setCodeNamespace("test namespace");
		mapped = baseDozerBeanMapper.map(ref, EntityDirectoryEntry.class);

	}
	
	@Test
	public void testGetCode(){
		// map to local entity name
		assertEquals("test code", mapped.getLocalEntityName().getName());
		// map to resource name
		assertEquals("test code", mapped.getResourceName());
	}
	
	@Test
	public void testGetNamespace(){
		assertEquals("test namespace", mapped.getLocalEntityName().getNamespace());
		
	}

}
