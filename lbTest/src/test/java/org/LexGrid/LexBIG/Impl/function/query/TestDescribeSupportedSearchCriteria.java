
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_27	TestDescribeSupportedSearchCriteria

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestDescribeSupportedSearchCriteria extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_27";

    @Override
    protected String getTestID() {
        return testID;
    }

//    @SuppressWarnings("null")
    @Test
    public void codingSchemeSummary() throws LBException {
        CodingSchemeRenderingList schemeList = ServiceHolder.instance().getLexBIGService().getSupportedCodingSchemes();

        // <dan> not sure why this is being done... doesn't have anythign to do
        // with the intent
        // of the test, but what the heck. I can't figure out from the design
        // document what on earth this
        // test is supposed to do.
        for (CodingSchemeRendering csr : schemeList.getCodingSchemeRendering()) {
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            assertTrue(css != null);
        }
    }

    public void testT1_FNC_27() throws LBException {
        codingSchemeSummary();
    }

}