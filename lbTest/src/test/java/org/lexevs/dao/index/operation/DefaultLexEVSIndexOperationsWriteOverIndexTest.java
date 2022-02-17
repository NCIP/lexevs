
package org.lexevs.dao.index.operation;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.function.TestUtil;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.admin.LoadLgXML;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class DefaultLexEVSIndexOperationsWriteOverIndexTest {
	static AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
	static String prefix;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtil.removeAll();

		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/Automobiles.xml").toURI(),
				true, true);
		System.out.println("Original Load started");
		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		
		reference.setCodingSchemeURN("urn:oid:11.11.0.1");
		reference.setCodingSchemeVersion("1.0");
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		RegistryEntry entry = registry.getEntriesForUri("urn:oid:11.11.0.1").get(0);
		prefix = entry.getPrefix();
		LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().dropCodingSchemeTablesByPrefix("lb" + prefix);
		LexEvsServiceLocator.getInstance().getRegistry().removeEntry(entry);
		System.out.println("Automobiles removed from db");
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		lbsm.removeCodingSchemeVersion(reference);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIndexDeletionAndOverwrite() throws InterruptedException, IOException, LBException {
		
			String separator = System.getProperty("file.separator");
			String classpath = System.getProperty("java.class.path");
			String path = System.getProperty("java.home")
		                + separator + "bin" + separator + "java";
			ProcessBuilder processBuilder = 
		                new ProcessBuilder(path, "-cp", 
		                classpath, 
		                LoadLgXML.class.getName(), "-in", "resources/testData/Automobiles.xml" ).inheritIO();
			System.out.println("New Automobiles load started");
			Process process = processBuilder.start();
			process.waitFor();
			System.out.println("Attempted load complete");
			assertFalse(CleanUpUtility.listUnusedIndexes().length > 0);
			//Give the db time to release the table lock before tear down
			Thread.sleep(5000);
			
	}

}