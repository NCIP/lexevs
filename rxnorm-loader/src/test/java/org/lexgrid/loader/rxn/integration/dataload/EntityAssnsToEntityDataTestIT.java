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
package org.lexgrid.loader.rxn.integration.dataload;

import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.test.util.DataTestUtils;

import util.integration.LoadRxnForIntegration;

/**
 * The Class EntityAssnsToEntityDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityDataTestIT extends DataLoadTestBase {
	
	/** The graph focus. */
	private ResolvedConceptReference graphFocus;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void buildTestEntity() throws Exception {
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("ACRMG", 
				LoadRxnForIntegration.RXN_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
	}
	
	/**
	 * Test source count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceCount() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation().length == 1);
	}
	
	/**
	 * Test source association name.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociationName() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociationName().equals("PAR"));	
	}
	
	/**
	 * Test source associated concept count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCount() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 3);	
	}
	
	/**
	 * Test source associated concept code.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCode() throws Exception {	
		AssociatedConcept[] concepts = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();	

		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "U000010"));
		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "MFCON"));
		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "MFEXT"));
	}
	
	/**
	 * Test target count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTargetCount() throws Exception {	
		assertTrue(graphFocus.getTargetOf() == null);
	}
}
