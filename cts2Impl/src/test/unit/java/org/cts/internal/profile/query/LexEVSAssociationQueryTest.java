package org.cts.internal.profile.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts.test.BaseCts2UnitTest;
import org.cts2.association.AssociationDirectory;
import org.cts2.internal.model.uri.factory.CodeSystemDirectoryURIFactory;
import org.cts2.internal.profile.query.LexEvsAssociationQueryService;
import org.cts2.profile.BaseService;
import org.cts2.uri.AssociationDirectoryURI;
import org.easymock.classextension.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

public class LexEVSAssociationQueryTest extends BaseCts2UnitTest {
	
	@Resource
	private LexEvsAssociationQueryService associationQuery;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private CodeSystemDirectoryURIFactory codeSystemDirectoryURIFactory;
	
	@Test
	public void testInit(){
		assertNotNull(associationQuery);
	}
	
	@Test
	public void testIsAvailableThroughBaseService(){
		assertNotNull(baseService.getQueryService().getCodeSystemQueryService());
	}
	
	@Test
	@Ignore
	public void testResolveDirectoryURINotNull() throws LBInvocationException{
		LexBIGService lbs = EasyMock.createMock(LexBIGService.class);
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		csr.setCodingSchemeSummary(new CodingSchemeSummary());
		csr.getCodingSchemeSummary().setCodingSchemeURI("testURI");
		csrl.addCodingSchemeRendering(csr);
		
		EasyMock.expect(lbs.getSupportedCodingSchemes()).andReturn(csrl).anyTimes();
		EasyMock.replay(lbs);
		
		associationQuery.setLexBigService(lbs);
		codeSystemDirectoryURIFactory.setLexBigService(lbs);
		
		AssociationDirectoryURI directoryUri = associationQuery.getAllAssociations();
		AssociationDirectory ad = directoryUri.get(null, null, AssociationDirectory.class);
		assertNotNull(ad);
		assertTrue(ad.getEntryCount() > 0);
	}

}
