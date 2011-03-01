package org.cts2.internal.profile.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.entity.EntityDirectory;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.EntityDirectoryURI;
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
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testGetCount(){
		DirectoryURI uri = 
			lexEvsEntityDescriptionQueryService.getEntities();
		
		assertEquals(19,uri.count(null));
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveEntityNotNull(){
		EntityDirectoryURI uri = 
			lexEvsEntityDescriptionQueryService.getEntities();
		
		EntityDirectory ed = 
			lexEvsEntityDescriptionQueryService.resolve(uri, null, null);
		
		assertNotNull(ed);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveEntityCount(){
		EntityDirectoryURI uri = 
			lexEvsEntityDescriptionQueryService.getEntities();
		
		EntityDirectory ed = 
			lexEvsEntityDescriptionQueryService.resolve(uri, null, null);
		
		assertEquals(19,ed.getEntryCount());
	}
	
	
	
}
