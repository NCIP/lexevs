
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
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
	public void buildTestEntity() throws Exception {
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("C0000005", 
				"NCI MetaThesaurus"), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
	}
	
	/**
	 * Test source count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceCount() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation().length == 1);
	}
	
	/**
	 * Test source association name.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociationName() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociationName().equals("RB"));	
	}
	
	/**
	 * Test source associated concept count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCount() throws Exception {	
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 1);	
	}
	
	/**
	 * Test source associated concept code.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCode() throws Exception {	
		AssociatedConcept concept = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept(0);	
		assertTrue(concept.getCode().equals("C0036775"));
	}
	
	/**
	 * Test target count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTargetCount() throws Exception {	
		assertTrue(graphFocus.getTargetOf() == null);
	}
}