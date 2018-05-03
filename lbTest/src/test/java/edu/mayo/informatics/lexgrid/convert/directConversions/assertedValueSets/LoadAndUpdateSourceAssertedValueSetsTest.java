package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.resolvedvalueset.impl.ExternalResolvedValueSetIndexService;
import org.springframework.core.annotation.Order;

@RunWith(OrderingTestRunner.class)
public class LoadAndUpdateSourceAssertedValueSetsTest {
	LexBIGServiceManager lbsm;
	private SourceAssertedValueSetSearchIndexService service;
	private ExternalResolvedValueSetIndexService extService;


@Before
public void setUp() throws LBException{
	lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
	service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
	extService = new ExternalResolvedValueSetIndexService();
}

@Order(1)
@Test
public void loadFirstValueSetCodingSchemeTest() throws LBException, InterruptedException{

	OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
    String fileName = "resources/testData/owl2/owl2-special-cases-Defined-Annotated.owl";
    loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodes.XML").toURI());
    loader.load(new File(fileName).toURI(),null,  1, false, true);
    
    while (loader.getStatus().getEndTime() == null) {
        Thread.sleep(1000);
    }
    assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
    assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

    lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
    lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], "PRODUCTION");
}

@Order(2)
@Test
public void loadSourceAssertedValueSetDefinitionsTest() throws LBParameterException, InterruptedException{
    AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
    		codingSchemeName("owl2lexevs").
    		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
    		baseValueSetURI("http://evs.nci.nih.gov/valueset/").
    		sourceName("Contributing_Source").
    		build();
	new SourceAssertedValueSetBatchLoader(params,
    		"NCI", "Semantic_Type").run(params.getSourceName());
    Thread.sleep(1000);
}

//@Order(3)
//@Test
//public void loadSourceAssertedResolvedValueSetsTest() throws InterruptedException, LBException{
//	 new SourceAssertedValueSetToSchemeBatchLoader("owl2lexevs", "0.1.5", 
//			 "Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/", 
//			 "NCI", "Semantic_Type").run("Contributing_Source");
//	    Thread.sleep(1000);
//}
//
//@Order(4)
//@Test
//public void loadHistoryTest() throws LBException, InterruptedException{
//    UriBasedHistoryLoaderImpl hloader = new UriBasedHistoryLoaderImpl("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
//
//    hloader.load(new File("resources/testData/owl2/owl2historytest.txt").toURI(), new File(
//            "resources/testData/owl2/owl2systemReleaseTest.txt").toURI(), false, true, true);
//    while (hloader.getStatus().getEndTime() == null) {
//        Thread.sleep(2000);
//    }
//    
//    assertEquals(ProcessState.COMPLETED,hloader.getStatus().getState());
//    assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
//}

@Order(5)
@Test
public void loadCurrentCodingSchemeTest() throws LBException, InterruptedException{

	OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
    String fileName = "resources/testData/owl2/owl2-special-cases-Defined-AnnotatedExpVS.owl";
    loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodes.XML").toURI());
    loader.load(new File(fileName).toURI(),null,  1, false, true);
    
    while (loader.getStatus().getEndTime() == null) {
        Thread.sleep(1000);
    }
    assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
    assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

    lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
}

//@Order(6)
//@Test
//public void updateResolvedValueSetsToCurrentScheme() throws LBException{
//	NCItSourceAssertedValueSetUpdateServiceImpl service = new NCItSourceAssertedValueSetUpdateServiceImpl(
//			"owl2lexevs", "0.1.5.1", "Concept_In_Subset", "true", 
//			"http://evs.nci.nih.gov/valueset/","NCI","Contributing_Source",
//			"Semantic_Type", "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
//	List<String> valueSetCodes = service.resolveUpdatedVSToReferences("0.1.5.1");
//	List<Node> mappedNodes = null;
//	try {
//		mappedNodes = service.mapSimpleReferencesToNodes(valueSetCodes);
//	} catch (LBException e1) {
//		e1.printStackTrace();
//	}
//	List<Node> finalNodes = service.getNodeListForUpdate(mappedNodes);
//	
//	service.prepServiceForUpdate(finalNodes);
//
//
//	service.loadUpdatedValueSets(finalNodes);
//}

	@Order(7)
	@Test
	public void loadNonSourceAssertedValueSetDefinition() throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
                .getLoader("LexGrid_Loader");
        
        // load non-async - this should block
        loader.load(new File("resources/testData/valueDomain/VSD_OWL2Annotations.xml").toURI(), true, false);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
        
        Thread.sleep(1000);
	}
	
	@Order(8)
	@Test
	public void resolveNonSourceAssertedValueSet() throws URISyntaxException, Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		AbsoluteCodingSchemeVersionReferenceList csVersionList = new AbsoluteCodingSchemeVersionReferenceList(); 
		AbsoluteCodingSchemeVersionReference vRef = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		csVersionList.addAbsoluteCodingSchemeVersionReference(vRef);

			loader.load(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, csVersionList, "owl2lexevs", "0.1.5");;

			while (loader.getStatus().getEndTime() == null) {
				Thread.sleep(3000);
			}
			assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
			assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

			lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
	        
	     }
	
	@Order(9)
	@Test
	public void createIndex() throws InterruptedException {
		service.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1"));
		boolean doesExist = service.doesIndexExist(Constructors.
				createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1"));
		Thread.sleep(1000);
		assertTrue(doesExist);
	}
	
	@Order(10)
	@Test
	public void createIndexForExternalValueSets() {
		extService.indexExternalResolvedValueSetsToAssertedValueSetIndex();
	}
	
	

}
