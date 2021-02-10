
package org.LexGrid.LexBIG.Impl.load.owl;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.junit.Before;
import org.junit.Test;

public class NPOQualifersJIRA_514TestIT extends DataLoadTestBase {

	private Property property;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("NPO_160"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1)
				.getResolvedConceptReference()[0];

		Property[] props = rcr1.getEntity().getProperty();

		assertTrue(props.length > 0);

		property = props[0];

	}
	
	@Test
	public void testDefinitionNotNull() throws Exception {	
		assertNotNull(property);
	}
	
	@Test
	public void testPropertyQualifierDefinition() throws Exception {	
		assertFalse(DataTestUtils.isQualifierNameAndValuePresentInProperty("comment", "It begins with the prefix \"NPO_\" " +
				"followed by a number (e.g., NPO_250). The smallest code value is NPO_100 of class Cylinder.", property));
	}
	
	@Test
	public void testPropertyQualifierLabel() throws Exception {	
		assertFalse(DataTestUtils.isQualifierNameAndValuePresentInProperty("label", "code", property));
	}
	
}