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
import org.cts2.codesystemversion.CodeSystemVersionDirectory;
import org.cts2.internal.model.uri.factory.CodeSystemVersionDirectoryURIFactory;
import org.cts2.internal.profile.query.LexEvsCodeSystemVersionQueryService;
import org.cts2.profile.BaseService;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class LexEvsCodeSystemVersionQueryTest extends BaseCts2UnitTest {
	
	@Resource
	private LexEvsCodeSystemVersionQueryService lexEvsCodeSystemVersionQuery;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private CodeSystemVersionDirectoryURIFactory codeSystemVersonDirectoryURIFactory;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemVersionQuery);
	}
	
	@Test
	public void testIsAvailableThroughBaseService(){
		assertNotNull(baseService.getQueryService().getCodeSystemQueryService());
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
		
		lexEvsCodeSystemVersionQuery.setLexBigService(lbs);
		codeSystemVersonDirectoryURIFactory.setLexBigService(lbs);
		
		CodeSystemVersionDirectoryURI directoryUri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		CodeSystemVersionDirectory csvd = directoryUri.get(null, null, CodeSystemVersionDirectory.class);
		assertNotNull(csvd);
		assertTrue(csvd.getEntryCount() > 0);

		CodeSystemVersionDirectory directory = lexEvsCodeSystemVersionQuery.resolve(directoryUri, null, null);
		
		assertNotNull(directory);
	}
}
