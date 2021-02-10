
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.LexGrid.util.PrintUtility;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToAnonymousTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToAnonymousTest extends BaseCodedNodeGraphTest {

	@Test
    public void testRestrictToNonAnonymousForMappingScheme() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(false);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("C0001", AUTO_SCHEME, AUTO_SCHEME), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        assertTrue(rcr[0].getSourceOf() != null);
    }
    
	@Test
    public void testRestrictToAnonymous() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(true);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME, AUTO_SCHEME), 
                    false, 
                    true, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        PrintUtility.print(rcr[0]);
        
        assertTrue(rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept()[0].getCode().equals("@"));
    }
    
	@Test
    public void testRestrictToNonAnonymous() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToAnonymous(false);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME, AUTO_SCHEME), 
                    false, 
                    true, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        PrintUtility.print(rcr[0]);
        
        assertNull(rcr[0].getTargetOf());
    }
   
}