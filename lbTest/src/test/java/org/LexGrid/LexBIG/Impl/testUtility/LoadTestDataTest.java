/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;

/**
 * This set of tests loads the necessary data for the full suite of JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends TestCase {
    public LoadTestDataTest(String serverName) {
        super(serverName);
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

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    public void testLoadHistory() throws InterruptedException, LBException {
 
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        NCIHistoryLoader hloader = (NCIHistoryLoader) lbsm.getLoader("NCIThesaurusHistoryLoader");

        hloader.load(new File("resources/testData/Filtered_pipe_out_12f.txt").toURI(), new File(
                "resources/testData/SystemReleaseHistory.txt").toURI(), false, true, true);

        while (hloader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(hloader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(hloader.getStatus().getErrorsLogged().booleanValue());
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

    public void testLoadHL7RIM() throws InterruptedException, LBException {

        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        HL7LoaderImpl loader = (HL7LoaderImpl) lbsm.getLoader("HL7Loader");
        loader.load(new File("resources/testData/rimSample.mdb").toURI().toString(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

    }

    public void testLoadMeta1() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaData_Loader metaLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");

        metaLoader.loadAuxiliaryData(
            new File("resources/testData/metadata1.xml").toURI(),
            Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0"),
            true, false, true);

        while (metaLoader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(metaLoader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(metaLoader.getStatus().getErrorsLogged().booleanValue());
    }

    public void testLoadMeta2() throws InterruptedException, LBException {
        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MetaData_Loader metaLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");

        metaLoader.loadAuxiliaryData(
            new File("resources/testData/metadata2.xml").toURI(),
            Constructors.createAbsoluteCodingSchemeVersionReference("test.2", "2.0"),
            true, true, true);

        while (metaLoader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        assertTrue(metaLoader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(metaLoader.getStatus().getErrorsLogged().booleanValue());
    }
    
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
    
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
}