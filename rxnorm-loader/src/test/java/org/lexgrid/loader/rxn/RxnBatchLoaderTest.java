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
package org.lexgrid.loader.rxn;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rxn.RxnBatchLoaderImpl;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import test.rrf.BaseTestRrf;
import util.integration.LoadRxnForIntegration;

/**
 * The Class RxnBatchLoaderTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnBatchLoaderTest extends BaseTestRrf {

	@Before
	public void setUp() {
		System.setProperty("LG_CONFIG_FILE", LoadRxnForIntegration.CONFIG_FILE);
	}
	
	/**
	 * Test single db rxn load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testRxnLoad() throws Exception {
		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
		loader.loadRxn(new File("src/test/resources/data/rrf").toURI(), "RXNORM");
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.COMPLETED));	
		removeAIRScheme();
	}	


	/**
	 * Test single db failed rxn load.
	 * 
	 * @throws Exception the exception
	 */
//	@Test
//	public void testFailedRxnLoad() throws Exception {
//		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
//		loader.loadRxn(new File(RRF_FAIL_DIRECTORY).toURI(), LoadRxnForIntegration.RXN_SAB);
//		JobExecution job = loader.getJobExecution();
//		assertTrue(job.getExitStatus().equals(ExitStatus.FAILED));
//
//		loader.removeLoad(new File(RRF_FAIL_DIRECTORY).toURI(), LoadRxnForIntegration.RXN_SAB, LoadRxnForIntegration.RXN_URN, LoadRxnForIntegration.RXN_VERSION);
//	}	
	
	/**
	 * Test single db restart rxn load.
	 * 
	 * @throws Exception the exception
	 */
//	@Test
//	public void testRestartRxnLoad() throws Exception {
//		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
//		loader.loadRxn(new File(RRF_FAIL_DIRECTORY).toURI(), LoadRxnForIntegration.RXN_SAB);
//		JobExecution job = loader.getJobExecution();
//		assertTrue(job.getExitStatus().equals(ExitStatus.FAILED));	
//
//		RxnBatchLoaderImpl restartLoader = new RxnBatchLoaderImpl();
//		restartLoader.resumeRxn(new File(RRF_DIRECTORY).toURI(), LoadRxnForIntegration.RXN_SAB, LoadRxnForIntegration.RXN_URN, LoadRxnForIntegration.RXN_VERSION);
//		JobExecution restartJob = restartLoader.getJobExecution();
//		assertTrue(restartJob.getExitStatus().equals(ExitStatus.COMPLETED));
//
//		removeAIRScheme();
//	}	
}
