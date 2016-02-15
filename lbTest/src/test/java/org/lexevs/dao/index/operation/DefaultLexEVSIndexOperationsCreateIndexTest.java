package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
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

public class DefaultLexEVSIndexOperationsCreateIndexTest {
	static AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();

	private static LexBIGServiceManager lbsm;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		lbsm = ServiceHolder.instance().getLexBIGService()
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
		lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		// deactivate
		lbsm.deactivateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
			reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()),null);		
		lbsm.removeCodingSchemeVersion(reference);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIndexCreationAndRegistration() throws LBParameterException, LBException {
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		assertFalse(ops.doesIndexExist(reference));
		assertFalse(ops.getConcurrentMetaData().getCodingSchemeList().size() > 0);
		LexEvsServiceLocator.getInstance().getLexEvsIndexOperations().
			registerCodingSchemeEntityIndex(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
		
		assertNotNull(ops.getConcurrentMetaData().getCodingSchemeList().get(0));
		
		// activate
		lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
			reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()));
		
		// Test the index is populated and valid
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("1.0");
		CodedNodeSet set = lbs.getCodingSchemeConcepts("Automobiles", csvt);
		ResolvedConceptReferencesIterator itr = set.resolve(null, null, null);
		assertNotNull(itr.next());
				
		assertTrue(ops.doesIndexExist(reference));
	}

}
