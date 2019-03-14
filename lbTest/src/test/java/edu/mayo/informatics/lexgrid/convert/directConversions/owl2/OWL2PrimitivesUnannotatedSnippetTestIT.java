package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
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
import org.junit.Test;

public class OWL2PrimitivesUnannotatedSnippetTestIT extends DataLoadTestBaseUnannotatedSnippet {

	public OWL2PrimitivesUnannotatedSnippetTestIT() {
		
	}
	
	//CodingScheme MetaData Tests
	
	@Test
	public void testForCodingSchemeMetaData() throws ParseException{
		assertTrue(csp.getDefaultLanguage().equals("en"));
		
		
		SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy");
		Date date;
		String stringDate = "august 08, 2014";
		date = formatDate.parse(stringDate);
		assertNotNull(csp.getEffectiveDate());
		assertTrue(csp.getEffectiveDate().compareTo(date) == 0);
		
		assertTrue(csp.getEntityDescription().getContent().equals("Test of OWL2 constructions for import into LexEVS.  This is the baseline file, all the entities are primitive and unannotated."));
		boolean hasVersionIRI = false;
		for(Property prop: csp.getProperties().getProperty()){
			if(prop.getPropertyName().equals("versionIRI") && prop.getValue().getContent().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl/0.1.0")){
				hasVersionIRI = true;
				break;
			}
		}
		assertTrue(hasVersionIRI);
		assertTrue(csp.getSourceCount() > 0);
		assertTrue(csp.getSource(0).getContent().equals("nci evs"));
		
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
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("CL_0000000"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());
	}
	

	@Test
	public void testURLForExternalClass2() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("CL_0000148", 
				LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferencesIterator itr1 = cnsp.resolve(null, null, null);
		assertNotNull(itr1);
		assertTrue(itr1.hasNext());
	}
	
	@Test
	public void testEntityForDataTypeProperty1st() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("has_physical_location"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
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
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cnsp.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
// provenance not defined because there is no annotation in this source			
//		assertTrue(rcr.getEntity().getDefinitionCount() > 0);
//		Definition def = rcr.getEntity().getDefinition()[0];
//		def.getValue().getContent().equals("hello there");
//		assertTrue(validatePropertyQualifierFromProperty(def, "somebody said so"));
	}
	
	@Test
	public void testEntityForDataTypePropertySemanticTypeList()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
			cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("semanticType"));
			ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
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
	
	
//	@Test
//	public void testEntityForAnnotationPropertyAssociationURI()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
//		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("AssociationURI"));
//		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
//		assertNotNull(itr);
//		assertTrue(itr.hasNext());
//		ResolvedConceptReference rcr = itr.next();		
//		assertTrue(validateProperty("term", "Association", rcr));
//	}
//	
//	@Test
//	public void testEntityForAnnotationPropertyAssociaitonLIT()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
//		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("AssociationLIT"));
//		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
//		assertNotNull(itr);
//		assertTrue(itr.hasNext());
//		ResolvedConceptReference rcr = itr.next();
//		assertTrue(validateProperty("term", "Association", rcr));
//	}
//	
//	@Test
//	public void testEntityForAnnotationPropertyAssociationSTR()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
//		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("AssociationSTR"));
//		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
//		assertNotNull(itr);
//		assertTrue(itr.hasNext());
//		ResolvedConceptReference rcr = itr.next();
//		assertTrue(validateProperty("term", "Association", rcr));
//	}
	
	@Test
	public void testEntityForAnnotationPropertySource()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("source"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testEntityForAnnotationPropertyTerm()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("term"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());;
	}
	
	@Test
	public void testEntityForAnnotationPropertyTermType()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("term_type"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testEntityForAnnotationPropertyDate()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("dc:date"));
		ResolvedConceptReferencesIterator itr = cnsp.resolve(null, null, null);
		assertNotNull(itr);
		assertFalse(itr.hasNext());
	}
	
	
	//Relationship Unit Tests
//	@Test
//	public void testAnonNodeWithUnattachedRestriction() throws LBInvocationException, LBParameterException {
//		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
//		ResolvedConceptReferenceList list = cngp.resolveAsList(null, 
//				true, false, 1, 1, null, null, null, null, -1);
//		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//		assertTrue(validateTarget("SickPatient", itr));
//		//TODO develop a better definition of the anonymous node
//		}
	
//	@Test
//	public void testAssocURIAnnotationLoadAlpha() throws LBInvocationException, LBParameterException {
//		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), null);
//		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
//				true, true, 1, 1, null, null, null, null, -1);
//		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//		assertTrue(validateTarget("PrognosisGood", itr));
//		}
	// Not a valid test for an unannotated source		
//	@Test
//	public void testAssocURIAnnotationLoadBeta() throws LBInvocationException, LBParameterException {
//		NameAndValueList nvlist = new NameAndValueList();
//		NameAndValue nv = new NameAndValue();
//		nv.setName("note");
//		nv.setContent("annotation on an AssociationURI.");
//		nvlist.addNameAndValue(nv);
//		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), nvlist);
//		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
//				true, true, 1, 1, null, null, null, null, -1);
//		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//		assertTrue(validateTarget("PrognosisGood", itr));
//		}
	
	public void testEquivalentClassAnonLoad() throws LBInvocationException, LBParameterException {
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
		}
	
	@Test
	public void testForDuplicateAssociations() throws LBInvocationException, LBParameterException{
		boolean foundIndividual = false;
		//TODO insure these duplicate associations are correct.  If not uncomment this and correct it as necessary.  
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationV1"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		if(itr.hasNext()){
			ResolvedConceptReference ref = itr.next();
			Association[] assocs = ref.getSourceOf().getAssociation();
			for(Association assoc : assocs){
				if(assoc.getAssociatedConcepts().getAssociatedConceptCount() > 1){
				AssociatedConcept[] concepts = assoc.getAssociatedConcepts().getAssociatedConcept();

				for(AssociatedConcept con: concepts){
					if(con.getEntity().getEntityType(0).equals("instance"))
					{
						foundIndividual = true;
						break;
					}
				}
				assertTrue(foundIndividual);
				assertNotSame(concepts[0].getCode(),concepts[1].getCode());
				}
			}
		}
	}
	
	@Test
	public void testAssocLoadingDisjointWith_LEXEVS_685() throws LBInvocationException, LBParameterException {
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointWith"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("Person", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("DiseasesDisordersFindings", itr));
		}
	
	@Test
	public void testSubClassOfExternalNamedClass() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
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
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationSTR"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertFalse(validateTarget("http://purl.obolibrary.org/obo/CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassV1() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationV1"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassURI() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationURI"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testAssociationExternalClassLIT() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("AssociationLIT"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("HappyPatientWalkingAround", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertFalse(validateTarget("http://purl.obolibrary.org/obo/CL_0000148", itr));
	}
	
	
	
	@Test
	public void testEquivalentClassIntersectionNamedClass() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("TotalPerson", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Person", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionNamedClassAnd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("TotalPerson", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PersonRole", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass1st() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("BRaf", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass2nd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Erbb2", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass3rd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Mefv", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass4th() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SOS", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass5th() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("actin", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass6th() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SHH", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass7th() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Ras", itr));
	}
	
	@Test
	public void testEquivalentClassUnionNamedDisjointUnionClass8th() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("disjointUnion"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("C123", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("k-Ras", itr));
	}
	
	@Test
	public void testEquivalentComplementNamedclass() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("complementOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("HealthyPatient", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("SickPatient", itr));
	}
	
//	@Test
//	public void testEquivalentClassOneOf() 
//			throws LBInvocationException, LBParameterException{
//	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
//	ResolvedConceptReferenceList list = cngp.resolveAsList(
//			Constructors.createConceptReference("Finding", 
//					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
//			true, true, 1, 1, null, null, null, null, -1);
//	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//	assertTrue(validateTarget("Gene", itr));
//	}
	
	@Test
	public void testEquivalentClassOneOfFirst() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("Fever", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassOneOfSecond() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("PaleSkin", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassOneOfThird() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("type"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("ShallowBreathing", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Finding", itr));
	}
	
	@Test
	public void testEquivalentClassSomeDatatypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testClassSomeDatatypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("xsd:positiveInteger"));
	ResolvedConceptReferencesIterator rcri = cnsp.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("xsd:positiveInteger", itr));
	}
	
	
	@Test
	public void testEquivalentClassAllDatatypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("k-Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testClassAllDatatypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("xsd:positiveInteger"));
	ResolvedConceptReferencesIterator rcri = cnsp.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("k-Ras", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("xsd:positiveInteger", itr));
	}
	
	@Test
	public void testEquivalentDatatypeHasValue() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("OncogeneTim", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentDatatypeHasValueAnd() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("integer"));
	ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null);
	assertNotNull(rcri);
	assertTrue(rcri.hasNext());
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("has_physical_location"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("OncogeneTim", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifier("integer", "12345", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("SOS", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("SOS", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("NeoplasticDisease", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionExternalClass() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("BRaf", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassSomeObjectTypeRestrictionExternalClassAnd() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("BRaf", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionExternalClass() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("Erbb2", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionExternalClassAnd() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("Erbb2", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("CL_0000148", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestriction() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("SHH", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("C123", itr));
	}
	
	@Test
	public void testEquivalentClassAllObjectTypeRestrictionAnd() 
			throws LBInvocationException, LBParameterException{
	cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
	ResolvedConceptReferenceList list = cngp.resolveAsList(
			Constructors.createConceptReference("SHH", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("NeoplasticDisease", itr));
	}
	
	@Test
	public void testEquivalentClassObjectHasValue() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("Mefv", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("C123", itr));
	}
	
	public void testEquivalentClassObjectHasValueAnd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_related_to_disease"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("Mefv", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("Fever", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictionsAnd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("MildlySickPatient", itr));
	}
	
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictions2ndAnd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
	}
	
	@Test
	public void testEquivalentClassIntersectionObjectRestrictions3ndAnd() 
			throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("MildlySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictionsAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("VerySickPatient", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictions1stOr() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisBad", itr));
	}
	
	@Test
	public void testEquivalentClassUnionObjectRestrictions2ndOr() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("VerySickCancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorMalignant", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup1stAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("CancerPatient", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("SickPatient", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup2ndAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup3rdAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_prognosis"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("PrognosisBad", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup4thAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorBenign", itr));
	}
	
	@Test
	public void testEquivalentClassRoleGroup5thAnd() throws LBInvocationException, LBParameterException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("TumorMalignant", itr));
	}
	
	@Test
	public void testEquivalentClassRoleStopNestingObjectValuesWithEmptyQualValue() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("CancerPatient", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateQualifier("TumorMalignant", "TumorMalignant", itr));
	}
	
	@Test
	public void testUnionObjectAndDataTypeFirstOr() throws LBException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("actin", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateQualifier("EpithelialCell", "EpithelialCell", itr));
	}
	
	@Test
	public void testUnionObjectAndDataType2ndOr() throws LBException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("string"));
		ResolvedConceptReferencesIterator rcri = cnsp.resolve(null, null, null);
		assertNotNull(rcri);
		assertTrue(rcri.hasNext());
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("in_organism"), null);
		ResolvedConceptReferenceList list1 = cngp.resolveAsList(
				Constructors.createConceptReference("actin", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr1 = list1.iterateResolvedConceptReference();
		assertTrue(validateQualifier("string", "all organisms", itr1));
	}
	
	@Test
	public void testIntersectionObjectAndDatatype1stAnd() throws LBException{
		cngp = cngp.restrictToAssociations(Constructors.createNameAndValueList("gene_expressed_in"), null);
		ResolvedConceptReferenceList list = cngp.resolveAsList(
				Constructors.createConceptReference("Brca1", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateQualifier("EpithelialCell", "EpithelialCell", itr));
	}
	
	@Test
	public void testIntersectionObjectAndDatatype2ndAnd() throws LBException{
		cnsp = cnsp.restrictToCodes(Constructors.createConceptReferenceList("string"));
		ResolvedConceptReferencesIterator rcri = cnsp.resolve(null, null, null);
		assertNotNull(rcri);
		assertTrue(rcri.hasNext());
		cngp = cngp.restrictToAssociations(
				Constructors.createNameAndValueList("in_organism"), null);
		ResolvedConceptReferenceList list1 = cng.resolveAsList(
				Constructors.createConceptReference("Brca1", 
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr1 = list1.iterateResolvedConceptReference();
		assertTrue(validateQualifier("string", "homo sapiens", itr1));
	}
	

}
