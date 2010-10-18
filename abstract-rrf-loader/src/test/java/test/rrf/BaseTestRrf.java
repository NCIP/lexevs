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
package test.rrf;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.ResourceManager;
import org.lexgrid.loader.test.util.LoaderTestUtils;

/**
 * The Class BaseTestRrf.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseTestRrf {

	/** The Constant RRF_DIRECTORY. */
	protected static final String RRF_DIRECTORY = "src/test/resources/data/sample-air";
	
	/** The Constant RRF_FAIL_DIRECTORY. */
	protected static final String RRF_FAIL_DIRECTORY = "src/test/resources/data/sample-airFAIL";
	
	/**
	 * Clean up before.
	 */
	@BeforeClass
	public static void cleanUpBefore(){
		LoaderTestUtils.cleanUpAll();
		LoaderTestUtils.cleanUpDatabase();
	}
	
	/**
	 * Clean up after.
	 */
	@AfterClass
	public static void cleanUpAfter(){
		LoaderTestUtils.cleanUpAll();
		LoaderTestUtils.cleanUpDatabase();
	}
	
	/**
	 * Clean up after test.
	 * 
	 * @throws Exception the exception
	 */
	@After
	public void cleanUpAfterTest() throws Exception {
		//no-op for now
	}
	
	/**
	 * Clean up before test.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void cleanUpBeforeTest() throws Exception {
		//no-op for now
	}

	/**
	 * Removes the meta scheme.
	 * 
	 * @throws Exception the exception
	 */
	public void removeMetaScheme() throws Exception {	
		LexEvsServiceLocator.getInstance().getSystemResourceService().
			removeCodingSchemeResourceFromSystem("urn:oid:2.16.840.1.113883.3.26.1.2", "2005AC");
	}
	

	/**
	 * Removes the air scheme.
	 * 
	 * @throws Exception the exception
	 */
	public void removeAIRScheme() throws Exception {	
		LexEvsServiceLocator.getInstance().getSystemResourceService().
			removeCodingSchemeResourceFromSystem("urn:oid:2.16.840.1.113883.6.110", "1993.bvt");
	}	
	
	
}