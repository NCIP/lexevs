
package org.lexevs.dao.index.operation;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.TestUtil;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SameSessionLoadandQueryTest {

	private static AbsoluteCodingSchemeVersionReference reference = Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"); // Automobiles 1.0

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtil.removeAll();

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
		lbsm.activateCodingSchemeVersion(reference);
		
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		lbsm.removeCodingSchemeVersion(reference);
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
		lbsm.deactivateCodingSchemeVersion(reference, null);
		
		ResolvedConceptReferencesIterator itr = set.resolve(null, null, null);
		assertNotNull(itr.next());
	}

}