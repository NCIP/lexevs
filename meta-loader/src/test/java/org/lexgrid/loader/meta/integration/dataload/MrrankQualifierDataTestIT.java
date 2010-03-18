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
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.test.util.DataTestUtils;

/**
 * The Class MrrankQualifierDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrankQualifierDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Presentation[] presentation;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void buildTestEntity() throws Exception {
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
		ResolvedConceptReference rcr = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		presentation = rcr.getEntity().getPresentation();
	}
	
	@Test
	public void testPresentationNotNull() throws Exception {	
		assertNotNull(presentation);
	}
	
	@Test
	public void testFirstMrrankQualifierExists() throws Exception {	
		assertTrue(
				DataTestUtils.getPropertyQualifiersFromProperty(
						presentation[0], RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME).size() > 0);
	}
	
	@Test
	public void testSecondMrrankQualifierExists() throws Exception {	
		assertTrue(
				DataTestUtils.getPropertyQualifiersFromProperty(
						presentation[1], RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME).size() > 0);
	}
	
	@Test
	public void testFirstMrrankQualifierValue() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(presentation, "(131)I-MAA");
		
		assertTrue(
						DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME, "310", prop)
						
		);
	}
	
	@Test
	public void testSecondMrrankQualifierValue() throws Exception {	
	Property prop = DataTestUtils.getPropertyWithValue(presentation, "(131)I-Macroaggregated Albumin");
		
		assertTrue(
						DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME, "100", prop)
						
		);
	}
}