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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.admin.RemoveResolvedValueSet;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import junit.framework.TestCase;

/**
 * This test removes the terminologies loaded by the JUnit tests.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUpTest extends TestCase {
    
	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;
	
	public void testRemoveAutombiles() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	public void testRemoveAutombilesV2() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
        
        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.1");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	public void testRemoveGermanMadeParts() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }

    public void testRemoveObo() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    public void testRemoveOWL2() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.1");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    @Test
	public void testRemoveAllTestValueSetDefs() throws LBException, URISyntaxException {
		List<String> uris = getValueSetDefService().listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:") || uri.startsWith("OWL2LEXEVS:") 
					|| uri.startsWith("XTEST:"))
				getValueSetDefService().removeValueSetDefinition(new URI(uri));
		}
		
		// check if we missed any test valueSetDefs
		uris = getValueSetDefService().listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:") || uri.toString().startsWith("OWL2LEXEVS:")
					|| uri.startsWith("XTEST:"))
				assertFalse("Not all test value domains were deleted.",true);
		}
	}
	
	@Test
	public void testRemovePickList() {
		try {
			getPickListService().removePickList("SRITEST:AUTO:DomesticAutoMakers");
		} catch (LBException e) {
			assertFalse("problem removing pickList.", true);
		}
		
		PickListDefinition pickList = null;
		try {
			pickList = getPickListService().getPickListDefinitionById("SRITEST:AUTO:DomesticAutoMakers");
			assertTrue(pickList == null);
		} catch (LBException e) {
			if (e.getMessage().indexOf("Problem gettingPickLists") != -1)
				assertTrue(pickList == null);
		}
		
		
	}
	
	@Test
	public void testRemoveAllTestPickLists() throws LBException {
		List<String> pickListIds = getPickListService().listPickListIds();
		for (String pickListId : pickListIds)
		{
			if (pickListId.startsWith("SRITEST"))
				getPickListService().removePickList(pickListId);
		}
		
		// check if we missed any test pick lists
		pickListIds = getPickListService().listPickListIds();
		for (String pickListId : pickListIds)
		{
			if (pickListId.startsWith("SRITEST"))
				assertFalse("Not all test pick lists were deleted.", true);
		}
		
		pickListIds.clear();
	}
	
    @Test 
    public void testRemoveResolvedValueSet() throws Exception {
    	RemoveResolvedValueSet remove_rvs= new RemoveResolvedValueSet();
    	AbsoluteCodingSchemeVersionReferenceList acsvrl= remove_rvs.getCodingSchemeVersions("urn:oid:11.11.0.1::1.0");
    	remove_rvs.remove(acsvrl, true);
    }
    
    @Test 
    public void testRemoveResolvedValueSe2t() throws Exception {
    	RemoveResolvedValueSet remove_rvs= new RemoveResolvedValueSet();
    	AbsoluteCodingSchemeVersionReferenceList acsvrl= remove_rvs.getCodingSchemeVersions("urn:oid:11.11.0.1::1.1");
    	remove_rvs.remove(acsvrl, true);
    }
    
    @Test
    public void testRemoveResolvedAllButGM() throws LBParameterException, LBInvocationException, LBException{
    	LexBIGServiceManager lbsm = null;
    	AbsoluteCodingSchemeVersionReference acsvr = null;
    
         lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
         acsvr = Constructors.
    			createAbsoluteCodingSchemeVersionReference("SRITEST:AUTO:AllDomesticButGM", "12.03test");

         try{
        	 lbsm.deactivateCodingSchemeVersion(acsvr , null);  
        	 fail("LBParameterException should be thrown");
         }
         catch(LBParameterException e){
        	 assertEquals(e.getMessage(), "Could not find Resource:SRITEST:AUTO:AllDomesticButGM, 12.03test");
         }
         try{
        	 lbsm.removeCodingSchemeVersion(acsvr);
          	 fail("RuntimeException should be thrown");
         }
         catch(RuntimeException e){
        	 assertEquals(e.getMessage(), "No CodingScheme Entry for URI: SRITEST:AUTO:AllDomesticButGM, Version: 12.03test");
         }
    }
    
    @Test 
    public void testRemoveXTest() throws LBException{

    	LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    	AbsoluteCodingSchemeVersionReference acsvr = Constructors.
    			createAbsoluteCodingSchemeVersionReference("XTEST:One.Node.ValueSet", "1.0");
    	try {
    		lbsm.deactivateCodingSchemeVersion(acsvr , null);
    		fail("LBParameterException should be thrown");
    	}
    	catch(LBParameterException e){
    		assertEquals(e.getMessage(), "Could not find Resource:XTEST:One.Node.ValueSet, 1.0");
    	}
    	try{
    		lbsm.removeCodingSchemeVersion(acsvr);
    		fail("RuntimeException should be thrown");
    	}
    	catch(RuntimeException e){
    		assertEquals(e.getMessage(), "No CodingScheme Entry for URI: XTEST:One.Node.ValueSet, Version: 1.0");
    	}
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