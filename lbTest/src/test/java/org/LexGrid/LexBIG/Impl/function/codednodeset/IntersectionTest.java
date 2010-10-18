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
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class IntersectionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IntersectionTest extends BaseCodedNodeSetTest {
    
    /** The cns2. */
    private CodedNodeSet cns2;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "CodedNodeSet Intersection Test";
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#setUp()
     */
    @Override
    public void setUp(){
        super.setUp();
        try {
            cns2 = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AUTO_SCHEME, null);
        } catch (LBException e) {
          fail(e.getMessage());
        }
    }

    /**
     * Test intersection.
     * 
     * @throws LBException the LB exception
     */
    public void testIntersection() throws LBException {

        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet intersect = cns.intersect(cns2);

        ResolvedConceptReference[] rcr = intersect.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
    /**
     * Test intersection with added restriction.
     * 
     * @throws LBException the LB exception
     */
    public void testIntersectionWithAddedRestriction() throws LBException {

        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy", "Ford" }, "Automobiles"));

        cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet cns3 = cns.intersect(cns2);
        
        cns3.restrictToCodes(Constructors.createConceptReferenceList("Ford"));

        ResolvedConceptReference[] rcr = cns3.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 1);
        assertTrue(contains(rcr, "Ford", "Automobiles"));
    }
}