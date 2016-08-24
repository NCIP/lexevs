package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class OWL2SpecialCaseSnippetTestIT extends DataLoadTestBaseSpecialCases {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
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
		assertTrue(validatePropertyQualifierFromProperty(prop, "IAO_0010000:138-001"));
	}

}
