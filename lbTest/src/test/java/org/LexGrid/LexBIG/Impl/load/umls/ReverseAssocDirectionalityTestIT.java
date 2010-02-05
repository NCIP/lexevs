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
package org.LexGrid.LexBIG.Impl.load.umls;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class HierarchyRootsTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReverseAssocDirectionalityTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReference graphFocus;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("ALOPE", 
				LexBIGServiceTestCase.AIR_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
	}
	
	@Test
	public void testFocusNotNull(){
		assertNotNull(graphFocus);
	}
	
	@Test
	public void testIsSourceOfNotNull(){
		assertNotNull(graphFocus.getSourceOf());
	}
	
	@Test
	public void testIsSourceOfLength(){
		assertTrue(graphFocus.getSourceOf().getAssociation().length == 1);
	}
	
	@Test
	public void testIsSourceOfAssociationName(){
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociationName().equals("PAR"));
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsNotNull(){
		assertNotNull(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts());
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsLength(){
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept().length == 1);
	}
	
	@Test
	public void testIsSourceOfAssociatedConcept(){
		assertTrue(graphFocus.getSourceOf()
				.getAssociation()[0]
				                  .getAssociatedConcepts()
				                  .getAssociatedConcept()[0].getCode().equals("U000035"));
	}
}
