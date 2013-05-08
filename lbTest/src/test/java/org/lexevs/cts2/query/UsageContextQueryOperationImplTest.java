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
package org.lexevs.cts2.query;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;

/**
 * @author m004181
 *
 */
public class UsageContextQueryOperationImplTest {
	private static UsageContextQueryOperation UC_QUERY_OP; 
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		UC_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getUsageContextQueryOperation();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		UC_QUERY_OP = null;
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#getUsageContextCodedNodeSet(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetUsageContextCodedNodeSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#getUsageContextCodingScheme(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetUsageContextCodingScheme() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#getUsageContextEntitisWithName(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetUsageContextEntitisWithName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#getUsageContextEntity(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetUsageContextEntity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#listAllUsageContextEntities(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testListAllUsageContextEntities() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.UsageContextQueryOperationImpl#listAllUsageContextIds(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testListAllUsageContextIds() {
		fail("Not yet implemented");
	}

}