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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

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
    
    //@Test
    //This is not guarenteed to run before the method below... and will fail if
    //so. Let the below method remove all.
	//public void testRemoveValueDomain() throws LBException, URISyntaxException {
	//	getValueDomainService().removeValueSetDefinition(new URI("SRITEST:AUTO:EveryThing"));
	//}
	
	@Test
	public void testRemoveAllTestValueDomains() throws LBException, URISyntaxException {
		List<String> uris = getValueDomainService().listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:"))
				getValueDomainService().removeValueSetDefinition(new URI(uri));
		}
		
		// check if we missed any test valueDomains
		uris = getValueDomainService().listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				assertFalse("Not all test value domains were deleted.",true);
		}
	}
	
	/**
	 * Test method for {@link org.lexgrid.valuedomain.impl.LexEVSPickListServicesImpl#removePickList(java.lang.String)}.
	 * @throws LBException 
	 * @throws RemoveException 
	 */
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
	
	/**
	 * Test method for {@link org.lexgrid.valuedomain.impl.LexEVSPickListServicesImpl#removeAllPickLists()}.
	 * @throws LBException 
	 * @throws RemoveException 
	 */
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
	
//	@Test
//	public void testDropValueDomainTables() throws LBException {
//		List<String> pickListIds = getPickListService().listPickListIds();
//		List<String> uris = getValueDomainService().listValueSetDefinitions(null);
//		if (pickListIds.size() == 0 && uris.size() == 0)
//		{
//			getValueDomainService().dropValueDomainTables();
//			assertTrue(true);
//		}
//		else
//		{
//			assertFalse("Can not delete valueDomain tables when entries exists.", true);
//		}
//	}
	
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