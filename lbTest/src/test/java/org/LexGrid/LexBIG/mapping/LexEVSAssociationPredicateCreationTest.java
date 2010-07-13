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

public class LexEVSAssociationPredicateCreationTest extends TestCase {
	
	   LexEVSAuthoringServiceImpl authoring;
	   LexBIGService lbs;

		public static String TARGET_SCHEME =  "Automobiles";
		public static String TARGET_VERSION = "1.0";
		public static String TARGET_URN = "urn:oid:11.11.0.1";
		public static String RELATIONS_CONTAINER = "relations";
		public static String ASSOC_NAME_A = "BY";
		public static String ASSOC_NAME_B = "NYE";
		public static String NEW_RELATIONS_CONTAINER = "newRelation";
		
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
		entryState.setPrevRevision("Should_not_be_set");
		entryState.setRelativeOrder(new Long(0));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(TARGET_URN);
		scheme.setCodingSchemeVersion(TARGET_VERSION);
		authoring.createAssociationPredicate(revision, entryState, scheme, RELATIONS_CONTAINER, ASSOC_NAME_A);
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(TARGET_VERSION);
		CodingScheme revisedScheme = lbs.resolveCodingScheme(TARGET_SCHEME, csvt);
		Relations[] relations = revisedScheme.getRelations();
		boolean predicateFound = false;
		for(Relations rel: relations){
			
			AssociationPredicate[] predicates = rel.getAssociationPredicate();
			for(AssociationPredicate predicate: predicates){
				if(predicate.getAssociationName().equals(ASSOC_NAME_A)){
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
		entryState.setPrevRevision("Should_not_be_set");
		entryState.setRelativeOrder(new Long(0));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(TARGET_URN);
		scheme.setCodingSchemeVersion(TARGET_VERSION);
		authoring.createAssociationPredicate(revision, entryState, scheme, NEW_RELATIONS_CONTAINER, ASSOC_NAME_B);
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(TARGET_VERSION);
		CodingScheme revisedScheme = lbs.resolveCodingScheme(TARGET_SCHEME, csvt);
		Relations[] relations = revisedScheme.getRelations();
		boolean predicateFound = false;
		for(Relations rel: relations){
			
			AssociationPredicate[] predicates = rel.getAssociationPredicate();
			for(AssociationPredicate predicate: predicates){
				if(predicate.getAssociationName().equals(ASSOC_NAME_B)){
					predicateFound = true;
				}
			}
			
		}
		assertTrue(predicateFound);
	}
}
