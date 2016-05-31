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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class CodedNodeSetOperationsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class CodedNodeSetOperationsTest extends BaseCodedNodeSetTest {
    
    /** The unioned coded node set. */
    private CodedNodeSet unionedCodedNodeSet;
    
    /** The intersected coded node set. */
    private CodedNodeSet intersectedCodedNodeSet;
    
    /** The differenced coded node set. */
    private CodedNodeSet differencedCodedNodeSet;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Test for mixing together CodedNodeSet-type operations.";
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#setUp()
     */
    @Override
    public void setUp(){
        super.setUp();
        try {
            CodedNodeSet unionedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            unionedCodedNodeSetPart1 = unionedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet unionedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            unionedCodedNodeSetPart2 = unionedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "GM" }, "Automobiles"));
            
            unionedCodedNodeSet = unionedCodedNodeSetPart1.union(unionedCodedNodeSetPart2);
            
            CodedNodeSet intersectedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            intersectedCodedNodeSetPart1 = intersectedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet intersectedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            intersectedCodedNodeSetPart2 = intersectedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chevy", "GM" }, "Automobiles"));
            
            intersectedCodedNodeSet = intersectedCodedNodeSetPart1.intersect(intersectedCodedNodeSetPart2);
            
            CodedNodeSet differencedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            differencedCodedNodeSetPart1 = differencedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet differencedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            differencedCodedNodeSetPart2 = differencedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chevy", "GM" }, "Automobiles"));
            
            differencedCodedNodeSet = differencedCodedNodeSetPart1.difference(differencedCodedNodeSetPart2);
        } catch (Exception e) {
            System.err.print(e);
            fail(e.getMessage());
        } 
    }
    
  /**
   * Test intersect a union.
   * 
   * @throws Exception the exception
   */
  public void testIntersectAUnion() throws Exception {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "GM" }, "Automobiles"));

        CodedNodeSet result = cns.intersect(unionedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 2);
        assertTrue(contains(rcr, "GM", "Automobiles"));
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
    /**
     * Test union an intersection.
     * 
     * @throws Exception the exception
     */
    public void testUnionAnIntersection() throws Exception {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        CodedNodeSet result = cns.union(intersectedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 2);
        assertTrue(contains(rcr, "Chevy", "Automobiles"));
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
 /**
  * Test intersect a difference.
  * 
  * @throws Exception the exception
  */
 public void testIntersectADifference() throws Exception {
        
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        CodedNodeSet result = cns.intersect(differencedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
}