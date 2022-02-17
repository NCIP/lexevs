
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_22	TestSpecifyReturnOrder

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestSpecifyReturnOrder extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_22";

    private LexBIGService lbs;
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    @Before
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();
    }

    @Test
    public void testT1_FNC_22() throws LBException {

        CodedNodeSet cns = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);

        ResolvedConceptReference[] rcr = cns.resolveToList(
                Constructors.createSortOptionList(new String[] { "code" }, new Boolean[] { null }), null, null, 3)
                .getResolvedConceptReference();
        assertTrue(rcr.length == 3);
        assertTrue(rcr[0].getConceptCode().equals("005"));
        assertTrue(rcr[1].getConceptCode().equals("73"));
        assertTrue(rcr[2].getConceptCode().equals("A"));

        rcr = cns.resolveToList(
                Constructors.createSortOptionList(new String[] { "entityDescription" }, new Boolean[] { null }), null,
                null, 0).getResolvedConceptReference();
        assertTrue(rcr[0].getConceptCode().equals("NoRelationsConcept"));
        assertTrue(rcr[1].getConceptCode().equals("A0001"));
        assertTrue(rcr[2].getConceptCode().equals("C0001"));
        assertTrue(rcr[3].getConceptCode().equals("C0011(5564)"));

        // reverse sort 1.
        rcr = cns.resolveToList(
                Constructors.createSortOptionList(new String[] { "code" }, new Boolean[] { new Boolean(false) }), null,
                null, 4).getResolvedConceptReference();
        assertTrue(rcr.length == 4);
        assertTrue(rcr[0].getConceptCode().equals("T0001"));
        assertTrue(rcr[1].getConceptCode().equals("SpecialCharactersConcept"));
        assertTrue(rcr[2].getConceptCode().equals("NoRelationsConcept"));
        assertTrue(rcr[3].getConceptCode().equals("Jaguar"));
    }
}