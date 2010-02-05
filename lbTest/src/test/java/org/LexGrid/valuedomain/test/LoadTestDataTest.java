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
package org.LexGrid.valuedomain.test;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.loaders.LexGridLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.junit.Test;
import org.lexgrid.valuedomain.LexEVSPickListServices;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.impl.LexEVSPickListServicesImpl;
import org.lexgrid.valuedomain.impl.LexEVSValueDomainServicesImpl;

/**
 * This set of tests loads the necessary data for the valuedomain 
 * suite of JUnit tests.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends TestCase {
	private LexEVSValueDomainServices vds_;
	private LexEVSPickListServices pls_;
	
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
        loadXML("resources/testData/valueDomain/German Made Parts.xml", LBConstants.KnownTags.PRODUCTION.toString());
    }
	
	private void loadXML(String fileName, String tag)throws LBException, InterruptedException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
        .getServiceManager(null);

        LexGridLoaderImpl loader = (LexGridLoaderImpl) lbsm
                .getLoader("LexGridLoader");
        
        // load non-async - this should block
        loader.load(new File(fileName).toURI(), true, false);
        
        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(1000);
        }
        assertTrue(loader.getStatus().getEndTime() != null);
        assertTrue(loader.getStatus().getState().getType() == ProcessState.COMPLETED_TYPE);
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], tag);
	}

	/**
	 * @throws InterruptedException
	 * @throws LBException
	 */
	public void testLoadObo() throws InterruptedException, LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

		loader.load(new File(
				"resources/testData/valueDomain/fungal_anatomy.obo").toURI(),
				null, true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		assertTrue(loader.getStatus().getState().getType() == ProcessState.COMPLETED_TYPE);
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
		getValueDomainService().loadValueDomain("resources/testData/valueDomain/vdTestData.xml", true);
	}

	private LexEVSValueDomainServices getValueDomainService(){
		if (vds_ == null) {
			vds_ = new LexEVSValueDomainServicesImpl();
		}
		return vds_;
	}
	
	private LexEVSPickListServices getPickListService(){
		if (pls_ == null) {
			pls_ = new LexEVSPickListServicesImpl();
		}
		return pls_;
	}
}