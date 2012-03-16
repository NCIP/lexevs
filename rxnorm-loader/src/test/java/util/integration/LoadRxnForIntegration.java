package util.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexgrid.loader.rxn.RxnBatchLoaderImpl;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import test.rrf.BaseTestRrf;

public class LoadRxnForIntegration {
	public static String RRF_DIRECTORY = "src/test/resources/data/sample-air";
	public static String CONFIG_FILE = "src/test/resources/config/configSingleDb.props";
	
	public static String RXN_URN = "urn:oid:2.16.840.1.113883.6.110";
	public static String RXN_VERSION = "1993.bvt";
	public static String RXN_SAB = "AIR";
	
	protected static LexBIGService lbs;
	
	/**
	 * Test single db meta load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSingleDbRxnLoad() throws Exception {
		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
		loader.loadRxn(new File(RRF_DIRECTORY).toURI(), RXN_SAB);
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
		ref.setCodingSchemeURN(RXN_URN);
		ref.setCodingSchemeVersion(RXN_VERSION);
		return ref;
		
	}
}
