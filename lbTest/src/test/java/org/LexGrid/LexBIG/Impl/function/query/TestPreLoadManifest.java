
package org.LexGrid.LexBIG.Impl.function.query;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfFormalName;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.codingSchemes.CodingScheme;

/**
 * This testcase checks if the manifest implementation works when using either a
 * manifest file or a manifest object. It also checks if the same ontology can
 * be loaded twice while using the manifest to adjust the registered name of the
 * ontology.
 * 
 * @author Pradip Kanjamala
 * 
 */
public class TestPreLoadManifest extends LexBIGServiceTestCase {

    private static LoadStatus status_ = null;
    final static String testID = "T1_FNC_50";

    final static  String AMINO_ACID_URI= "http://www.bioontology.org/MyAminoAcid.owl";
    private LexBIGService lbs;
    private LexBIGServiceManager lbsm;
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    public void setUp() throws LBException {
        lbs = ServiceHolder.instance().getLexBIGService();
        lbsm = lbs.getServiceManager(null);
    }

    /**
     * Load OBO Ontology and adjust using manifest file
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testLoadOboWithManifestFile() throws InterruptedException, LBException {
       
        OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");
        loader.setCodingSchemeManifestURI(new File("resources/testData/fungal_anatomy-Manifest.xml").toURI());
        loader.load(new File("resources/testData/fungal_anatomy.obo").toURI(), null, true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
    }

    /**
     * Load OBO Ontology and adjust using manifest file
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testLoadOboWithManifestObjectVersion02() throws InterruptedException, LBException {
        OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");
        CodingSchemeManifest csm = createOBOCodingSchemeManifest("02");
        loader.setCodingSchemeManifest(csm);
        loader.load(new File("resources/testData/fungal_anatomy.obo").toURI(), null, true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

    }

    /**
     * Load OBO Ontology and adjust using manifest file
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testLoadOboWithManifestObjectVersion03() throws InterruptedException, LBException {
        OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");
        CodingSchemeManifest csm = createOBOCodingSchemeManifest("03");
        loader.setCodingSchemeManifest(csm);
        loader.load(new File("resources/testData/fungal_anatomy.obo").toURI(), null, true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        activateCodingScheme(loader, lbsm);

    }

    private CodingSchemeManifest createOBOCodingSchemeManifest(String version) {
        CodingSchemeManifest csm = new CodingSchemeManifest();
        String registeredName = "http://www.bioontology.org/" + version + "/fungal_anatomy.obo#";
        CsmfCodingSchemeName name = new CsmfCodingSchemeName();
        name.setContent(version + ":" + "oboManifestName");
        name.setToOverride(true);
        csm.setCodingScheme(name);
        CsmfCodingSchemeURI csmfRegisteredName = new CsmfCodingSchemeURI();
        csmfRegisteredName.setContent(registeredName);
        csm.setCodingSchemeURI(csmfRegisteredName);
        // Override version using metadata from the ontology bean
        CsmfVersion csmfVersion = new CsmfVersion();
        csmfVersion.setContent(version);
        csm.setRepresentsVersion(csmfVersion);
        // Override formal name using metadata from the ontology bean
        CsmfFormalName csmfFormalName = new CsmfFormalName();
        csmfFormalName.setContent("Fungal Anatomy");
        csm.setFormalName(csmfFormalName);
        return csm;

    }

    private CodingSchemeManifest createOWLCodingSchemeManifest(String version) {
        CodingSchemeManifest csm = new CodingSchemeManifest();
        CsmfCodingSchemeName name = new CsmfCodingSchemeName();
        name.setContent(version + ":" + "owlManifestName");
        name.setToOverride(true);
        csm.setCodingScheme(name);
        CsmfCodingSchemeURI csmfCSURI = new CsmfCodingSchemeURI();
        csm.setId("SomeID");
        csmfCSURI.setContent(AMINO_ACID_URI);
        csm.setCodingSchemeURI(csmfCSURI);
        // Override version using metadata from the ontology bean
        CsmfVersion csmfVersion = new CsmfVersion();
        csmfVersion.setContent(version);
        csm.setRepresentsVersion(csmfVersion);
        // Override formal name using metadata from the ontology bean
        CsmfFormalName csmfFormalName = new CsmfFormalName();
        csmfFormalName.setContent("My Amino Acid");
        csm.setFormalName(csmfFormalName);
        return csm;

    }

    public void testLoadGenericOwlWithManifestObject() throws InterruptedException, LBException {
        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
        CodingSchemeManifest csm = createOWLCodingSchemeManifest("01");
        loader.setCodingSchemeManifest(csm);
        
        loader.load(new File("resources/testData/amino-acid.owl").toURI(), null, 1, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        AbsoluteCodingSchemeVersionReference acsv= loader.getCodingSchemeReferences()[0];
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(acsv.getCodingSchemeVersion());
        CodingScheme codingScheme = lbs.resolveCodingScheme(acsv.getCodingSchemeURN(), versionOrTag);

        assertNotNull(codingScheme);
        String formalName = codingScheme.getFormalName();
        assertTrue("Error in Formal Name.", "My Amino Acid".equalsIgnoreCase(formalName));
    }    
    

    
    public void testRemoveLoadedOntologies() throws Exception {        
        removeCodingScheme("http://www.bioontology.org/01/fungal_anatomy.obo#", "01");
        removeCodingScheme("http://www.bioontology.org/02/fungal_anatomy.obo#", "02");
        removeCodingScheme("http://www.bioontology.org/03/fungal_anatomy.obo#", "03");
        removeCodingScheme(AMINO_ACID_URI, "01");
        
    }

    private void removeCodingScheme(String codingSchemeURI, String version) throws Exception {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                codingSchemeURI, version);
        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
    }
    
    
    private void activateCodingScheme(Loader loader, LexBIGServiceManager lbsm) throws LBException {
        AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();     
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            lbsm.activateCodingSchemeVersion(ref);
        }
    }

}