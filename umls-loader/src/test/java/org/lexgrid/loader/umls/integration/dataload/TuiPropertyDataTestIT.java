
package org.lexgrid.loader.umls.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.test.util.DataTestUtils;

/**
 * The Class PrensentationPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TuiPropertyDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Entity testEntity;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void buildTestEntity() throws Exception {
		cns.restrictToCodes(Constructors.createConceptReferenceList("ACRMG"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	@Test
	public void testPropertyNotNull() throws Exception {	
		assertNotNull(testEntity.getProperty());
	}
	
	
	@Test
	public void testTuiPopertyCount() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.size() == 1);
	}
	
	@Test
	public void testTuiPopertyName() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.get(0).getPropertyName().equals(RrfLoaderConstants.TUI_PROPERTY));
	}
	
	@Test
	public void testTuiPopertyValue() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.get(0).getValue().getContent().equals("T047"));
	}
}