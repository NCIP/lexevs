
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

//LexBIG Test ID: TEST_FNQRY_CSRNDR_FOR_SUPPASSOC TestCodingSchemesWithSupportedAssociation

@Category(IncludeForDistributedTests.class)
public class TestCodingSchemesWithSupportedAssociation extends LexBIGServiceTestCase {
    final static String testID = "TEST_FNQRY_CSRNDR_FOR_SUPPASSOC";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void test_FNQRY_CSRNDR_FOR_SUPPASSOC() throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        CodingSchemeRenderingList csrl = cm.getCodingSchemesWithSupportedAssociation("uses");
        CodingSchemeRendering csr[] = csrl.getCodingSchemeRendering();
        boolean found = false;

        for (int i = 0; i < csr.length; i++) {
            if (csr[i].getCodingSchemeSummary().getLocalName().equals(AUTO_SCHEME))
                found = true;
        }
        assertTrue(found);
    }
}