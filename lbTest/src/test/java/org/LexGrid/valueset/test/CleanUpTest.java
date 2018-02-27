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
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.admin.RemoveAllResolvedValueSets;
import org.lexgrid.valuesets.admin.RemoveResolvedValueSet;
import org.lexgrid.valuesets.admin.RemoveVSResolvedFromCodingSchemes;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.springframework.core.annotation.Order;

import junit.framework.TestCase;

/**
 * This test removes the terminologies loaded by the JUnit tests.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@RunWith(OrderingTestRunner.class)
public class CleanUpTest extends TestCase {
    
	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;
	private LexEVSResolvedValueSetService rvs_;
	
	@Test
	@Order(0)
	public void testRemoveAutombiles() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	@Test
	@Order(1)
	public void testRemoveAutombilesV2() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
        
        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.1");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	@Test
	@Order(2)
	public void testRemoveGermanMadeParts() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }

	@Test
	@Order(3)
    public void testRemoveObo() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
	@Test
	@Order(4)
    public void testRemoveOWL2() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    @Test
	@Order(5)
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
	@Order(6)
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
	@Order(7)
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
	@Order(8)
    public void testRemoveResolvedValueSet() throws Exception {
    	RemoveVSResolvedFromCodingSchemes remove_rvs= new RemoveVSResolvedFromCodingSchemes();
    	AbsoluteCodingSchemeVersionReferenceList acsvrl= remove_rvs.getCodingSchemeVersions("urn:oid:11.11.0.1::1.0");
    	remove_rvs.remove(acsvrl, true);
    }
    
    @Test 
	@Order(9)
    public void testRemoveResolvedValueSe2t() throws Exception {
    	RemoveVSResolvedFromCodingSchemes remove_rvs= new RemoveVSResolvedFromCodingSchemes();
    	AbsoluteCodingSchemeVersionReferenceList acsvrl= remove_rvs.getCodingSchemeVersions("urn:oid:11.11.0.1::1.1");
    	remove_rvs.remove(acsvrl, true);
    }
    
    @Test
	@Order(10)
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
	@Order(11)
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
    
    @Test 
	@Order(12)
    public void testRemoveAllRemainingResolvedValueSets() throws Exception{
    	RemoveAllResolvedValueSets rm = new RemoveAllResolvedValueSets();
    	List<CodingScheme> schemes = getResovledVSService().listAllResolvedValueSets();
    	for(CodingScheme scheme : schemes){
    	AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
    	ref.setCodingSchemeURN(scheme.getCodingSchemeURI());
    	ref.setCodingSchemeVersion(scheme.getRepresentsVersion());
    	rm.remove(ref, true);
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
	
	private LexEVSResolvedValueSetService getResovledVSService(){
		if (rvs_ == null) {
			rvs_ = new LexEVSResolvedValueSetServiceImpl();
		}
		return rvs_;
	}
}