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
package org.lexevs.cts2.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.test.Cts2TestConstants;
import org.lexevs.dao.database.service.association.AssociationService.AssociationTriple;

public class AssociationQueryOperationImplTest {
	private AssociationQueryOperationImpl query = new AssociationQueryOperationImpl();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		
		try {
			csLoadOp.load(new File("resources/testData/cts2/Cts2Automobiles.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);

		} catch (LBException e) {
			e.printStackTrace();
		}		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
	}
	
	@Test
	public void testListAssociations() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);

		// isBackward is false
		ResolvedConceptReferenceList list1 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005", null, false,
				-1, -1);
		assertEquals(3, list1.getResolvedConceptReference(0).getSourceOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

		// isBackward is true
		ResolvedConceptReferenceList list2 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005", null, true,
				-1, -1);
		assertEquals(1, list2.getResolvedConceptReference(0).getTargetOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

		// a not existed association name is specified.
		ResolvedConceptReferenceList list3 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005",
				"associationName", false, -1, -1);
		assertEquals(1, list3.getResolvedConceptReferenceCount());
		assertTrue(list3.getResolvedConceptReference(0).getSourceOf() == null);
		assertTrue(list3.getResolvedConceptReference(0).getTargetOf() == null);

		// an association name is specified.
		ResolvedConceptReferenceList list4 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005",
				"hasSubtype", false, -1, -1);
		assertEquals(3, list4.getResolvedConceptReference(0).getSourceOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

	}

	@Test
	public void testComputeSubsumptionRelationship() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);

		ConceptReference parentConRef = Constructors.createConceptReference(
				"005", "Automobiles", "Automobiles");
		ConceptReference childConRef = Constructors.createConceptReference("C",
				"Automobiles", "Automobiles");
		boolean result = query.computeSubsumptionRelationship("Automobiles",
				versionOrTag, "hasSubtype", parentConRef, childConRef);
		assertEquals(true, result);
	}

	@Test
	public void testDetermineTransitiveConceptRelationship() {
		String codingSchemeUri = Cts2TestConstants.CTS2_AUTOMOBILES_URI;
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		String containerName = "relations";
		String associationName = "hasSubtype";
		String sourceCode = "005", sourceNS = "Automobiles", targetCode = "C", targetNS = "Automobiles";

		ResolvedConceptReference path = query
				.determineTransitiveConceptRelationship(codingSchemeUri,
						versionOrTag, containerName, associationName,
						sourceCode, sourceNS, targetCode, targetNS);
		// the path string should be
		// 005|,Automobiles->A|,Automobiles->B|,Automobiles->C|,Automobiles
		// now let is traverse the graph to see if it is correct

		// for root
		assertEquals("005", path.getCode());
		assertEquals("Automobiles", path.getCodeNamespace());
		assertEquals(Cts2TestConstants.CTS2_AUTOMOBILES_URI, path.getCodingSchemeURI());
		assertEquals(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, path.getCodingSchemeVersion());

		AssociationList assnList = path.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		Association assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		AssociatedConceptList assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 2nd node
		ResolvedConceptReference assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("A", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:cts:1.1.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());

		assnList = assnCon.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 3rd node
		assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("B", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:cts:1.1.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());

		assnList = assnCon.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 4th node
		assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("C", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:cts:1.1.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());
	}

	@Test
	public void testGetAssociationDetails() {

		// association target
		String codingSchemeUri = Cts2TestConstants.CTS2_AUTOMOBILES_URI;
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);

		AssociationTriple associationTriple = query.getAssociationDetails(
				codingSchemeUri, versionOrTag, "instance001");
		assertEquals("hasSubtype", associationTriple
				.getAssociationPredicateName());
		assertEquals("relations", associationTriple.getRelationContainerName());
		assertEquals("005", associationTriple.getAssociationSource()
				.getSourceEntityCode());
		assertEquals("Automobiles", associationTriple.getAssociationSource()
				.getSourceEntityCodeNamespace());
		assertEquals(1, associationTriple.getAssociationSource()
				.getTargetCount());
		assertEquals(0, associationTriple.getAssociationSource()
				.getTargetDataCount());
		assertEquals("A", associationTriple.getAssociationSource().getTarget(0)
				.getTargetEntityCode());
		assertEquals("Automobiles", associationTriple.getAssociationSource()
				.getTarget(0).getTargetEntityCodeNamespace());	
	}
}