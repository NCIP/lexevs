package org.LexGrid.LexBIG.Impl.dataAccess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
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
     
    LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

    LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

    loader.load(new File("resources/testData/Automobiles.xml").toURI(), true, true);

    while (loader.getStatus().getEndTime() == null) {
        Thread.sleep(1000);
    }

    assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
    assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
	}
	
	@Before
	public void setUp() throws LBParameterException{
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		List<RegistryEntry> entries = registry.getEntriesForUri("urn:oid:11.11.0.1");
		entry = entries.get(0);
		LexEvsServiceLocator.getInstance().getRegistry().removeEntry(entry);
	}

	@Test
	public void cleanupUtilsTest() throws LBInvocationException, LBParameterException {
		String[] dbNames = CleanUpUtility.listUnusedDatabases();
		assertTrue(dbNames.length > 0);
		assertTrue(CleanUpUtility.listUnusedIndexes().length > 0);
		CleanUpUtility.removeUnusedDatabase(dbNames[0]);
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
		CleanUpUtility.removeAllUnusedIndexes();
		assertTrue(CleanUpUtility.listUnusedIndexes().length == 0);
	}

}
