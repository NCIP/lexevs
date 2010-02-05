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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.managedobj.RemoveException;
import org.junit.Test;
import org.lexgrid.valuedomain.LexEVSPickListServices;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.impl.LexEVSPickListServicesImpl;
import org.lexgrid.valuedomain.impl.LexEVSValueDomainServicesImpl;

/**
 * This test removes the terminologies loaded by the JUnit tests.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUpTest extends TestCase {
    
	private LexEVSValueDomainServices vds_;
	private LexEVSPickListServices pls_;
	
	public void testRemoveAutombiles() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	public void testRemoveAutombilesV2() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "2.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	public void testRemoveGermanMadeParts() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }

    public void testRemoveObo() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    @Test
	public void testRemoveValueDomain() throws LBException, URISyntaxException, RemoveException {
		getValueDomainService().removeValueDomain(new URI("SRITEST:AUTO:EveryThing"));
	}
	
	@Test
	public void testRemoveAllTestValueDomains() throws LBException, RemoveException {
		URI[] uris = getValueDomainService().listValueDomains(null);
		assertTrue(uris.length > 0);
		
		for (URI uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				getValueDomainService().removeValueDomain(uri);
		}
		
		// check if we missed any test valueDomains
		uris = getValueDomainService().listValueDomains(null);
		
		for (URI uri : uris)
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
	public void testRemovePickList() throws RemoveException {
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
	public void testRemoveAllTestPickLists() throws LBException, RemoveException {
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
	public void testDropValueDomainTables() throws LBException {
		List<String> pickListIds = getPickListService().listPickListIds();
		URI[] uris = getValueDomainService().listValueDomains(null);
		if (pickListIds.size() == 0 && uris.length == 0)
		{
			try {
				getValueDomainService().dropValueDomainTables();
			} catch (RemoveException e) {
				assertFalse("Can not delete valueDomain tables when value domain or pick list entries exists.", true);
			}
			assertTrue(true);
		}
		else
		{
			assertFalse("Can not delete valueDomain tables when entries exists.", true);
		}
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