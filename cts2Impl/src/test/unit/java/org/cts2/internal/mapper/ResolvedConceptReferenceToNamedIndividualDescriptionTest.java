package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedDegreeOfFidelity;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;
import org.cts2.core.types.DefinitionRole;
import org.cts2.entity.NamedIndividualDescription;
import org.cts2.entity.types.DesignationRole;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.DefinitionPreferredToDefinitionRoleConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionAboutConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionDefinitionListConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionDesignationListConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionNoteListConverter;
import org.cts2.internal.mapper.converter.NamedEntityDescriptionPropertyListConverter;
import org.cts2.internal.mapper.converter.PresentationPreferredToDesignationRoleConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;

public class ResolvedConceptReferenceToNamedIndividualDescriptionTest extends
		BaseDozerBeanMapperTest {
	@Resource
	private NamedEntityDescriptionAboutConverter namedEntityDescriptionAboutConverter;
	@Resource
	private PresentationPreferredToDesignationRoleConverter presentationConverter;
	@Resource
	private DefinitionPreferredToDefinitionRoleConverter definitionConverter;
	@Resource
	private NamedEntityDescriptionPropertyListConverter namedEntityDescriptionPropertyListConverter;
	@Resource
	private NamedEntityDescriptionDesignationListConverter namedEntityDescriptionDesignationListConverter;
	@Resource
	private NamedEntityDescriptionDefinitionListConverter namedEntityDescriptionDefinitionListConverter;
	@Resource
	private NamedEntityDescriptionNoteListConverter namedEntityDescriptionNoteListConverter;

	private ResolvedConceptReference ref;
	private NamedIndividualDescription mapped;

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

		// presentation
		this.initPresentations();

		// property
		this.initProperties();

		// comment
		this.initComments();

		// definition
		this.initDefinitions();

		CodingSchemeService css = EasyMock
				.createMock(CodingSchemeService.class);

		CodingScheme cs = new CodingScheme();
		cs.setMappings(new Mappings());
		cs.getMappings().addSupportedNamespace(new SupportedNamespace());
		cs.getMappings().getSupportedNamespace(0).setLocalId("test-namespace");
		cs.getMappings().getSupportedNamespace(0).setUri("test-namespace-uri");
		cs.getMappings().addSupportedLanguage(new SupportedLanguage());
		cs.getMappings().getSupportedLanguage(0).setLocalId("test lang");
		cs.getMappings().getSupportedLanguage(0).setUri("test lang uri");
		cs.getMappings().addSupportedLanguage(new SupportedLanguage());
		cs.getMappings().getSupportedLanguage(1).setLocalId("en");
		cs.getMappings().getSupportedLanguage(1).setUri("en uri");
		cs.getMappings().addSupportedDataType(new SupportedDataType());
		cs.getMappings().getSupportedDataType(0).setLocalId("string");
		cs.getMappings().getSupportedDataType(0).setUri("string uri");
		cs.getMappings().addSupportedDegreeOfFidelity(
				new SupportedDegreeOfFidelity());
		cs.getMappings().getSupportedDegreeOfFidelity(0).setLocalId("testFed");
		cs.getMappings().getSupportedDegreeOfFidelity(0).setUri("testFed uri");
		cs.getMappings().addSupportedContext(new SupportedContext());
		cs.getMappings().getSupportedContext(0)
				.setLocalId("test usage context 1");
		cs.getMappings().getSupportedContext(0)
				.setUri("test usage context 1 uri");
		cs.getMappings().addSupportedContext(new SupportedContext());
		cs.getMappings().getSupportedContext(1)
				.setLocalId("test usage context 2");
		cs.getMappings().getSupportedContext(1)
				.setUri("test usage context 2 uri");
		cs.getMappings().addSupportedContext(new SupportedContext());
		cs.getMappings().getSupportedContext(2)
				.setLocalId("test usage context 3");
		cs.getMappings().getSupportedContext(2)
				.setUri("test usage context 3 uri");

		cs.setRepresentsVersion("testVersion");
		cs.setCodingSchemeURI("testUri");

		EasyMock.expect(
				css.getCodingSchemeByUriAndVersion("testUri", "testVersion"))
				.andReturn(cs).times(5);

		EasyMock.replay(css);

		this.namedEntityDescriptionAboutConverter.setCodingSchemeService(css);
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		this.namedEntityDescriptionAboutConverter
				.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		this.presentationConverter
				.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		definitionConverter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		namedEntityDescriptionPropertyListConverter
				.setBaseDozerBeanMapper(baseDozerBeanMapper);
		namedEntityDescriptionPropertyListConverter.setCodingSchemeService(css);
		namedEntityDescriptionDesignationListConverter
				.setBaseDozerBeanMapper(baseDozerBeanMapper);
		namedEntityDescriptionDesignationListConverter
				.setCodingSchemeService(css);
		namedEntityDescriptionDefinitionListConverter
				.setBaseDozerBeanMapper(baseDozerBeanMapper);
		namedEntityDescriptionDefinitionListConverter
				.setCodingSchemeService(css);
		namedEntityDescriptionNoteListConverter
				.setBaseDozerBeanMapper(baseDozerBeanMapper);
		namedEntityDescriptionNoteListConverter.setCodingSchemeService(css);

		mapped = baseDozerBeanMapper.map(ref, NamedIndividualDescription.class);
		// mapped.setAbout(about)
		// mapped.setDefinition(vDefinitionArray)
		// mapped.setDesignation(vDesignationArray)
		// mapped.setEntityType(vEntityTypeArray)
		// mapped.setNote(vNoteArray)
		// mapped.setEntryState(entryState) ?
		// mapped.setProperty(vPropertyArray)
		// mapped.setEquivalentEntities(vEquivalentEntitiesArray)
		// mapped.setDescribingCodeSystemVersion(describingCodeSystemVersion)
		// mapped.setInstances(instances)

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
		assertEquals("testFed", mapped.getDesignation(0).getDegreeOfFidelity()
				.getContent());
		assertEquals("en", mapped.getDesignation(0).getLanguage().getContent());
		assertEquals("testPropertyID", mapped.getDesignation(0)
				.getExternalIdentifier());
		assertEquals("testValue", mapped.getDesignation(0).getValue());
		assertEquals("test usage context 1", mapped.getDesignation(0)
				.getUsageContext(0).getContent());
		assertEquals("test usage context 2", mapped.getDesignation(0)
				.getUsageContext(1).getContent());
		assertEquals("test usage context 3", mapped.getDesignation(0)
				.getUsageContext(2).getContent());
		assertEquals(DesignationRole.ALTERNATIVE, mapped.getDesignation(0)
				.getDesignationRole());
		assertEquals("en uri", mapped.getDesignation(0).getLanguage()
				.getMeaning());
		assertEquals("testFed uri", mapped.getDesignation(0)
				.getDegreeOfFidelity().getMeaning());
		assertEquals("string uri", mapped.getDesignation(0).getFormat()
				.getMeaning());
		assertEquals("test usage context 1 uri", mapped.getDesignation(0)
				.getUsageContext(0).getMeaning());
		assertEquals("test usage context 2 uri", mapped.getDesignation(0)
				.getUsageContext(1).getMeaning());
		assertEquals("test usage context 3 uri", mapped.getDesignation(0)
				.getUsageContext(2).getMeaning());
	}

	@Test
	public void testGetPropertyCount() {
		assertEquals(2, mapped.getPropertyCount());
	}

	@Test
	public void testGetProperty() {
		assertEquals("propertyId", mapped.getProperty(0)
				.getExternalIdentifier());
		assertEquals("content", mapped.getProperty(0).getValue().getValue());
		assertEquals("string", mapped.getProperty(0).getValue().getFormat()
				.getContent());
		assertEquals("string uri", mapped.getProperty(0).getValue().getFormat()
				.getMeaning());
		assertEquals("test lang", mapped.getProperty(0).getValue()
				.getLanguage().getContent());
		assertEquals("test lang uri", mapped.getProperty(0).getValue()
				.getLanguage().getMeaning());
	}

	@Test
	public void testGetCommentCount() {
		assertEquals(2, mapped.getNoteCount());
	}

	@Test
	public void testGetComment() {
		assertEquals("content", mapped.getNote(0).getValue());
		assertEquals("test lang", mapped.getNote(0).getLanguage()
				.getContent());
		assertEquals("string", mapped.getNote(0).getFormat().getContent());
		assertEquals("propertyId", mapped.getNote(0).getExternalIdentifier());
		assertEquals("string uri", mapped.getNote(0).getFormat().getMeaning());
		assertEquals("test lang uri", mapped.getNote(0).getLanguage().getMeaning());
	}

	@Test
	public void testGetDefinitionCount() {
		assertEquals(2, mapped.getDefinitionCount());
	}

	@Test
	public void testGetDefinition() {
		assertEquals("test propertyid", mapped.getDefinition(0)
				.getExternalIdentifier());
		assertEquals("test lang", mapped.getDefinition(0).getLanguage()
				.getContent());
		assertEquals("content", mapped.getDefinition(0).getValue());
		assertEquals("string", mapped.getDefinition(0).getFormat().getContent());
		assertEquals(DefinitionRole.INFORMATIVE, mapped.getDefinition(0)
				.getDefinitionRole());
		assertEquals("test usage context 1", mapped.getDefinition(0)
				.getUsageContext().getContent());
		assertEquals("test lang uri", mapped.getDefinition(0).getLanguage()
				.getMeaning());
		assertEquals("string uri", mapped.getDefinition(0).getFormat()
				.getMeaning());
		assertEquals("test usage context 1 uri", mapped.getDefinition(0)
				.getUsageContext().getMeaning());

	}

	private void initProperties() {
		org.LexGrid.commonTypes.Property prop1 = new org.LexGrid.commonTypes.Property();
		prop1.setPropertyId("propertyId");
		Text t = new Text();
		t.setContent("content");
		t.setDataType("string");
		prop1.setValue(t);
		prop1.setLanguage("test lang");
		org.LexGrid.commonTypes.Property prop2 = new org.LexGrid.commonTypes.Property();
		prop2.setPropertyId("prop2");

		ref.getEntity().addProperty(prop1);
		ref.getEntity().addProperty(prop2);
	}

	private void initComments() {
		org.LexGrid.concepts.Comment com1 = new org.LexGrid.concepts.Comment();
		com1.setLanguage("test lang");
		Text t = new Text();
		t.setContent("content");
		t.setDataType("string");
		com1.setValue(t);
		com1.setPropertyId("propertyId");
		org.LexGrid.concepts.Comment com2 = new org.LexGrid.concepts.Comment();
		com2.setLanguage("test lang2");
		com2.setPropertyId("propertyId2");

		ref.getEntity().addComment(com1);
		ref.getEntity().addComment(com2);
	}

	private void initDefinitions() {
		org.LexGrid.concepts.Definition def1 = new org.LexGrid.concepts.Definition();
		def1.setPropertyId("test propertyid");
		def1.setLanguage("test lang");
		Text t = new Text();
		t.setDataType("string");
		t.setContent("content");
		def1.setValue(t);
		def1.addUsageContext("test usage context 1");
		def1.addUsageContext("test usage context 2");
		org.LexGrid.concepts.Definition def2 = new org.LexGrid.concepts.Definition();
		def2.setPropertyId("test propertyid 2");

		ref.getEntity().addDefinition(def1);
		ref.getEntity().addDefinition(def2);
	}

	private void initPresentations() {
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
	}
}
