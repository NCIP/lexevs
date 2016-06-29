package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

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
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("10651:ADL", 
				LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
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
		assertEquals("ComponentOf", graphFocus.getSourceOf().getAssociation()[0].getAssociationName());	
	}
	
	/**
	 * Test source associated concept count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCount() throws Exception {	
		assertEquals(1, graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());	
	}
	
	/**
	 * Test source associated concept code.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCode() throws Exception {	
		AssociatedConcept[] concepts = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();	

		assertTrue(DataTestUtils.isAssociatedConceptPresent(concepts, "22619:AL"));
	}
	
	/**
	 * Test target count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTargetCount() throws Exception {	
		// The concept 10651:ADL is a target for the codeSystem entity and other code concepts
		assertNotNull(graphFocus.getTargetOf());
	}
		
	@Test
	public void testCorrectConceptIsPresent() throws Exception {
		
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("10458:BRTH", 
				LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
		
		assertTrue(graphFocus != null);
	}

}
