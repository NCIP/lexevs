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
package org.LexGrid.valueset.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.springframework.core.annotation.Order;

/**
 * This set of tests loads the necessary data for the value set and pick list definition test.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@RunWith(OrderingTestRunner.class)
public class LoadTestDataTest {

	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;

	@Test
	@Order(0)
	public void testLoadAutombilesV1() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {
	    loadXML("resources/testData/valueDomain/Automobiles.xml", "devel");
	}

	@Test
	@Order(1)
	public void testLoadAutombilesV2() throws LBParameterException,
            LBInvocationException, InterruptedException, LBException {
	     loadXML("resources/testData/valueDomain/AutomobilesV2.xml", LBConstants.KnownTags.PRODUCTION.toString());
	}

	@Test
	@Order(2)
	public void testLoadGermanMadeParts() throws LBParameterException,
            LBInvocationException, InterruptedException, LBException {
        loadXML("resources/testData/German_Made_Parts.xml", LBConstants.KnownTags.PRODUCTION.toString());
    }
	
	private void loadXML(String fileName, String tag)throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
                .getLoader("LexGrid_Loader");
        
        // load non-async - this should block
        loader.load(new File(fileName).toURI(), true, false);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], tag);
        
        Thread.sleep(1000);
	}
	
	private void loadOWL2(String fileName, String tag) throws LBException, InterruptedException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
        
        //loader.setLoaderPreferences(new File("resources/testData/OWLPrefsLoadAnonAsAssocPF.XML").toURI());
        loader.load(new File(fileName).toURI(),null,  1, false, true);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

	/**
	 * @throws InterruptedException
	 * @throws LBException
	 */
	@Test
	@Order(3)
	public void testLoadObo() throws InterruptedException, LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

		loader.load(new File(
				"resources/testData/fungal_anatomy.obo").toURI(),
				null, true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(3000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
		
		Thread.sleep(1000);
	}
	
	/**
	 * gForge #24967
	 * Test to determine that ValueSet Services will not throw null pointer exception when there are
	 * no Value Set Definition in the system. Also to remove if any test value set definitions are
	 * present.
	 *  
	 * @throws LBException
	 * @throws URISyntaxException
	 */
	@Test
	@Order(4)
	public void testCheckValueSetDef() throws LBException, URISyntaxException{
		List<String> uris = getValueSetDefService().listValueSetDefinitions(null);
		
		// check if we missed any test valueSefDefs
		uris = getValueSetDefService().listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				assertFalse("Not all test value domains were deleted.",true);
		}
	}

	@Test
	@Order(5)
	public void testLoadPickList() throws LBException {
		getPickListService().loadPickList("resources/testData/valueDomain/pickListTestData.xml", true);
	}

	@Test
	@Order(6)
	public void testLoadValueSetDef() throws Exception {
		getValueSetDefService().loadValueSetDefinition("resources/testData/valueDomain/vdTestData.xml", true);
	}
	
	@Test
	@Order(7)
	public void testLoadValueSetDefinition() throws Exception {
				
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		loader.load(new URI("SRITEST:AUTO:AllDomesticButGM"), null, null, null);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(3000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

	}

	@Test
	@Order(8)
	public void testLoadValueSetDefinitionForLongName() throws Exception {
				
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		loader.load(new URI("SRITEST:AUTO:AllDomesticButGMWithlt250charName"), null, null, null);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(3000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

	}

	@Test
	@Order(9)
	public void testLoadOWL2() throws LBParameterException,
            LBInvocationException, InterruptedException, LBException {
		loadOWL2("resources/testData/owl2/owl2-test-cases-Primitive-Annotated.owl", 
				LBConstants.KnownTags.PRODUCTION.toString());
	}
	
	@Test
	@Order(10)
	public void testLoadOWLValueSetDef() throws Exception {
		getValueSetDefService().loadValueSetDefinition("resources/testData/valueDomain/VSD_OWL2Annotations.xml", true);
	}
	
	@Test
	@Order(11)
	public void testLoadOWL2ResolvedValueSetDefinition() throws Exception {
				
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		loader.load(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null, null);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(3000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

	}

	private LexEVSValueSetDefinitionServices getValueSetDefService(){
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}
	
	private LexEVSPickListDefinitionServices getPickListService(){
		if (pls_ == null) {
			pls_ = LexEVSPickListDefinitionServicesImpl.defaultInstance();
		}
		return pls_;
	}
}