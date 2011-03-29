package org.cts2.internal.profile.query;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.test.BaseCts2UnitTest;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.valueset.ValueSetDefinitionDirectory;
import org.junit.Test;

public class LexEvsValueSetDefinitionQueryServiceTestIT extends BaseCts2UnitTest {
	
	@Resource
	private LexEvsValueSetDefinitionQueryService lexEvsValueSetDefinitionVersionQuery;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsValueSetDefinitionVersionQuery);
	}
	
//	@Test
//	@LoadContent(contentPath="classpath:content/Automobiles.xml")
//	public void testGetAllCodeSystemVersionsNotNull(){
//		assertNotNull(lexEvsValueSetDefinitionVersionQuery.getCodeSystemVersions());
//	}
	

//	@Test
////	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
//	public void testCount(){
//		ValueSetDefinitionDirectoryURI uri = lexEvsValueSetDefinitionVersionQuery.getDirectoryURIFactory().getDirectoryURI();
//		System.out.println(uri.count(null));
//	}
	
	@Test
//	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testResolve(){
		ValueSetDefinitionDirectoryURI uri = lexEvsValueSetDefinitionVersionQuery.getDirectoryURIFactory().getDirectoryURI();
		ValueSetDefinitionDirectory vsdDirectory = lexEvsValueSetDefinitionVersionQuery.resolve(uri, null, null);
		System.out.println(vsdDirectory.getEntryCount());
	}
	
//	@Test
//	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
//	public void testRestrictToEntities(){
//		CodeSystemVersionDirectoryURI uri = lexEvsValueSetDefinitionVersionQuery.getCodeSystemVersions();
//		
//		EntityReference entityReference = new EntityReference();
//		ScopedEntityName name = new ScopedEntityName();
//		name.setName("C0001");
//		entityReference.setLocalEntityName(name);
//		
//		uri = lexEvsValueSetDefinitionVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference), RestrictionType.ALL, ActiveOrAll.ACTIVE_ONLY);
//		
//		assertEquals(1,lexEvsValueSetDefinitionVersionQuery.resolve(uri, null, null).getEntryCount());
//	}
//	
//	@Test
//	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
//	public void testRestrictToEntitiesAllWithWrongEntity(){
//		CodeSystemVersionDirectoryURI uri = lexEvsValueSetDefinitionVersionQuery.getCodeSystemVersions();
//		
//		EntityReference entityReference1 = new EntityReference();
//		ScopedEntityName name1 = new ScopedEntityName();
//		name1.setName("C0001");
//		entityReference1.setLocalEntityName(name1);
//		
//		EntityReference entityReference2 = new EntityReference();
//		ScopedEntityName name2 = new ScopedEntityName();
//		name2.setName("___INVALID____");
//		entityReference2.setLocalEntityName(name2);
//		
//		uri = lexEvsValueSetDefinitionVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference1,entityReference2), RestrictionType.ALL, ActiveOrAll.ACTIVE_ONLY);
//		
//		assertEquals(0,lexEvsValueSetDefinitionVersionQuery.resolve(uri, null, null).getEntryCount());
//	}
//	
//	@Test
//	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
//	public void testRestrictToEntitiesAtLeastOneWithWrongEntity(){
//		CodeSystemVersionDirectoryURI uri = lexEvsValueSetDefinitionVersionQuery.getCodeSystemVersions();
//		
//		EntityReference entityReference1 = new EntityReference();
//		ScopedEntityName name1 = new ScopedEntityName();
//		name1.setName("C0001");
//		entityReference1.setLocalEntityName(name1);
//		
//		EntityReference entityReference2 = new EntityReference();
//		ScopedEntityName name2 = new ScopedEntityName();
//		name2.setName("___INVALID____");
//		entityReference2.setLocalEntityName(name2);
//		
//		uri = lexEvsValueSetDefinitionVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference1,entityReference2), RestrictionType.AT_LEAST_ONE, ActiveOrAll.ACTIVE_ONLY);
//		
//		assertEquals(1,lexEvsValueSetDefinitionVersionQuery.resolve(uri, null, null).getEntryCount());
//	}
//	
}
