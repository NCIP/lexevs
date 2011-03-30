package org.cts2.internal.profile.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.ScopedEntityName;
import org.cts2.internal.model.uri.factory.AssociationDirectoryURIFactory;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.uri.AssociationDirectoryURI;
import org.junit.Test;

public class LexEvsAssociationQueryServiceTestIT extends
		BaseCts2IntegrationTest {
	
	@Resource
	private LexEvsAssociationQueryService lexEvsAssociationQueryService;
	
	@Resource
	AssociationDirectoryURIFactory associationDirectoryURIFactory;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsAssociationQueryService);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testResolve() {
	QueryControl queryControl = new QueryControl();
	ReadContext readContext = null;
	AssociationDirectoryURI associationQueryURI = lexEvsAssociationQueryService.getAssociations();
	AssociationDirectory associationDirectory = lexEvsAssociationQueryService.resolve(associationQueryURI, queryControl , readContext);
	assertTrue(associationDirectory.getEntryCount()>0);
	}

	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testResolveAsList() {
		QueryControl queryControl = new QueryControl();
		ReadContext readContext = null;
		AssociationDirectoryURI associationQueryURI = lexEvsAssociationQueryService.getAssociations();
		AssociationList associationList = lexEvsAssociationQueryService.resolveAsList(associationQueryURI, queryControl , readContext);
		assertTrue(associationList.getEntryCount()>0);
	}

	@Test
	public void testGetAllSourceAndTargetEntities() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPredicates() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSourceEntities() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTargetEntities() {
		fail("Not yet implemented");
	}

	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToCodeSystemVersion() {
		AssociationDirectoryURI associationQueryURI = lexEvsAssociationQueryService.getAssociations();
		NameOrURI version = new NameOrURI();
		version.setName("Automobiles:1.0");
		//version.setUri("urn:oid:11.11.0.1");
		lexEvsAssociationQueryService.restrictToCodeSystemVersion(associationQueryURI, version);
		ReadContext readContext = null;
		QueryControl queryControl = new QueryControl();
		AssociationList list = lexEvsAssociationQueryService.resolveAsList(associationQueryURI, queryControl , readContext);
		assertTrue(list.getEntryCount()>0);
	}

	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToPredicate() {
		AssociationDirectoryURI associationQueryURI = lexEvsAssociationQueryService.getAssociations();
		//NameOrURI version = new NameOrURI();
		EntityNameOrURI predicate = new EntityNameOrURI();
		//version.setName("Automobiles");
		ScopedEntityName name = new ScopedEntityName();
		name.setName("hasSubtype");
		predicate.setEntityName(name);
		//lexEvsAssociationQueryService.restrictToCodeSystemVersion(associationQueryURI, version);
		lexEvsAssociationQueryService.restrictToPredicate(associationQueryURI, predicate);
		ReadContext readContext = null;
		QueryControl queryControl = new QueryControl();
		AssociationList associationList = lexEvsAssociationQueryService.resolveAsList(associationQueryURI, queryControl , readContext);
		assertTrue(associationList.getNumEntries()>0);
	}

	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml,classpath:content/German_Made_Parts.xml")
	public void testRestrictToSourceEntity() {
			AssociationDirectoryURI directory = lexEvsAssociationQueryService.getAssociations();
			EntityNameOrURI sourceEntity = new EntityNameOrURI();
			lexEvsAssociationQueryService.restrictToSourceEntity(directory, sourceEntity);
			ReadContext readContext = null;
			QueryControl queryControl = new QueryControl();
			AssociationList associationList = lexEvsAssociationQueryService.resolveAsList(directory, queryControl , readContext);
			assertTrue(associationList.getNumEntries()>0);
	}

	@Test
	public void testRestrictToSourceOrTargetEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestrictToTargetEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestrictToTargetExpression() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestrictToTargetLiteral() {
		fail("Not yet implemented");
	}

}
