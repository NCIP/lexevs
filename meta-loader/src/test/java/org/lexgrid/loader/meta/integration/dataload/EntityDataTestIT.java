
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;

/**
 * The Class EntityDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityDataTestIT extends DataLoadTestBase {

	/**
	 * Test load.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testEntityCode() throws Exception {
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
		ResolvedConceptReference rcr = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
		assertTrue(rcr.getCode().equals("C0000005"));
		assertTrue(rcr.getEntity().getEntityCode().equals("C0000005"));
		/*
	
		Entity entity = rcr.getEntity();
		
		List<Property> luiProps = super.getPropertiesFromEntity(entity, LoaderConstants.LUI_PROPERTY);
		
		Assert.assertTrue(luiProps.size() == 1);
		
		Property luiProp = luiProps.get(0);
		
		Assert.assertTrue(luiProp.getPropertyName().equals(LoaderConstants.LUI_PROPERTY));
		Assert.assertTrue(luiProp.getPropertyType().equals(SQLTableConstants.TBLCOLVAL_PROPERTY));
		*/
	}
}