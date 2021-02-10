
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * This testcase checks that the transitive api works as desired.
 * 
 * @author Pradip Kanjamala
 * 
 */
@Category(IncludeForDistributedTests.class)
public class TestTransitiveClosure extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_150";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    @Test
    public void testAreCodesRelatedInferredOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
       
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        CodedNodeGraph cng = lbs.getNodeGraph(CELL_URN, csvt, null);
        boolean related= cng.areCodesRelated(ConvenienceMethods.createNameAndValue("is_a", CELL_URN),
                ConvenienceMethods.createConceptReference("CL:0000004", CELL_URN), ConvenienceMethods
                        .createConceptReference("CL:0000000", CELL_URN), false);
        assertTrue(related);
      
    }
    
    @Test
    public void testAreCodesRelatedAssertedOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
       
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        CodedNodeGraph cng = lbs.getNodeGraph(CELL_URN, csvt, null);
        boolean related= cng.areCodesRelated(ConvenienceMethods.createNameAndValue("is_a", CELL_URN),
                ConvenienceMethods.createConceptReference("CL:0000003", CELL_URN), ConvenienceMethods
                        .createConceptReference("CL:0000000", CELL_URN), false);
        assertTrue(related);
       

    }
    


    
    
}