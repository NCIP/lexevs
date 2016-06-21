package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.util.Arrays;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.junit.Before;
import org.junit.Test;

public class OWLUnitTests extends TestCase {

	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	
	@Before
	public void setUp() throws Exception {
		lbs = ServiceHolder.instance().getLexBIGService();
		cns = lbs.getNodeSet(LexBIGServiceTestCase.OWL_COMPLEX_PROP_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL_COMPLEX_PROP_VERSION), null);;
	}
	
	@Test
	public void testPropertyForAnnotationPropertySource()throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		String[] qualiferNames = {"Relationship_to_Target", "Target_Term_Type", "Target_Code", "Target_Terminology" };
		String[] qualiferValues = {"Has Synonym", "LLT", "10012444", "MedDRA" };
		
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("C10000"));
		
		ResolvedConceptReferencesIterator itr  = cns.resolve(null, null, null);
		assertNotNull(itr);
		assertTrue(itr.hasNext());	
		ResolvedConceptReference ref = itr.next();
		
		PropertyQualifier[] qualifiers = ref.getEntity().getProperty()[0].getPropertyQualifier();
		
		assertTrue(qualifiers.length == 4);
		
		for (int i = 0; i < qualifiers.length; i++) {
			String qualifier = qualifiers[i].getPropertyQualifierName();
			String value = qualifiers[i].getValue().getContent();
			
			assertTrue(Arrays.asList(qualiferNames).contains(qualifier));
			assertTrue(Arrays.asList(qualiferValues).contains(value));
		}
			
	}
	
}

