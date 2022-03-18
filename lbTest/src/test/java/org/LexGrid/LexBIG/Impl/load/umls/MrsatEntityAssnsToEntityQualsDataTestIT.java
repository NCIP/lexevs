
package org.LexGrid.LexBIG.Impl.load.umls;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class EntityAssnsToEntityQualsDataTestIT.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public class MrsatEntityAssnsToEntityQualsDataTestIT extends DataLoadTestBase {
	
	
	/** The graph focus. */
	private AssociatedConcept[] associatedConcept;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		associatedConcept = cng.resolveAsList(Constructors.createConceptReference("DIABT", 
				LexBIGServiceTestCase.AIR_URN), true, true, 1, 1, null, null, null, -1)
				.getResolvedConceptReference(0)
				.getSourceOf()
				.getAssociation()[0]
				.getAssociatedConcepts()
				.getAssociatedConcept();
	}
	
	@Test
	public void testQualsNotNull() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "U000010").getAssociationQualifiers();
		assertNotNull(quals);
	}
	
	@Test
	public void testRelaQualMODID() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "U000010").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent("MODIFIER_ID", "900000000000451002", quals));
	}
	
	@Test
	public void testRelaQualCHARTYPEID() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "U000010").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent("CHARACTERISTIC_TYPE_ID", "900000000000011006", quals));
	}
	
	@Test
	public void testSupportedQualifierLoad() throws Exception {
		CodingScheme scheme = lbs.resolveCodingScheme(LexBIGServiceTestCase.AIR_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AIR_VERSION));
		Mappings mappings = scheme.getMappings();
		assertTrue(mappings.getSupportedAssociationQualifierAsReference().stream().
		anyMatch(x -> x.getContent().equals("MODIFIER_ID")));
		assertTrue(mappings.getSupportedAssociationQualifierAsReference().stream().
				anyMatch(x -> x.getContent().equals("CHARACTERISTIC_TYPE_ID")));
		
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(EntityAssnsToEntityQualsDataTestIT.class);  
	}   
}