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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToTargetCodesTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToTargetCodesTest extends BaseCodedNodeGraphTest {

    /**
     * Test restrict to target codes.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("Ford", AUTO_SCHEME));

        cng = cng.restrictToTargetCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertTrue("Length: " + rcr.length, rcr.length == 1);

        ResolvedConceptReference ref = rcr[0];
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    /**
     * Test restrict to target codes two codes.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodesTwoCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"Ford", "T0001"}));

        cng = cng.restrictToTargetCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertTrue("Length: " + rcr.length, rcr.length == 2);

        resolvedConceptListContains(rcr, "005");
        resolvedConceptListContains(rcr, "A0001");
    }
    
    /**
     * Test restrict to target codes no match.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodesNoMatch() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"NO_MATCH"}));

        cng = cng.restrictToTargetCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertTrue("Length: " + rcr.length, rcr.length == 0);
    }
}