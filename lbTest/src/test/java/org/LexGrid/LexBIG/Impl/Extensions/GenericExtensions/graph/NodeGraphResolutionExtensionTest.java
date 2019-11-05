package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
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
	public void testGetConceptReferenceListForCodeAndAssociationTargetOf(){
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
	public void testGetResolvedConceptReferenceForContainsName(){
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


}
