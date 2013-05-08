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
package org.lexevs.cts2.admin.load;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetLoadOperationImplTest {
	private LexEVSValueSetDefinitionServices vds_;
	
	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadURIURIStringBoolean() throws LBException {
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		
		URNVersionPair[] urns = vsLoadOp.load(new File(
						"resources/testData/cts2/valueSets/vdTestData.xml").toURI(), 
						null, "LexGrid_Loader", true);
		
		assertTrue("Number of VSD loaded : " + urns.length, urns.length == 18);	

		urns = vsLoadOp.load(new File(
						"resources/testData/cts2/valueSets/VSDOnlyTest.xml").toURI(), 
						null, "LexGrid_Loader", true);

		assertTrue("Number of VSD loaded : " + urns.length, urns.length == 1);
		
		for (URNVersionPair urn : urns) {
			assertTrue(urn.getUrn().equals("SRITEST:AUTO:PropertyRefTest1-VSDONLY"));
			assertTrue(urn.getVersion().equals("R001"));
		}		
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.ValueSetDefinition, java.net.URI, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadValueSetDefinitionURIBoolean() throws LBException {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:JUNIT:TEST:VSD1");
		vsd.setValueSetDefinitionName("JUnit test vsd 1");
		vsd.setConceptDomain("cd");
		vsd.addRepresentsRealmOrContext("context");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setIsActive(false);
		vsd.setOwner("cts2");
		List<Source> srcList = new ArrayList<Source>();
		Source src = new Source();
		src.setContent("lexevs");
		srcList.add(src);
		src = new Source();
		src.setContent("cts2");
		srcList.add(src);
		vsd.setSource(srcList);
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		CodingSchemeReference csr = new CodingSchemeReference();
		csr.setCodingScheme("Automobiles");
		de.setCodingSchemeReference(csr);
		vsd.addDefinitionEntry(de);
		
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		String vsdURI = vsLoadOp.load(vsd, null, true);
		
		assertTrue(vsdURI.equals("SRITEST:JUNIT:TEST:VSD1"));
	}
	
	@Test
	public void testRemoveAllTestValueSetDefs() throws LBException, URISyntaxException {
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
				assertTrue("Not all test value domains were deleted.",false);
		}
	}
	
	private LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}
}