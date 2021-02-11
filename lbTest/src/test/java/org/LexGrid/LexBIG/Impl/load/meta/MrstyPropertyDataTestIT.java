
package org.LexGrid.LexBIG.Impl.load.meta;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class MrstyPropertyDataTestIT
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyPropertyDataTestIT extends DataLoadTestBase {
	
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
	public void testSemanticTypePropertyNotNull() throws Exception {
		List<Property> semTypeProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY);
		assertNotNull(semTypeProps);
	}

	@Test
	public void testSemanticTypePropertyCount() throws Exception {
		List<Property> semTypeProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY);
		assertTrue(semTypeProps.size() == 3);
	}
	
	@Test
	public void testSemanticTypePropertyValue1() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Amino Acid, Peptide, or Protein"));
	}
	
	@Test
	public void testSemanticTypePropertyValue2() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Pharmacologic Substance"));
	}
	
	@Test
	public void testSemanticTypePropertyValue3() throws Exception {
			assertTrue(DataTestUtils.isPropertyWithValuePresent(
				testEntity, 
				RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY, 
				"Indicator, Reagent, or Diagnostic Aid"));
	}
}