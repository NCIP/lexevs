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
package org.LexGrid.valuedomain.test;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * This set of tests loads the necessary data for the value set and pick list definition test.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends TestCase {
	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;
	
	public LoadTestDataTest(String serverName) {
		super(serverName);
	}

	public void testLoadAutombilesV1() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {
	    loadXML("resources/testData/valueDomain/Automobiles.xml", "devel");
	}
	
	public void testLoadAutombilesV2() throws LBParameterException,
            LBInvocationException, InterruptedException, LBException {
	     loadXML("resources/testData/valueDomain/AutomobilesV2.xml", LBConstants.KnownTags.PRODUCTION.toString());
	}

	public void testLoadGermanMadeParts() throws LBParameterException,
            LBInvocationException, InterruptedException, LBException {
        loadXML("resources/testData/German_Made_Parts.xml", LBConstants.KnownTags.PRODUCTION.toString());
    }
	
	private void loadXML(String fileName, String tag)throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
                .getLoader("LexGrid_Loader");
        
        // load non-async - this should block
        loader.load(new File(fileName).toURI(), true, false);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(2000);
        }
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], tag);
	}

	/**
	 * @throws InterruptedException
	 * @throws LBException
	 */
	public void testLoadObo() throws InterruptedException, LBException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

		OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

		loader.load(new File(
				"resources/testData/fungal_anatomy.obo").toURI(),
				null, true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(2000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}
	
	@Test
	public void testLoadPickList() throws LBException {
		getPickListService().loadPickList("resources/testData/valueDomain/pickListTestData.xml", true);
	}
	
	@Test
	public void testLoadValueDomain() throws Exception {
		getValueDomainService().loadValueSetDefinition("resources/testData/valueDomain/vdTestData.xml", true);
	}

	private LexEVSValueSetDefinitionServices getValueDomainService(){
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