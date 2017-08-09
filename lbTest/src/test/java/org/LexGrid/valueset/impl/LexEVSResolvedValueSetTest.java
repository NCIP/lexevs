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
package org.LexGrid.valueset.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

/**
 * JUnit for Resolved Value Set Service.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
public class LexEVSResolvedValueSetTest extends TestCase {

	LexEVSResolvedValueSetService service;
	private LexBIGService lbs;

	public void setUp() {
		lbs = getLexBIGService();
		service = new LexEVSResolvedValueSetServiceImpl(lbs);
	}

	@Test
	public void testListAllResolvedValueSets() throws Exception {
		long start = System.currentTimeMillis();
		List<CodingScheme> list = service.listAllResolvedValueSets();
		long end = System.currentTimeMillis();
		System.out.println("Retrieving full scheme value sets: " + (end - start) + " mseconds");
		assertTrue(list.size() > 0);
		assertTrue(list.size() == 3);
		CodingScheme scheme = list.get(0);
		
		// no coding scheme version or tag was passed in, so retrieve the PRODUCTION tag (version 1.1)
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop).equals(
						"Automobiles"));
//				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop).equals("1.1"));
				System.out.println("Coding Scheme: "
						+ getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop));
				System.out.println("Version: "
						+ getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop));
			}
		}
		LexBIGService lbs = getLexBIGService();
		CodedNodeSet set = lbs.getCodingSchemeConcepts(scheme.getCodingSchemeName(),
				Constructors.createCodingSchemeVersionOrTag(null, scheme.getRepresentsVersion()));
		ResolvedConceptReferencesIterator refs = set.resolve(null, null, null);
		while (refs.hasNext()) {

			ResolvedConceptReference ref = refs.next();
			System.out.println("Namespace: " + ref.getEntity().getEntityCodeNamespace());
			System.out.println("Code: " + ref.getCode());
			System.out.println("Description: " + ref.getEntityDescription().getContent());

		}
	}
	
	@Test
	public void testListAllResolvedValueSetsWithMiniScheme() throws Exception {
		long start = System.currentTimeMillis();
		List<CodingScheme> schemes = service.getMinimalResolvedValueSetSchemes();
		assertTrue(schemes.size() > 0);
		assertTrue(schemes.size() == 3);
		assertTrue(schemes.stream().anyMatch(x -> x.getFormalName().equals("All Domestic Autos But GM")));
		assertTrue(schemes.stream().anyMatch(x -> x.getFormalName().equals("All Domestic Autos But GM  and "
				+ "as many characters as it takes to exceed 50 chars but not 250 chars and that "
				+ "should about do it")));
		assertTrue(schemes.stream().anyMatch(x -> x.getFormalName().equals("One Child Value Set")));
		assertTrue(schemes.stream().anyMatch(x -> x.getCodingSchemeURI().equals("SRITEST:AUTO:AllDomesticButGM")));
		assertTrue(schemes.stream().anyMatch(x -> x.getCodingSchemeURI().equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName")));
		assertTrue(schemes.stream().anyMatch(x -> x.getCodingSchemeURI().equals("XTEST:One.Node.ValueSet")));
		assertTrue(schemes.stream().anyMatch(x -> x.getRepresentsVersion().equals("12.03test")));
		assertTrue(schemes.stream().anyMatch(x -> x.getRepresentsVersion().equals("1.0")));
		assertTrue(schemes.stream().anyMatch(x -> x.isIsActive()));
		final int count[] = {0};
		schemes.forEach(x ->{ count[0]++; System.out.println(x.getFormalName() + " count: " +  count[0]);});
	}

	@Test
	public void testGetResolvedValueSetsforConceptReference() {
		ConceptReference ref = new ConceptReference();
		ref.setCode("005");
		ref.setCodeNamespace("Automobiles");
		ref.setCodingSchemeName("Automobiles");
		List<CodingScheme> schemes = service.getResolvedValueSetsForConceptReference(ref);
		assertTrue(schemes.size() > 0);
	}

	@Test
    @Category(RemoveFromDistributedTests.class)
	public void testGetCodingSchemeMetadataForResolvedValueSetURI() throws URISyntaxException {
		
		// No coding scheme version or tag defined.  This will resolve against RPODCUTION tag of automobiles.
		URI uri = new URI("SRITEST:AUTO:AllDomesticButGM");
		CodingScheme scheme = service.getResolvedValueSetForValueSetURI(uri);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop).equals(
						"Automobiles"));
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop).equals("1.1"));
			}
		}
	}
	
	@Test
	public void testCorrectTruncationForFormalNameJIRA_594() throws URISyntaxException {
		URI uri = new URI("SRITEST:AUTO:AllDomesticButGMWithlt250charName");
		CodingScheme scheme = service.getResolvedValueSetForValueSetURI(uri);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals("formalName")) {
				assertTrue(scheme.getFormalName().length() > 50);
				
			}
		}
	}
	
	@Test
	public void testVerifyLoadOfChildNodeOnly() throws URISyntaxException {
		URI uri = new URI("XTEST:One.Node.ValueSet");
		ResolvedConceptReferenceList list = service.getValueSetEntitiesForURI(uri.toString());
		assertTrue(list.getResolvedConceptReferenceCount() == 1);
		assertTrue(list.getResolvedConceptReference(0).getConceptCode().equals("C0011(5564)"));
	}
	
	@Test
	public void testGetValueSetURIAndVersionForCode() throws LBException{
		List<AbsoluteCodingSchemeVersionReference> refs = service.getResolvedValueSetsforEntityCode("C0011(5564)");
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		AbsoluteCodingSchemeVersionReference ref = refs.get(0);
		assertEquals(ref.getCodingSchemeURN(), "XTEST:One.Node.ValueSet");
	}
	
	@Test
	public void testGetValueSetURIAndVersionForTextExact() throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs = 
				service.getResolvedValueSetsforTextSearch("TrailerCar(Yahoo)", 
						MatchAlgorithm.PRESENTATION_EXACT);
		long end = System.currentTimeMillis();
		System.out.println("Exact Match: " + (end - start) + " mseconds");
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		AbsoluteCodingSchemeVersionReference ref = refs.get(0);
		assertEquals(ref.getCodingSchemeURN(), "XTEST:One.Node.ValueSet");
	}
	
	@Test
	public void testGetValueSetURIAndVersionForTextLucene() throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs = 
				service.getResolvedValueSetsforTextSearch("Domestic", 
						MatchAlgorithm.LUCENE);
		long end = System.currentTimeMillis();
		System.out.println("Lucene Search: " + (end - start) + " mseconds");
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		AbsoluteCodingSchemeVersionReference ref = refs.get(0);
		assertTrue(ref.getCodingSchemeURN().equals( "SRITEST:AUTO:AllDomesticButGM") || 
				ref.getCodingSchemeURN().equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName"));

	}
	
	@Test
	public void testGetValueSetURIAndVersionForTextContains() throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs = 
				service.getResolvedValueSetsforTextSearch("Domestic", 
						MatchAlgorithm.PRESENTATION_CONTAINS);
		long end = System.currentTimeMillis();
		System.out.println("Contians search: " + (end - start) + " mseconds");
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		AbsoluteCodingSchemeVersionReference ref = refs.get(0);
		assertTrue(ref.getCodingSchemeURN().equals( "SRITEST:AUTO:AllDomesticButGM") || 
				ref.getCodingSchemeURN().equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName"));
	}
	
	private String getPropertyQualifierValue(String qualifierName, Property prop) {
		for (PropertyQualifier pq : prop.getPropertyQualifier()) {
			if (pq.getPropertyQualifierName().equals(qualifierName)) {
				return pq.getValue().getContent();
			}
		}
		return "";
	}
	
	public LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
	public void setLexBIGService(LexBIGService lbsvc){
		lbs = lbsvc;
	}

	/**
	 * @return the service
	 */
	public LexEVSResolvedValueSetService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(LexEVSResolvedValueSetService service) {
		this.service = service;
	}

}