
package org.LexGrid.LexBIG.Impl.testUtility;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MIFVocabularyLoader;
import org.LexGrid.LexBIG.Extensions.Load.MedDRA_Loader;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MedDRALoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MrmapRRFLoader;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.LexGrid.LexBIG.admin.LoadMetaBatchWithMetadata;
import org.LexGrid.LexBIG.mapping.MappingTestConstants;
import org.LexGrid.LexBIG.mapping.MappingTestUtility;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.UncategorizedSQLException;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MappingRelationsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This set of tests loads the necessary data for the full suite of JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@RunWith(OrderingTestRunner.class)
public class LoadTestDataTest extends LexBIGServiceTestCase {
    
    @Override
	protected String getTestID() {
		return LoadTestDataTest.class.getName();
	}

    @Test
    @Order(0)
    public void testLoadAutombiles() throws LBParameterException, LBInvocationException, InterruptedException,
            LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/Automobiles.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(1)
    public void testLoadAutombilesExtension() throws LBParameterException, LBInvocationException, InterruptedException,
    LBException {
    	LexBIGServiceManager lbsm = getLexBIGServiceManager();

    	LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

    	loader.load(new File("resources/testData/testExtension.xml").toURI(), true, true);

    	while (loader.getStatus().getEndTime() == null) {
    		Thread.sleep(1000);
    	}

    	assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
    	assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

    	lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

    	lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    	
    	lbsm.registerCodingSchemeAsSupplement(
    			Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION), 
    			Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_EXTENSION_URN, AUTO_EXTENSION_VERSION));
    			
    }

    @Test
    @Order(2)
    public void testLoadGermanMadeParts() throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        // load non-async - this should block
        loader.load(new File("resources/testData/German_Made_Parts.xml").toURI(), true, false);

    	while (loader.getStatus().getEndTime() == null) {
    		Thread.sleep(1000);
    	}
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(3)
    public void testLoadBoostScheme() throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        // load non-async - this should block
        loader.load(new File("resources/testData/BoostedQuery.xml").toURI(), true, false);

    	while (loader.getStatus().getEndTime() == null) {
    		Thread.sleep(1000);
    	}
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(4)
    public void testLoadNCIMeta() throws Exception {
    	String[] args  = {"-in",  new File("resources/testData/sampleNciMeta").toURI().toString()};
    	
    	LoadMetaBatchWithMetadata metaBatch = new LoadMetaBatchWithMetadata();
    	metaBatch.run(args);
    	
    	LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	lbsm.activateCodingSchemeVersion(metaBatch.getCodingSchemeRef());
    }

    @Test(expected = RuntimeException.class)
    @Order(5)
    public void testLoadNCItHistoryMultReleaseFail() throws InterruptedException, LBException {
 
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        NCIHistoryLoader hloader = (NCIHistoryLoader) lbsm.getLoader("NCIThesaurusHistoryLoader");

        hloader.load(new File("resources/testData/CumulativeHist_MultiReleaseDate.txt").toURI(), new File(
                "resources/testData/SystemReleaseHistory.txt").toURI(), false, true, false);

        assertEquals(ProcessState.COMPLETED,hloader.getStatus().getState());
        assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
    }
    
    
    @Test(expected = RuntimeException.class)
    @Order(5)
    public void testLoadNCItHistoryNoReleaseFail() throws InterruptedException, LBException {
 
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        NCIHistoryLoader hloader = (NCIHistoryLoader) lbsm.getLoader("NCIThesaurusHistoryLoader");
        
        hloader.load(new File("resources/testData/CumulativeHistNoRelease.txt").toURI(), new File(
                "resources/testData/SystemReleaseHistory.txt").toURI(), false, true, false);

        assertEquals(ProcessState.COMPLETED,hloader.getStatus().getState());
        assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
    }
    
    
    @Test
    @Order(5)
    public void testLoadNCItHistory() throws InterruptedException, LBException {
 
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        NCIHistoryLoader hloader = (NCIHistoryLoader) lbsm.getLoader("NCIThesaurusHistoryLoader");

        hloader.load(new File("resources/testData/Filtered_pipe_out_12f.txt").toURI(), new File(
                "resources/testData/SystemReleaseHistory.txt").toURI(), false, true, false);

        assertEquals(ProcessState.COMPLETED,hloader.getStatus().getState());
        assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
    }

    @Test
    @Order(6)
    public void testLoadMetaHistory() throws LBException {
        ServiceHolder.configureForSingleConfig();
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        UMLSHistoryLoader loader = (UMLSHistoryLoader) lbsm
                .getLoader(org.LexGrid.LexBIG.Impl.loaders.UMLSHistoryLoaderImpl.name);
        loader.load((new File("resources/testData/sampleNciMeta/sampleNciMetaHistory")).toURI(), false, true, false);

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
    }

    @Test
    @Order(7)
    public void testLoadObo() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

        loader.load(new File("resources/testData/cell.obo").toURI(), null, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(8)
    public void testLoadLongSourceObo() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

        loader.load(new File("resources/testData/testLoadLongsource.obo").toURI(), null, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        
        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:lsid:bioontology.org:test", "UNASSIGNED");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    @Test
    @Order(9)
    public void testLoadOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl)lbsm.getLoader("OWL2Loader");
 
        CodingSchemeManifest csm = new CodingSchemeManifest();
        CsmfCodingSchemeURI uri = new CsmfCodingSchemeURI();
        csm.setId("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
        uri.setContent("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
        uri.setToOverride(true);
        csm.setCodingSchemeURI(uri);
        loader.setCodingSchemeManifest(csm);
        
        loader.load(new File("resources/testData/pizza.owl").toURI(), null, 1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    @Test
    @Order(10)
    public void testLoadOwlThesaurus() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
 
        CodingSchemeManifest csm = new CodingSchemeManifest();
        CsmfCodingSchemeURI uri = new CsmfCodingSchemeURI();
        csm.setId("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        uri.setContent("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        uri.setToOverride(true);
        csm.setCodingSchemeURI(uri);
        loader.setCodingSchemeManifest(csm);
        
        loader.load(new File("resources/testData/sample.owl").toURI(), null, 1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    @Test
    @Order(11)
    public void testLoadOwlLoaderPreferences() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");

        loader.setLoaderPreferences(new File("resources/testData/OWLPrefs.xml").toURI());
        loader.load(new File("resources/testData/camera.owl").toURI(), new File(
                "resources/testData/Camera-manifest.xml").toURI(), 1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    @Test
    @Order(12)
    public void testLoadGenericOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
        loader.load(new File("resources/testData/amino-acid.owl").toURI(), new File(
                "resources/testData/amino-acid-manifest.xml").toURI(), 0, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    @Test
    @Order(13)
    public void testLoadGenericOwlWithInstanceData() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
        loader.load(new File("resources/testData/OvarianMass_SNOMED_ValueSets.owl").toURI(), null,  1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }
    
//    public void testLoadGenericOwlWithNPOsansQuals() throws InterruptedException, LBException {
//        LexBIGServiceManager lbsm = getLexBIGServiceManager();
//
//        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
//        loader.load(new File("resources/testData/npotest.owl").toURI(), null,  1, false, true);
//
//        while (loader.getStatus().getEndTime() == null) {
//            Thread.sleep(1000);
//        }
//        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
//        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
//
//        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
//        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
//
//    }

    @Test
    @Order(14)
    public void testLoadOWL2NPOwMultiNamespace() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
        loader.setLoaderPreferences(new File("resources/testData/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
        loader.load(new File("resources/testData/multiName_npo-2011-12-08_inferred.owl").toURI(), 
        		new File("resources/testData/NPOMF.xml").toURI(),  1, false, true);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    @Test
    @Order(15)
    public void testLoadCompPropsOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
        loader.setLoaderPreferences(new File("resources/testData/OWLPrefs.xml").toURI());
        
        CodingSchemeManifest csm = new CodingSchemeManifest();
        csm.setId("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        CsmfCodingSchemeURI uri = new CsmfCodingSchemeURI();
        uri.setContent("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        uri.setToOverride(true);
        csm.setCodingSchemeURI(uri);
        loader.setCodingSchemeManifest(csm);
        
        loader.load(new File("resources/testData/sample.cp.2.owl").toURI(),
                null, 0, true, true);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
    }

    @Test
    @Order(16)
    public void testLoadNCIMeta2() throws Exception {
    	String[] args  = {"-in",  new File("resources/testData/SAMPLEMETA").toURI().toString()};
    	
    	LoadMetaBatchWithMetadata metaBatch = new LoadMetaBatchWithMetadata();
    	metaBatch.run(args);
    	
    	LexBIGServiceManager lbsm = getLexBIGServiceManager();
        lbsm.activateCodingSchemeVersion(metaBatch.getCodingSchemeRef());
        lbsm.setVersionTag(metaBatch.getCodingSchemeRef(), LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(17)
    public void testLoadMedDRA() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	File accessPath = new File("resources/testData/medDRA");

        MedDRA_Loader loader = (MedDRALoaderImpl) lbsm.getLoader("MedDRALoader");
        loader.load(accessPath.toURI(), null, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(18)
	public void testloadOWL2Snippet() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		loader.load(new File("resources/testData/owl2/owl2-snippet-data.owl")
				.toURI(), null, 1, true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

	}

    @Test
    @Order(19)
	public void testloadOWL2SnippetWithIndividuals() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
		loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
		loader.load(new File("resources/testData/owl2/owl2-test-cases-Defined-Annotated.owl")
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

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());

	}

    @Test
    @Order(20)
	public void testloadOWL2SnippetWithPrimitives() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
		loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
		loader.load(new File("resources/testData/owl2/owl2-test-cases-Primitive-Annotated.owl")
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

//		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
//				LBConstants.KnownTags.PRODUCTION.toString());

	}

    @Test
    @Order(21)
	public void testloadOWL2SnippetWithIndividualsUnannotated() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
			loader.load(new File("resources/testData/owl2/owl2-test-cases-Defined-Unannotated.owl")
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

//		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
//				LBConstants.KnownTags.PRODUCTION.toString());
	}

    @Test
    @Order(22)
	public void testloadOWL2SnippetWithPrimitivesUnannotated() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
			loader.load(new File("resources/testData/owl2/owl2-test-cases-Primitive-Unannotated.owl")
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

//		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
//				LBConstants.KnownTags.PRODUCTION.toString());
	}

    @Test
    @Order(23)
	public void testloadOWL2SnippetSpecialCasesAnnotatedDefined() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodes.XML").toURI());
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
		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], "PRODUCTION");
	}

    @Test
    @Order(24)
	public void testloadOWL2SnippetSpecialCasesAnnotatedByCodeDefined() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodesByNameToCode.XML").toURI());
			loader.load(new File("resources/testData/owl2/owl2-special-cases-byName-Defined-Annotated2.owl")
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
    @Test
    @Order(25)
	public void testloadOWL2SnippetSpecialCasesNamespaces() throws Exception {
		
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
		try{
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodesByNameToCode.XML").toURI());
			loader.load(new File("resources/testData/owl2/owl2-special-cases-Namespaces.owl")
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
    @Test
    @Order(26)
	public void testLoadHL7JMifVocabularyForBadSource() throws LBException,
			InterruptedException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		MIFVocabularyLoader loader = null;
		try {
			lbsm = getLexBIGServiceManager();
			loader = (MIFVocabularyLoaderImpl) lbsm
					.getLoader(org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl.name);
			loader.load(new File("resources/testData/German_Made_Parts.xml")
					.toURI(), null, true, false);
		} catch (RuntimeException e) {
			assertEquals(
					"Source file is invalid. Please check to see if this is a valid HL7 vocabulary mif file",
					e.getMessage());
		} finally {
			while (loader.getStatus().getEndTime() == null) {
				Thread.sleep(1000);
			}
		}
	}

    @Test
    @Order(27)
    public void testLoadHL7MifVocabulary() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	File accessPath = new File("resources/testData/hl7MifVocabulary/DEFN=UV=VO=1189-20121121.coremif");

    	MIFVocabularyLoader loader = (MIFVocabularyLoaderImpl) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl.name);
        loader.load(accessPath.toURI(), null, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(28)
    public void testLoadMeta1() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaData_Loader metaLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");

        metaLoader.loadAuxiliaryData(
            new File("resources/testData/metadata1.xml").toURI(),
            Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION),
            true, false, true);

        while (metaLoader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(metaLoader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(metaLoader.getStatus().getErrorsLogged().booleanValue());
    }

    @Test
    @Order(28)
    public void testLoadMeta2() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaData_Loader metaLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");

        metaLoader.loadAuxiliaryData(
                new File("resources/testData/metadata2.xml").toURI(),
                Constructors.createAbsoluteCodingSchemeVersionReference(PARTS_URN, PARTS_VERSION),
                true, true, true);

        while (metaLoader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(metaLoader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(metaLoader.getStatus().getErrorsLogged().booleanValue());
    }
    
    @Test
    @Order(30)
    public void testLoadUMLSFail1() throws Exception {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        UmlsBatchLoader loader = (UmlsBatchLoader) lbsm.getLoader("UmlsBatchLoader");

        // Test invalid file
        loader.loadUmls(new File("resources/wrong/dir").toURI(), "AIR");

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getCodingSchemeReferences().length == 0);
        assertTrue(loader.getStatus().getState().equals(ProcessState.FAILED));
        assertTrue(loader.getStatus().getErrorsLogged().booleanValue());
    }
    
    @Test
    @Order(31)
    public void testLoadUMLSFail2() throws Exception {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        UmlsBatchLoader loader = (UmlsBatchLoader) lbsm.getLoader("UmlsBatchLoader");

        // Test invalid SAB
        loader.loadUmls(new File("resources/testData/sampleUMLS-AIR").toURI(), "XXXXXX");

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getCodingSchemeReferences().length == 0);
        assertTrue(loader.getStatus().getState().equals(ProcessState.FAILED));
        assertTrue(loader.getStatus().getErrorsLogged().booleanValue());
    }

    @Test
    @Order(32)
    public void testLoadUMLS() throws Exception {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        UmlsBatchLoader loader = (UmlsBatchLoader) lbsm.getLoader("UmlsBatchLoader");

        loader.loadUmls(new File("resources/testData/sampleUMLS-AIR").toURI(), "AIR");

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(33)
    public void testLoadMappingWithDefaultSettings() throws LBException{
    	
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
        LexEVSAuthoringServiceImpl authoring = new LexEVSAuthoringServiceImpl();
        
		AssociationSource source = new AssociationSource();
		AssociationSource source1 = new AssociationSource();
		AssociationSource source2 = new AssociationSource();
		source.setSourceEntityCode("T0001");
		source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("005");
		target.setTargetEntityCodeNamespace("Automobiles");
		source.addTarget(target);

		source1.setSourceEntityCode("P0001");
		source1.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target1 = new AssociationTarget();
		target1.setTargetEntityCode("A0001");
		target1.setTargetEntityCodeNamespace("Automobiles");
		source1.addTarget(target1);

		source2.setSourceEntityCode("P0001");
		source2.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target2 = new AssociationTarget();
		target2.setTargetEntityCode("005");
		target2.setTargetEntityCodeNamespace("Automobiles");
		source2.addTarget(target2);
		AssociationSource[] sources = new AssociationSource[] { source,
				source1, source2 };
		authoring.createMappingWithDefaultValues(sources, "GermanMadeParts",
                "2.0", "Automobiles", "1.0", "SY", false);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion
				.setCodingSchemeURN("http://default.mapping.container");
		codingSchemeVersion.setCodingSchemeVersion("1.0");
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
	
    }

    @Test
    @Order(34)
    public void testLoadCodingSchemeWithMoreMetaData() throws LBException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
        LexEVSAuthoringServiceImpl authoring = new LexEVSAuthoringServiceImpl();
        MappingTestUtility utility = new MappingTestUtility();
        
		List<String> localNameList = Arrays.asList(new String[] { "name1",
				"name2", "name3" });
		Source source = new Source();
		source.setContent("Source_Vocabulary");
		List<Source> sourceList = Arrays.asList();
		Text copyright = new Text();
		copyright.setContent("Mayo copyright");
		CodingScheme mappingSchemeMetadata = authoring.populateCodingScheme(
				"Mapping_Test", "Tested_URI", "Formal_Mapping_Name", "EN", 5L,
				"0.0", localNameList, sourceList, copyright, new Mappings(),
				null, null, null);

		AssociationTarget target1 = utility.createTargetWithValuesPopulated();
		AssociationTarget target2 = utility.createTarget("Ford", "Automobiles");
		AssociationTarget target3 = utility.createTarget("73", "Automobiles");
		AssociationTarget[] targets = new AssociationTarget[] { target1,
				target2, target3 };
		AssociationSource associationSource = new AssociationSource();
		associationSource.setSourceEntityCode("R0001");
		associationSource
				.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		associationSource.setTarget(targets);
		AssociationSource[] sourcesAndTargets = new AssociationSource[] { associationSource };
		String sourceCodingScheme = MappingTestConstants.SOURCE_SCHEME;
		String sourceCodingSchemeVersion = MappingTestConstants.SOURCE_VERSION;
		String targetCodingScheme = MappingTestConstants.TARGET_SCHEME;
		String targetCodingSchemeVersion = MappingTestConstants.TARGET_VERSION;
		String associationName = "SY";
		String relationsContainerName = "GermanMadeParts_to_Automobiles_Mappings";
		String revisionId = "Non-Default_NEW_Mapping";
		authoring.createMappingScheme(mappingSchemeMetadata, sourcesAndTargets,
				sourceCodingScheme, sourceCodingSchemeVersion,
				targetCodingScheme, targetCodingSchemeVersion, associationName,
				relationsContainerName, revisionId, false);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion.setCodingSchemeURN(mappingSchemeMetadata.getCodingSchemeURI());
		codingSchemeVersion.setCodingSchemeVersion(mappingSchemeMetadata.getRepresentsVersion());
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
    }

    @Test
    @Order(35)
    public void testLoadAuthoringShellSystem() throws LBException, LBInvocationException, InterruptedException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/assoc_authoring/AuthoringTestBase.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(36)
    public void testLoadMappinglSystem() throws LBException, LBInvocationException, InterruptedException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/testMapping.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }
    
    @Test
    @Order(37)
	public void testLoadMrMap() throws LBException, LBInvocationException, InterruptedException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException{

        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MrmapRRFLoader loader = (MrmapRRFLoader) lbsm.getLoader("MrMap_Loader");
        MappingRelationsUtil map = new  MappingRelationsUtil();
   	 	HashMap<String, Relations> relationsMap = map.processMrSatBean(
   	 			"resources/testData/mrmap_mapping/MRSAT1.RRF", "resources/testData/mrmap_mapping/MRMAP1.RRF");
   	 	for(Map.Entry<String, Relations> rel: relationsMap.entrySet()){
   	 		loader.load(new File(("resources/testData/mrmap_mapping/MRMAP1.RRF")).toURI(), 
        		new File("resources/testData/mrmap_mapping/MRSAT1.RRF").toURI(), 
        		null, null, null, rel, true, true);

   	 		while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
   	 		}

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], 
        		LBConstants.KnownTags.PRODUCTION.toString());
    }
	}
    
    
    @Test
    @Order(38)
    public void testLoadValueSetDefinitions() throws LBException{

    	LexEVSValueSetDefinitionServicesImpl vds_ = (LexEVSValueSetDefinitionServicesImpl) 
    			LexEVSValueSetDefinitionServicesImpl.defaultInstance();
        vds_.loadValueSetDefinition("resources/testData/valueDomain/vdTestData.xml", true);

    }
    
    @Test
    @Order(39)
    public void testResolveToCodingSchemeValueSetDefinitions() throws URISyntaxException, Exception{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		loader.load(new URI("SRITEST:AUTO:AllDomesticButGM"), null, null, "PRODUCTION", "12.03test");
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
    }
    
	@Order(40)
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
    
    
    private LexBIGServiceManager getLexBIGServiceManager() throws LBException {
    	return ServiceHolder.instance().getLexBIGService().getServiceManager(null);
    }	
}