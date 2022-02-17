
package org.lexgrid.loader.meta.processor.support;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.lexgrid.loader.meta.staging.processor.MetaMrconsoStagingDao;
import static org.junit.Assert.*;

public class MetaRootNodeResolverTest {

	@Test
	public void testGoodRootNode(){
		MetaRootNodeResolver resolver = new MetaRootNodeResolver();
		
		MetaMrconsoStagingDao dao = createMock(MetaMrconsoStagingDao.class);  
		
		List<String> rootCuis = Arrays.asList(new String[]{"C1","C2"});
		
		expect(dao.getMetaRootCuis()).andReturn(rootCuis);  
		replay(dao);  
		
		resolver.setMetaMrconsoStagingDao(dao);
		
		assertTrue(resolver.isSourceRootNode("C1"));
	}
	
	@Test
	public void testBadRootNode(){
		MetaRootNodeResolver resolver = new MetaRootNodeResolver();
		
		MetaMrconsoStagingDao dao = createMock(MetaMrconsoStagingDao.class);  
		
		List<String> rootCuis = Arrays.asList(new String[]{"C1","C2"});
		
		expect(dao.getMetaRootCuis()).andReturn(rootCuis);  
		replay(dao);  
		
		resolver.setMetaMrconsoStagingDao(dao);
		
		assertFalse(resolver.isSourceRootNode("C3"));
	}
}