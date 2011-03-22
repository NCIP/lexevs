package org.cts2.internal.profile.read;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.utility.ConstructorUtils;
import org.junit.Test;

public class LexEvsCodeSystemVersionReadServiceTestIT extends BaseCts2IntegrationTest {

	@Resource
	private LexEvsCodeSystemVersionReadService lexEvsCodeSystemVersionReadService;

	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemVersionReadService);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersion(){
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, null);
		
		assertEquals("urn:oid:11.11.0.1", codeSystemVersion.getAbout());
	}
}
