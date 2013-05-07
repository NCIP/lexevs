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

import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class DefinitionPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefinitionPropertyDataTestIT extends DataLoadTestBase {

	/** The test entity with definition. */
	private Entity testEntityWithDefinition;
	
	/** The test entity with definition. */
	private Entity testEntityWithTwoDefinitions;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000039"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntityWithDefinition = rcr1.getEntity();
		
		cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000097"));
		ResolvedConceptReference rcr2 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntityWithTwoDefinitions = rcr2.getEntity();
	}
	
	@Test
	public void testDefinitionNotNull() throws Exception {	
		assertNotNull(testEntityWithDefinition.getDefinition());
	}
	
	/**
	 * Test definition count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testDefinitionCount() throws Exception {	
		assertTrue(testEntityWithDefinition.getDefinition().length == 1);
	}
	
	@Test
	public void testTwoDefinitionsNotNull() throws Exception {	
		assertNotNull(testEntityWithDefinition.getDefinition());
	}
	
	/**
	 * Test definition count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTwoDefinitionsCount() throws Exception {	
		assertTrue(testEntityWithTwoDefinitions.getDefinition().length == 2);
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(DefinitionPropertyDataTestIT.class);  
	}  
}