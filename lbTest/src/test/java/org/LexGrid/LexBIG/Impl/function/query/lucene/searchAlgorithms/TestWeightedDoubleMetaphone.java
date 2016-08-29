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
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestWeightedDoubleMetaphone.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestWeightedDoubleMetaphone extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "WeightedDoubleMetaphoneLuceneQuery";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene WeightedDoubleMetaphoneLuceneQuery tests";
    }

    /**
     * Test double metaphone.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphone() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test double metaphone different metaphone value.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneDifferentMetaphoneValue() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("kar", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test double metaphone case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("CaR", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test double metaphone different metaphone value case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneDifferentMetaphoneValueCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("KaR", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test double metaphone return order.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneReturnOrder() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(rcrl[0].getCode().equals("C0001"));
    }
    
    /**
     * Test double metaphone return order different metaphone value.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneReturnOrderDifferentMetaphoneValue() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("kar", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(rcrl[0].getCode().equals("C0002"));
    } 
    
    /**
     * Test double metaphone and terms.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDoubleMetaphoneANDTerms() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("kar truk", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);

    } 
}