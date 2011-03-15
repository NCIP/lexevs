package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedNamespace;
import org.cts2.entity.EntityDirectoryEntry;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.EntityDirectoryEntryAboutConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;

public class ResolvedConceptReferenceToEntityDirectoryEntryTest extends BaseDozerBeanMapperTest {
	
	@Resource
	private EntityDirectoryEntryAboutConverter entityDirectoryEntryAboutConverter;
	
	private ResolvedConceptReference ref;
	private EntityDirectoryEntry mapped;
	
	
	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCode("test code");
		ref.setCodeNamespace("test-namespace");
		ref.setCodingSchemeURI("testUri");
		ref.setCodingSchemeVersion("testVersion");
		
		CodingSchemeService css = EasyMock.createMock(CodingSchemeService.class);
		
		CodingScheme cs = new CodingScheme();
		cs.setMappings(new Mappings());
		cs.getMappings().addSupportedNamespace(new SupportedNamespace());
		cs.getMappings().getSupportedNamespace(0).setLocalId("test-namespace");
		cs.getMappings().getSupportedNamespace(0).setUri("test-namespace-uri");
		cs.setRepresentsVersion("testVersion");
		cs.setCodingSchemeURI("testUri");
		
		EasyMock.expect(css.getCodingSchemeByUriAndVersion("testUri", "testVersion")).andReturn(cs).times(1);
		
		EasyMock.replay(css);
		
		this.entityDirectoryEntryAboutConverter.setCodingSchemeService(css);
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		this.entityDirectoryEntryAboutConverter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		
		mapped = baseDozerBeanMapper.map(ref, EntityDirectoryEntry.class);
		
	}
	
	@Test
	public void converterIsNotNull(){
		assertNotNull(this.entityDirectoryEntryAboutConverter);
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
		assertEquals("test-namespace", mapped.getLocalEntityName().getNamespace());
		
	}
	
	@Test
	public void testCTS2About() {
		assertEquals("test-namespace-uri:test code", mapped.getAbout());
	}
	
}
