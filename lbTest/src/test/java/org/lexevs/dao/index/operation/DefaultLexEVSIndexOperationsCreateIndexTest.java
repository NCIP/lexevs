package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
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

public class DefaultLexEVSIndexOperationsCreateIndexTest {
	static AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();

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
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		List<RegistryEntry> entries = registry
				.getEntriesForUri("urn:oid:11.11.0.1");
		reference.setCodingSchemeURN(entries.get(0).getResourceUri());
		reference.setCodingSchemeVersion( entries.get(0).getResourceVersion());
		LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService().dropIndex(reference);
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
	public void testIndexCreationAndRegistration() throws LBParameterException {
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		assertFalse(ops.doesIndexExist(reference));
		assertFalse(ops.getConcurrentMetaData().getCodingSchemeList().size() > 0);
		LexEvsServiceLocator.getInstance().getLexEvsIndexOperations().
		registerCodingSchemeEntityIndex(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
		assertNotNull(ops.getConcurrentMetaData().getCodingSchemeList().get(0));
		assertTrue(ops.doesIndexExist(reference));
	}

}
