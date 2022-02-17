
package org.LexGrid.LexBIG.Impl.load.meta;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
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
		assertTrue(graphFocus.getSourceOf().getAssociation()[0].getAssociationName().equals("RN"));	
	}
	
	/**
	 * Test source associated concept count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAssociatedConceptCountFromArray() throws Exception {
		AssociatedConcept[] acs = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
		assertEquals(1, acs.length);	
	}
	
	@Test
	public void testSourceAssociatedConceptCountFromList() throws Exception {
		AssociatedConceptList acs = graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts();
		assertEquals(1, acs.getAssociatedConceptCount());
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

	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(EntityAssnsToEntityDataTestIT.class);  
	}  
}