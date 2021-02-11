
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