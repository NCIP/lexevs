package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.ModelMatch;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class NodeGraphResolutionExtensionTest {

	
	NodeGraphResolutionExtensionImpl ngr;
	Properties p;
	
	public static String url;


	@Before
	public void setUp() throws Exception {
		ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
				.defaultInstance()
				.getGenericExtension("NodeGraphResolution");
		Properties p = new Properties();
		p.load(new FileReader(new File("resources/testData/test.properties")));
		url = p.getProperty("grapdbURL");
		ngr.init(url);
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors
				.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
						"0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("EpithelialCell");
		codes.add("string");
		codes.add("C123");
		GraphNodeContentTrackingIterator<ConceptReference>  itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.
				getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				null, 
				"Brca1", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(3, itr.getTotalCacheSize());

		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingExactMatchEmptyName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		Iterator<ConceptReference> itr = ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"patient_has_prognosis", 
				"CancerPatient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyExactMatchName2() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"patient_has_prognosis", 
				"CancerPatient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(itr.next().getCode().startsWith("Prognosis"));
	}
	
	@Test
	public void testInGoingOnlyExactMatchName2() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("CancerPatient");
		codes.add("VerySickCancerPatient");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"patient_has_prognosis", 
				"PrognosisBad", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyExactMatchNameSlightVariant() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("CancerPatient");
		codes.add("MildlySickCancerPatient");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"patient_has_prognosis", 
				"PrognosisGood", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyEmptyExactMatchName() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"patient_has_prognosis", 
				"PrognosisBad", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.NAME);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyContainsName3() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		codes.add("C48323");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(5, itr.getTotalCacheSize());
		assertEquals(3, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyContainsName4() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref 
		= Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyEmptyContainsName() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.NAME );
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyLuceneProperty2() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.LUCENE, 
				ModelMatch.PROPERTY );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testIncomingLucentPropertyWSource(){
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C37927");
		codes.add("C48323");
		codes.add("C99998");
		codes.add("C99999");
		codes.add("C99988");
		codes.add("C99989");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				-1, 
				ref, 
				"Concept_In_Subset", 
				"OETESTCD",
				AlgorithmMatch.LUCENE, 
				ModelMatch.PROPERTY,
				Constructors.createLocalNameList("CDISC"),
				null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(6, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
				
	}
	
	
	@Test
	public void testIncomingLucentPropertyWQual(){
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("EpithelialCell");
		codes.add("CL_0000148");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				-1, 
				ref, 
				"subClassOf", 
				"purl",
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY,
				null,
				Constructors.createNameAndValueList("source-code", "CTESTCODE"));
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
				
	}
	
	@Test
	public void testIncomingLucentPropertyWQualToo(){
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("EpithelialCell");
		codes.add("CL_0000148");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				-1, 
				ref, 
				"subClassOf", 
				"obo",
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY,
				null,
				Constructors.createNameAndValueList("source-code", "CTESTCODETOO"));
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
				
	}
	
	@Test
	public void testInGoingOnlyLuceneProperty4() throws InterruptedException {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		Thread.sleep(1000);
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"black", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyEmptyLuceneProperty() throws InterruptedException {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		Thread.sleep(1000);
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY );
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testOutGoingOnlyExactMatchCode() throws InterruptedException {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		Thread.sleep(1000);
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertNotSame(3, itr.getTotalCacheSize());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyExactMatchCode4() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyExactMatchCodeDepth1() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				1,
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyExactMatchCodeDepth2() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				2,
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyExactMatchCodeDepth1() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				1,
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingExactMatchCodeDepthPatient1() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("Person");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				1,
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(1, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingExactMatchCodeDepthPatient2NoChange() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("Person");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				2,
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(1, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInComingExactMatchCodeDepthPatientDepth1() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				1,
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(4, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInComingExactMatchCodeDepthPatientDepth2() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		codes.add("VerySickPatient");
		codes.add("MildlySickPatient");
		codes.add("CancerPatient");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				2,
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(7, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInComingExactMatchCodeDepthPatientDepth3() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		codes.add("VerySickPatient");
		codes.add("MildlySickPatient");
		codes.add("CancerPatient");
		codes.add("VerySickCancerPatient");
		codes.add("PatientWithCold");
		codes.add("MildlySickCancerPatient");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) 
				ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				3,
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(10, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	
	
	
	@Test
	public void testOutGoingOnlyExactMatchCodeDepth2NoChange() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				2,
				ref, 
				"Concept_In_Subset", 
				"C48323", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertEquals(2, itr.getTotalCacheSize());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyEmptyExactMatchCode() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationTargetOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertFalse(itr.hasNext());	
	}
	
	@Test
	public void testInGoingOnlyEmptyExactMatchCode() {
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<String> codes = new ArrayList<String>();
		codes.add("SHH");
		codes.add("SOS");
		GraphNodeContentTrackingIterator<ConceptReference> itr = (GraphNodeContentTrackingIterator<ConceptReference>) ngr.getConceptReferencesForTextSearchAndAssociationSourceOf(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testGetResolvedConceptReferenceForPropertyContains(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"gene_related_to_disease", 
				"NeoplasticDisease", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY );
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
				ModelMatch.NAME );
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
				ModelMatch.CODE );
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C48323")));;
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOF(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"gene_related_to_disease", 
				Direction.SOURCE_OF, 
				"NeoplasticDisease" );
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SHH")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SOS")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOf(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"Concept_In_Subset", 
				Direction.SOURCE_OF, 
				"C48323" );
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99999")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99998")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99988")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99989")));
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationTargetOf(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"Concept_In_Subset", 
				Direction.TARGET_OF, 
				"C48323" );
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"AllDifferent", 
				Direction.SOURCE_OF, 
				"C48323" );
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationTargetOfEmptyGraph(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"AllDifferent", 
				Direction.TARGET_OF, 
				"C48323" );
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForCodeAndAssociationSourceOfB(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"subClassOf", 
				Direction.SOURCE_OF, 
				"Patient" );
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ConceptReference> refs = ngr.getConceptReferenceListResolvedFromGraphForEntityCode(
				ref, 
				"subClassOf", 
				Direction.TARGET_OF, 
				"Patient" );
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.CONTAINS, 
				ModelMatch.PROPERTY );
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
		
	}
	
	@Test
	public void testGetResolvedConceptReferenceForLuceneProperty(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.LUCENE, 
				ModelMatch.PROPERTY );
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
	}
	
	
	@Test
	public void testGetResolvedConceptReferenceForCode(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List<ResolvedConceptReference> refs = ngr.getCandidateConceptReferencesForTextAndAssociation(
				ref, 
				"subClassOf", 
				"Patient", 
				AlgorithmMatch.EXACT_MATCH, 
				ModelMatch.CODE );
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.CODE, AlgorithmMatch.EXACT_MATCH, "C61410", null, null);
		List<ConceptReference> refs = ngr.getConceptReferenceListForValidatedAssociation(-1, ref, "subClassOf", Direction.TARGET_OF, set);
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
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient", null, null);
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
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient", null, null);
		ResolvedConceptReference[] refs = ngr.getValidatedList(ref, "AllDifferent", set);
		assertNotNull(refs);
		assertFalse(refs.length > 0);
	}
	
	@Test
	public void testGetConceptReferenceListForAllAssociationsSource() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient", null, null);
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations(-1, ref, Direction.SOURCE_OF, set);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(11, refs.size());
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("patient_has_prognosis")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
	}
	
	@Test
	public void testGetConceptReferenceListForAllAssociationsTarget() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "Patient", null, null);
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations( -1, ref, Direction.TARGET_OF, set);
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
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		CodedNodeSet set = ngr.getCodedNodeSetForScheme(ref);
		set = ngr.getCodedNodeSetForModelMatch(set, ModelMatch.PROPERTY, AlgorithmMatch.CONTAINS, "PatientWithCold", null, null);
		List<ConceptReference> refs = ngr.getConceptReferenceListForAllAssociations(-1, ref, Direction.TARGET_OF, set);
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetAssociatedConceptsForPropertyNoMatch() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, null,
                null,
                null,
                "PatientWithCold",
                "contains", 
                null);
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.TARGET_OF, -1, null);
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetAssociatedConceptsForAllAssociationsTargetContains() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set = set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, null,
                null,
                null,
                "Patient",
                "contains", 
                null);
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.TARGET_OF, -1, null);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(5, refs.size());
		refs.get(0).getCodingSchemeURI();
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Person")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("MildlySickPatient")));
		assertNotNull(refs.stream().filter(x -> x.getCode().equals("MildlySickPatient")).findAny().get().getEntityDescription().getContent());
		assertEquals(refs.stream().filter(x -> x.getCode().equals("MildlySickPatient")).findAny().get().getEntityDescription().getContent(), "MildlySickPatient");
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Patient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertEquals(refs.stream().filter(x -> x.getCode().equals("SickPatient")).findAny().get().getCodingSchemeURI(), "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
		assertEquals(refs.stream().filter(x -> x.getCode().equals("SickPatient")).findAny().get().getCodingSchemeVersion(), "0.1.5");
		assertEquals(refs.stream().filter(x -> x.getCode().equals("SickPatient")).findAny().get().getCodingSchemeName(), "owl2lexevs");
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Cold")));
	}
	
	@Test
	public void testGetAssociatedConceptsForAllAssociationsTargetExactMatch() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set = set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, null,
                null,
                null,
                "Patient",
                "exactMatch", 
                null);
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.TARGET_OF, -1, null);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
 		assertEquals(1, refs.size());
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("Person")));
	}
	
	@Test
	public void testGetAssociatedConceptsForAllAssociationsSource() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set = set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, null,
                null,
                null,
                "Patient",
                "contains", 
                null);
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set,Direction.SOURCE_OF, -1, null);
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(11, refs.size());
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("patient_has_prognosis")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SickPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientDrivingAround")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HappyPatientWalkingAround")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("HealthyPatient")));
	}
	
	@Test
	public void testGetAssociatedConceptReferenceForCodeTargetOfB() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("Patient"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.TARGET_OF, -1, Constructors.createNameAndValueList("association", "subClassOf"));
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
	public void testGetAssociatedConceptReferenceForCodeSourceOfB() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("Patient"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.SOURCE_OF, -1, Constructors.createNameAndValueList("association", "subClassOf"));
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
	public void testGetAssociatedConceptsForCodeAndAssociationTargetOfEmptyGraph() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("C48323"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(
				set, 
				Direction.TARGET_OF,
				-1,
				 Constructors.createNameAndValueList("associations", "AllDifferent"));
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetAssociatedConceptsForCodeAndAssociationSourceOfEmptyGraph() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("C48323"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(
				set, 
				Direction.SOURCE_OF,
				-1,
				 Constructors.createNameAndValueList("associations", "AllDifferent"));
		assertNotNull(refs);
		assertFalse(refs.size() > 0);
	}
	
	@Test
	public void testGetAssociatedConceptsForCodeAndAssociationTargetOf() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("C48323"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(
				set, Direction.TARGET_OF, -1, 
				Constructors.createNameAndValueList("association", "Concept_In_Subset"));
		assertTrue(refs.size() > 0);
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99999")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99998")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99988")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C99989")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C54453")));
	}
	
	@Test
	public void testGetAssociatedConceptsForCodeAndAssociationSourceOf() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("C48323"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(
				set, Direction.SOURCE_OF, -1, 
				Constructors.createNameAndValueList("association", "Concept_In_Subset"));
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99999")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99998")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99988")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("C99989")));
		assertFalse(refs.stream().anyMatch(x -> x.getCode().equals("C54453")));
	}
	
	@Test
	public void testGetAssociatedConceptsForForCodeAndAssociationSourceOF() throws LBException{
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		CodedNodeSet set = getLexBIGService().getCodingSchemeConcepts("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
		set.restrictToCodes(Constructors.createConceptReferenceList("NeoplasticDisease"));
		List<ResolvedConceptReference> refs = ngr.getAssociatedConcepts(set, Direction.SOURCE_OF, -1, Constructors.createNameAndValueList("association", "gene_related_to_disease" ));
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SHH")));
		assertTrue(refs.stream().anyMatch(x -> x.getCode().equals("SOS")));
	}
	
	
	@Test
	public void testGetSystemMetaRepresentationOfDatabases(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		List<String> list = ngr.getTerminologyGraphDatabaseList();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertTrue(list.stream().anyMatch(x -> x.equals("owl2lexevs")));
	}
	
	@Test
	public void testGetGraphNamesForDatabaseName(){
		assumeTrue(new GraphDbValidateConnnection(url).connect());
		List<String> list = ngr.getGraphsForCodingSchemeName("owl2lexevs");
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertTrue(list.stream().anyMatch(x -> x.equals("subClassOf")));
		assertTrue(list.stream().anyMatch(x -> x.equals("subPropertyOf")));
		assertTrue(list.stream().anyMatch(x -> x.equals("AssociationURI")));
		assertTrue(list.stream().anyMatch(x -> x.equals("Concept_In_Subset")));
		assertTrue(list.stream().anyMatch(x -> x.equals("AllDifferent")));
		assertTrue(list.stream().anyMatch(x -> x.equals("has_grain")));
		assertTrue(list.stream().anyMatch(x -> x.equals("IAO_0000116")));
		assertTrue(list.stream().anyMatch(x -> x.equals("IAO_0000136")));
	}
	
	private LexBIGServiceImpl getLexBIGService(){
		return LexBIGServiceImpl.defaultInstance();
	}
}
