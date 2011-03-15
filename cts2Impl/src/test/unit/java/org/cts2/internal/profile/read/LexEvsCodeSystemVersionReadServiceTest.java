package org.cts2.internal.profile.read;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.internal.model.resource.factory.CodeSystemVersionFactory;
import org.cts2.internal.model.uri.factory.CodeSystemDirectoryURIFactory;
import org.cts2.internal.profile.read.LexEvsCodeSystemVersionReadService;
import org.cts2.profile.BaseService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.test.BaseCts2UnitTest;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class LexEvsCodeSystemVersionReadServiceTest extends BaseCts2UnitTest {
	
	@Resource
	private LexEvsCodeSystemVersionReadService lexEvsCodeSystemVersionReadService;
	
	@Resource
	private BaseService baseService;
	
	@Resource
	private CodeSystemDirectoryURIFactory codeSystemDirectoryURIFactory;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemVersionReadService);
	}
	
	@Test
	public void testIsAvailableThroughBaseService(){
		assertNotNull(baseService.getQueryService().getCodeSystemQueryService());
	}
	
	@Test
	public void testResolveDirectoryURINotNull() throws LBInvocationException{
		
		CodeSystemVersionFactory factory = 
			EasyMock.createMock(CodeSystemVersionFactory.class);
		
		NameOrURI id = new NameOrURI();
		
		EasyMock.expect(factory.getCodeSystemVersion(id)).andReturn(new CodeSystemVersion());
		
		
		EasyMock.replay(factory);
		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		assertNotNull(
				lexEvsCodeSystemVersionReadService.read(id, null, null));
	}
	
	@Test
	public void Get_correct_About_CodeSystemVersion() throws LBInvocationException{
		
		CodeSystemVersionFactory factory = 
			EasyMock.createMock(CodeSystemVersionFactory.class);
		
		NameOrURI id = new NameOrURI();
		
		CodeSystemVersion csv = new CodeSystemVersion();
		csv.setAbout("test_about");
		
		EasyMock.expect(factory.getCodeSystemVersion(id)).andReturn(csv);
		
		
		EasyMock.replay(factory);
		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		CodeSystemVersion returned =
				lexEvsCodeSystemVersionReadService.read(id, null, null);
		
		assertEquals("test_about", returned.getAbout());
	}
	
	@Test
	public void Test_Read_CodeSystemVersion_By_Name() throws LBInvocationException{
		
		CodeSystemVersionFactory factory = 
			EasyMock.createMock(CodeSystemVersionFactory.class);
		
		NameOrURI id = new NameOrURI();
		
		CodeSystemVersion csv = new CodeSystemVersion();
		csv.setCodeSystemVersionName("test:name");
		csv.setDocumentURI("doc:uri");
		
		EasyMock.expect(factory.getCodeSystemVersion(id)).andReturn(csv);
		
		
		EasyMock.replay(factory);
		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		CodeSystemVersion returned =
				lexEvsCodeSystemVersionReadService.read(id, null, null);
		
		assertEquals("test:name", returned.getCodeSystemVersionName());
		assertEquals("doc:uri", returned.getDocumentURI());
	}
	
	@Test
	public void Test_Read_CodeSystemVersion_By_DocumentURI() throws LBInvocationException{
		
		CodeSystemVersionFactory factory = 
			EasyMock.createMock(CodeSystemVersionFactory.class);
		
		NameOrURI id = new NameOrURI();
		id.setName("test:name");
		
		CodeSystemVersion csv = new CodeSystemVersion();
		csv.setCodeSystemVersionName("test:name");
		csv.setDocumentURI("doc:uri");
		
		EasyMock.expect(factory.getCodeSystemVersion(id)).andReturn(csv);
		
		
		EasyMock.replay(factory);
		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		CodeSystemVersion returned =
				lexEvsCodeSystemVersionReadService.read(id, null, null);
		
		assertEquals("test:name", returned.getCodeSystemVersionName());
		assertEquals("doc:uri", returned.getDocumentURI());
	}

	@Test(expected=Exception.class)
	public void Test_Exceed_Timeout() throws LBInvocationException{
		
		final CodeSystemVersion csv = new CodeSystemVersion();
		csv.setAbout("test_about");
		
		CodeSystemVersionFactory factory = 
			new CodeSystemVersionFactory(){

				@Override
				public CodeSystemVersion getCodeSystemVersion(
						NameOrURI nameOrUri) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
					return csv;
				}
			
		};
		
		NameOrURI id = new NameOrURI();
		QueryControl queryControl = new QueryControl();
		queryControl.setTimeLimit(50l);

		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		CodeSystemVersion returned =
				lexEvsCodeSystemVersionReadService.read(id, queryControl, null);
	}
	
	@Test
	public void Test_Exceed_Timeout_Not_Exceeded() throws LBInvocationException{
		
		final CodeSystemVersion csv = new CodeSystemVersion();
		csv.setAbout("test_about");
		
		CodeSystemVersionFactory factory = 
			new CodeSystemVersionFactory(){

				@Override
				public CodeSystemVersion getCodeSystemVersion(
						NameOrURI nameOrUri) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
					return csv;
				}
			
		};
		
		NameOrURI id = new NameOrURI();
		QueryControl queryControl = new QueryControl();
		queryControl.setTimeLimit(2000l);

		lexEvsCodeSystemVersionReadService.setCodeSystemVersionFactory(factory);
		
		CodeSystemVersion returned =
				lexEvsCodeSystemVersionReadService.read(id, queryControl, null);
		
		assertEquals("test_about", returned.getAbout());
	}
}
