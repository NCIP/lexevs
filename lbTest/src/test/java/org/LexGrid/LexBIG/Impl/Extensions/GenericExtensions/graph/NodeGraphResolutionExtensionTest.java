package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.ModelMatch;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
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
		AbsoluteCodingSchemeVersionReference ref = Constructors
				.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidAssociation("subClassOf", ref));
	}
	@Test
	public void testValidNodeInAssociation() {
		AbsoluteCodingSchemeVersionReference ref = Constructors
				.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidNodeForAssociation(ref, "C123", "subClassOf"));
	}
	
	@Test
	public void testValidNodeInAssociationNot() {
		AbsoluteCodingSchemeVersionReference ref = Constructors
				.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertFalse(ngr.isValidNodeForAssociation(ref, "C19448", "subClassOf"));
	}
	@Test
	public void testInGoingAllAssnTargetOfCode() {
		AbsoluteCodingSchemeVersionReference ref = Constructors
				.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
						"0.1.5");
		GraphNodeContentTrackingIterator  itr = (GraphNodeContentTrackingIterator) ngr.
				getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				null, 
				"Brca1", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(3, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().equals("EpithelialCell"));
		assertTrue(itr.next().getCode().equals("string"));
		assertTrue(itr.next().getCode().equals("C123"));
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
		assertNotSame(5, itr.getTotalCacheSize());
		assertEquals(3, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().equals("C117743"));
		itr.next();
		assertTrue(itr.next().getCode().equals("C48323"));
	}
	
	@Test
	public void testInGoingOnlyContainsName4() {
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
		assertEquals(4, itr.getTotalCacheSize());
		ConceptReference cref = itr.next();
		assertTrue(cref.getCode().equals("C99999"));
		assertTrue(itr.next().getCode().equals("C99998"));
		assertTrue(itr.next().getCode().equals("C99988"));
		assertTrue(itr.next().getCode().equals("C99989"));
	}
	
	@Test
	public void testInGoingOnlyEmptyContainsName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyLuceneProperty2() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.LUCENE, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().equals("C117743"));
		assertTrue(itr.next().getCode().equals("C54453"));
	}
	
	@Test
	public void testInGoingOnlyLuceneProperty4() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		ConceptReference cref = itr.next();
		assertTrue(cref.getCode().equals("C99999"));
		assertTrue(itr.next().getCode().equals("C99998"));
		assertTrue(itr.next().getCode().equals("C99988"));
		assertTrue(itr.next().getCode().equals("C99989"));
	}
	
	@Test
	public void testInGoingOnlyEmptyLuceneProperty() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyExactMatchCode() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().equals("C117743"));
		assertTrue(itr.next().getCode().equals("C54453"));
	}
	
	@Test
	public void testInGoingOnlyExactMatchCode4() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		ConceptReference cref = itr.next();
		assertTrue(cref.getCode().equals("C99999"));
		assertTrue(itr.next().getCode().equals("C99998"));
		assertTrue(itr.next().getCode().equals("C99988"));
		assertTrue(itr.next().getCode().equals("C99989"));
	}
	
	@Test
	public void testOutGoingOnlyEmptyExactMatchCode() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(itr);
		assertFalse(itr.hasNext());	
	}
	
	@Test
	public void testInGoingOnlyEmptyExactMatchCode() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator itr = (GraphNodeContentTrackingIterator) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		assertEquals("SHH", itr.next().getCode());
		assertEquals("SOS",itr.next().getCode());
		
	}
	
	@Test
	public void testGetResolvedConceptReferenceForPropertyContains(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("NeoplasticDisease")));
	}
	
	@Test
	public void testGetResolvedConceptReferenceForExactMatchName(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C48323")));;
	}
	
	@Test
	public void testGetResolvedConceptReferenceForExactMatchCode(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C48323")));;
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOF(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"gene_related_to_disease", 
				Direction.SOURCE_OF, 
				"NeoplasticDisease", 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SHH")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SOS")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOf(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"Concept_In_Subset", 
				Direction.SOURCE_OF, 
				"C48323", 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99999")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99998")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99988")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99989")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationTargetOf(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"Concept_In_Subset", 
				Direction.TARGET_OF, 
				"C48323", 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99999")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99998")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99988")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99989")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C54453")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOfEmptyGraph(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"AllDifferent", 
				Direction.SOURCE_OF, 
				"C48323", 
				url);
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationTargetOfEmptyGraph(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"AllDifferent", 
				Direction.TARGET_OF, 
				"C48323", 
				url);
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOfB(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"subClassOf", 
				Direction.SOURCE_OF, 
				"Patient", 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientDrivingAround")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientWalkingAround")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HealthyPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientDrivingAround")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("CancerPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("VerySickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickCancerPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("Person")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("TotalPerson")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationTargetOfB(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"subClassOf", 
				Direction.TARGET_OF, 
				"Patient", 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientDrivingAround")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientWalkingAround")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("HealthyPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientDrivingAround")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("CancerPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("VerySickPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickCancerPatient")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Person")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("TotalPerson")));
	}
	
	@Test
	public void testGetResolvedConceptReferenceForContainsProperty(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
		
	}
	
	@Test
	public void testGetResolvedConceptReferenceForLuceneProperty(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.LUCENE, 
				ModelMatch.PROPERTY, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
	}
	
	
	@Test
	public void testGetResolvedConceptReferenceForCode(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE, 
				url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
	}
	
	
	@Test
	public void testGetAssnsForRelandCode(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> assns = ngr.getValidAssociationsForTargetOrSourceOf(ref, "BRaf");
		assertNotNull(assns);
		assertTrue(assns.size() > 0);
		assertEquals(4, assns.size());
		assertTrue(assns.stream().anyMatch(x -> x.equals("gene_expressed_in")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("subClassOf")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("equivalentClass")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("disjointUnion")));
		
	}
	
	@Test
	public void testGetAssnsForRelandCodeOneDifferent(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> assns = ngr.getValidAssociationsForTargetOrSourceOf(ref, "Ras");
		assertNotNull(assns);
		assertTrue(assns.size() > 0);
		assertEquals(4, assns.size());
		assertTrue(assns.stream().anyMatch(x -> x.equals("disjointUnion")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("subClassOf")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("equivalentClass")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("has_physical_location")));
		
	}
	
	@Test
	public void testGetAssnsForRelandCodeOneOnly(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> assns = ngr.getValidAssociationsForTargetOrSourceOf(ref, "C61410");
		assertNotNull(assns);
		assertTrue(assns.size() > 0);
		assertEquals(1, assns.size());
		assertFalse(assns.stream().anyMatch(x -> x.equals("disjointUnion")));
		assertTrue(assns.stream().anyMatch(x -> x.equals("subClassOf")));
		assertFalse(assns.stream().anyMatch(x -> x.equals("equivalentClass")));
		assertFalse(assns.stream().anyMatch(x -> x.equals("has_physical_location")));
		assertFalse(assns.stream().anyMatch(x -> x.equals("non_existent_rel")));
	}
	
	@Test
	public void testIsValidNodeForAssnOne(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidNodeForAssociation(ref, "C61410", "subClassOf"));
		assertFalse(ngr.isValidNodeForAssociation(ref, "C61410", "disjointUnion"));
		assertFalse(ngr.isValidNodeForAssociation(ref, "C61410", "equivalentClass"));		
	}
	
	@Test
	public void testIsValidNodeForAssnMultiple(){
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidNodeForAssociation(ref, "Ras", "subClassOf"));
		assertTrue(ngr.isValidNodeForAssociation(ref, "Ras", "disjointUnion"));
		assertTrue(ngr.isValidNodeForAssociation(ref, "Ras", "equivalentClass"));	
		assertTrue(ngr.isValidNodeForAssociation(ref, "Ras", "has_physical_location"));
		assertFalse(ngr.isValidNodeForAssociation(ref, "Ras", "gene_expressed_in"));
		assertFalse(ngr.isValidNodeForAssociation(ref, "Ras", "non_existent_rel"));
	}
	
	@Test
	public void testGetDistinctByProperty(){
		String[] strings = new String[]{"cat", "dog", "rat", "chicken", "horse", "bat"};
		List<String> filteredList = 
		Stream.of(strings).filter(ngr.distinctByProperty(String::length)).collect(Collectors.toList());
		assertTrue(filteredList.size() > 0);
		assertEquals(3, filteredList.size());
		assertTrue(filteredList.contains("cat"));
		assertFalse(filteredList.contains("dog"));
		assertFalse(filteredList.contains("rat"));
		assertTrue(filteredList.contains("chicken"));
		assertTrue(filteredList.contains("horse"));
		assertFalse(filteredList.contains("bat"));	
	}
	
	@Test
	public void testGetConceptReferenceListForValidatedAssociation() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.CODE, AlgorithmMatch.EXACT_MATCH, "C61410");
		List<ConceptReference> refs = ngr.getConceptReferenceListForValidatedAssociation(ref, "subClassOf", Direction.TARGET_OF, set, url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals("C54443", refs.get(0).getCode());
	}
	
	@Test
	public void testIsValidAssociation() throws LBParameterException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(ngr.isValidAssociation("subClassOf", ref));
		assertTrue(ngr.isValidAssociation("AllDifferent", ref));
		assertFalse(ngr.isValidAssociation("FalseAssociation", ref));
	}
	
	@Test
	public void testGetValidatedList() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient");
		ResolvedConceptReference[] refs = ngr.getValidatedList(ref, "subClassOf", set);
		assertNotNull(refs);
		assertTrue(refs.length > 0);
		assertTrue(Stream.of(refs).anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(Stream.of(refs).anyMatch(x -> x.getCode().equals("PatientWithCold")));
	}
	
	@Test
	public void testGetValidatedListEmpty() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient");
		ResolvedConceptReference[] refs = ngr.getValidatedList(ref, "AllDifferent", set);
		assertNotNull(refs);
		assertFalse(refs.length > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForAllAssociationsSource() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient");
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations(ref, Direction.SOURCE_OF, set, url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(11, refs.size());
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("patient_has_prognosis")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
	}
	
	@Test
	public void testGetConceptReferenceListForAllAssociationsTarget() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient");
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations(ref, Direction.TARGET_OF, set, url);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(5, refs.size());
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Person")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Cold")));
	}
	
	@Test
	public void testGetConceptReferenceListForAllAssociationsLeaf() throws LBException{
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "PatientWithCold");
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations(ref, Direction.TARGET_OF, set, url);
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
}
