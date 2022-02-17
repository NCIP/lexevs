
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_32	TestMapAttributestoTypes

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedProperty;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestMapAttributestoTypes extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_32";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_FNC_32() throws LBException {

        CodingScheme cs = ServiceHolder.instance().getLexBIGService().resolveCodingScheme(AUTO_SCHEME, null);

        SupportedProperty[] sp = cs.getMappings().getSupportedProperty();

        assertTrue(contains(sp, "definition"));
        assertTrue(contains(sp, "textualPresentation"));

    }

    private boolean contains(SupportedProperty[] sp, String item) {
        boolean result = false;

        for (int i = 0; i < sp.length; i++) {
            if (sp[i].getContent().equals(item)) {
                result = true;
                break;
            }

        }
        return result;
    }

}