
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class SearchByRelationshipTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class SearchByRelationshipTest extends BaseCodedNodeGraphTest {

	//get all sub codes of codes that contain 'car'
	//looking for 'C0011(5564)', 'Ford', 'GM' 'A'
	@Test
    public void testSearchByRelationshipChildrenOf() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null);
 
        cng = cng.restrictToSourceCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    false, 
                    true, 
                    0, 
                    0, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(4,rcr.length);
        
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "C0011(5564)"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "Ford"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "GM"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "A"));
  
        for(ResolvedConceptReference ref : rcr) {
        	assertNull(ref.getSourceOf());
        	assertNull(ref.getTargetOf());
        } 
    }
    

    //get all parent codes of codes that contain 'car'
    //looking for 'A0001', 'C0001'
	@Test
    public void testSearchByRelationshipParentsOf() throws Exception {
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null);

        cng = cng.restrictToTargetCodes(cns);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    0, 
                    0, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(2,rcr.length);
        
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "A0001"));
        assertTrue(DataTestUtils.isConceptReferencePresent(Arrays.asList(rcr), "C0001"));
  
        for(ResolvedConceptReference ref : rcr) {
        	assertNull(ref.getSourceOf());
        	assertNull(ref.getTargetOf());
        } 
    }
}