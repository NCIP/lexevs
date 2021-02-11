
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToSourceCodesTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToSourceCodesTest extends BaseCodedNodeGraphTest {

    /**
     * Test restrict to source codes.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToSourceCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList("005", AUTO_SCHEME));
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

       assertTrue("Length: " + rcr.length, rcr.length == 1);

       resolvedConceptListContains(rcr, "005");  
    }

    @Test
    public void testRestrictToSourceCodesAndOnlySourceCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList("005", AUTO_SCHEME));
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

       assertTrue("Length: " + rcr.length, rcr.length == 1);

       resolvedConceptListContains(rcr, "005");  
       
       assertNotNull(rcr[0].getSourceOf());
       assertNull(rcr[0].getTargetOf());
       
       assertEquals(1,rcr[0].getSourceOf().getAssociationCount());
       
       for(AssociatedConcept ac : rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept()) {
    	   assertNull(ac.getSourceOf());
       }
       
    }
    
    /**
     * Test restrict to source codes check associated concepts.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRestrictToSourceCodesCheckAssociatedConcepts() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList("005", AUTO_SCHEME));
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

       assertTrue("Length: " + rcr.length, rcr.length == 1);

       assertTrue(resolvedConceptListContains(rcr, "005"));  
       
       assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
               rcr[0].getSourceOf().getAssociation().length == 1);
       
       assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
               rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 3);
    }
    
    /**
     * Test restrict to source codes two codes.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRestrictToSourceCodesTwoCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"Ford", "A0001"}));
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertTrue("Length: " + rcr.length, rcr.length == 2);

        resolvedConceptListContains(rcr, "Ford");
        resolvedConceptListContains(rcr, "A0001");
    }
    
    /**
     * Test restrict to source codes no match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRestrictToSourceCodesNoMatch() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"NO_MATCH"}));
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertTrue("Length: " + rcr.length, rcr.length == 0);
    }
}