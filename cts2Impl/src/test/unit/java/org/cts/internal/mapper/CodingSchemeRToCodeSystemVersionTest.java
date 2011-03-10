package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CodingSchemeRToCodeSystemVersionTest extends BaseDozerBeanMapperTest {
	
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
		LexEvsIdentityConverter converter = EasyMock.createMock(LexEvsIdentityConverter.class);
		
		CodingScheme mockCs = (CodingScheme)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeToCodeSystemVersionName(
				mockCs)).andReturn("test_cs_name:v1").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
		
		CodeSystemVersion csv = 
			this.baseDozerBeanMapper.map(cs, CodeSystemVersion.class);
		
		assertEquals("test_cs_name:v1", csv.getCodeSystemVersionName());
	}
	

}
