package org.cts2.internal.mapper;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.types.EntryState;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CodingSchemeToCodeSystemVersionTest extends BaseDozerBeanMapperTest {
	
	@Resource
	private org.cts2.internal.mapper.converter.CodeSystemVersionIdentityConverter converter;
	
	private CodingScheme cs;
	
	@Before
	public void buildCodingScheme(){
		cs = new CodingScheme();
		cs.setApproxNumConcepts(1000l);
		cs.setCodingSchemeName("test_cs_name");
		cs.setCodingSchemeURI("test_cs_uri");
		cs.setCopyright(Constructors.createText("test copyright"));
		cs.setDefaultLanguage("ENG");
		cs.setEffectiveDate(new Date());
		cs.setEntityDescription(Constructors.createEntityDescription("test description"));
		cs.setExpirationDate(new Date());
		cs.setFormalName("test_formal_name");
		cs.setIsActive(true);
		cs.addLocalName("ln1");
		cs.addLocalName("ln2");
		cs.setOwner("some_owner");
		cs.setRepresentsVersion("v1");
		cs.setStatus("test_status");
	}
	
	@Test
	public void Map_CodingSchemeURI_To_About(){
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test_cs_uri", csv.getAbout());
	}
	
	@Test
	@DirtiesContext
	public void Map_CodingSchemeName_To_CodeSystemVersionName(){
		LexEvsIdentityConverter converter = EasyMock.createNiceMock(LexEvsIdentityConverter.class);
		
		CodingScheme mockCs = (CodingScheme)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeToCodeSystemVersionName(
				mockCs)).andReturn("test_cs_name:v1").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
		
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test_cs_name:v1", csv.getCodeSystemVersionName());
	}
	
	@Test
	@DirtiesContext
	public void Map_CodingScheme_To_CodeSystemVersionDocumentURI(){
		LexEvsIdentityConverter converter = EasyMock.createNiceMock(LexEvsIdentityConverter.class);
		
		CodingScheme mockCs = (CodingScheme)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeToCodeSystemVersionDocumentUri(
				mockCs)).andReturn("test_cs_uri:v1:RRF").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
		
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test_cs_uri:v1:RRF", csv.getDocumentURI());
	}
	
	@Test
	@DirtiesContext
	public void Map_CodingScheme_Copyright_To_CodeSystemVersionDocumentURI(){
		LexEvsIdentityConverter converter = EasyMock.createNiceMock(LexEvsIdentityConverter.class);
		
		CodingScheme mockCs = (CodingScheme)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeToCodeSystemVersionDocumentUri(
				mockCs)).andReturn("test_cs_uri:v1:RRF").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
		
		cs.getCopyright().setDataType("d");
		
		cs.setMappings(new Mappings());
		SupportedDataType datatype = new SupportedDataType();
		datatype.setUri("datatypeUri");
		datatype.setLocalId("d");
		cs.getMappings().addSupportedDataType(datatype);
		
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test copyright", csv.getRights().getValue());
		
		assertEquals("d", csv.getRights().getFormat().getContent());
		assertEquals("datatypeUri", csv.getRights().getFormat().getHref());
	}
	
	@Test
	@DirtiesContext
	public void Map_CodingScheme_DefaultLanguage_To_DefaultLanguage(){
		LexEvsIdentityConverter converter = EasyMock.createNiceMock(LexEvsIdentityConverter.class);
		
		CodingScheme mockCs = (CodingScheme)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeToCodeSystemVersionDocumentUri(
				mockCs)).andReturn("test_cs_uri:v1:RRF").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
	
		cs.setMappings(new Mappings());
		SupportedLanguage language = new SupportedLanguage();
		language.setUri("languageUri");
		language.setLocalId("ENG");
		cs.getMappings().addSupportedLanguage(language);
		
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("ENG", csv.getDefaultLanguage().getContent());
		assertEquals("languageUri", csv.getDefaultLanguage().getHref());
	}
	
	@Test
	public void Map_CodingScheme_EntityDescription(){
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test description", csv.getResourceSynopsis().getValue());
	}
	
	@Test
	public void Map_CodingScheme_FormalName(){

		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);

		assertEquals("test_formal_name", csv.getFormalName());
	}
	
	@Test
	public void Map_CodingScheme_IsActive(){

		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals(EntryState.ACTIVE,csv.getEntryState());
		
		cs.setIsActive(false);
		csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
	
		assertEquals(EntryState.INACTIVE,csv.getEntryState());
		
		cs.setIsActive(null);
		csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
	
		assertNull(csv.getEntryState());
	}
	
	@Test
	public void Map_CodingScheme_localNames(){

		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);

		assertEquals(2, csv.getKeyword().length);
		
		String[] result = csv.getKeyword();
		String[] expected = new String[]{"ln1", "ln2"};
		
		Arrays.sort(result);
		Arrays.sort(expected);
		
		assertArrayEquals(expected,result);
	}
	
	@Test
	public void Map_CodingScheme_RepresentsVersion(){

		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("v1",csv.getOfficialResourceVersionId());
	}

}
