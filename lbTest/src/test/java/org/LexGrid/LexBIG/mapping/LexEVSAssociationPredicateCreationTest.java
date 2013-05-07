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

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSAssociationPredicateCreationTest extends TestCase {
	
	   LexEVSAuthoringServiceImpl authoring;
	   LexBIGService lbs;


		
	   public void setUp(){

		   authoring = new LexEVSAuthoringServiceImpl();
		   lbs = LexBIGServiceImpl.defaultInstance();
	   }

	public void testCreateNewAssocationPredicate() throws LBException {
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
		entryState.setContainingRevision("Mayo_predicate_revision");
		revision.setRevisionId("Mayo_predicate_revision");
		entryState.setRelativeOrder(new Long(0));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(MappingTestConstants.AUTHORING_URN);
		scheme.setCodingSchemeVersion(MappingTestConstants.AUTHORING_VERSION);
		authoring.createAssociationPredicate(revision, entryState, scheme, MappingTestConstants.RELATIONS_CONTAINER, MappingTestConstants.ASSOC_NAME_A);
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(MappingTestConstants.AUTHORING_VERSION);
		CodingScheme revisedScheme = lbs.resolveCodingScheme(MappingTestConstants.AUTHORING_SCHEME, csvt);
		Relations[] relations = revisedScheme.getRelations();
		boolean predicateFound = false;
		for(Relations rel: relations){
			
			AssociationPredicate[] predicates = rel.getAssociationPredicate();
			for(AssociationPredicate predicate: predicates){
				if(predicate.getAssociationName().equals(MappingTestConstants.ASSOC_NAME_A)){
					predicateFound = true;
				}
			}
			
		}
		assertTrue(predicateFound);
	}

	public void testCreateNewAssociationPredicateAndRelations() throws LBException {
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
		entryState.setContainingRevision("Mayo_predicate_revisionb");
		revision.setRevisionId("Mayo_predicate_revisionb");
		entryState.setRelativeOrder(new Long(0));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(MappingTestConstants.AUTHORING_URN);
		scheme.setCodingSchemeVersion(MappingTestConstants.AUTHORING_VERSION);
		authoring.createAssociationPredicate(revision, entryState, scheme, MappingTestConstants.NEW_RELATIONS_CONTAINER, MappingTestConstants.ASSOC_NAME_B);
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(MappingTestConstants.AUTHORING_VERSION);
		CodingScheme revisedScheme = lbs.resolveCodingScheme(MappingTestConstants.AUTHORING_SCHEME, csvt);
		Relations[] relations = revisedScheme.getRelations();
		boolean predicateFound = false;
		for(Relations rel: relations){
			
			AssociationPredicate[] predicates = rel.getAssociationPredicate();
			for(AssociationPredicate predicate: predicates){
				if(predicate.getAssociationName().equals(MappingTestConstants.ASSOC_NAME_B)){
					predicateFound = true;
				}
			}
			
		}
		assertTrue(predicateFound);
	}
}