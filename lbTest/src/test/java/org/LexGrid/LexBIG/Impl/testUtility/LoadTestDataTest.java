/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.testUtility;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
import org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MedDRALoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
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

/**
 * This set of tests loads the necessary data for the full suite of JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends LexBIGServiceTestCase {
    
    @Override
	protected String getTestID() {
		return LoadTestDataTest.class.getName();
	}

    public void testLoadAutombiles() throws LBParameterException, LBInvocationException, InterruptedException,
            LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/Automobiles.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }
    
    public void testLoadAutombilesExtension() throws LBParameterException, LBInvocationException, InterruptedException,
    LBException {
    	LexBIGServiceManager lbsm = getLexBIGServiceManager();

    	LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

    	loader.load(new File("resources/testData/testExtension.xml").toURI(), true, true);

    	while (loader.getStatus().getEndTime() == null) {
    		Thread.sleep(500);
    	}

    	assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
    	assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

    	lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

    	lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    	
    	lbsm.registerCodingSchemeAsSupplement(
    			Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION), 
    			Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_EXTENSION_URN, AUTO_EXTENSION_VERSION));
    			
    }

    public void testLoadGermanMadeParts() throws LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        // load non-async - this should block
        loader.load(new File("resources/testData/German_Made_Parts.xml").toURI(), true, false);

        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }
    
    public void testLoadNCIMeta() throws Exception {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaBatchLoader loader = (MetaBatchLoader) lbsm.getLoader("MetaBatchLoader");

        loader.loadMeta(new File("resources/testData/sampleNciMeta").toURI());

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
    }

    public void testLoadNCItHistory() throws InterruptedException, LBException {
 
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        NCIHistoryLoader hloader = (NCIHistoryLoader) lbsm.getLoader("NCIThesaurusHistoryLoader");

        hloader.load(new File("resources/testData/Filtered_pipe_out_12f.txt").toURI(), new File(
                "resources/testData/SystemReleaseHistory.txt").toURI(), false, true, false);

        assertEquals(ProcessState.COMPLETED,hloader.getStatus().getState());
        assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
    }
    
    public void testLoadMetaHistory() throws LBException {
        ServiceHolder.configureForSingleConfig();
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        UMLSHistoryLoader loader = (UMLSHistoryLoader) lbsm
                .getLoader(org.LexGrid.LexBIG.Impl.loaders.UMLSHistoryLoaderImpl.name);
        loader.load((new File("resources/testData/sampleNciMetaHistory")).toURI(), false, true, false);

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
    }

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

    public void testLoadOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");
 
        CodingSchemeManifest csm = new CodingSchemeManifest();
        CsmfCodingSchemeURI uri = new CsmfCodingSchemeURI();
        csm.setId("http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl#");
        uri.setContent("http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl#");
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
    
    public void testLoadOwl2() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");
 
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


    public void testLoadOwlLoaderPreferences() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");

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

    public void testLoadGenericOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
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

    public void testLoadGenericOwlWithInstanceData() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
        loader.load(new File("resources/testData/OvarianMass_SNOMED_ValueSets.owl").toURI(), null,  1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }
    
    public void testLoadGenericOwlWithNPOsansQuals() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
        loader.load(new File("resources/testData/npotest.owl").toURI(), null,  1, false, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }
    
    public void testLoadCompPropsOwl() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        OWLLoaderImpl loader = (OWLLoaderImpl) lbsm.getLoader("OWLLoader");
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

    public void testLoadNCIMeta2() throws Exception {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaBatchLoader loader = (MetaBatchLoader) lbsm.getLoader("MetaBatchLoader");

        loader.loadMeta(new File("resources/testData/SAMPLEMETA").toURI());
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    public void testLoadMedDRA() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	File accessPath = new File("resources/testData/medDRA");

        MedDRA_Loader loader = (MedDRALoaderImpl) lbsm.getLoader("MedDRALoader");
        loader.load(accessPath.toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

  public void testLoadHL7JMifVocabularyForBadSource() throws LBException, InterruptedException{
  LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
  MIFVocabularyLoader loader = null;
	try {
		lbsm = getLexBIGServiceManager();
  	loader = (MIFVocabularyLoaderImpl) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl.name);
      loader.load(new File("resources/testData/German_Made_Parts.xml").toURI(), true, false);
	} catch (RuntimeException e) {
		assertEquals("Source file is invalid. Please check to see if this is a valid HL7 vocabulary mif file", e.getMessage());
	}finally{
     while (loader.getStatus().getEndTime() == null) {
         Thread.sleep(1000);
     }
	}
}
    public void testLoadHL7MifVocabulary() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	File accessPath = new File("resources/testData/hl7MifVocabulary/DEFN=UV=VO=1189-20121121.coremif");

    	MIFVocabularyLoader loader = (MIFVocabularyLoaderImpl) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl.name);
        loader.load(accessPath.toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }


    
    private LexBIGServiceManager getLexBIGServiceManager() throws LBException {
    	return ServiceHolder.instance().getLexBIGService().getServiceManager(null);
    }	
}