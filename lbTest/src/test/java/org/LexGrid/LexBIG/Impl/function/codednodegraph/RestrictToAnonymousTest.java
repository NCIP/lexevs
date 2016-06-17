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
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.LexGrid.util.PrintUtility;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToAnonymousTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToAnonymousTest extends BaseCodedNodeGraphTest {

	@Test
    public void testRestrictToNonAnonymousForMappingScheme() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(false);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("C0001", AUTO_SCHEME, AUTO_SCHEME), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        assertTrue(rcr[0].getSourceOf() != null);
    }
    
	@Test
    public void testRestrictToAnonymous() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(true);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME, AUTO_SCHEME), 
                    false, 
                    true, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        PrintUtility.print(rcr[0]);
        
        assertTrue(rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept()[0].getCode().equals("@"));
    }
    
	@Test
    public void testRestrictToNonAnonymous() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(false);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME, AUTO_SCHEME), 
                    false, 
                    true, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        PrintUtility.print(rcr[0]);
        
        assertNull(rcr[0].getTargetOf());
    }
   
}