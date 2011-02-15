package org.cts.internal.profile.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.cts.test.BaseCts2Test;
import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.internal.profile.query.LexEvsCodeSystemQuery;
import org.cts2.profile.BaseService;
import org.cts2.uri.DirectoryURI;
import org.junit.Test;

public class LexEvsCodeSystemQueryTest extends BaseCts2Test {
	
	@Resource
	private LexEvsCodeSystemQuery lexEvsCodeSystemQuery;
	
	@Resource
	private BaseService baseService;
	
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
	public void testResolveDirectoryURINotNull(){
		DirectoryURI<CodeSystemDirectory> directoryUri = lexEvsCodeSystemQuery.getAllCodeSystems();
		CodeSystemDirectory csd = directoryUri.resolve(null, null);
		assertNotNull(csd);
		assertTrue(csd.getEntryCount() > 0);
	}
}
