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
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class IntersectionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IntersectionTest extends BaseCodedNodeGraphTest {

    /**
     * Test intersect.
     * 
     * @throws Exception the exception
     */
    public void testIntersect() throws Exception {
        CodedNodeGraph cngIntersect1 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns1.restrictToCodes(Constructors.createConceptReferenceList(new String[] {"005", "A0001", "Chevy"}));
 
        cngIntersect1.restrictToSourceCodes(cns1);

        CodedNodeGraph cngIntersect2 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] {"GM","Ford","005"}));
            
        cngIntersect2.restrictToSourceCodes(cns2);
 
        CodedNodeGraph cngIntersect = cngIntersect2.intersect(cngIntersect1);
        
        ResolvedConceptReference[] rcr = 
            cngIntersect.resolveAsList(null, 
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
     * Test intersect no match.
     * 
     * @throws Exception the exception
     */
    
    public void testIntersectNoMatch() throws Exception {
        CodedNodeGraph cngIntersect1 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns1.restrictToCodes(Constructors.createConceptReferenceList("T0001", AUTO_SCHEME));
 
        cngIntersect1.restrictToTargetCodes(cns1);
        
        CodedNodeGraph cngIntersect2 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns2.restrictToCodes(Constructors.createConceptReferenceList("GM", AUTO_SCHEME));
            
        cngIntersect2.restrictToTargetCodes(cns2);
        
        CodedNodeGraph cngIntersect = cngIntersect1.intersect(cngIntersect2);
        
        ResolvedConceptReference[] rcr = 
            cngIntersect.resolveAsList(null, 
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
