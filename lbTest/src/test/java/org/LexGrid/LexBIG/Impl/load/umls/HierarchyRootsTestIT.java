
package org.LexGrid.LexBIG.Impl.load.umls;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class HierarchyRootsTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HierarchyRootsTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReferenceList roots;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		LexBIGServiceConvenienceMethods lbscm = 
			(LexBIGServiceConvenienceMethods)lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
		
		roots = lbscm.getHierarchyRoots(LexBIGServiceTestCase.AIR_URN, null, null);
	}
	
	@Test
	public void testRootsNotNull(){
		assertNotNull(roots);
	}
	
	@Test
	public void testRootsLength(){
		assertTrue("Roots: " + roots.getResolvedConceptReferenceCount(), roots.getResolvedConceptReferenceCount() == 65);
	}
}