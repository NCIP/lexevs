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
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class PrensentationPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PresentationPropertyDataTestIT extends DataLoadTestBase {
	
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
	public void testPresentationNotNull() throws Exception {	
		assertNotNull(testEntity.getPresentation());
	}
	
	/**
	 * Test presentation count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresentationCount() throws Exception {	
		assertTrue(testEntity.getPresentation().length == 2);
	}
	
	@Test
	public void testPreferredPresentation() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		List<Presentation> preferredPres = new ArrayList<Presentation>();
		
		for(Presentation pres : preses) {
			if(pres.getIsPreferred() != null && pres.getIsPreferred()) {
				preferredPres.add(pres);
			}
		}
		
		assertTrue(preferredPres.size() == 1);
		
		assertTrue(preferredPres.get(0).getValue().getContent().equals("(131)I-MAA"));
	}
	
	@Test
	public void testNonPreferredPresentation() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		List<Presentation> preferredPres = new ArrayList<Presentation>();
		
		for(Presentation pres : preses) {
			if(pres.getIsPreferred() == null || !pres.getIsPreferred()) {
				preferredPres.add(pres);
			}
		}
		
		assertTrue(preferredPres.size() == 1);
		
		assertTrue(preferredPres.get(0).getValue().getContent().equals("(131)I-Macroaggregated Albumin"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(PresentationPropertyDataTestIT.class);  
	}  
}