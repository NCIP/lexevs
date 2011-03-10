package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.codesystemversion.CodeSystemVersionDirectoryEntry;
import org.cts2.core.VersionTagReference;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CodingSchemeRenderingToCodeSystemVersionDirectoryEntryTest extends BaseDozerBeanMapperTest {
	
	@Resource
	private org.cts2.internal.mapper.converter.CodeSystemVersionDirectoryEntryIdentityConverter converter;
	
	
	@Test
	public void testFormalName(){
		CodingSchemeRendering csr = new CodingSchemeRendering();
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setFormalName("test formal name");
		csr.setCodingSchemeSummary(css);
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(csr, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("test formal name", mapped.getFormalName());
		
	}
	
	@Test
	public void testEntityDescription(){
		CodingSchemeRendering csr = new CodingSchemeRendering();
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setCodingSchemeDescription(Constructors.createEntityDescription("test desc"));
		csr.setCodingSchemeSummary(css);
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(csr, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("test desc", mapped.getResourceSynopsis().getValue());
		
	}
	
	@Test
	@DirtiesContext
	public void Map_Everything(){
		LexEvsIdentityConverter converter = EasyMock.createMock(LexEvsIdentityConverter.class);
		
		AbsoluteCodingSchemeVersionReference ref = (AbsoluteCodingSchemeVersionReference)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeReferenceToCodeSystemVersionName(
				ref)).andReturn("test local name:testVersion").anyTimes();
		
		ref = (AbsoluteCodingSchemeVersionReference)EasyMock.anyObject();
		
		EasyMock.expect(converter.codingSchemeReferenceToCodeSystemVersionDocumentUri(
				ref)).andReturn("testURI:testVersion:RRF").anyTimes();
		
		EasyMock.replay(converter);	

		this.converter.setLexEvsIdentityConverter(converter);
		
		CodingSchemeRendering csr = new CodingSchemeRendering();
		
		csr.setRenderingDetail(new RenderingDetail());
		CodingSchemeTagList tags = new CodingSchemeTagList();
		tags.addTag("TEST");
		tags.addTag("PRODUCTION");
		
		csr.getRenderingDetail().setVersionTags(tags);
		
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setCodingSchemeURI("testURI");
		css.setRepresentsVersion("testVersion");
		css.setFormalName("Test Formal Name");
		css.setLocalName("test local name");

		css.setCodingSchemeDescription(Constructors.createEntityDescription("test desc"));
		
		csr.setCodingSchemeSummary(css);
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(csr, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("testURI", mapped.getAbout());
		assertEquals("test local name:testVersion", mapped.getCodeSystemVersionName());
		assertEquals("testURI:testVersion:RRF", mapped.getDocumentURI());
		assertEquals("Test Formal Name", mapped.getFormalName());
		
		//not tracked in LexEVS
		assertEquals(null, mapped.getMatchStrength());
		
		//not tracked in LexEVS
		assertEquals(null, mapped.getOfficialReleaseDate());
		assertEquals("testVersion", mapped.getOfficialResourceVersionId());
		assertEquals("test local name:testVersion", mapped.getResourceName());
		assertEquals("testURI:testVersion:RRF", mapped.getResourceID());
		assertEquals("test desc", mapped.getResourceSynopsis().getValue());
		
		//not supported in LexEVS
		assertEquals(null, mapped.getVersionOf());

		assertEquals(2,mapped.getCodeSystemVersionTagCount());

	}
	
	@Test
	public void Map_VersionTagReference(){
		String tag = "PRODUCTION";
		
		VersionTagReference mapped = 
			baseDozerBeanMapper.map(tag, VersionTagReference.class);
		
		assertEquals("PRODUCTION", mapped.getContent());
	}

}
