
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

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
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(DefinitionPropertyDataTestIT.class);  
	}  
}