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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.LexGrid.util.PrintUtility;

/**
 * The Class IntersectionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class IntersectionTest extends BaseCodedNodeSetTest {
    
    /** The cns2. */
    private CodedNodeSet cns2;
    private CodedNodeSet cns3;
    private CodedNodeSet testMapping;
    
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
            cns2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            cns3 = lbs.getCodingSchemeConcepts(PARTS_SCHEME, null);
            testMapping = lbs.getCodingSchemeConcepts(MAPPING_SCHEME_URI, null);
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

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet intersect = cns.intersect(cns2);

        ResolvedConceptReference[] rcr = intersect.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
    public void testIntersectionDifferentCodingSchemes() throws LBException {

    	CodedNodeSet intersect = cns.intersect(cns3);
  
        ResolvedConceptReference[] rcr = intersect.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertEquals(0, rcr.length);
    }
    
    //Jaguar A0001 005 C0002 Ford C0001
    public void testIntersectionDifferentWithMapping() throws LBException {

    	CodedNodeSet intersect = cns.intersect(testMapping);
    	
        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        PrintUtility.print(rcr);

        assertEquals(6, rcr.getResolvedConceptReferenceCount());
    }
    
    //Jaguar A0001 005 C0002 Ford C0001
    public void testIntersectionDifferentWithMappingWithoutRestriction() throws LBException {

    	CodedNodeSet intersect = cns.intersect(testMapping);
    	
    	intersect = intersect.intersect(cns2);
    	
        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);

        assertEquals(6, rcr.getResolvedConceptReferenceCount());
    }
    
    // A0001
    public void testIntersectionDifferentWithMappingWithRestriction() throws LBException {

    	testMapping = testMapping.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	CodedNodeSet intersect = cns.intersect(testMapping);
    	
    	intersect = intersect.intersect(cns2);
    	
        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        assertEquals(1, rcr.getResolvedConceptReferenceCount());
    }
    
    // none
    public void testIntersectionDifferentWithMappingWithRestrictionAndExtraIntersection() throws LBException {

    	testMapping = testMapping.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	CodedNodeSet intersect = cns.intersect(testMapping);
    	
    	intersect = intersect.intersect(cns2);
    	
    	intersect = intersect.intersect(cns3);
    	
        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        assertEquals(0, rcr.getResolvedConceptReferenceCount());
    }
    
    // none
    public void testIntersectionDifferentShouldBeNone() throws LBException {

    	cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	CodedNodeSet intersect = cns.intersect(cns3);
    	
        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        assertEquals(0, rcr.getResolvedConceptReferenceCount());
    }
    
    // none
    public void testIntersectionDifferentComplex1() throws LBException {

    	cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	//should have 1
    	CodedNodeSet one = cns.intersect(lbs.getNodeSet(AUTO_SCHEME, null, null));
    	
    	//should still have one
    	CodedNodeSet intersect = one.intersect(testMapping);

        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        assertEquals(1, rcr.getResolvedConceptReferenceCount());
    }
    
    public void testIntersectionDifferentComplex2() throws LBException {

    	//none
    	cns = cns.intersect(cns3);
    	
    	//should have 0
    	CodedNodeSet one = cns.intersect(lbs.getNodeSet(AUTO_SCHEME, null, null));
    	
    	//should still have 0
    	CodedNodeSet intersect = one.intersect(testMapping);

        ResolvedConceptReferenceList rcr = intersect.resolveToList(null, null, null, 50);
        
        System.out.println(rcr.getResolvedConceptReferenceCount());
        assertEquals(0, rcr.getResolvedConceptReferenceCount());
    }
    
    /**
     * Test intersection with added restriction.
     * 
     * @throws LBException the LB exception
     */
    public void testIntersectionWithAddedRestriction() throws LBException {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy", "Ford" }, "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet cns3 = cns.intersect(cns2);
        
        cns3.restrictToCodes(Constructors.createConceptReferenceList("Ford"));

        ResolvedConceptReference[] rcr = cns3.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 1);
        assertTrue(contains(rcr, "Ford", "Automobiles"));
    }
}