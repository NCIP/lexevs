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
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class DifferenceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class DifferenceTest extends BaseCodedNodeSetTest {
    
    /** The cns2. */
    private CodedNodeSet cns2; 
    private CodedNodeSet testMapping;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "CodedNodeSet Union Test";
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#setUp()
     */
    @Override
    public void setUp(){
        super.setUp();
        try {
            cns2 = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AUTO_SCHEME, null);
            testMapping = lbs.getCodingSchemeConcepts(MAPPING_SCHEME_URI, null);
        } catch (LBException e) {
          fail(e.getMessage());
        }
    }
    
    /**
     * Test difference.
     * 
     * @throws LBException the LB exception
     */
    @Ignore
    public void testDifference() throws LBException {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy", "Ford", "A0001" },
                "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet difference = cns.difference(cns2);

        ResolvedConceptReference[] rcr = difference.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 2);
        assertTrue(contains(rcr, "Chevy", "Automobiles"));
        assertTrue(contains(rcr, "A0001", "Automobiles"));
    }
    
    /**
     * Test difference correct set theory example.
     * 
     * @throws LBException the LB exception
     */
    public void testDifferenceCorrectSetTheoryExample() throws LBException {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" },
                "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "A0001" }, "Automobiles"));

        CodedNodeSet difference = cns.difference(cns2);

        ResolvedConceptReference[] rcr = difference.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 1);
        assertTrue(contains(rcr, "Chevy", "Automobiles"));
    }
    
    /**
     * Test difference correct set theory example other way.
     * 
     * @throws LBException the LB exception
     */
    public void testDifferenceCorrectSetTheoryExampleOtherWay() throws LBException {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" },
                "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "A0001" }, "Automobiles"));

        CodedNodeSet difference =  cns2.difference(cns);

        ResolvedConceptReference[] rcr = difference.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 1);
        assertTrue(contains(rcr, "A0001", "Automobiles"));
    }
    
    /**
     * Test difference with added restriction.
     * 
     * @throws LBException the LB exception
     */
    public void testDifferenceWithAddedRestriction() throws LBException {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy", "Ford", "A0001" },
                "Automobiles"));

        cns2 = cns2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Ford", "005" }, "Automobiles"));

        CodedNodeSet cns3 = cns.difference(cns2);
        
        cns3.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"Chevy"}));

        ResolvedConceptReference[] rcr = cns3.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue("Actual Length: " + rcr.length, rcr.length == 1);
        assertTrue(contains(rcr, "Chevy", "Automobiles"));
    }
    
    /*
    Code: 73, Description: Oldsmobile Hash: 1605346438
    Code: A, Description: First Code in cycle Hash: 73042558
    Code: T0001, Description: Truck Hash: 1413109869
    Code: SpecialCharactersConcept, Description: Concept containing special characters Hash: 1115916110
    Code: Chevy, Description: Chevrolet Hash: 1802121333
    Code: NoRelationsConcept, Description: A concept for testing Graph Building on Concepts with no relations Hash: 143886443
    Code: C0011(5564), Description: Car With Trailer Hash: 263093125
    Code: GM, Description: General Motors Hash: 137322702
    Code: DifferentNamespaceConcept, Description: Concept for testing same code but different Namespace - 1 Hash: 1585215636
    Code: DifferentNamespaceConcept, Description: Concept for testing same code but different Namespace - 2 Hash: 1037069570
    Code: B, Description: Second Code in cycle Hash: 1050983938
    Code: C, Description: Third Code in cycle Hash: 834049391
------------ Leftovers
    Code: R0001, Description: Rims Hash: 618395549
    Code: P0001, Description: Piston Hash: 1700624210
    Code: E0001, Description: Engine Hash: 1531239547
    */ 
    public void testDifferenceCrossCodingSchemes() throws LBException {

    	CodedNodeSet difference = cns.difference(testMapping);
    	
    	ResolvedConceptReferencesIterator itr = difference.resolve(null, null, null);
    	
    	Set<String> codes = new HashSet<String>();
    	while(itr.hasNext()){
    		codes.add(itr.next().getCode());
    	}
    	
    	assertEquals(codes, new HashSet<String>(
    			Arrays.asList("73","A","T0001","SpecialCharactersConcept",
    			"Chevy","NoRelationsConcept","C0011(5564)","GM","DifferentNamespaceConcept","B","C"
    			)));
    }
}