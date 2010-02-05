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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class HierarchyRootsTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HierarchyRootsTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReferenceList roots;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		LexBIGServiceConvenienceMethods lbscm = 
			(LexBIGServiceConvenienceMethods)lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
		
		roots = lbscm.getHierarchyRoots(LexBIGServiceTestCase.AIR_URN, null, null);
	}
	
	@Test
	public void testRootsNotNull(){
		assertNotNull(roots);
	}
	
	@Test
	public void testRootsLength(){
		assertTrue("Roots: " + roots.getResolvedConceptReferenceCount(), roots.getResolvedConceptReferenceCount() == 65);
	}
}
