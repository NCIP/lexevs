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

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.junit.Test;
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

	public void setUp() {
		service = new LexEVSResolvedValueSetServiceImpl();
	}

	@Test
	public void testListAllResolvedValueSets() throws Exception {
		List<CodingScheme> list = service.listAllResolvedValueSets();
		assertTrue(list.size() > 0);
		CodingScheme scheme = list.get(0);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop).equals(
						"Automobiles"));
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop).equals("1.0"));
				System.out.println("Coding Scheme: "
						+ getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop));
				System.out.println("Version: "
						+ getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop));
			}
		}
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
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
	public void testGetResolvedValueSetsforConceptReference() {
		ConceptReference ref = new ConceptReference();
		ref.setCode("005");
		ref.setCodeNamespace("Automobiles");
		ref.setCodingSchemeName("Automobiles");
		List<CodingScheme> schemes = service.getResolvedValueSetsForConceptReference(ref);
		assertTrue(schemes.size() > 0);
	}

	@Test
	public void testGetCodingSchemeMetadataForResolvedValueSetURI() throws URISyntaxException {
		URI uri = new URI("SRITEST:AUTO:AllDomesticButGM");
		CodingScheme scheme = service.getResolvedValueSetForValueSetURI(uri);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop).equals(
						"Automobiles"));
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop).equals("1.0"));
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
	private String getPropertyQualifierValue(String qualifierName, Property prop) {
		for (PropertyQualifier pq : prop.getPropertyQualifier()) {
			if (pq.getPropertyQualifierName().equals(qualifierName)) {
				return pq.getValue().getContent();
			}
		}
		return "";
	}

}