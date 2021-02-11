
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class SortGraphTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
@Category(IncludeForDistributedTests.class)
public class CrossOntologyResolveTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testResolveMappingRelationshWithFocus() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);

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
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertEquals(1, rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertEquals(1,assocCons.length);
        assertTrue(assocCons[0].getCode().equals("E0001"));
    }
    
	@Test
    public void testEntityDescriptions() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);

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
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue(rcr[0].getEntityDescription() != null);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertEquals(1, rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertEquals(1,assocCons.length);
        assertTrue(assocCons[0].getCode().equals("E0001"));
        
        assertTrue(assocCons[0].getEntityDescription() != null);
    }
    
	@Test
    public void testResolveMappingRelationshNoFocusLength() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(5,rcr.length, 
                rcr.length);
    }
    
	@Test
    public void testResolveMappingRelationshNoFocusRelationships() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(5,rcr.length, 
                rcr.length);
        
        String[][] codes = new String[][] 
                {{"C0001","Jaguar","A0001","005","Ford"},
        		{"E0001", "E0001","R0001","P0001","E0001"}};
        
        for(int i=0;i<codes.length;i++) {
	        ResolvedConceptReference ref = DataTestUtils.getConceptReference(rcr, codes[0][i]);
	        assertEquals(1,ref.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept().length);
	        assertEquals(codes[1][i],ref.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getCode());
        }
    }
    
	@Test
    public void testResolveMappingRelationshNoFocusEntityDescriptions() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(5,rcr.length, 
        		rcr.length);

        String[][] codes = new String[][] 
                {{"C0001","Jaguar","A0001","005","Ford"},
        		{"T0001", "E0001","R0001","P0001","T0001"}};

        for(int i=0;i<codes.length;i++) {
        	ResolvedConceptReference ref = DataTestUtils.getConceptReference(rcr, codes[0][i]);
        	assertTrue(ref.getEntityDescription() != null);
        	assertTrue(codes[1][i],ref.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getEntityDescription() != null);
        }
    }
}