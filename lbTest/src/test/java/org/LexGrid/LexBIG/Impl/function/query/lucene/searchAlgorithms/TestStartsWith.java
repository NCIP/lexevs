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
 * The Class TestStartsWith.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestStartsWith extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "startsWith";

    /** The match code. */
    private static String matchCode = "A0001";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene exactMatch tests";
    }

    /**
     * Test starts with.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWith() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Automob", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }

    /**
     * Test starts with and terms.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithANDTerms() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("car truck", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test starts with no match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithNoMatch() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Makers", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }

    /**
     * Test starts with case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("automob", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    
    /**
     * Test starts with special characters.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithSpecialCharacters() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Car (with special) charaters!", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "C0001"));
    }
    
    /**
     * Test starts with one special character.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithOneSpecialCharacter() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Car (", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 2);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0011(5564)"));
    }
    
    /**
     * Test starts with one special character doesnt start with.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithOneSpecialCharacterDoesntStartWith() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("(with special) charaters!", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test starts with no match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithNoMatchSpecialCharacters() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Car {", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }

    /**
     * Test starts with case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithCaseInsensitiveSpecialCharacters() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("CaR (wiTh SpecIal) cHaraters!", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "C0001"));
    }
    
    /**
     * Gets the algorithm.
     * 
     * @return the algorithm
     */
    protected String getAlgorithm() {
        return algorithm;
    }
}