package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedVStoCodingSchemLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetToSchemeBatchLoader;
import org.LexGrid.LexBIG.Impl.loaders.UriBasedHistoryLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexgrid.valuesets.sourceasserted.impl.NCItSourceAssertedValueSetUpdateServiceImpl;
import org.springframework.core.annotation.Order;

@RunWith(OrderingTestRunner.class)
public class LoadAndUpdateSourceAssertedValueSetsTest {
	LexBIGServiceManager lbsm;

@Order(1)
@Before
public void setUp() throws LBException{
	lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
}

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
}

@Order(2)
@Test
public void loadSourceAssertedValueSetDefinitionsTest() throws LBParameterException, InterruptedException{
    new SourceAssertedValueSetBatchLoader("owl2lexevs", "0.1.5",
    		"Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/",
    		"NCI", "Semantic_Type").run("Contributing_Source");
    Thread.sleep(1000);
}

@Order(3)
@Test
public void loadSourceAssertedResolvedValueSetsTest() throws InterruptedException, LBException{
	 new SourceAssertedValueSetToSchemeBatchLoader("owl2lexevs", "0.1.5", 
			 "Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/", 
			 "NCI", "Semantic_Type").run("Contributing_Source");
	    Thread.sleep(1000);
}

@Order(4)
@Test
public void loadHistoryTest() throws LBException, InterruptedException{
    UriBasedHistoryLoaderImpl hloader = new UriBasedHistoryLoaderImpl("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");

    hloader.load(new File("../lbTest/resources/testData/owl2/owl2historytest.txt").toURI(), new File(
            "../lbTest/resources/testData/owl2/owl2systemReleaseTest.txt").toURI(), false, true, true);
    Thread.sleep(1000);
}

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

@Order(6)
@Test
public void updateResolvedValueSetsToCurrentScheme() throws LBException{
	NCItSourceAssertedValueSetUpdateServiceImpl service = new NCItSourceAssertedValueSetUpdateServiceImpl(
			"owl2lexevs", "0.1.5.1", "Concept_In_Subset", "true", 
			"http://evs.nci.nih.gov/valueset/","NCI","Contributing_Source",
			"Semantic_Type", "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
	List<String> valueSetCodes = service.resolveUpdatedVSToReferences("0.1.5.1");
	List<Node> mappedNodes = null;
	try {
		mappedNodes = service.mapSimpleReferencesToNodes(valueSetCodes);
	} catch (LBException e1) {
		e1.printStackTrace();
	}
	List<Node> finalNodes = service.getNodeListForUpdate(mappedNodes);
	
	service.prepServiceForUpdate(finalNodes);


	service.loadUpdatedValueSets(finalNodes);
}

}
