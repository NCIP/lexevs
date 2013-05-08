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
package util.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexgrid.loader.meta.MetaBatchLoaderImpl;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import test.rrf.BaseTestRrf;

public class LoadMetaForIntegration {
	public static String RRF_DIRECTORY = "src/test/resources/data/SAMPLEMETA";
	public static String CONFIG_FILE = "src/test/resources/config/configSingleDb.props";
	
	public static String META_URN = "urn:oid:2.16.840.1.113883.3.26.1.2";
	public static String META_VERSION = "200902_For_Test";
	
	protected static LexBIGService lbs;
	
	/**
	 * Test single db meta load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSingleDbMetaLoad() throws Exception {
		MetaBatchLoader loader = new MetaBatchLoaderImpl();
		loader.loadMeta(new File(RRF_DIRECTORY).toURI());
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.COMPLETED));	
		
		activateMetaScheme();
	}

	@BeforeClass
	public static void clean() throws Exception {
		BaseTestRrf.cleanUpBefore();
	}
	
	@Before
	public void setUpLbs() throws Exception {
		System.setProperty("LG_CONFIG_FILE", CONFIG_FILE);
		lbs = LexBIGServiceImpl.defaultInstance();	
	}
	
	protected static void activateMetaScheme() throws Exception {	
		AbsoluteCodingSchemeVersionReference ref = buildMetaAcsvr();
		
		lbs.getServiceManager(null).activateCodingSchemeVersion(ref);	
	}
	
	
	public void removeMetaScheme() throws Exception {	
		AbsoluteCodingSchemeVersionReference ref = buildMetaAcsvr();

		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, new Date());
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
	}
	
	protected static AbsoluteCodingSchemeVersionReference buildMetaAcsvr(){
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(META_URN);
		ref.setCodingSchemeVersion(META_VERSION);
		return ref;
		
	}
}