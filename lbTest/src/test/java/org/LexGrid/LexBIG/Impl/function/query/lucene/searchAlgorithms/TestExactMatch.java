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
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestExactMatch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestExactMatch extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "exactMatch";

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
     * Test exact match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testExactMatch() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Automobile", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test exact match multiple tokens.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testExactMatchMultipleTokens() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("General Motors", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "GM"));
    }

    /**
     * Test exact match wrong case.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testExactMatchWrongCase() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal exact match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralExactMatch() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp*cial co{nce]pt you (know/don't know)", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "SpecialCharactersConcept"));
    }
    
    /**
     * Test literal exact match different code.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralExactMatchDifferentCode() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Car (with special) charaters!", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "C0001"));
    }

    /**
     * Test literal exact match wrong case.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralExactMatchWrongCase() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("A^s sp*cial Co{nce]Pt you (know/don't know)", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, "SpecialCharactersConcept"));
    }
    
    /**
     * Test literal exact match not exact.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralExactMatchNotExact() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp*cial", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test literal exact match wrong special character.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralExactMatchWrongSpecialCharacter() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp^cial co{nce]pt", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    @Test
    public void testLiteralExactMatchPhraseComparison() throws Exception {
        CodedNodeSet cns = super.getOWLSchemeCodedNodeSet();
        ConceptReferenceList list = new ConceptReferenceList();
        list.addConceptReference(Constructors.createConceptReference("C3810", "owl2lexevs"));
        list.addConceptReference(Constructors.createConceptReference("C53684", "owl2lexevs"));
        list.addConceptReference(Constructors.createConceptReference("C25377", "owl2lexevs"));
        list.addConceptReference(Constructors.createConceptReference("C48333", "owl2lexevs"));
 //       cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"C3810", "C53684"}));
        cns = cns.restrictToCodes(list);
        cns = cns.restrictToProperties(null, 
                new PropertyType[]{PropertyType.PRESENTATION,PropertyType.GENERIC}, 
                Constructors.createLocalNameList("NCI"), null, null);
        cns = cns.restrictToMatchingDesignations("Connective and Soft Tissue Neoplasm", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        System.out.println(rcrl.getResolvedConceptReferenceCount());
        assertTrue("Length: " + rcrl.getResolvedConceptReferenceCount(), rcrl.getResolvedConceptReferenceCount() == 1);
    }
    
    @Test
    public void testLiteralExactMatchPhraseComparisonAlt() throws Exception {
        CodedNodeSet cns = super.getOWLSchemeCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Connective and Soft Tissue Neoplasm, Benign", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);
    }
    
    @Test
    public void testLiteralExactMatchPhraseComparisonAlt2() throws Exception {
        CodedNodeSet cns = super.getOWLSchemeCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Benign Connective and Soft Tissue Neoplasm", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);
    }
    
    @Test
    public void testLiteralExactMatchPhraseComparisonSource() throws Exception {
        CodedNodeSet cns = super.getOWLSchemeCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Connective and Soft Tissue Neoplasm, Benign", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);
    }
    
	@Test
    public void testsearchAllAscendentsInTransitiveClosureDomainExactmatch( ) throws LBException{
		LexBIGService svc = LexBIGServiceImpl.defaultInstance();
	   LexBIGServiceConvenienceMethods lbscm =(LexBIGServiceConvenienceMethods) svc.getGenericExtension("LexBIGServiceConvenienceMethods");
	   long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("C53316");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
    			"NCI_Thesaurus", 
    			null,
    			codes, 
    			"subClassOf", 
    			"Connective and Soft Tissue Neoplasm",
    			getAlgorithm(),
    			SearchDesignationOption.ALL, 
    			Constructors.createLocalNameList("NCI"));
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);

    	System.out.println(list.size());

    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("C3810")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("C53684")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("ncit")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Connective and Soft Tissue Neoplasm"))); 
    }
	
	@Test
    public void testsearchAllAscendentsInTransitiveClosureDomainSourceSpecificPreferred( ) throws LBException{
		LexBIGService svc = LexBIGServiceImpl.defaultInstance();
	   LexBIGServiceConvenienceMethods lbscm =(LexBIGServiceConvenienceMethods) svc.getGenericExtension("LexBIGServiceConvenienceMethods");
	   long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("C53316");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
    			"NCI_Thesaurus",
    			null,
    			codes, 
    			"subClassOf", 
    			"Connective and Soft Tissue Neoplasm",
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	System.out.println(list.size());
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("C3810")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("C53684")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("ncit")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Connective and Soft Tissue Neoplasm"))); 
    }
	
	
	@Test
    public void testsearchAllAscendentsInTransitiveClosureDomainContainsPreferred( ) throws LBException{
		LexBIGService svc = LexBIGServiceImpl.defaultInstance();
	   LexBIGServiceConvenienceMethods lbscm =(LexBIGServiceConvenienceMethods) svc.getGenericExtension("LexBIGServiceConvenienceMethods");
	   long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("C53316");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
    			"NCI_Thesaurus",
    			null,
    			codes, 
    			"subClassOf", 
    			"Neoplasm",
    			LBConstants.MatchAlgorithms.contains.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	System.out.println(list.size());
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("C3810")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("C53684")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("ncit")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Connective and Soft Tissue Neoplasm"))); 
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