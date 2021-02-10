
package org.LexGrid.LexBIG.Impl.load.meta;

import java.util.ArrayList;
import java.util.List;

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
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
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
		assertTrue(testEntity.getPresentation().length == 2);
	}
	
	@Test
	public void testPreferredPresentation() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		List<Presentation> preferredPres = new ArrayList<Presentation>();
		
		for(Presentation pres : preses) {
			if(pres.getIsPreferred() != null && pres.getIsPreferred()) {
				preferredPres.add(pres);
			}
		}
		
		assertTrue(preferredPres.size() == 1);
		
		assertTrue(preferredPres.get(0).getValue().getContent().equals("(131)I-MAA"));
	}
	
	@Test
	public void testRepForm() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		List<Presentation> preferredPres = new ArrayList<Presentation>();
		
		for(Presentation pres : preses) {
			if(pres.getIsPreferred() != null && pres.getIsPreferred()) {
				preferredPres.add(pres);
			}
		}
		
		assertTrue(preferredPres.size() == 1);
		
		assertEquals("EN",preferredPres.get(0).getRepresentationalForm());
	}
	
	@Test
	public void testNonPreferredPresentation() throws Exception {	
		Presentation[] preses = testEntity.getPresentation();
		
		List<Presentation> preferredPres = new ArrayList<Presentation>();
		
		for(Presentation pres : preses) {
			if(pres.getIsPreferred() == null || !pres.getIsPreferred()) {
				preferredPres.add(pres);
			}
		}
		
		assertTrue(preferredPres.size() == 1);
		
		assertTrue(preferredPres.get(0).getValue().getContent().equals("(131)I-Macroaggregated Albumin"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(PresentationPropertyDataTestIT.class);  
	}  
}