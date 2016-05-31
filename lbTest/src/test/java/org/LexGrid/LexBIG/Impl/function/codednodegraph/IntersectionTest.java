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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

import java.util.Arrays;
import java.util.List;

/**
 * The Class IntersectionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
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
        cns1 = cns1.restrictToCodes(Constructors.createConceptReferenceList(new String[] {"005", "A0001", "Chevy"}));
 
        //005,GM,Ford,A,C0001,T0001,Batteries,Brakes,Tires
        cngIntersect1 = cngIntersect1.restrictToSourceCodes(cns1);

        CodedNodeGraph cngIntersect2 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] {"GM","Ford","005"}));

        cngIntersect2 = cngIntersect2.restrictToSourceCodes(cns2);
 
        CodedNodeGraph cngIntersect = cngIntersect2.intersect(cngIntersect1);
        
        ResolvedConceptReference[] rcr = 
            cngIntersect.toNodeList(null, true, false, -1, -1).resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        List<? extends ResolvedConceptReference> list = Arrays.asList(rcr);
        
        assertTrue(DataTestUtils.isConceptReferencePresent(list, "005"));
        assertTrue(DataTestUtils.isConceptReferencePresent(list, "Ford"));
        assertTrue(DataTestUtils.isConceptReferencePresent(list, "A"));
        assertTrue(DataTestUtils.isConceptReferencePresent(list, "Chevy"));
        assertTrue(DataTestUtils.isConceptReferencePresent(list, "GM"));

        assertTrue("Length: " + rcr.length, rcr.length == 5);    
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
        cns1 = cns1.restrictToCodes(Constructors.createConceptReferenceList("Jaguar", AUTO_SCHEME));

        cngIntersect1 = cngIntersect1.restrictToCodes(cns1);
        
        CodedNodeGraph cngIntersect2 = 
            lbs.getNodeGraph(AUTO_SCHEME, null, null);
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList("Ford", AUTO_SCHEME));

        cngIntersect2 = cngIntersect2.restrictToCodes(cns2);
        
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