package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
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
			
			// activate
			lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
					ref.getCodingSchemeURN(), ref.getCodingSchemeVersion()));
		}
		RegistryEntry entry = registry.getCodingSchemeEntry(references.get(0));
		registry.removeEntry(entry);
		LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService().dropIndex(references.get(2));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		
		// deactivate
		lbsm.deactivateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
			references.get(1).getCodingSchemeURN(), references.get(1).getCodingSchemeVersion()),null);		
		
		lbsm.deactivateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
			references.get(2).getCodingSchemeURN(), references.get(2).getCodingSchemeVersion()),null);		
		
		lbsm.removeCodingSchemeVersion(references.get(1));
		lbsm.removeCodingSchemeVersion(references.get(2));
		CleanUpUtility.removeAllUnusedDatabases();
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testMultipleIndexCreation() throws LBParameterException, LBException {

		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		// Mocking list that is derived from the launcher from the revised entry list
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		list.add(references.get(1));
		list.add(references.get(2));
		ops.cleanUp(list, true);
		assertTrue(ops.getConcurrentMetaData().getCodingSchemeList().size() == 2);
		
		// Test the index is populated and valid (GermanMadeParts, version 1.0)
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(references.get(1).getCodingSchemeVersion());
		CodedNodeSet set = lbs.getCodingSchemeConcepts(references.get(1).getCodingSchemeURN(), csvt);
		ResolvedConceptReferencesIterator itr = set.resolve(null, null, null);
		assertNotNull(itr.next());
		
		assertFalse(ops.doesIndexExist(references.get(0)));
		assertNotNull(ops.getConcurrentMetaData().getCodingSchemeList().get(0));
		
		// Test the index is populated and valid (Automobiles, version 1.1)
		lbs = LexBIGServiceImpl.defaultInstance();
		csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(references.get(2).getCodingSchemeVersion());
		set = lbs.getCodingSchemeConcepts(references.get(2).getCodingSchemeURN(), csvt);
		itr = set.resolve(null, null, null);
		assertNotNull(itr.next());
		
		assertTrue(ops.doesIndexExist(references.get(2)));
	}

}
