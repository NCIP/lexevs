
package org.LexGrid.LexBIG.Impl.load.umls;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class SameCodeDifferentCuiTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SameCodeDifferentCuiTestIT extends DataLoadTestBase {
	
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
		cns.restrictToCodes(Constructors.createConceptReferenceList("ALOPE"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	/**
	 * Test property not null.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPropertyNotNull() throws Exception {	
		assertNotNull(testEntity.getProperty());
	}
	
	
	/**
	 * Test presentation poperty count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresentationPopertyCount() throws Exception {	
		Property[] preses = testEntity.getPresentation();
		assertTrue(preses.length == 2);
	}
	
	/**
	 * Test pres poperty name cui1.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresPopertyNameCui1() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(testEntity.getPresentation(), "Alopecia - Different CUI");
		assertNotNull(prop);
		assertTrue(prop instanceof Presentation);
		Presentation pres = (Presentation)prop;
		assertTrue(pres.getIsPreferred());
	}
	
	/**
	 * Test pres poperty name cui2.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testPresPopertyNameCui2() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(testEntity.getPresentation(), "Alopecia - Different CUI");
		assertNotNull(prop);
		assertTrue(prop instanceof Presentation);
		Presentation pres = (Presentation)prop;
		assertTrue(pres.getIsPreferred());
	}
	
	@Test
	public void testEntityDescription() throws Exception {	
		String entityDescription = testEntity.getEntityDescription().getContent();
		assertTrue(entityDescription.equals("Alopecia - Different CUI"));
	}
	
	/**
	 * Test cui property count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCuiPropertyCount() throws Exception {	
		List<Property> props = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.UMLS_CUI_PROPERTY);
		assertTrue(props.size() == 2);
	}
	
	/**
	 * Test cui property cui1.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCuiPropertyCui1() throws Exception {	
		assertTrue(DataTestUtils.isPropertyWithValuePresent(testEntity, 
				RrfLoaderConstants.UMLS_CUI_PROPERTY, 
				"C0002170"));
	}
	
	/**
	 * Test cui property cui2.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCuiPropertyCui2() throws Exception {	
		assertTrue(DataTestUtils.isPropertyWithValuePresent(testEntity, 
				RrfLoaderConstants.UMLS_CUI_PROPERTY, 
				"C0002171"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(SameCodeDifferentCuiTestIT.class);  
	}  
}