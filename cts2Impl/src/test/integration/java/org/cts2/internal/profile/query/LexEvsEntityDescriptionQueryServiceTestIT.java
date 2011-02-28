package org.cts2.internal.profile.query;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.test.BaseCts2IntegrationTest;
import org.junit.Test;

public class LexEvsEntityDescriptionQueryServiceTestIT extends BaseCts2IntegrationTest {
	
	@Resource
	private LexEvsEntityDescriptionQueryService lexEvsEntityDescriptionQueryService;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsEntityDescriptionQueryService);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testGetEntitiesNotNull(){
		assertNotNull(lexEvsEntityDescriptionQueryService.getEntities());
	}
	
	
	
}
