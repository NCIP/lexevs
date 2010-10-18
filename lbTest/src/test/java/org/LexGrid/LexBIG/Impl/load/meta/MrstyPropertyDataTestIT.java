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
package org.LexGrid.LexBIG.Impl.load.meta;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class MrstyPropertyDataTestIT
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyPropertyDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Entity testEntity;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	@Test
	public void testSemanticTypePropertyNotNull() throws Exception {
		List<Property> semTypeProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY);
		assertNotNull(semTypeProps);
	}

	@Test
	public void testSemanticTypePropertyCount() throws Exception {
		List<Property> semTypeProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY);
		assertTrue(semTypeProps.size() == 3);
	}
	
	@Test
	public void testSemanticTypePropertyValue1() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Amino Acid, Peptide, or Protein"));
	}
	
	@Test
	public void testSemanticTypePropertyValue2() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Pharmacologic Substance"));
	}
	
	@Test
	public void testSemanticTypePropertyValue3() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Indicator, Reagent, or Diagnostic Aid"));
	}
}