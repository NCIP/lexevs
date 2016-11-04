package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class ManifestLoadWithAssociationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void LoadOwlWAssocDefManifest() throws InterruptedException, LBException {
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");

		loader.load(new File("resources/testData/ManifestTest/sample.owl").toURI(),
				new File("resources/testData/ManifestTest/Thesaurus_MF_Test.xml").toURI(), 1, false, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		ref.setCodingSchemeVersion("16.Teste");
		lbsm.deactivateCodingSchemeVersion(ref, null);
		lbsm.removeCodingSchemeVersion(ref);
	}

	private static LexBIGServiceManager getLexBIGServiceManager() throws LBException {
		return ServiceHolder.instance().getLexBIGService().getServiceManager(null);
	}

}
