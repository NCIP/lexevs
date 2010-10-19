/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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