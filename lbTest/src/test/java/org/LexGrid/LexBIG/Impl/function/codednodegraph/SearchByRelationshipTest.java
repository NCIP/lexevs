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
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

import java.util.Arrays;

/**
 * The Class SearchByRelationshipTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class SearchByRelationshipTest extends BaseCodedNodeGraphTest {

	//get all sub codes of codes that contain 'car'
	//looking for 'C0011(5564)', 'Ford', 'GM' 'A'
    public void testSearchByRelationshipChildrenOf() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null);
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    false, 
                    true, 
                    0, 
                    0, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(4,rcr.length);
        
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "C0011(5564)"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "Ford"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "GM"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "A"));
  
        for(ResolvedConceptReference ref : rcr) {
        	assertNull(ref.getSourceOf());
        	assertNull(ref.getTargetOf());
        } 
    }
    

    //get all parent codes of codes that contain 'car'
    //looking for 'A0001', 'C0001'
    public void testSearchByRelationshipParentsOf() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null);

        cng = cng.restrictToTargetCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    0, 
                    0, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(2,rcr.length);
        
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "A0001"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "C0001"));
  
        for(ResolvedConceptReference ref : rcr) {
        	assertNull(ref.getSourceOf());
        	assertNull(ref.getTargetOf());
        } 
    }
}