package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.EntityDirectoryEntry;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.EntityDirectoryEntryAboutConverter;
import org.junit.Before;
import org.junit.Test;

public class ResolvedConceptReferenceToEntityDirectoryEntryTest extends BaseDozerBeanMapperTest {
	private ResolvedConceptReference ref;
	private EntityDirectoryEntry mapped;
	
	@Resource 
	private EntityDirectoryEntryAboutConverter converter;
	
	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCode("test code");
		ref.setCodeNamespace("test namespace");
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		converter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
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
	
	@Test
	public void testCTS2About() {
		
	}
	
}
