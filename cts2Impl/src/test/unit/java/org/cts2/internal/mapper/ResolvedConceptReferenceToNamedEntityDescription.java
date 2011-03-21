package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedNamespace;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.entity.types.DesignationRole;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionAboutConverter;
import org.cts2.internal.mapper.converter.PresentationPreferredToDesignationRoleConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;

public class ResolvedConceptReferenceToNamedEntityDescription extends
		BaseDozerBeanMapperTest {
	@Resource
	private NamedEntityDescriptionAboutConverter namedEntityDescriptionAboutConverter;
	@Resource
	private PresentationPreferredToDesignationRoleConverter converter;
	private ResolvedConceptReference ref;
	private NamedEntityDescription mapped;

	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCode("testCode");
		ref.setCodeNamespace("testNamespace");
		ref.setCodingSchemeName("codingSchemeName");
		ref.setCodingSchemeURI("testUri");
		ref.setCodingSchemeVersion("testVersion");
		
		Entity entity = new Entity();
		entity.setEntityCode("testCode");
		entity.setEntityCodeNamespace("testNamespace");
		
		ref.setEntity(entity);

		//presentation
		org.LexGrid.concepts.Presentation presentation1 = new org.LexGrid.concepts.Presentation();
		presentation1.setDegreeOfFidelity("testFed");
		presentation1.setPropertyId("testPropertyID");
		presentation1.setLanguage("en");
		Text value = new Text();
		value.setContent("testValue");
		value.setDataType("string");
		presentation1.setValue(value);
		presentation1.setPropertyName("property name");
		presentation1.addUsageContext("test usage context 1");
		presentation1.addUsageContext("test usage context 2");
		presentation1.addUsageContext("test usage context 3");
		org.LexGrid.concepts.Presentation presentation2 = new org.LexGrid.concepts.Presentation();
		presentation2.setDegreeOfFidelity("degree of fidelity");
		presentation2.setPropertyId("presentation 2");
		Text t2 = new Text();
		t2.setContent("2 content");
		t2.setDataType("string");
		
		ref.getEntity().addPresentation(presentation1);
		ref.getEntity().addPresentation(presentation2);
		
		//property
		
		
		
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
		
		this.namedEntityDescriptionAboutConverter.setCodingSchemeService(css);
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		this.namedEntityDescriptionAboutConverter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		this.converter.setLexEvsIdentityConverter(lexEvsIdentityConverter);		
		
		mapped = baseDozerBeanMapper.map(ref, NamedEntityDescription.class);
//		mapped.setAbout(about)
//		mapped.setDefinition(vDefinitionArray)
//		mapped.setDesignation(vDesignationArray)
//		mapped.setEntityType(vEntityTypeArray)
//		mapped.setNote(vNoteArray)
//		mapped.setEntryState(entryState)			?
//		mapped.setProperty(vPropertyArray)
//		mapped.setEquivalentEntities(vEquivalentEntitiesArray)
//		mapped.setDescribingCodeSystemVersion(describingCodeSystemVersion)
//		mapped.setInstances(instances)
		
	}

	@Test
	public void testGetCode() {
		assertEquals("testCode", mapped.getEntityId().getName());
	}

	@Test
	public void testGetNamespace() {
		assertEquals("testNamespace", mapped.getEntityId().getNamespace());
	}

	@Test
	public void testGetCodingSchemeURI() {
		assertEquals("testUri", mapped.getDescribingCodeSystemVersion()
				.getMeaning());
	}

	@Test
	public void testGetCodingSchemeName() {
		assertEquals("codingSchemeName", mapped
				.getDescribingCodeSystemVersion().getContent());
	}
	
	@Test
	public void testGetAbout() {
		assertEquals("testUri:testCode", mapped.getAbout());
	}
	
	@Test
	public void testGetPresentationCount() {
		assertEquals(2, mapped.getDesignationCount());
	}
	
	@Test
	public void testGetPresentation() {
		assertEquals("testFed", mapped.getDesignation(0).getDegreeOfFidelity().getContent());
		assertEquals("en", mapped.getDesignation(0).getLanguage().getContent());
		assertEquals("testPropertyID", mapped.getDesignation(0).getExternalIdentifier());
		assertEquals("testValue", mapped.getDesignation(0).getValue());
		assertEquals("test usage context 1", mapped.getDesignation(0).getUsageContext(0).getContent());
		assertEquals("test usage context 2", mapped.getDesignation(0).getUsageContext(1).getContent());
		assertEquals("test usage context 3", mapped.getDesignation(0).getUsageContext(2).getContent());
		assertEquals(DesignationRole.ALTERNATIVE, mapped.getDesignation(0).getDesignationRole());
	}
	
	
}
