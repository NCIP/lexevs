package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.ModelMatch;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;

public class NodeGraphResolutionExtensionTest {
	NodeGraphResolutionExtensionImpl ngr;
	LexEVSSpringRestClientImpl client;
	String url = "http://localhost:8080/graph-resolve";

	@Before
	public void setUp() throws Exception {
		ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
				.defaultInstance()
				.getGenericExtension("NodeGraphResolution");
	}

	
	@Test
	public void testValidAssociation() throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidAssociation("subClassOf", ref));
	}
	@Test
	public void testValidNodeInAssociation() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidNodeForAssociation(ref, "C123", "subClassOf"));
	}
	
	@Test
	public void testValidNodeInAssociationNot() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertFalse(ngr.isValidNodeForAssociation(ref, "C19448", "subClassOf"));
	}
	
	@Test
	public void testInGoingExactMatchEmptyName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		Iterator<ConceptReference> itr = ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"patient_has_prognosis", 
				"CancerPatient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyExactMatchName2() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"patient_has_prognosis", 
				"CancerPatient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().startsWith("Prognosis"));
	}
	
	@Test
	public void testInGoingOnlyExactMatchName2() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"patient_has_prognosis", 
				"PrognosisBad", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		ConceptReference cref = itr.next();
		assertTrue(cref.getCode().equals("MildlySickCancerPatient") || cref.getCode().equals("CancerPatient") );
	}
	
	@Test
	public void testInGoingOnlyEmptyExactMatchName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"patient_has_prognosis", 
				"PrognosisBad", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyContainsName2() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(5, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().startsWith("Prognosis"));
	}
	
	@Test
	public void testInGoingOnlyContainsName2() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		ConceptReference cref = itr.next();
		assertTrue(cref.getCode().equals("MildlySickCancerPatient") || cref.getCode().equals("CancerPatient") );
	}
	
	@Test
	public void testInGoingOnlyEmptyContainsName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}


}
