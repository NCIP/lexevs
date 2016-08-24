package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Definition;
import org.junit.Before;
import org.junit.Test;

public class NewOWL2SnippetTestIT extends DataLoadTestBaseSnippet2 {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
	}
	//CodingScheme MetaData Tests
	
	@Test
	public void testForCodingSchemeMetaData() throws ParseException{
		assertTrue(cs.getDefaultLanguage().equals("en"));
		
		
		SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy");
		Date date;
		String stringDate = "august 08, 2014";
		date = formatDate.parse(stringDate);
		assertNotNull(cs.getEffectiveDate());
		assertTrue(cs.getEffectiveDate().compareTo(date) == 0);
		
		assertTrue(cs.getEntityDescription().getContent().equals("Test of OWL2 constructions for import into LexEVS.  This file contains defines with annotations."));
		boolean hasVersionIRI = false;
		for(Property prop: cs.getProperties().getProperty()){
			if(prop.getPropertyName().equals("versionIRI") && prop.getValue().getContent().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl/0.1.2")){
				hasVersionIRI = true;
				break;
			}
		}
		assertTrue(hasVersionIRI);
		assertTrue(cs.getSourceCount() > 0);
		assertTrue(cs.getSource(0).getContent().equals("nci evs"));
		
	}

	//Entity Unit Tests

	@Test
	public void testNamespaceNotPresentInEntityName1() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("http://purl.obolibrary.org/obo/CL_0000000"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}

	@Test
	public void testNamespaceNotPresentInEntityName2() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("owl2lexevs:CL_0000000"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testURLForExternalClass() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("CL_0000000"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
	}
	

	@Test
	public void testURLForExternalClass2() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("CL_0000148", 
				LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferencesIterator itr1 = cns.resolve(null, null, null);
		assertNotNull(itr1);
		assertTrue(itr1.hasNext());
	}
	
	@Test
	public void testEntityForDataTypeProperty1st() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("has_physical_location"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
	}
	
	@Test
	public void testEntityForDataTypePropertyProvenance() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		ConceptReference cr = new ConceptReference();
		cr.addEntityType("association");
		cr.setCode("in_organism");
		
		ConceptReferenceList list = new ConceptReferenceList();
		list.addConceptReference(cr);
		String[] stringList = {"in_organism"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
				
		assertTrue(rcr.getEntity().getDefinitionCount() > 0);
		Definition def = rcr.getEntity().getDefinition()[0];
		def.getValue().getContent().equals("hello there");
		assertTrue(validatePropertyQualifierFromProperty(def, "somebody said so"));
	}
	
	@Test
	public void testEntityForDataTypePropertySemanticTypeList()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
			cns = cns.restrictToCodes(Constructors.createConceptReferenceList("semanticType"));
			ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			ResolvedConceptReference rcr = itr.next();
			assertTrue(rcr.getEntity().getDefinitionCount() > 0);
			Definition def = rcr.getEntity().getDefinition()[0];
			assertTrue(def.getValue().getContent().equals("Anatomic"));
			Definition def1 = rcr.getEntity().getDefinition()[1];
			assertTrue(def1.getValue().getContent().equals("Conceptual"));
			Definition def2 = rcr.getEntity().getDefinition()[2];
			assertTrue(def2.getValue().getContent().equals("Disease"));
			Definition def3 = rcr.getEntity().getDefinition()[3];
			assertTrue(def3.getValue().getContent().equals("Gene"));
			Definition def4 = rcr.getEntity().getDefinition()[4];
			assertTrue(def4.getValue().getContent().equals("Organism"));	
	}
	
	
	//Loading Annotations as Properties
	
	@Test
	public void testPropertyForAnnotationPropertyAssociationURI()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("HappyPatientWalkingAround"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();		
		assertTrue(validateProperty("AssociationURI", "http://purl.obolibrary.org/obo/CL_0000148", rcr));
	}
	
	@Test
	public void testPropertyForAnnotationPropertyAssociationLIT()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("HappyPatientWalkingAround"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();		
		assertTrue(validateProperty("AssociationLIT", "http://purl.obolibrary.org/obo/cl_0000148", rcr));
	}
	
	@Test
	public void testPropertyForAnnotationPropertyAssociationSTR()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("HappyPatientWalkingAround"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();		
		assertTrue(validateProperty("AssociationSTR", "http://purl.obolibrary.org/obo/CL_0000148", rcr));
	}
	
	@Test
	public void testPropertyForAnnotationPropertyAssociationV1()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("HappyPatientWalkingAround"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();		
		assertTrue(validateProperty("AssociationV1", "http://purl.obolibrary.org/obo/CL_0000148", rcr));
	}
	
	@Test
	public void testEntityForAnnotationPropertyAssociationURI()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("AssociationURI"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();		
		assertTrue(validateProperty("term", "Association", rcr));
	}
	
	@Test
	public void testEntityForAnnotationPropertyAssociaitonLIT()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("AssociationLIT"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
 		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testEntityForAnnotationPropertyAssociationSTR()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("AssociationSTR"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	
	@Test
	public void testPropertyForAnnotationPropertySource()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("Person"));
		ResolvedConceptReferencesIterator itr  = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		ResolvedConceptReference ref = itr.next();
		assertTrue(validatePropertyQualifier("definition", "common knowledge", ref));
	}
	
	@Test
	public void testPropertyForAnnotationPropertyterm()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("PersonRole"));
		ResolvedConceptReferencesIterator itr  = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		ResolvedConceptReference ref = itr.next();
		assertTrue(validateProperty("term", "Personal Role", ref));
	}
	
	public void testPropertyForAnnotationPropertyterm_type()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("PersonRole"));
		ResolvedConceptReferencesIterator itr  = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		ResolvedConceptReference ref = itr.next();
		//Gets term property which has a qualifier of term_type
		assertTrue(validatePropertyQualifier("term", "SY", ref));
	}
		
	@Test
	public void testEntityForAnnotationPropertySource()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("source"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	
	
	@Test
	public void testEntityForAnnotationPropertyTerm()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("term"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());;
	}
	
	@Test
	public void testEntityForAnnotationPropertyTermType()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("term_type"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testEntityForAnnotationPropertyDate()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("dc:date"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	
	//Relationship Unit Tests
	@Test
	public void testAnonNodeWithUnattachedRestriction() throws LBInvocationException, LBParameterException {
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(null, 
				true, false, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SickPatient", itr));
		//TODO develop a better definition of the anonymous node
		}
	
	@Test
	public void testAssocURIAnnotationLoadAlpha() throws LBInvocationException, LBParameterException {
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
		}
		
	@Test
	public void testAssocURIAnnotationLoadBeta() throws LBInvocationException, LBParameterException {
		NameAndValueList nvlist = new NameAndValueList();
		NameAndValue nv = new NameAndValue();
		nv.setName("note");
		nv.setContent("annotation on an AssociationURI.");
		nvlist.addNameAndValue(nv);
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), nvlist);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
		}
	
	public void testEquivalentClassAnonLoad() throws LBInvocationException, LBParameterException {
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
		}
	
	@Test
	public void testForDuplicateAssociations() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationV1"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround_OWL_IND", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(list.getResolvedConceptReferenceCount() == 1);
		assertTrue(validateTarget("PrognosisGood", itr));		
	}
	
	@Test
	public void testAssocLoadingDisjointWith_LEXEVS_685() throws LBInvocationException, LBParameterException {
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointWith"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("Person", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("DiseasesDisordersFindings", itr));
		}
	
	@Test
	public void testSubClassOfExternalNamedClass() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("EpithelialCell", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, false, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(validateTarget("CL_0000000", itr));
	}
	
	@Test
	public void testAssociationExternalClassSTR() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationSTR"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertFalse(validateTarget("http://purl.obolibrary.org/obo/CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassV1() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationV1"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassURI() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassLIT() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationLIT"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertFalse(validateTarget("http://purl.obolibrary.org/obo/CL_0000148", itr));
	}
	
	
	
	@Test
	public void testEquivalentClassIntersectionNamedClass() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("TotalPerson", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Person", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionNamedClassAnd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("TotalPerson", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PersonRole", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass1st() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("BRaf", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass2nd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Erbb2", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass3rd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Mefv", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass4th() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SOS", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass5th() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("actin", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass6th() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SHH", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass7th() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Ras", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass8th() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("k-Ras", itr));
	}
	
	@Test
	public void testEquivalentComplementNamedclass() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("complementOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("HealthyPatient", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("SickPatient", itr));
	}
	
//	@Test
//	public void testEquivalentClassOneOf() 
//			throws LBInvocationException, LBParameterException{
//	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
//	ResolvedConceptReferenceList list = cng.resolveAsList(
//			Constructors.createConceptReference("Finding", 
//					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
//			true, true, 1, 1, null, null, null, null, -1);
//	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//	assertTrue(validateTarget("Gene", itr));
//	}
	
	@Test
	public void testEquivalentClassOneOfFirst() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("Fever", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassOneOfSecond() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("PaleSkin", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassOneOfThird() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("ShallowBreathing", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassSomeDatatypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testClassSomeDatatypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("xsd:positiveInteger"));
	ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("xsd:positiveInteger", itr));
	}
	
	
	@Test
	public void testEquivalentClassAllDatatypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("k-Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testClassAllDatatypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("xsd:positiveInteger"));
	ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("k-Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("xsd:positiveInteger", itr));
	}
	
	@Test
	public void testEquivalentDatatypeHasValue() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("OncogeneTim", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentDatatypeHasValueAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("integer"));
	ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("OncogeneTim", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifier("integer", "12345", itr));
	}
	
	@Test
	public void testEquivalentDatatypeHasValueRestrictToValue() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("integer"));
	ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), 
			Constructors.createNameAndValueList("DataHasValue", "12345"));
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("OncogeneTim", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifier("integer", "12345", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("SOS", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("SOS", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("NeoplasticDisease", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionExternalClass() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("BRaf", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionExternalClassAnd() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("BRaf", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionExternalClass() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("Erbb2", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionExternalClassAnd() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("Erbb2", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("SHH", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("SHH", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("NeoplasticDisease", itr));
	}
	
	@Test
	public void testEquivalentClassObjectHasValue() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("Mefv", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("C123", itr));
	}
	
	public void testEquivalentClassObjectHasValueAnd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("Mefv", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Fever", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictionsAnd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("MildlySickPatient", itr));
	}
	
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictions2ndAnd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictions3ndAnd() 
			throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictionsAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("VerySickPatient", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictions1stOr() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisBad", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictions2ndOr() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorMalignant", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup1stAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("CancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SickPatient", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup2ndAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup3rdAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisBad", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup4thAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup5thAnd() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorMalignant", itr));
	}
	
	@Test
	public void testEquivalentClassRoleStopNestingObjectValues() throws LBInvocationException, LBParameterException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorMalignant", itr));
	}
	
	@Test
	public void testEquivalentClassRoleStopNestingObjectValuesWithEmptyQualValue() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateQualifier("TumorMalignant", "", itr));
	}
	
	@Test
	public void testUnionObjectAndDataTypeFirstOr() throws LBException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("actin", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//		assertTrue(validateTarget("EpithelialCell", itr));
		assertTrue(validateQualifier("EpithelialCell", "", itr));
	}
	
	@Test
	public void testUnionObjectAndDataType2ndOr() throws LBException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("PlainLiteral"));
		ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
		assertNotNull(rcri);
		assertTrue(rcri.hasNext());
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("in_organism"), null);
		ResolvedConceptReferenceList list1 = cng.resolveAsList(
				Constructors.createConceptReference("actin", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr1 = list1.iterateResolvedConceptReference();
		assertTrue(validateQualifier("PlainLiteral", "all organisms", itr1));
	}
	
	@Test
	public void testIntersectionObjectAndDatatype1stAnd() throws LBException{
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(
				Constructors.createConceptReference("Brca1", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateQualifier("EpithelialCell", "", itr));
	}
	
	@Test
	public void testIntersectionObjectAndDatatype2ndAnd() throws LBException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("PlainLiteral"));
		ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
		assertNotNull(rcri);
		assertTrue(rcri.hasNext());
		cng = cng.restrictToAssociations(
				Constructors.createNameAndValueList("in_organism"), null);
		ResolvedConceptReferenceList list1 = cng.resolveAsList(
				Constructors.createConceptReference("Brca1", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr1 = list1.iterateResolvedConceptReference();
		assertTrue(validateQualifier("PlainLiteral", "homo sapiens", itr1));
	}
	

}
