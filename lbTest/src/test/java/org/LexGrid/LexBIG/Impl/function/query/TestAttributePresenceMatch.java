
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_14	TestAttributePresenceMatch

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestAttributePresenceMatch extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_14";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public boolean matchAttribute(String attribute) throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        LocalNameList lnl = new LocalNameList();
        lnl.addEntry(attribute);

        CodedNodeSet matches = null;
        try {
            matches = cns.restrictToProperties(lnl, null);
        } catch (LBParameterException e) {
            return (false);
        }

        int count = matches.resolveToList(null, null, null, 0).getResolvedConceptReferenceCount();
        return (count > 0);
    }
    
    @Test
    public void testT1_FNC_14a() throws LBException {
        assertTrue(matchAttribute("definition"));
    }

    @Test
    public void testT1_FNC_14b() throws LBException {
        assertFalse(matchAttribute("defunition"));
    }

}