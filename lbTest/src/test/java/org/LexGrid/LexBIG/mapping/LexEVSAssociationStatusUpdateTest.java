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
package org.LexGrid.LexBIG.mapping;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;

import junit.framework.TestCase;

public class LexEVSAssociationStatusUpdateTest extends TestCase {

	LexEVSAuthoringServiceImpl authoring;
	LexBIGService lbs;
	LexBIGServiceManager lbsm;




	public void setUp() {

		authoring = new LexEVSAuthoringServiceImpl();
		lbs = LexBIGServiceImpl.defaultInstance();

		try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			e.printStackTrace();
		}
	}

	public void testAssociationStatusUpdate() throws LBException {
		Revision revision = new Revision();

		revision.setChangeAgent("Mayo_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestAssociationStatusRevision");
		revision.setEntityDescription(entityDescription);

		Date revisionDate = new Date();
		revision.setRevisionDate(revisionDate);
		EntryState entryState = new EntryState();
		entryState.setContainingRevision("Mayo_Status_Revision");
		entryState.setRelativeOrder(new Long(0));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(MappingTestConstants.AUTHORING_URN);
		scheme.setCodingSchemeVersion(MappingTestConstants.AUTHORING_VERSION);
		assertTrue(authoring.setAssociationStatus(revision, entryState, scheme,
				"authoring_relations", "original_type", "01", MappingTestConstants.AUTHORING_SCHEME, "02",
				MappingTestConstants.AUTHORING_SCHEME,"instance001", "ACTIVE", true));
	}

	   
}