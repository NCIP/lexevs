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
package org.lexevs.cts2.admin.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.cts2.test.Cts2TestConstants;

public class AssociationExportOperationImplTest extends Cts2BaseTest {

	private AssociationExportOperationImpl operation = new AssociationExportOperationImpl();
	
	private File exportFile ;
	
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
	
	@Before
	public void createFile() {
		exportFile = new File(System.getProperty("java.io.tmpdir") + File.separator + 
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME + "_" + Cts2TestConstants.CTS2_AUTOMOBILES_VERSION + ".xml");
	}
	
	@After
	public void deleteFile() {
		assertTrue(exportFile.delete());
	}
	
	@Test
	public void testExportAssociation() throws Exception {
		
		
		operation.exportAssociation(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				super.getLexBIGService().getNodeGraph(
						Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
						Constructors.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), 
						null), 
				new File(System.getProperty("java.io.tmpdir")).toURI(), 
				true, 
				true, 
				false);
	
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertEquals(1, cs.getRelationsCount());
		assertEquals(3, cs.getRelations(0).getAssociationPredicateCount());	
		assertTrue(this.getAssociationPredicate(cs.getRelations(0), "hasSubtype").getSourceCount() > 0);
		assertTrue(this.getAssociationPredicate(cs.getRelations(0), "uses").getSourceCount() > 0);
	}
	
	@Test
	public void testExportOnlyOneAssociation() throws Exception {
		CodedNodeGraph cng = super.getLexBIGService().getNodeGraph(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), 
				null);
		
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), null);
		
		operation.exportAssociation(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				cng, 
				new File(System.getProperty("java.io.tmpdir")).toURI(), 
				true, 
				true, 
				false);
	
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertEquals(1, cs.getRelationsCount());
		
		AssociationPredicate hasSubType = this.getAssociationPredicate(cs.getRelations(0), "hasSubtype");
		AssociationPredicate uses = this.getAssociationPredicate(cs.getRelations(0), "uses");
		AssociationPredicate differentEntityCodeAssoc = this.getAssociationPredicate(cs.getRelations(0), "differentEntityCodeAssoc");
		
		assertTrue(hasSubType.getSourceCount() > 0);
		assertEquals(0,uses.getSourceCount());
		assertEquals(0,differentEntityCodeAssoc.getSourceCount());
	}
	
	private AssociationPredicate getAssociationPredicate(Relations relations, String predicateName) {
		for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
			if(predicate.getAssociationName().equals(predicateName)) {
				return predicate;
			}
		}
		
		throw new RuntimeException("not found");
	}
	
}