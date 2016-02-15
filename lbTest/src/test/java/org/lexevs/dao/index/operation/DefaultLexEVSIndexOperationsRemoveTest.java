package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class DefaultLexEVSIndexOperationsRemoveTest {
	
	String uri = "urn:oid:11.11.0.1";
	String ver = "1.0";
	
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
		LexEvsServiceLocator.getInstance().getRegistry().removeEntry(entries.get(0));
	}

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetMap() {
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		list.add(Constructors.createAbsoluteCodingSchemeVersionReference(uri, ver));
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		Map<String, AbsoluteCodingSchemeVersionReference> dbNames = ops.getExpectedMap(list);
		assertTrue(dbNames.size() > 0);
	}
	
	@Test
	public void testGetIndexLocation(){
		String location = LexEvsServiceLocator.getInstance().getLexEvsIndexOperations().getLexEVSIndexLocation();
		assertTrue(location.length() > 0);
	}
	
	@Test
	public void testDoesIndexHaveMatchingRegistryEntry(){
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		list.add(Constructors.createAbsoluteCodingSchemeVersionReference(uri, ver));
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		File file = new File("Automobiles-1_0");
		AbsoluteCodingSchemeVersionReference cs = ops.doesIndexHaveMatchingRegistryEntry(file, list);
		assertNotNull(cs);
	}
	
	@Test
	public void testiIsIndexNameRegisteredWithTheSystem(){
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		CodingSchemeMetaData cs = ops.isIndexNameRegisteredWithTheSystem("Automobiles-1_0");
		assertNotNull(cs);
	}
	
	@Test
	public void testDropIndex() throws LBParameterException, LBException {
		DefaultLexEvsIndexOperations ops = (DefaultLexEvsIndexOperations) LexEvsServiceLocator.getInstance().getLexEvsIndexOperations();
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(uri, ver);
		String codingScheme = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(uri, ver);
		ops.dropIndex(codingScheme, ref);
		assertFalse(ops.doesIndexExist(ref));	
		
		// Test the index is not removed
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(ref.getCodingSchemeVersion());
		ResolvedConceptReferencesIterator itr = null;
		
		try {
			CodedNodeSet set = lbs.getCodingSchemeConcepts(ref.getCodingSchemeURN(), csvt);
			itr = set.resolve(null, null, null);
		} catch (RuntimeException rte) {
			itr = null;
		}
		assertTrue(itr == null);

	}
	
	@AfterClass
	public  static void removeBrokenScheme() throws LBInvocationException, LBParameterException{
		CleanUpUtility.removeAllUnusedDatabases();
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
	}
	

}
