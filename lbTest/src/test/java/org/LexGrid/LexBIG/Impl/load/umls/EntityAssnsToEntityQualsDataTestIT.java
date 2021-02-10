
package org.LexGrid.LexBIG.Impl.load.umls;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class EntityAssnsToEntityQualsDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityQualsDataTestIT extends DataLoadTestBase {
	
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
		associatedConcept = cng.resolveAsList(Constructors.createConceptReference("ACRMG", 
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
	public void testRelaQual() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "U000010").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.RELA_QUALIFIER, "mapped_from", quals));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(EntityAssnsToEntityQualsDataTestIT.class);  
	}  
}