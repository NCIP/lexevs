
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

public class DefinitionPropertyDataTestIT extends DataLoadTestBase {

/** The test entity. */
private Entity testEntity;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("16022:AA"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	@Test
	public void testDefinitionNotNull() throws Exception {	
		assertNotNull(testEntity.getDefinition());
	}
	
	/**
	 * Test presentation count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresentationCount() throws Exception {	
		assertTrue(testEntity.getDefinition().length == 1);
	}
	
	@Test
	public void testPreferredPresentation() throws Exception {	
		Definition[] defs = testEntity.getDefinition();

		assertTrue(defs.length == 1);
		
		assertTrue(defs[0].isIsPreferred());
	}
	
	@Test
	public void testPresentationText() throws Exception {	
		Definition[] defs = testEntity.getDefinition();
		
		assertTrue(defs.length == 1);
		
		assertTrue(defs[0].getValue().getContent().equals("Receiving application successfully processed message."));
	}
	

}