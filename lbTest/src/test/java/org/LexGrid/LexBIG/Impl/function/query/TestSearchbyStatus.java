
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_16	TestSearchbyStatus

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestSearchbyStatus extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_16";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_FNC_16() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);

        cns = cns.restrictToStatus(null, new String[] { "Retired" });
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("73"));

    }
}