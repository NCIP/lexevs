package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class SameSessionLoadandQueryTest {
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
		
		// Activate the coding scheme
		lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));
		
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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		for(AbsoluteCodingSchemeVersionReference acsv: references){
			lbsm.removeCodingSchemeVersion(acsv);
		}
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testQueryAfterCreation() throws LBException {
		LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
		LexBIGServiceManager lbsm = lbsi.getServiceManager(null);
		
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("1.0");
		CodedNodeSet set = lbs.getCodingSchemeConcepts("Automobiles", csvt);
	
		// deactivate the coding scheme
		AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0");
		lbsm.deactivateCodingSchemeVersion(acsvr, null);
		
		ResolvedConceptReferencesIterator itr = set.resolve(null, null, null);
		assertNotNull(itr.next());
	}

}
