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
package org.LexGrid.LexBIG.Impl.load.umls;

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
		assertEquals(1, graphFocus.getSourceOf().getAssociation().length);
	}
	
	@Test
	public void testIsSourceOfAssociationName(){
		assertEquals("CHD", graphFocus.getSourceOf().getAssociation()[0].getAssociationName());
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsNotNull(){
		assertNotNull(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts());
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsLength(){
		assertEquals(1, graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept().length);
	}
	
	@Test
	public void testIsSourceOfAssociatedConcept(){
		assertEquals("U000035", graphFocus.getSourceOf()
				.getAssociation()[0]
				                  .getAssociatedConcepts()
				                  .getAssociatedConcept()[0].getCode());
	}
}