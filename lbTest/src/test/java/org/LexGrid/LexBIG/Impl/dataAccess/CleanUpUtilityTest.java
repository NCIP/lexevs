
package org.LexGrid.LexBIG.Impl.dataAccess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class CleanUpUtilityTest {

	RegistryEntry entry;

	@BeforeClass
	public static void loadUp() throws Exception {

		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/Automobiles.xml").toURI(),
				true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		MetaData_Loader metaLoader = (MetaData_Loader) lbsm
				.getLoader("MetaDataLoader");

		metaLoader.loadAuxiliaryData(new File(
				"resources/testData/metadata1.xml").toURI(), Constructors
				.createAbsoluteCodingSchemeVersionReference(
						"urn:oid:11.11.0.1", "1.0"), true, false, true);

		while (metaLoader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(metaLoader.getStatus().getState()
				.equals(ProcessState.COMPLETED));
		assertFalse(metaLoader.getStatus().getErrorsLogged().booleanValue());
	}

	@Before
	public void setUp() throws LBParameterException {
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		List<RegistryEntry> entries = registry
				.getEntriesForUri("urn:oid:11.11.0.1");
		entry = entries.get(0);
		LexEvsServiceLocator.getInstance().getRegistry().removeEntry(entry);
	}

	@Test
	public void cleanupUtilsTest() throws LBInvocationException,
			LBParameterException {
		String[] dbNames = CleanUpUtility.listUnusedDatabases();
		assertTrue(dbNames.length > 0);
		assertTrue(CleanUpUtility.listUnusedIndexes().length > 0);
		assertTrue(CleanUpUtility.listUnusedMetadata().length > 0);
		CleanUpUtility.removeUnusedDatabase(dbNames[0]);
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
		CleanUpUtility.removeAllUnusedIndexes();
		assertTrue(CleanUpUtility.listUnusedIndexes().length == 0);
		CleanUpUtility.removeAllUnusedMetaData();
		assertTrue(CleanUpUtility.listUnusedIndexes().length == 0);
	}

}