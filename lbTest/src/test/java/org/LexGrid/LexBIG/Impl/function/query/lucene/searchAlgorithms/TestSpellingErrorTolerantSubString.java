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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
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
public class TestSpellingErrorTolerantSubString extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "SpellingErrorTolerantSubStringSearch";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene SpellingErrorTolerantSubStringSearch tests";
    }

    /**
     * Test spelling error tolerant sub string.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubString() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test spelling error tolerant sub string first result.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringFirstResult() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(rcrl[0].getCode().equals("C0001"));
    }
    
    /**
     * Test spelling error tolerant sub string different metaphone value.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringDifferentMetaphoneValue() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("kar", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test spelling error tolerant sub string different metaphone value first result.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringDifferentMetaphoneValueFirstResult() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("kar", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue("Code: " + rcrl[0].getCode(), rcrl[0].getCode().equals("C0002"));
    }
    
    /**
     * Test spelling error tolerant sub string two tokens.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringTwoTokens() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("General Motors", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(rcrl[0].getCode().equals("GM"));
    }
    
    /**
     * Test spelling error tolerant sub string three tokens correct spelling.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringThreeTokensCorrectSpelling() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Ford Motor Company", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(rcrl[0].getCode().equals("Ford"));
    }
    
    /**
     * Test spelling error tolerant sub string three tokens different order.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringThreeTokensDifferentOrder() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Motor Ford Company", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test spelling error tolerant sub string three tokens too much distance.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringThreeTokensTooMuchDistance() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Ford Company", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    } 
    
    /**
     * Test spelling error tolerant sub string three tokens spelling error tolerant.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringThreeTokensSpellingErrorTolerant() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("testyng sam kode", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 3);

        assertTrue(checkForMatch(rcrl, "DifferentNamespaceConcept"));

        List<String> foundNamespaces = new ArrayList<String>();
        
        foundNamespaces.add(rcrl[0].getCodeNamespace());
        foundNamespaces.add(rcrl[1].getCodeNamespace());
        foundNamespaces.add(rcrl[2].getCodeNamespace());
        
        assertTrue(foundNamespaces.contains("Automobiles"));
        assertTrue(foundNamespaces.contains("TestForSameCodeNamespace"));
    }
    
    /**
     * Test spelling error tolerant sub string special characters.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringSpecialCharacters() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Kar (with special) charaters!", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 2);

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

        assertTrue(checkForMatch(rcrl, "C0001"));
        assertTrue(checkForMatch(rcrl, "C0002"));
    }
    
    /**
     * Test spelling error tolerant sub string wrong special characters.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testSpellingErrorTolerantSubStringWrongSpecialCharacters() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Kar (with special) charaters^", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    } 
    
    /**
     * Additional boost centered testing
     * @throws LBException
     */
    @Test
    public void testBoostOne() throws LBException{
    	CodedNodeSet cns = lbs.getCodingSchemeConcepts(BOOST_SCHEME, null);
    	cns = cns.restrictToMatchingDesignations("maintenance", SearchDesignationOption.ALL, algorithm, null);
    	 ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
    	 assertTrue("Length: " + rcrl.length, rcrl.length > 0);
    	 assertTrue(rcrl[0].getEntity().getEntityCode().equals("111"));
    }
    @Test
    public void testBoostTwo() throws LBException{
    	CodedNodeSet cns = lbs.getCodingSchemeConcepts(BOOST_SCHEME, null);
    	cns = cns.restrictToMatchingDesignations("mayntenance", SearchDesignationOption.ALL, algorithm, null);
    	 ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
    	 assertTrue("Length: " + rcrl.length, rcrl.length > 0);
    	 assertTrue(rcrl[0].getEntity().getEntityCode().equals("22"));
    }
    @Test
    public void testBoostThree() throws LBException{
    	CodedNodeSet cns = lbs.getCodingSchemeConcepts(BOOST_SCHEME, null);
    	cns = cns.restrictToMatchingDesignations("maintinance", SearchDesignationOption.ALL, algorithm, null);
    	 ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
    	 assertTrue("Length: " + rcrl.length, rcrl.length > 0);
    	 assertTrue(rcrl[0].getEntity().getEntityCode().equals("33"));
    }
    @Test
    public void testBoostFive() throws LBException{
    	CodedNodeSet cns = lbs.getCodingSchemeConcepts(BOOST_SCHEME, null);
    	cns = cns.restrictToMatchingDesignations("maintenanse", SearchDesignationOption.ALL, algorithm, null);
    	 ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
    	 assertTrue("Length: " + rcrl.length, rcrl.length > 0);
    	 assertTrue(rcrl[0].getEntity().getEntityCode().equals("55"));
    }
    
}