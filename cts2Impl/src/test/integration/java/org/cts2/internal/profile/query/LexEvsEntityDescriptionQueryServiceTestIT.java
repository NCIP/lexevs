package org.cts2.internal.profile.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.constant.ExternalCts2Constants;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PropertyReference;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
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
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveToList(){
		EntityDirectoryURI uri = 
			lexEvsEntityDescriptionQueryService.getEntities();
		
		EntityList list = 
			lexEvsEntityDescriptionQueryService.resolveAsList(uri, null, null);
		
		assertEquals(19,list.getEntryCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testRestrictToEntityDescription(){
		EntityDirectoryURI uri = 
			lexEvsEntityDescriptionQueryService.getEntities();
		Filter filter = new Filter();
		
		FilterComponent filterComponent = new FilterComponent();
		filterComponent.setMatchValue("automobile");
		
		MatchAlgorithmReference matchRef = new MatchAlgorithmReference();
		matchRef.setContent("LuceneQuery");
		
		filterComponent.setMatchAlgorithm(matchRef);
		
		PropertyReference ref = new PropertyReference();
		ref.setReferenceType(TargetReferenceType.ATTRIBUTE);
		
		NameOrURI nameOrUri = new NameOrURI();
		nameOrUri.setUri(ExternalCts2Constants.ENTITY_DESCRIPTION_DESIGNATION_URI);
		ref.setReferenceTarget(nameOrUri);
		
		filterComponent.setFilterComponent(ref);
		
		filter.addComponent(filterComponent);
		
		uri = lexEvsEntityDescriptionQueryService.restrict(uri, filter);
		
		EntityDirectory ed = lexEvsEntityDescriptionQueryService.resolve(uri, null, null);
		
		assertEquals(1,ed.getEntryCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testUnion(){
		NameOrURI nameOrUri = new NameOrURI();
		nameOrUri.setUri(ExternalCts2Constants.ENTITY_DESCRIPTION_DESIGNATION_URI);
		
		EntityDirectoryURI uri1 = 
			lexEvsEntityDescriptionQueryService.getEntities();
		Filter filter1 = new Filter();
		
		FilterComponent filterComponent1 = new FilterComponent();
		filterComponent1.setMatchValue("automobile");
		
		MatchAlgorithmReference matchRef1 = new MatchAlgorithmReference();
		matchRef1.setContent("LuceneQuery");
		
		filterComponent1.setMatchAlgorithm(matchRef1);
		
		PropertyReference ref1 = new PropertyReference();
		ref1.setReferenceType(TargetReferenceType.ATTRIBUTE);
		
		
		ref1.setReferenceTarget(nameOrUri);
		
		filterComponent1.setFilterComponent(ref1);
		
		filter1.addComponent(filterComponent1);
		
		uri1 = lexEvsEntityDescriptionQueryService.restrict(uri1, filter1);
		
		EntityDirectoryURI uri2 = 
			lexEvsEntityDescriptionQueryService.getEntities();
		Filter filter2 = new Filter();
		
		FilterComponent filterComponent2 = new FilterComponent();
		filterComponent2.setMatchValue("ford");
		
		MatchAlgorithmReference matchRef2 = new MatchAlgorithmReference();
		matchRef2.setContent("LuceneQuery");
		
		filterComponent2.setMatchAlgorithm(matchRef2);
		
		PropertyReference ref2 = new PropertyReference();
		ref2.setReferenceType(TargetReferenceType.ATTRIBUTE);
		
		ref2.setReferenceTarget(nameOrUri);
		
		filterComponent2.setFilterComponent(ref2);
		
		filter2.addComponent(filterComponent2);
		
		uri2 = lexEvsEntityDescriptionQueryService.restrict(uri2, filter2);
		
		EntityDirectoryURI uri3 = lexEvsEntityDescriptionQueryService.union(uri1, uri2);
		
		EntityDirectory ed = lexEvsEntityDescriptionQueryService.resolve(uri3, null, null);
		
		assertEquals(2,ed.getEntryCount());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testIntersect(){
		NameOrURI nameOrUri = new NameOrURI();
		nameOrUri.setUri(ExternalCts2Constants.ENTITY_DESCRIPTION_DESIGNATION_URI);

		EntityDirectoryURI uri1 = 
			lexEvsEntityDescriptionQueryService.getEntities();
		Filter filter1 = new Filter();
		
		FilterComponent filterComponent1 = new FilterComponent();
		filterComponent1.setMatchValue("automobile");
		
		MatchAlgorithmReference matchRef1 = new MatchAlgorithmReference();
		matchRef1.setContent("LuceneQuery");
		
		filterComponent1.setMatchAlgorithm(matchRef1);
		
		PropertyReference ref1 = new PropertyReference();
		ref1.setReferenceType(TargetReferenceType.ATTRIBUTE);
		
		ref1.setReferenceTarget(nameOrUri);
		
		filterComponent1.setFilterComponent(ref1);
		
		filter1.addComponent(filterComponent1);
		
		uri1 = lexEvsEntityDescriptionQueryService.restrict(uri1, filter1);
		
		EntityDirectoryURI uri2 = 
			lexEvsEntityDescriptionQueryService.getEntities();
		Filter filter2 = new Filter();
		
		FilterComponent filterComponent2 = new FilterComponent();
		filterComponent2.setMatchValue("*a*");
		
		MatchAlgorithmReference matchRef2 = new MatchAlgorithmReference();
		matchRef2.setContent("LuceneQuery");
		
		filterComponent2.setMatchAlgorithm(matchRef2);
		
		PropertyReference ref2 = new PropertyReference();
		ref2.setReferenceType(TargetReferenceType.ATTRIBUTE);
		
		ref2.setReferenceTarget(nameOrUri);
		
		filterComponent2.setFilterComponent(ref2);
		
		filter2.addComponent(filterComponent2);
		
		uri2 = lexEvsEntityDescriptionQueryService.restrict(uri2, filter2);
		
		EntityDirectoryURI uri3 = lexEvsEntityDescriptionQueryService.intersect(uri1, uri2);
		
		EntityDirectory ed = lexEvsEntityDescriptionQueryService.resolve(uri3, null, null);
		
		assertEquals(1,ed.getEntryCount());
	}
}
