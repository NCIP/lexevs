
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class ResolveToListTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class ResolveToListTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testResolveToListAssociatedConceptCount() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 3);
    }
}