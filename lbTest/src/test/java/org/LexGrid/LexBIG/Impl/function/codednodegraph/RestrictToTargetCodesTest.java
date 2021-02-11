
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToTargetCodesTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToTargetCodesTest extends BaseCodedNodeGraphTest {

    /**
     * Test restrict to target codes.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("Ford", AUTO_SCHEME));

        cng = cng.restrictToTargetCodes(cns);
        
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

        ResolvedConceptReference ref = rcr[0];
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    /**
     * Test restrict to target codes two codes.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodesTwoCodes() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"Ford", "T0001"}));

        cng = cng.restrictToTargetCodes(cns);
        
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

        resolvedConceptListContains(rcr, "005");
        resolvedConceptListContains(rcr, "A0001");
    }
    
    /**
     * Test restrict to target codes no match.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testRestrictToTargetCodesNoMatch() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[]{"NO_MATCH"}));

        cng = cng.restrictToTargetCodes(cns);
        
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