package org.cts2.internal.profile.author;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.service.codesystemversion.UpdateCodeSystemVersionRequest;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.utility.ConstructorUtils;
import org.junit.Test;

public class LexEvsCodeSystemVersionAuthoringServiceTest extends
	BaseCts2IntegrationTest {

	@Resource
	private LexEvsCodeSystemVersionAuthoringService service;
	
	@Resource
	private LexBIGService lbs;
	
	@Test
	public void testSetUp(){
		assertNotNull(this.service);
	}
	
	@Test
	public void testCreateCodeSystemVersionCount() throws Exception {
		this.service.createCodeSystemVersion("http://a.change.org", "http://mycsv.org", "testName", null, null);
		
		assertEquals(1, lbs.getSupportedCodingSchemes().getCodingSchemeRenderingCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testUpdateCodeSystemVersionFormalName() throws Exception {
		UpdateCodeSystemVersionRequest request = new UpdateCodeSystemVersionRequest();
		request.setFormalName("updated formal name");
		
		this.service.updateCodeSystemVersion(
				ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), request);
		
		assertEquals(1, this.lbs.getSupportedCodingSchemes().getCodingSchemeRenderingCount());
		assertEquals("updated formal name", 
				this.lbs.getSupportedCodingSchemes().getCodingSchemeRendering(0).getCodingSchemeSummary().getFormalName());
	}
}
