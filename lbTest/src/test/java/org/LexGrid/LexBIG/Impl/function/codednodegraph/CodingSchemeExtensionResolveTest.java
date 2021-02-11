
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class CodingSchemeExtensionResolveTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
@Category(IncludeForDistributedTests.class)
public class CodingSchemeExtensionResolveTest extends BaseCodedNodeGraphTest {

   @Test
    public void testResolveWithinExtension() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(AUTO_EXTENSION_URN, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("Cadillac", null, null), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertEquals(1, rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertEquals(1,assocCons.length);
        assertTrue(assocCons[0].getCode().equals("DeVille"));
    }
    
   @Test
    public void testResolveFromExtensionBackToParent() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(AUTO_EXTENSION_URN, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("Cadillac", null, null), 
                    false, 
                    true, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getTargetOf().getAssociation(), 
                rcr[0].getTargetOf().getAssociation().length == 1);
        
        assertEquals(1, rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
          
        AssociatedConcept[] assocCons = rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertEquals(1,assocCons.length);
        assertTrue(assocCons[0].getCode().equals("GM"));
    }
    
   @Test
    public void testResolveFromParentToExtension() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(AUTO_EXTENSION_URN, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", null, null), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertEquals(1, rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertEquals(3,assocCons.length);
        
        assertTrue(isAssociatedConceptPresent(assocCons, "Chevy"));
        assertTrue(isAssociatedConceptPresent(assocCons, "73"));
        assertTrue(isAssociatedConceptPresent(assocCons, "Cadillac"));
       
    }
    
	private static boolean isAssociatedConceptPresent(AssociatedConcept[] assocConcepts, String code){
		for(AssociatedConcept concept : assocConcepts){
			if(concept.getCode().equals(code)){
				return true;
			}
		}
		
		return false;
	}
    
}