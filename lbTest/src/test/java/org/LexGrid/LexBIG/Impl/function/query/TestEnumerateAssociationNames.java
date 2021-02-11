
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_31	TestEnumerateRelationships

import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestEnumerateAssociationNames extends LexBIGServiceTestCase {
    final static String testID = "TEST_FNQRY_ASSOCIATIONNAME_FOR_CODINGSCHEME";

    @Override
    protected String getTestID() {
        return testID;
    }
    
    @Test
    public void testTEST_FNQRY_ASSOCIATIONNAME_FOR_CODINGSCHEME() throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        String[] association_names;
        association_names = cm.getAssociationForwardNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "hasSubtype", "uses" }));
        association_names = cm.getAssociationReverseNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "isA", "usedBy" }));
        association_names = cm.getAssociationForwardAndReverseNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "hasSubtype", "uses", "isA", "usedBy" }));
    }

    private boolean contains(String[] association_names, String[] ref_list) {
        List association_names_list = Arrays.asList(association_names);
        boolean found = true;
        for (int i = 0; i < ref_list.length; i++) {
            if (!association_names_list.contains(ref_list[i])) {
                found = false;
                break;
            }
        }
        return found;
    }
}