
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_26	TestDescribeSearchTechniques

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestDescribeSearchTechniques extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_26";

    @Override
    protected String getTestID() {
        return testID;
    }
    
    @Test
    public void testT1_FNC_26() {

        ModuleDescription[] ed = ServiceHolder.instance().getLexBIGService().getMatchAlgorithms()
                .getModuleDescription();

        assertTrue(ed.length > 0);
        assertTrue(ed[0].getName().length() > 0);
        assertTrue(ed[0].getDescription().length() > 0);

    }
}