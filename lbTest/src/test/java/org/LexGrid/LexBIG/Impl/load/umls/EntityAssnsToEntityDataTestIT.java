
package org.LexGrid.LexBIG.Impl.load.umls;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class EntityAssnsToEntityDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityDataTestIT extends DataLoadTestBase {
	
	/** The graph focus. */
	private ResolvedConceptReference graphFocus;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("ACRMG", 
				LexBIGServiceTestCase.AIR_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
	}
	
	/**
	 * Test source count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceCount() throws Exception {
		assertEquals(1, graphFocus.getSourceOf().getAssociation().length);
	}
	
	/**
	 * Test source association name.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociationName() throws Exception {	
		assertEquals("CHD", graphFocus.getSourceOf().getAssociation()[0].getAssociationName());	
	}
	
	/**
	 * Test source associated concept count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCount() throws Exception {	
		assertEquals(3, graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());	
	}
	
	/**
	 * Test source associated concept code.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCode() throws Exception {	
		AssociatedConcept[] concepts = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();	

		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "U000010"));
		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "MFCON"));
		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "MFEXT"));
	}
	
	/**
	 * Test target count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTargetCount() throws Exception {	
		assertNull(graphFocus.getTargetOf());
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(EntityAssnsToEntityDataTestIT.class);  
	}  
}