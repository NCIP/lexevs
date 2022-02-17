
package org.LexGrid.LexBIG.Impl.load.umls;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class HierarchyRootsTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReverseAssocDirectionalityTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReference graphFocus;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		graphFocus = cng.resolveAsList(Constructors.createConceptReference("ALOPE", 
				LexBIGServiceTestCase.AIR_URN), true, true, 1, 1, null, null, null, -1).getResolvedConceptReference(0);
	}
	
	@Test
	public void testFocusNotNull(){
		assertNotNull(graphFocus);
	}
	
	@Test
	public void testIsSourceOfNotNull(){
		assertNotNull(graphFocus.getSourceOf());
	}
	
	@Test
	public void testIsSourceOfLength(){
		assertEquals(1, graphFocus.getSourceOf().getAssociation().length);
	}
	
	@Test
	public void testIsSourceOfAssociationName(){
		assertEquals("CHD", graphFocus.getSourceOf().getAssociation()[0].getAssociationName());
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsNotNull(){
		assertNotNull(graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts());
	}
	
	@Test
	public void testIsSourceOfAssociatedConceptsLength(){
		assertEquals(1, graphFocus.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept().length);
	}
	
	@Test
	public void testIsSourceOfAssociatedConcept(){
		assertEquals("U000035", graphFocus.getSourceOf()
				.getAssociation()[0]
				                  .getAssociatedConcepts()
				                  .getAssociatedConcept()[0].getCode());
	}
}