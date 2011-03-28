package org.cts2.internal.profile.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.association.AssociationDirectory;
import org.cts2.internal.model.uri.factory.AssociationDirectoryURIFactory;
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
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolve() {
	QueryControl queryControl = new QueryControl();
	queryControl.setTimeLimit(Long.valueOf("1000"));
	ReadContext readContext = null;
	AssociationDirectoryURI associationQueryURI = lexEvsAssociationQueryService.getAssociations();
	AssociationDirectory associationDirectory = lexEvsAssociationQueryService.resolve(associationQueryURI, queryControl , readContext);
	assertTrue(associationDirectory.getEntryCount()>0);
	}

	@Test
	public void testResolveAsList() {
		fail("Not yet implemented");
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
	public void testRestrictToCodeSystemVersion() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestrictToPredicate() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestrictToSourceEntity() {
		fail("Not yet implemented");
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
