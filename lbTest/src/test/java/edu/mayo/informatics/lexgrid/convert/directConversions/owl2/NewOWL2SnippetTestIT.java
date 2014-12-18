package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class NewOWL2SnippetTestIT extends DataLoadTestBaseSnippet2 {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
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
	
	public void testForDuplicateAssociations() throws LBInvocationException, LBParameterException{
		
		//TODO insure these duplicate associations are correct.  If not uncomment this and correct it as necessary.  
//		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("AssociationV1"), null);
//		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("HappyPatientDrivingAround", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
//				true, true, 1, 1, null, null, null, null, -1);
//		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
//		if(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			Association[] assocs = ref.getSourceOf().getAssociation();
//			for(Association assoc : assocs){
//				if(assoc.getAssociatedConcepts().getAssociatedConceptCount() > 1){
//				AssociatedConcept[] concepts = assoc.getAssociatedConcepts().getAssociatedConcept();
//				assertNotSame(concepts[0].getCode(),concepts[1].getCode());
//				}
//			}
//		}
	}
	
	@Test
	public void testAssocLoadingDisjointWith_LEXEVS_685() throws LBInvocationException, LBParameterException {
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("disjointWith"), null);
		ResolvedConceptReferenceList list = cng.resolveAsList(Constructors.createConceptReference("Person", LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN), 
				true, true, 1, 1, null, null, null, null, -1);
		Iterator<? extends ResolvedConceptReference> itr = list.iterateResolvedConceptReference();
		assertTrue(validateTarget("DiseasesDisordersFindings", itr));
		}
	
	private boolean validateTarget(String target,
			Iterator<? extends ResolvedConceptReference> itr) {
		boolean validate = false;
		while (itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			AssociationList assoc1 = ref.getSourceOf();
			Association[] assocs = assoc1.getAssociation();
			for (Association as : assocs) {
				AssociatedConceptList acl = as.getAssociatedConcepts();
				AssociatedConcept[] acs = acl.getAssociatedConcept();
				for (AssociatedConcept ac : acs) {
					if (ac.getCode().equals(target)) {
						validate = true;
					}
				}
			}
		}
		return validate;
	}

}
