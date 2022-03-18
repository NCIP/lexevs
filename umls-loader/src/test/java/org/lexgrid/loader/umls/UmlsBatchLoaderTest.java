
package org.lexgrid.loader.umls;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import test.rrf.BaseTestRrf;
import util.integration.LoadUmlsForIntegration;

/**
 * The Class UmlsBatchLoaderTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsBatchLoaderTest extends BaseTestRrf {

	@Before
	public void setUp() {
		System.setProperty("LG_CONFIG_FILE", LoadUmlsForIntegration.CONFIG_FILE);
	}
	
	/**
	 * Test single db umls load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testUmlsLoad() throws Exception {
		UmlsBatchLoader loader = new UmlsBatchLoaderImpl();
		loader.getOptions().getStringOption(UmlsBatchLoaderImpl.SAB_OPTION).setOptionValue(LoadUmlsForIntegration.UMLS_SAB);
		loader.getOptions().getBooleanOption(UmlsBatchLoaderImpl.ASYNC_OPTION).setOptionValue(false);
		loader.load(new File(RRF_DIRECTORY).toURI());
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.COMPLETED));	
		removeAIRScheme();
	}
	
	@Test
	public void testUmlsMultipleSABsLoad() throws Exception {
		UmlsBatchLoader loader = new UmlsBatchLoaderImpl();
		loader.getOptions().getStringOption(UmlsBatchLoaderImpl.SAB_OPTION).setOptionValue("RCD");
		loader.getOptions().getBooleanOption(UmlsBatchLoaderImpl.ASYNC_OPTION).setOptionValue(false);
		loader.load(new File(RRF_DIRECTORY).toURI());
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.COMPLETED));
		removeRCDScheme();
	}
	
	/**
	 * Test single db failed umls load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testFailedUmlsLoad() throws Exception {
		System.setProperty("LG_CONFIG_FILE", LoadUmlsForIntegration.CONFIG_FILE);
		UmlsBatchLoader loader = new UmlsBatchLoaderImpl();
		loader.getOptions().getStringOption(UmlsBatchLoaderImpl.SAB_OPTION).setOptionValue(LoadUmlsForIntegration.UMLS_SAB);
		loader.getOptions().getBooleanOption(UmlsBatchLoaderImpl.ASYNC_OPTION).setOptionValue(false);
		loader.load(new File(RRF_FAIL_DIRECTORY).toURI());
		
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.FAILED));

		loader.removeLoad(LoadUmlsForIntegration.UMLS_URN, LoadUmlsForIntegration.UMLS_VERSION);
	}	
	
	/**
	 * Test single db restart umls load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testRestartUmlsLoad() throws Exception {
		System.setProperty("LG_CONFIG_FILE", LoadUmlsForIntegration.CONFIG_FILE);
		
		UmlsBatchLoader loader = new UmlsBatchLoaderImpl();
		loader.getOptions().getStringOption(UmlsBatchLoaderImpl.SAB_OPTION).setOptionValue(LoadUmlsForIntegration.UMLS_SAB);
		loader.getOptions().getBooleanOption(UmlsBatchLoaderImpl.ASYNC_OPTION).setOptionValue(false);
		loader.load(new File(RRF_FAIL_DIRECTORY).toURI());
		
		JobExecution job = loader.getJobExecution();
		assertTrue(job.getExitStatus().equals(ExitStatus.FAILED));	

		UmlsBatchLoader restartLoader = new UmlsBatchLoaderImpl();
		restartLoader.resumeUmls(new File(RRF_DIRECTORY).toURI(), LoadUmlsForIntegration.UMLS_SAB, LoadUmlsForIntegration.UMLS_URN, LoadUmlsForIntegration.UMLS_VERSION);
		JobExecution restartJob = restartLoader.getJobExecution();
		assertTrue(restartJob.getExitStatus().equals(ExitStatus.COMPLETED));

		removeAIRScheme();
	}
	
	public void removeRCDScheme() throws Exception {	
		LexEvsServiceLocator.getInstance().getSystemResourceService().
			removeCodingSchemeResourceFromSystem("urn:oid:2.16.840.1.113883.6.6", "1999");
	}	
}