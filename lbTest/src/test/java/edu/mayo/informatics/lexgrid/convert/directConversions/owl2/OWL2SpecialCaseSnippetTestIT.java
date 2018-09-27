package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.mayo.informatics.lexgrid.convert.directConversions.owlapi.OwlApi2LG;

public class OWL2SpecialCaseSnippetTestIT extends DataLoadTestBaseSpecialCases {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
	}
	
	@Test
	public void testLoadofLabelsWhenPreferredSourceHasEmptyValue() throws LBInvocationException, LBParameterException{

		String[] stringList = {"OBI_0000699"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
				
		assertTrue(rcr.getEntity().getEntityDescription().getContent().equals("survival assessment"));;
		assertTrue(rcr.getEntity().getPresentationCount() > 2);
		List<Presentation> presentations = rcr.getEntity().getPresentationAsReference();
		Boolean hasEmptyPresentation = false;
		boolean hasLabel = false;
		boolean hasEditorTerm = false;
		for(Presentation p: presentations){
			if(p.getPropertyName().equals("OBI_9991118") && StringUtils.isBlank(p.getValue().getContent())){
				hasEmptyPresentation = true;
			}
			if(p.getPropertyName().equals("editor preferred term") && p.getValue().getContent().equals("survival assessment") ){
				hasEditorTerm = true;
			}
			if(p.getPropertyName().equals("label") && p.getValue().getContent().equals("survival assessment") ){
				hasLabel = true;
			}
		}
		assertTrue(hasEmptyPresentation);
		assertTrue(hasLabel);
		assertTrue(hasEditorTerm);
	}


	@Test
	public void testRestrictOnSubClassOfToProperty() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("patient_has_finding"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("PatientWithCold", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("Cold", itr));
	}
	
	@Test
	public void testLoadOfAssociationURIAsResource() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationURIAsResource"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("HappyPatientDrivingAround", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testNoLoadOfAssociationV1AsResource() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("InvalidAssociationV1AsResource"), null);
	ResolvedConceptReferenceList list = cng.resolveAsList(
			Constructors.createConceptReference("HappyPatientDrivingAround", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 1, 1, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertFalse(validateTarget("PrognosisGood", itr));
	}
	
	@Test
	public void testRestrictOnAssociationLoadedByCodeFromByName() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.6");	
    CodedNodeGraph specialGraph = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag, null);
	specialGraph = specialGraph.restrictToAssociations(Constructors.createNameAndValueList("Chemotherapy_Regimen_Has_Component"), null);
	ResolvedConceptReferenceList list = specialGraph.resolveAsList(null, true, false, 1, 1, null, null, null, null, -1);
	assertTrue(list.getResolvedConceptReferenceCount() > 0);
	}
	
	
	@Test
	public void testContinuantHasCorrectPropertyQualification() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeSet newSet = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
		newSet= newSet.restrictToCodes(Constructors.createConceptReferenceList("BFO_0000002"));
	ResolvedConceptReferenceList list = newSet.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	ResolvedConceptReference ref = itr.next();
	for(Property prop : ref.getEntity().getAllProperties()){
		if(validatePropertyQualifierFromProperty(prop, "http://purl.obolibrary.org/obo/bfo/axiom/009-002")){
			return;
		}
	}
	fail();
	}
	
	@Test
	public void testdataTypeWithCorrectBuiltinLoads() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeSet newSet = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
		newSet= newSet.restrictToCodes(Constructors.createConceptReferenceList("Clinical_Infection"));
	ResolvedConceptReferenceList list = newSet.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(itr.hasNext());
	}
	
	@Test
	public void testEntityHasCorrectPropertyQualification() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeSet newSet = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
		newSet= newSet.restrictToCodes(Constructors.createConceptReferenceList("BFO_0000001"));
	ResolvedConceptReferenceList list = newSet.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	ResolvedConceptReference ref = itr.next();
	for(Property prop : ref.getEntity().getAllProperties()){
		if(validatePropertyQualifierFromProperty(prop, "http://purl.obolibrary.org/obo/bfo/axiom/0000004")){
			return;
		}
	}
	fail();
	}
	
	@Test
	public void testEntityHasCorrectPropertyQualifierName() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeSet newSet = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
		newSet= newSet.restrictToCodes(Constructors.createConceptReferenceList("BFO_0000001"));
	ResolvedConceptReferenceList list = newSet.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	ResolvedConceptReference ref = itr.next();
	for(Property prop : ref.getEntity().getAllProperties()){
		if(validatePropertyQualifierNameFromProperty(prop, "has axiom label")){
			return;
		}
	}
	fail();
	}
	
	@Test
	public void testRestrictOnHasRoleProperty() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("has role"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("CHEBI_15956", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("REO_0000171", itr));
	}
	
	@Test
	public void testRestrictOnHasGrainProperty() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("has grain"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("CL_0000001", itr));
	}
	
	@Test
	public void testValidateHasGrainPropertyQualifierNameOnly() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("has grain"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, false, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifierName("CL_0000001", "only", itr));
	
	}
	
	@Test
	public void testValidateHasGrainPropertyQualifierNameSome() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("has grain"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, false, 10, 10, null, null, null, null, -1);
	ResolvedConceptReference[] refs = list.getResolvedConceptReference();
	for(ResolvedConceptReference ref : refs){
		AssociationList assocs = ref.getSourceOf();
		Association[] ans = assocs.getAssociation();
		for(Association assoc: ans){
			AssociatedConcept[] acs = assoc.getAssociatedConcepts().getAssociatedConcept();
			for(AssociatedConcept ac : acs){
				NameAndValue[] nvs = ac.getAssociationQualifiers().getNameAndValue();
				for(NameAndValue nv: nvs){
					String name = nv.getName();
					String value = nv.getContent();
					System.out.println("name: " + name + " value: " +value);
				}
			}
		}
	}
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifierName("CL_0000001", "some", itr));
	
	}
	
	@Test
	public void testValidateHasGrainPropertyQualifierValueForSome() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("has grain"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, false, 10, 10, null, null, null, null, -1);

	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifier("CL_0000001", "obo:CL_0000001", itr));
	
	}
	
	@Test
	public void testRestrictToSpecifiedOutput() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("is_specified_output_of"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateTarget("OBI_0600037", itr));
	}
	
	@Test
	public void testValidateHasGrainPropertyQualifierNameExactly() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("is_specified_output_of"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifierName("OBI_0600037", "exactly", itr));
	}
	
	@Test
	public void testValidateHasGrainPropertyQualifierValueExactly() 
			throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , null);
	 newCng= newCng.restrictToAssociations(Constructors.createNameAndValueList("is_specified_output_of"), null);
	ResolvedConceptReferenceList list = newCng.resolveAsList(
			Constructors.createConceptReference("OBI_0100061", 
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
			true, true, 10, 10, null, null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertTrue(validateQualifier("OBI_0600037", "0", itr));
	}
	
	@Test
	public void testRemovalofExplicitThing() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("Thing"));
	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertFalse(validateCodeInList("Thing", itr));
	}
	
	@Test
	public void testRemovalofExplicitNothing() 
			throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
	cns = cns.restrictToCodes(Constructors.createConceptReferenceList("Nothing"));
	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
	Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
	assertFalse(validateCodeInList("Nothing", itr));
	}
	
	@Test
	public void testTopNodeLoadFromPreferences() 
			throws LBException{
	CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
	versionOrTag.setVersion(LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);
	ResolvedConceptReferenceList rootNodes = cm.getHierarchyRoots(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag , "is_a");

	Iterator<? extends ResolvedConceptReference> itr = rootNodes.iterateResolvedConceptReference();
	assertFalse(validateCodeInList("C123", itr));
	itr = rootNodes.iterateResolvedConceptReference();
	assertFalse(validateCodeInList("CL_0000000", itr));
	itr = rootNodes.iterateResolvedConceptReference();
	assertTrue(validateCodeInList("Person", itr));
	itr = rootNodes.iterateResolvedConceptReference();
	assertTrue(validateCodeInList("PersonRole", itr));
	itr = rootNodes.iterateResolvedConceptReference();
	assertTrue(validateCodeInList("DiseasesDisordersFindings", itr));
	}
	
	@Test
	public void testLoadofOBODefinedQualifiers() throws LBInvocationException, LBParameterException{

		String[] stringList = {"BFO_0000182"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
				
		assertTrue(rcr.getEntity().getPropertyCount() > 0);
		org.LexGrid.commonTypes.Property prop = null;
		for(org.LexGrid.commonTypes.Property p: rcr.getEntity().getPropertyAsReference()){
			if(p.getValue().getContent().equals("A history is a process. (axiom label in BFO2 Reference: [138-001])")){
				prop = p;
				break;
			}
		}
		assertTrue(prop.getValue().getContent().equals("A history is a process. (axiom label in BFO2 Reference: [138-001])"));
		assertTrue(validatePropertyQualifierFromProperty(prop, "http://purl.obolibrary.org/obo/bfo/axiom/138-001"));
	}
	
	@Test
	public void testLoadAnnotationsAsQualifiers() throws LBException {

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(
				LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
				versionOrTag, null);
		newCng = newCng.restrictToAssociations(
				Constructors.createNameAndValueList("has curation status"),
				null);
		ResolvedConceptReferenceList list = newCng.resolveAsList(Constructors
				.createConceptReference("IAO_0000033",
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN),
				true, true, 10, 10, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list
				.iterateResolvedConceptReference();
		assertTrue(validateTarget("IAO_0000122", itr));
	}
	
	@Test
	public void testLoadAppropriateAnnotationPredicatesInOrder() throws LBException {

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(
				LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
				versionOrTag, null);
		newCng = newCng.restrictToAssociations(
				Constructors.createNameAndValueList("imported from"),
				null);
		ResolvedConceptReferenceList list = newCng.resolveAsList(Constructors
				.createConceptReference("OBI_0500000",
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN),
				true, true, 10, 10, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list
				.iterateResolvedConceptReference();
		assertTrue(validateTarget("obi.owl", itr));
	}
	
	@Test
	public void testLoadTransitivePropertiesAsHierarchies() throws LBException {

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodingScheme scheme = lbs.resolveCodingScheme(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
		List<SupportedHierarchy> hrchy = scheme.getMappings().getSupportedHierarchyAsReference();
		boolean exists = false;
		for(SupportedHierarchy sh :hrchy){
			if(sh.getLocalId().equals("Anatomic_Structure_Has_Location")){
				exists = true;
			}
		}
		assertTrue(exists);
	}
	
	@Test
	public void testLoadTransitivePropertiesAndInversesAsTHierarchies() throws LBException {

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodingScheme scheme = lbs.resolveCodingScheme(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
		List<SupportedHierarchy> hrchy = scheme.getMappings().getSupportedHierarchyAsReference();

		assertTrue(hrchy.stream().anyMatch(x -> x.getLocalId().equals("precedes")));
		assertTrue(hrchy.stream().anyMatch(x -> x.getLocalId().equals("preceded by")));
		assertTrue(hrchy.stream().filter(x -> x.getLocalId().equals("precedes")).anyMatch(y -> y.getRootCode().equals("@@")));
		assertTrue(hrchy.stream().filter(x -> x.getLocalId().equals("preceded by")).anyMatch(y -> y.getRootCode().equals("@")));
	}
	
	@Test
	public void testLoadValidOWL2ComplexPropsOff() throws LBInvocationException, LBParameterException{
		String[] stringList = {"C61410"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
		for(Property prop :rcr.getEntity().getAllProperties()){
			if(prop.getPropertyName().equals("Term_Browser_Value_Set_Description")){
				assertNotNull(prop.getValue());
			}
		}
		
	}
	
	@Test
	public void testAnnotationPropAsAssociation() throws LBException{
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("0.1.5");
		CodedNodeGraph newCng = lbs.getNodeGraph(
				LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
				versionOrTag, null);
		newCng = newCng.restrictToAssociations(
				Constructors.createNameAndValueList("Concept_In_Subset"),
				null);
		ResolvedConceptReferenceList list = newCng.resolveAsList(Constructors
				.createConceptReference("C37927",
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN),
				true, true, 10, 10, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list
				.iterateResolvedConceptReference();
		assertTrue(validateTarget("C117743", itr));
	}
		
	
	@Test
	public void testAnnotationPropAsProperty() throws LBInvocationException, LBParameterException{
		String[] stringList = {"C37927"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
		for(Property prop :rcr.getEntity().getAllProperties()){
			if(prop.getPropertyName().equals("Concept_In_Subset")){
				fail();
			}
		}		
	}
	
	@Test
	public void testPopulateRepresentationalForm() throws LBInvocationException, LBParameterException{
		String[] stringList = {"C117743"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
		assertTrue(rcr.getEntity().getPresentation().length > 0);
		for(Presentation prop :rcr.getEntity().getPresentation()){
			if(prop.getValue().getContent().equals("OETESTCD")){
			assertTrue(prop.getRepresentationalForm() != null);
			assertTrue(prop.getRepresentationalForm().equals("PT"));
		}
		}
	}
	
	@Test
	public void testPopulateSource() throws LBInvocationException, LBParameterException{
		String[] stringList = {"C117743"};
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList(stringList, LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN));
		ResolvedConceptReferenceList rcrlist = cns.resolveToList(null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		ResolvedConceptReference rcr = itr.next();
		assertTrue(rcr.getEntity().getPresentation().length > 0);
		for(Presentation prop :rcr.getEntity().getPresentation()){
			if(prop.getValue().getContent().equals("OETESTCD")){
			assertTrue(prop.getSource().length > 0);
			assertTrue(prop.getSource()[0].getContent().equals("CDISC"));
		}
		}
	}
		
		@Test
		public void testDefSourceAsSource() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodedNodeSet set = lbs.getCodingSchemeConcepts(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			set = set.restrictToCodes(Constructors.createConceptReferenceList("C117743"));
			ResolvedConceptReferenceList rcrlist = set.resolveToList(null, null, null, -1);
			Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			ResolvedConceptReference rcr = itr.next();
			assertTrue(rcr.getEntity().getDefinition().length > 0);
			for(Definition def :rcr.getEntity().getDefinition()){
				if(def.getValue().getContent().equals("Terminology relevant to the test codes that describe findings from ophthalmic examinations.")){
				assertTrue(def.getSource().length > 0);
				assertTrue(def.getSource()[0].getContent().equals("CDISC"));
			}
			}
		}
		
		@Test
		public void testPublishValueSet() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodedNodeSet set = lbs.getCodingSchemeConcepts(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			set = set.restrictToCodes(Constructors.createConceptReferenceList("C54453"));
			ResolvedConceptReferenceList rcrlist = set.resolveToList(null, null, null, -1);
			Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			ResolvedConceptReference rcr = itr.next();
			assertTrue(rcr.getEntity().getProperty().length > 0);
			assertTrue(rcr.getEntity().getPropertyAsReference().stream().filter(x ->
			x.getPropertyName().equals("Publish_Value_Set")).findFirst().isPresent());
			assertTrue(rcr.getEntity().getPropertyAsReference().stream().filter(x ->
			x.getPropertyName().equals("Publish_Value_Set")).findFirst().get().getValue().
					getContent().equalsIgnoreCase("yes"));
		}
	
		@Test
		public void testPopulateSupportedRepresentationalForm() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodingScheme scheme = lbs.resolveCodingScheme(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			List<SupportedRepresentationalForm> repForms = scheme.getMappings().getSupportedRepresentationalFormAsReference();
			assertTrue(repForms.stream().anyMatch(x -> x.getContent().equals("PT")));
			assertTrue(repForms.stream().anyMatch(x -> x.getContent().equals("SY")));
			assertTrue(repForms.stream().anyMatch(x -> x.getContent().equals("AB")));
		}
	
		@Test
		public void testPopulateSupportedSource() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodingScheme scheme = lbs.resolveCodingScheme(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			List<SupportedSource> supportedSourceList = scheme.getMappings().getSupportedSourceAsReference();
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("NCI")));
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("CDISC")));
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("nci evs")));
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("ctep")));
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("caDSR")));
			assertTrue(supportedSourceList.stream().anyMatch(x -> x.getContent().equals("ONC")));
		}
		
		@Test
		public void testRestrictToSupporedSource() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodedNodeSet set = lbs.getCodingSchemeConcepts(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			set = set.restrictToCodes(Constructors.createConceptReferenceList("C117743"));
			
			ResolvedConceptReferenceList rcrlist = set.resolveToList(null, null, null, -1);
			Iterator<? extends ResolvedConceptReference> itr = rcrlist.iterateResolvedConceptReference();
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			ResolvedConceptReference rcr = itr.next();
			
			Presentation[] presentations = rcr.getEntity().getPresentation();
			boolean NCI_sourceFound = false;
			boolean CDISC_sourceFound = false;
			
			for (int i = 0; i < presentations.length; i++) {
				List<Source> sources = presentations[i].getSourceAsReference();
				if (!NCI_sourceFound && sources.stream().anyMatch(x -> x.getContent().equals("NCI"))) {
					NCI_sourceFound = true;
				}
				else if (!CDISC_sourceFound && sources.stream().anyMatch(x -> x.getContent().equals("CDISC"))) {
					CDISC_sourceFound = true;
				}
			}
			assertTrue(NCI_sourceFound);
			assertTrue(CDISC_sourceFound);
		}
		
		@Test
		public void testRestrictToSupportedClassDefinedSource() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodedNodeSet set = lbs.getCodingSchemeConcepts(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			set.restrictToProperties( Constructors.createLocalNameList("definition"), null, Constructors.createLocalNameList("ctep"), null, null);
			ResolvedConceptReferencesIterator itr = set.resolve(null, null, null, null);
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			assertTrue(itr.next().getEntity().getDefinitionAsReference().get(0).getSourceAsReference().
					stream().anyMatch(x -> x.getContent().equals("ctep")));
		}
		
		@Test
		public void testRestrictToSupportedAnnotationDefinedSource() throws LBException{
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion("0.1.5");
			CodedNodeSet set = lbs.getCodingSchemeConcepts(
					LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, versionOrTag);
			set.restrictToProperties( Constructors.createLocalNameList("FULL_SYN"), null, Constructors.createLocalNameList("NCI"), null, null);
			ResolvedConceptReferencesIterator itr = set.resolve(null, null, null, null);
			assertNotNull(itr);
			assertTrue(itr.hasNext());
			while(itr.hasNext()){
				boolean found = false;
				ResolvedConceptReference ref = itr.next();
				if(ref.getConceptCode().equals("C117743")){
					Presentation pres = ref.getEntity().getPresentationAsReference().stream().filter(x -> x.getValue().
							getContent().equals("CDISC SDTM Ophthalmic Exam Test Code Terminology")).findFirst().get();
					assertNotNull(pres);
					assertTrue(pres.getSourceAsReference().
							stream().anyMatch(x -> x.getContent().equals("NCI")));
				}
			}

		}
		
		public void testCodingSchemeSourceDefinition() throws LBException{
			CodingScheme scheme = lbs.resolveCodingScheme("owl2lexevs" , Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"));
			assertTrue(scheme.getSourceAsReference().stream().anyMatch(x -> x.getContent().equals("nci evs")));
		}
}
