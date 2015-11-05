package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class DefaultLexEVSIndexOperationsCleanupIndexesTest {
	static List<AbsoluteCodingSchemeVersionReference> references = new ArrayList<AbsoluteCodingSchemeVersionReference>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/Automobiles.xml").toURI(),
				true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		
		LexGridMultiLoaderImpl GMPloader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		GMPloader.load(new File("resources/testData/German_Made_Parts.xml").toURI(),
				true, true);

		while (GMPloader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		
		LexGridMultiLoaderImpl A2loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		A2loader.load(new File("resources/testData/Automobiles2.xml").toURI(),
				true, true);

		while (A2loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		List<RegistryEntry> entries = registry.getAllRegistryEntries();
		for(RegistryEntry re: entries){
			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(re.getResourceUri());
			ref.setCodingSchemeVersion(re.getResourceVersion());
			references.add(ref);
		}
		RegistryEntry entry = registry.getCodingSchemeEntry(references.get(0));
		registry.removeEntry(entry);
		LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService().dropIndex(references.get(2));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
			lbsm.removeCodingSchemeVersion(references.get(1));
			lbsm.removeCodingSchemeVersion(references.get(2));
			CleanUpUtility.removeAllUnusedDatabases();
			assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testMultipleIndexCreation() throws LBParameterException {

		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		// Mocking list that is derived from the launcher from the revised entry list
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		list.add(references.get(1));
		list.add(references.get(2));
		ops.cleanUp(list, true);
		assertTrue(ops.getConcurrentMetaData().getCodingSchemeList().size() == 2);
		assertFalse(ops.doesIndexExist(references.get(0)));
		assertNotNull(ops.getConcurrentMetaData().getCodingSchemeList().get(0));
		assertTrue(ops.doesIndexExist(references.get(2)));
		
	}

}
