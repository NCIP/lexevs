/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.prefix;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.hibernate.registry.HibernateRegistryDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class DefaultPrefixResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDaoPrefixResolver-test.xml"})
public class DefaultPrefixResolverTest extends LexEvsDbUnitTestBase {

	/** The default prefix resolver. */
	@Autowired
	protected DefaultPrefixResolver defaultPrefixResolver;
	
	/** The hibernate registry dao. */
	@Autowired
	protected HibernateRegistryDao hibernateRegistryDao;

	/**
	 * Test get prefix not null.
	 */
	@Test
	public void testGetPrefixNotNull(){
		hibernateRegistryDao.initRegistryMetadata();
		String prefix = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotNull(prefix);
	}
	
	/**
	 * Test get next prefix.
	 */
	@Test
	public void testGetNextPrefix(){
		hibernateRegistryDao.initRegistryMetadata();
		String prefix1 = defaultPrefixResolver.getNextCodingSchemePrefix();
		String prefix2 = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotSame(prefix1, prefix2);
	}
}
