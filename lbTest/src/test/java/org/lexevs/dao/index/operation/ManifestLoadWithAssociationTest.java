package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;

public class ManifestLoadWithAssociationTest {
	private static String codingSchemeURI  = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	private static String sampleVersion = "16.Teste";
	private static String cpVersion = "05.09.comp.prop.bvt";
	private String tag = "PRODUCTION";

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
		AbsoluteCodingSchemeVersionReference scheme = Constructors.
				createAbsoluteCodingSchemeVersionReference(codingSchemeURI, sampleVersion);
		lbsm.setVersionTag(scheme, tag);
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

	}
	
	@Test
	 public void LoadOwlWithConflictingProductionTag() throws InterruptedException, LBException {
		
		        LexBIGServiceManager lbsm = getLexBIGServiceManager();

		        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
		        loader.setLoaderPreferences(new File("resources/testData/OWLPrefs.xml").toURI());
		        
		        CodingSchemeManifest csm = new CodingSchemeManifest();
		        csm.setId(codingSchemeURI);
		        CsmfCodingSchemeURI uri = new CsmfCodingSchemeURI();
		        uri.setContent(codingSchemeURI);
		        uri.setToOverride(true);
		        csm.setCodingSchemeURI(uri);
		        loader.setCodingSchemeManifest(csm);
		        
		        loader.load(new File("resources/testData/sample.cp.2.owl").toURI(),
		                null, 0, true, true);
		        
		        while (loader.getStatus().getEndTime() == null) {
		            Thread.sleep(500);
		        }
		        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		        
		        //Get the registry and check that the first load of the sample has the tag
		        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		        
		        List<RegistryEntry> sampEntries = registry.getAllRegistryEntriesOfTypeURIAndVersion(
		        		ResourceType.CODING_SCHEME, codingSchemeURI, sampleVersion);
		        assertTrue(sampEntries.size() == 1);
		        RegistryEntry sampEntry = sampEntries.get(0);	
		        assertTrue(sampEntry.getTag().equals(tag));
		        
				AbsoluteCodingSchemeVersionReference scheme = Constructors.
						createAbsoluteCodingSchemeVersionReference(codingSchemeURI, cpVersion);
				
				//Tag the second load of the sample
				lbsm.setVersionTag(scheme, tag);
		        
				//Refresh the registry. Check the first load and see if it is still tagged
				//Should not be. 
		        registry = LexEvsServiceLocator.getInstance().getRegistry();
		        
		        List<RegistryEntry> entries = registry.getAllRegistryEntriesOfTypeURIAndVersion(
		        		ResourceType.CODING_SCHEME, codingSchemeURI, sampleVersion);
		        assertTrue(entries.size() == 1);
		        RegistryEntry entry = entries.get(0);	
		        assertFalse(entry.getTag().equals(tag));
		        
		        //Shouldn't need to refresh the registry again, just get the
		        //second sample and see if it's tagged

		        List<RegistryEntry> cpentries = registry.getAllRegistryEntriesOfTypeURIAndVersion(
		        		ResourceType.CODING_SCHEME, codingSchemeURI, cpVersion);
		        assertTrue(cpentries.size() == 1);
		        RegistryEntry cpentry = cpentries.get(0);	
		        assertTrue(cpentry.getTag().equals(tag));

	 }

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = getLexBIGServiceManager();

		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeURI);
		ref.setCodingSchemeVersion(sampleVersion);
		lbsm.deactivateCodingSchemeVersion(ref, null);
		lbsm.removeCodingSchemeVersion(ref);
		

		AbsoluteCodingSchemeVersionReference ref1 = Constructors.
				createAbsoluteCodingSchemeVersionReference(codingSchemeURI, cpVersion);
		lbsm.deactivateCodingSchemeVersion(ref1, null);
		lbsm.removeCodingSchemeVersion(ref1);
	}

	private static LexBIGServiceManager getLexBIGServiceManager() throws LBException {
		return ServiceHolder.instance().getLexBIGService().getServiceManager(null);
	}

}
