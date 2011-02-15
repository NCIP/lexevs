package org.cts.internal.profile.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts.test.BaseCts2Test;
import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.internal.profile.query.LexEvsCodeSystemQuery;
import org.cts2.internal.uri.factory.CodeSystemDirectoryURIFactory;
import org.cts2.profile.BaseService;
import org.cts2.uri.DirectoryURI;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class LexEvsCodeSystemQueryTest extends BaseCts2Test {
	
	@Resource
	private LexEvsCodeSystemQuery lexEvsCodeSystemQuery;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private CodeSystemDirectoryURIFactory codeSystemDirectoryURIFactory;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemQuery);
	}
	
	@Test
	public void testIsAvailableThroughBaseService(){
		assertNotNull(baseService.getQueryService().getCodeSystemQuery());
	}
	
	@Test
	public void testGetAllCodeSystemsNotNull(){
		assertNotNull(lexEvsCodeSystemQuery.getAllCodeSystems());
	}
	
	@Test
	public void testResolveDirectoryURINotNull() throws LBInvocationException{
		LexBIGService lbs = EasyMock.createMock(LexBIGService.class);
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		csr.setCodingSchemeSummary(new CodingSchemeSummary());
		csr.getCodingSchemeSummary().setCodingSchemeURI("testURI");
		csrl.addCodingSchemeRendering(csr);
		
		EasyMock.expect(lbs.getSupportedCodingSchemes()).andReturn(csrl).anyTimes();
		EasyMock.replay(lbs);
		
		lexEvsCodeSystemQuery.setLexBigService(lbs);
		codeSystemDirectoryURIFactory.setLexBigService(lbs);
		
		DirectoryURI<CodeSystemDirectory> directoryUri = lexEvsCodeSystemQuery.getAllCodeSystems();
		CodeSystemDirectory csd = directoryUri.resolve(null, null);
		assertNotNull(csd);
		assertTrue(csd.getEntryCount() > 0);
	}
}
