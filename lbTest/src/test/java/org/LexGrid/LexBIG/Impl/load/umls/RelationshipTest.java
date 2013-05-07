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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class RelAssociationTest.
 * 
 * @author <a href="mailto:suesse.dale@mayo.edu">Dale Suesse</a>
 */
public class RelationshipTest {

	private CodedNodeGraph cng;
	
	@Before
	public void setUp() throws LBException{
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.AIR_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AIR_VERSION), null);
	}
	
	@Test
	public void testRelationshipDirection() throws Exception {
		ConceptReference cRefSource = ConvenienceMethods.createConceptReference("ANXIE", LexBIGServiceTestCase.AIR_VERSION);
		ConceptReference cRefTarget = ConvenienceMethods.createConceptReference("U000052", LexBIGServiceTestCase.AIR_VERSION);
		assertNotNull(cng);
		List<String> relationships = cng.listCodeRelationships(cRefSource, cRefTarget, true);
		assertEquals(1, relationships.size());
		assertEquals("CHD", relationships.get(0));
		
		/* The UMLS loader is only loading child relationships to prevent redundant data. */
		relationships = cng.listCodeRelationships(cRefTarget, cRefSource, true);
		assertEquals(0, relationships.size());
	}

}
