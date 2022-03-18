
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;

/**
 * The Class MrrankQualifierDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrankQualifierDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Presentation[] presentation;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
		ResolvedConceptReference rcr = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		presentation = rcr.getEntity().getPresentation();
	}
	
	@Test
	public void testPresentationNotNull() throws Exception {	
		assertNotNull(presentation);
	}
	
	@Test
	public void testFirstMrrankQualifierExists() throws Exception {	
		assertTrue(
				DataTestUtils.getPropertyQualifiersFromProperty(
						presentation[0], RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME).size() > 0);
	}
	
	@Test
	public void testSecondMrrankQualifierExists() throws Exception {	
		assertTrue(
				DataTestUtils.getPropertyQualifiersFromProperty(
						presentation[1], RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME).size() > 0);
	}
	
	@Test
	public void testFirstMrrankQualifierValue() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(presentation, "(131)I-MAA");
		
		assertTrue(
						DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME, "310", prop)
						
		);
	}
	
	@Test
	public void testSecondMrrankQualifierValue() throws Exception {	
	Property prop = DataTestUtils.getPropertyWithValue(presentation, "(131)I-Macroaggregated Albumin");
		
		assertTrue(
						DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME, "100", prop)
						
		);
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MrrankQualifierDataTestIT.class);  
	}  
}