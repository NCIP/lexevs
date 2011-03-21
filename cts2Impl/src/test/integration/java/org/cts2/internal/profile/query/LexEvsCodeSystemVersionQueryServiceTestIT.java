package org.cts2.internal.profile.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.core.EntityReference;
import org.cts2.core.ScopedEntityName;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.junit.Test;

public class LexEvsCodeSystemVersionQueryServiceTestIT extends BaseCts2IntegrationTest {
	
	@Resource
	private LexEvsCodeSystemVersionQueryService lexEvsCodeSystemVersionQuery;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemVersionQuery);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testGetAllCodeSystemVersionsNotNull(){
		assertNotNull(lexEvsCodeSystemVersionQuery.getCodeSystemVersions());
	}
	

	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testCount(){
		CodeSystemVersionDirectoryURI uri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		assertEquals(2,lexEvsCodeSystemVersionQuery.count(uri, null));
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToEntities(){
		CodeSystemVersionDirectoryURI uri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		EntityReference entityReference = new EntityReference();
		ScopedEntityName name = new ScopedEntityName();
		name.setName("C0001");
		entityReference.setLocalEntityName(name);
		
		uri = lexEvsCodeSystemVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference), RestrictionType.ALL, ActiveOrAll.ACTIVE_ONLY);
		
		assertEquals(1,lexEvsCodeSystemVersionQuery.resolve(uri, null, null).getEntryCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToEntitiesAllWithWrongEntity(){
		CodeSystemVersionDirectoryURI uri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		EntityReference entityReference1 = new EntityReference();
		ScopedEntityName name1 = new ScopedEntityName();
		name1.setName("C0001");
		entityReference1.setLocalEntityName(name1);
		
		EntityReference entityReference2 = new EntityReference();
		ScopedEntityName name2 = new ScopedEntityName();
		name2.setName("___INVALID____");
		entityReference2.setLocalEntityName(name2);
		
		uri = lexEvsCodeSystemVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference1,entityReference2), RestrictionType.ALL, ActiveOrAll.ACTIVE_ONLY);
		
		assertEquals(0,lexEvsCodeSystemVersionQuery.resolve(uri, null, null).getEntryCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToEntitiesAtLeastOneWithWrongEntity(){
		CodeSystemVersionDirectoryURI uri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		EntityReference entityReference1 = new EntityReference();
		ScopedEntityName name1 = new ScopedEntityName();
		name1.setName("C0001");
		entityReference1.setLocalEntityName(name1);
		
		EntityReference entityReference2 = new EntityReference();
		ScopedEntityName name2 = new ScopedEntityName();
		name2.setName("___INVALID____");
		entityReference2.setLocalEntityName(name2);
		
		uri = lexEvsCodeSystemVersionQuery.restrictToEntities(uri, Arrays.asList(entityReference1,entityReference2), RestrictionType.AT_LEAST_ONE, ActiveOrAll.ACTIVE_ONLY);
		
		assertEquals(1,lexEvsCodeSystemVersionQuery.resolve(uri, null, null).getEntryCount());
	}
	
}
