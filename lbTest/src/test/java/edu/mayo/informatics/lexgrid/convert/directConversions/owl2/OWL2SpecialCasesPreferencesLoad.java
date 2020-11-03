package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedHierarchy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class OWL2SpecialCasesPreferencesLoad extends TestCase{
	LexBIGService lbs;

	@Before
	public void setUp() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().
				getLexBIGService().getServiceManager(null);
		lbs = ServiceHolder.instance().getLexBIGService();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLTransPrefsLoadAnonAsAssocPF2SetTopNodes.XML").toURI());
			loader.load(new File("resources/testData/owl2/owl2-special-cases-Defined-Annotated.owl")
					.toURI(), null, 1, true, true);
		}
		catch(ClassCastException e){
			fail("Failed on class cast exception: " + e.getMessage());
		}
		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
	}

	@After
	public void tearDown() throws Exception {
		
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);
		
	}

	@Test
	public void test() throws LBException {

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodingScheme scheme = lbs.resolveCodingScheme(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
		List<SupportedHierarchy> hrchy = scheme.getMappings().getSupportedHierarchyAsReference();

		assertTrue(hrchy.stream().anyMatch(x -> x.getLocalId().equals("willing")));
		assertTrue(hrchy.stream().anyMatch(x -> x.getLocalId().equals("unwilling")));
		assertTrue(hrchy.stream().filter(x -> x.getLocalId().equals("willing")).anyMatch(y -> y.getRootCode().equals("@")));
		assertTrue(hrchy.stream().filter(x -> x.getLocalId().equals("unwilling")).anyMatch(y -> y.getRootCode().equals("@@")));
	}

}
