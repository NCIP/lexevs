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
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.test.util.DataTestUtils;

import util.integration.LoadMetaForIntegration;

public class MrhierAssocQualifierTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReference refC0000005;
	private ResolvedConceptReference refC0036775;
	private ResolvedConceptReference refC0001555;
	private ResolvedConceptReference refCL385598;
	
	@Before
	public void buildTestEntity() throws Exception {
		refC0000005 = cng.resolveAsList(Constructors.createConceptReference("C0000005", LoadMetaForIntegration.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];	
		refC0036775 = cng.resolveAsList(Constructors.createConceptReference("C0036775", LoadMetaForIntegration.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refC0001555 = cng.resolveAsList(Constructors.createConceptReference("C0001555", LoadMetaForIntegration.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refCL385598 = cng.resolveAsList(Constructors.createConceptReference("CL385598", LoadMetaForIntegration.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
	}
	
	@Test
	public void testNotNull() throws Exception {	
		assertNotNull(refC0000005);
		assertNotNull(refC0036775);
		assertNotNull(refC0001555);
		assertNotNull(refCL385598);
	}
	
	@Test
	public void testIsSourceOfNotNull() throws Exception {	
		assertNotNull(refC0000005.getSourceOf());
		assertNotNull(refC0036775.getSourceOf());
		assertNotNull(refC0001555.getSourceOf());
		assertNotNull(refCL385598.getSourceOf());
	}
	
	@Test
	public void testIsC0000005SourceOfC0036775() throws Exception {	
		Association[] assocs = refC0000005.getSourceOf().getAssociation();
		assertTrue(assocs.length == 1);
				
		AssociatedConcept[] acons = assocs[0].getAssociatedConcepts().getAssociatedConcept();
		
		assertTrue(acons.length == 1);
		
		assertTrue(acons[0].getCode().equals("C0036775"));
	}
	
	@Test
	public void testIsrefC0036775SourceOfC0001555() throws Exception {	
		Association[] assocs = refC0036775.getSourceOf().getAssociation();
				
		AssociatedConcept[] acons = assocs[0].getAssociatedConcepts().getAssociatedConcept();

		assertTrue(
				DataTestUtils.isAssociatedConceptPresent(acons, "C0001555")
				);
	}
	
	@Test
	public void testIsrefC0001555SourceOfCL385598() throws Exception {	
		Association[] assocs = refC0001555.getSourceOf().getAssociation();

		Association assoc = DataTestUtils.getAssociation(assocs, "PAR");
		
		AssociatedConcept[] acons = assoc.getAssociatedConcepts().getAssociatedConcept();
		
		assertTrue(
				DataTestUtils.isAssociatedConceptPresent(acons, "CL385598")
				);
	}
	
	@Test
	public void testIsC0000005toC0036775AssocQualifierPresent() throws Exception {	
		Association[] assocs = refC0000005.getSourceOf().getAssociation();
		AssociatedConcept acon = assocs[0].getAssociatedConcepts().getAssociatedConcept()[0];
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy",  acon.getAssociationQualifiers()));	
	}
	
	@Test
	public void testIsC0036775toC0001555AssocQualifierPresent() throws Exception {	
		Association[] assocs = refC0036775.getSourceOf().getAssociation();
		AssociatedConcept[] acons = assocs[0].getAssociatedConcepts().getAssociatedConcept();
		AssociatedConcept concept = DataTestUtils.getAssociatedConcept(acons, "C0001555");
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy",  concept.getAssociationQualifiers()));	
	}
	
	@Test
	public void testIsC0001555toCL385598AssocQualifierPresent() throws Exception {	
		Association[] assocs = refC0001555.getSourceOf().getAssociation();
		AssociatedConcept[] acons = DataTestUtils.getAssociation(assocs, "PAR").getAssociatedConcepts().getAssociatedConcept();
		AssociatedConcept concept = DataTestUtils.getAssociatedConcept(acons, "CL385598");
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy",  concept.getAssociationQualifiers()));	
	}
}