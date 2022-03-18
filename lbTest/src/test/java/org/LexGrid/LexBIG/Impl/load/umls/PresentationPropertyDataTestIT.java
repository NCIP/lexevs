
package org.LexGrid.LexBIG.Impl.load.umls;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class PrensentationPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PresentationPropertyDataTestIT extends DataLoadTestBase {
	
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
		cns.restrictToCodes(Constructors.createConceptReferenceList("ACRMG"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	@Test
	public void testPresentationNotNull() throws Exception {	
		assertNotNull(testEntity.getPresentation());
	}
	
	/**
	 * Test presentation count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresentationCount() throws Exception {	
		assertTrue(testEntity.getPresentation().length == 1);
	}
	
	@Test
	public void testPreferredPresentation() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();

		assertTrue(preses.length == 1);
		
		assertTrue(preses[0].isIsPreferred());
	}
	
	@Test
	public void testPresentationText() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		assertTrue(preses.length == 1);
		
		assertTrue(preses[0].getValue().getContent().equals("Acromegaly"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(PresentationPropertyDataTestIT.class);  
	}  
}