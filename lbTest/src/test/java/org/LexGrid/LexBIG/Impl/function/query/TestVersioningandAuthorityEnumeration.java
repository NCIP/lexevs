
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_33 TestVersioningandAuthorityEnumeration

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestVersioningandAuthorityEnumeration extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_33";

    @Override
    protected String getTestID() {
        return testID;
    }
    @Test
    public void testT1_FNC_33() throws LBException {

        CodingScheme cs = ServiceHolder.instance().getLexBIGService().resolveCodingScheme(AUTO_SCHEME, null);
        assertTrue(cs.getCopyright().getContent().equals("Copyright by Mayo Clinic."));
        assertTrue(cs.getRepresentsVersion().equals("1.0"));
        assertEquals(1,cs.getSource().length);
        assertTrue(cs.getSource()[0].getContent().equals("lexgrid.org"));

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        assertTrue(cm.getRenderingDetail(AUTO_SCHEME, null).getRenderingDetail().getLastUpdateTime() != null);
    }

}