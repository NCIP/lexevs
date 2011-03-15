package org.cts2.internal.profile.query;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.codesystemversion.CodeSystemVersionDirectory;
import org.cts2.internal.model.uri.factory.CodeSystemVersionDirectoryURIFactory;
import org.cts2.internal.profile.query.LexEvsCodeSystemVersionQueryService;
import org.cts2.profile.BaseService;
import org.cts2.service.core.QueryControl;
import org.cts2.test.BaseCts2UnitTest;
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

		CodeSystemVersionDirectory directory = lexEvsCodeSystemVersionQuery.resolve(directoryUri, null, null);
		
		assertNotNull(directory);
	}
	
	public static class TimeoutCodingSchemeRendering extends CodingSchemeRendering{

		private static final long serialVersionUID = 1L;

		@Override
		public CodingSchemeSummary getCodingSchemeSummary() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			return null;
		}
	};
	
	@Test(expected=Exception.class)
	public void testResolveDirectoryWithTimeout() throws LBInvocationException{
		LexBIGService lbs = EasyMock.createMock(LexBIGService.class);
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new TimeoutCodingSchemeRendering();
		
		csrl.addCodingSchemeRendering(csr);
		
		EasyMock.expect(lbs.getSupportedCodingSchemes()).andReturn(csrl).anyTimes();
		EasyMock.replay(lbs);
		
		lexEvsCodeSystemVersionQuery.setLexBigService(lbs);
		codeSystemVersonDirectoryURIFactory.setLexBigService(lbs);
		
		CodeSystemVersionDirectoryURI directoryUri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		QueryControl queryControl = new QueryControl();
		queryControl.setTimeLimit(100l);

		lexEvsCodeSystemVersionQuery.resolve(directoryUri, queryControl, null);
	}
	
	@Test(expected=Exception.class)
	public void testResolveDirectoryBeforeTimeout() throws LBInvocationException{
		LexBIGService lbs = EasyMock.createMock(LexBIGService.class);
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new TimeoutCodingSchemeRendering();
		
		csrl.addCodingSchemeRendering(csr);
		
		EasyMock.expect(lbs.getSupportedCodingSchemes()).andReturn(csrl).anyTimes();
		EasyMock.replay(lbs);
		
		lexEvsCodeSystemVersionQuery.setLexBigService(lbs);
		codeSystemVersonDirectoryURIFactory.setLexBigService(lbs);
		
		CodeSystemVersionDirectoryURI directoryUri = lexEvsCodeSystemVersionQuery.getCodeSystemVersions();
		
		QueryControl queryControl = new QueryControl();
		queryControl.setTimeLimit(600l);

		CodeSystemVersionDirectory directory = lexEvsCodeSystemVersionQuery.resolve(directoryUri, queryControl, null);
		
		assertNotNull(directory);
	}
}
