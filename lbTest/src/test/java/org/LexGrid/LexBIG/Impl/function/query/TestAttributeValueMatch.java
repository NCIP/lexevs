
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_15	TestAttributeValueMatch

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

public class TestAttributeValueMatch extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_15";

    @Override
    protected String getTestID() {
        return testID;
    }

    private boolean matchAttributeValue(String prop, String value) throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        LocalNameList lnl = new LocalNameList();
        lnl.addEntry(prop);
        CodedNodeSet matches = cns.restrictToMatchingProperties(lnl, null, value, "startsWith", null);
        int count = matches.resolveToList(null, null, null, 0).getResolvedConceptReferenceCount();
        return (count > 0);
    }

    private boolean matchAttributeValueType(PropertyType prop, String value) throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        CodedNodeSet matches = cns.restrictToMatchingProperties(null, new PropertyType[] { prop }, value, "startsWith",
                null);
        int count = matches.resolveToList(null, null, null, 0).getResolvedConceptReferenceCount();
        return (count > 0);
    }

    public void testT1_FNC_15a() throws LBException {
        assertTrue(matchAttributeValue("dDEFINITION", "<def"));
    }

    public void testT1_FNC_15b() throws LBException {
        assertFalse(matchAttributeValue("dDEFINITION", "vx"));
    }

    public void testT1_FNC_15c() throws LBException {
        assertTrue(matchAttributeValueType(PropertyType.DEFINITION, "<def"));
    }

    public void testT1_FNC_15d() throws LBException {
        assertFalse(matchAttributeValueType(PropertyType.DEFINITION, "vx"));
    }

}