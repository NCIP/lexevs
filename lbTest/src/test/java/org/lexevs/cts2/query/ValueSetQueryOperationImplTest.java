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
package org.lexevs.cts2.query;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.admin.load.ValueSetLoadOperation;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetQueryOperationImplTest extends Cts2BaseTest{
	private static LexEVSValueSetDefinitionServices vds_;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		
		try {
			csLoadOp.load(new File("resources/testData/cts2/valueSets/Automobiles.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);
			csLoadOp.load(new File("resources/testData/cts2/valueSets/AutomobilesV2.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);

		} catch (LBException e) {
			e.printStackTrace();
		}		
		
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		
		URNVersionPair[] urns = vsLoadOp.load(new File(
						"resources/testData/cts2/valueSets/vdTestData.xml").toURI(), 
						null, "LexGrid_Loader", true);
		
		assertTrue("Number of VSD loaded : " + urns.length, urns.length == 18);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		AbsoluteCodingSchemeVersionReference ref =  
			Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0");
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
		
		ref = Constructors.createAbsoluteCodingSchemeVersionReference(
				"urn:oid:11.11.0.1","1.1");
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
		
		List<String> uris = getValueSetDefinitionService().listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:"))
				getValueSetDefinitionService().removeValueSetDefinition(new URI(uri));
		}
		
		// check if we missed any test valueSetDefs
		uris = getValueSetDefinitionService().listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				assertTrue("Not all test value set definitions were deleted.",false);
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkConceptValueSetMembership(java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckConceptValueSetMembership() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.0");
		
		try {
			boolean member = vsQueryop.checkConceptValueSetMembership("GMC", new URI("Automobiles"), acsvr, "SRITEST:AUTO:GM", null, null);
			assertFalse(member);			
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.1");
		
		try {
			boolean member = vsQueryop.checkConceptValueSetMembership("GMC", new URI("Automobiles"), acsvr, "SRITEST:AUTO:GM", null, null);
			assertTrue(member);			
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkValueSetSubsumption(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)}.
	 */
	@Test
	public void testCheckValueSetSubsumption() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.1");
		
		AbsoluteCodingSchemeVersionReferenceList csList = new AbsoluteCodingSchemeVersionReferenceList();
		csList.addAbsoluteCodingSchemeVersionReference(acsvr);
		
		try {
			boolean subsume = vsQueryop.checkValueSetSubsumption("SRITEST:AUTO:DomasticLeafOnly", null, "SRITEST:AUTO:EveryThing", null, csList, null);
			assertTrue(subsume);
			
			subsume = vsQueryop.checkValueSetSubsumption("SRITEST:AUTO:Ford", null, "SRITEST:AUTO:GM", null, csList, null);
			assertFalse(subsume);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#getValueSetDetails(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetValueSetDetails() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		try {
			ValueSetDefinition vsd = vsQueryop.getValueSetDetails("SRITEST:AUTO:AllDomesticButGM", null);
			
			assertTrue(vsd.getValueSetDefinitionURI().equals("SRITEST:AUTO:AllDomesticButGM"));
			assertTrue(vsd.getValueSetDefinitionName().equals("All Domestic Autos But GM"));
			assertTrue(vsd.isIsActive());
			assertTrue(vsd.getStatus().equals("ACTIVE"));
			assertTrue(vsd.getDefinitionEntryCount() == 2);
			assertTrue(vsd.getProperties().getPropertyCount() == 1);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSetContents(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSetContents() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		AbsoluteCodingSchemeVersionReference acsvr1 = new AbsoluteCodingSchemeVersionReference();
		acsvr1.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr1.setCodingSchemeVersion("1.0");
		AbsoluteCodingSchemeVersionReferenceList csList1 = new AbsoluteCodingSchemeVersionReferenceList();
		csList1.addAbsoluteCodingSchemeVersionReference(acsvr1);
		
		try {
			ResolvedValueSetDefinition vsdResolved = vsQueryop.listValueSetContents("SRITEST:AUTO:DomasticLeafOnly", null, csList1, null, null);
			assertTrue(vsdResolved.getValueSetDefinitionURI().toString().equals("SRITEST:AUTO:DomasticLeafOnly"));
			assertTrue(vsdResolved.getValueSetDefinitionName().equals("Domestic Leaf Only"));
			
			AbsoluteCodingSchemeVersionReferenceList csList = vsdResolved.getCodingSchemeVersionRefList();
			if (csList != null)
			{
				for (AbsoluteCodingSchemeVersionReference acsvr : csList.getAbsoluteCodingSchemeVersionReference())
				{
					assertTrue(acsvr.getCodingSchemeURN().equals("urn:oid:11.11.0.1"));
					assertTrue(acsvr.getCodingSchemeVersion().equals("1.0"));
				}
			}
			
			ResolvedConceptReferencesIterator cItr = vsdResolved.getResolvedConceptReferenceIterator();
			if (cItr.hasNext())
			{
				ResolvedConceptReference rcr = cItr.next();
				assertTrue(rcr.getCode().equals("Jaguar") ||
						rcr.getCode().equals("Chevy"));
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		acsvr1 = new AbsoluteCodingSchemeVersionReference();
		acsvr1.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr1.setCodingSchemeVersion("1.1");
		csList1 = new AbsoluteCodingSchemeVersionReferenceList();
		csList1.addAbsoluteCodingSchemeVersionReference(acsvr1);
		
		try {
			ResolvedValueSetDefinition vsdResolved = vsQueryop.listValueSetContents("SRITEST:AUTO:DomasticLeafOnly", null, csList1, null, null);
			assertTrue(vsdResolved.getValueSetDefinitionURI().toString().equals("SRITEST:AUTO:DomasticLeafOnly"));
			assertTrue(vsdResolved.getValueSetDefinitionName().equals("Domestic Leaf Only"));
			
			AbsoluteCodingSchemeVersionReferenceList csList = vsdResolved.getCodingSchemeVersionRefList();
			if (csList != null)
			{
				for (AbsoluteCodingSchemeVersionReference acsvr : csList.getAbsoluteCodingSchemeVersionReference())
				{
					assertTrue(acsvr.getCodingSchemeURN().equals("urn:oid:11.11.0.1"));
					assertTrue(acsvr.getCodingSchemeVersion().equals("1.1"));
				}
			}
			
			ResolvedConceptReferencesIterator cItr = vsdResolved.getResolvedConceptReferenceIterator();
			if (cItr.hasNext())
			{
				ResolvedConceptReference rcr = cItr.next();
				assertTrue(rcr.getCode().equals("Jaguar") ||
						rcr.getCode().equals("Chevy") ||
						rcr.getCode().equals("Windsor") ||
						rcr.getCode().equals("F150") ||
						rcr.getCode().equals("Focus") ||
						rcr.getCode().equals("GMC"));
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSets(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSets() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		SortOption sortOption = new SortOption();
		sortOption.setAscending(true);
		try {
			List<String> vsdURIs = vsQueryop.listValueSets("Automobiles", "Autos", null, null, sortOption);
			for (String vsdURI : vsdURIs)
			{
				assertTrue(vsdURI.equals("SRITEST:AUTO:Automobiles") ||
						vsdURI.equals("SRITEST:AUTO:DomesticAutoMakers") ||
						vsdURI.equals("SRITEST:AUTO:EveryThing"));
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listAllValueSets(org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListAllValueSets() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		SortOption sortOption = new SortOption();
		sortOption.setAscending(true);
		try {
			List<String> vsdURIs = vsQueryop.listAllValueSets(sortOption);
			assertTrue(vsdURIs.size() >= 18);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}
}