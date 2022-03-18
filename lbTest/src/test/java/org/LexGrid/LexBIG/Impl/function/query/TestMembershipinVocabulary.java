
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_24	TestMembershipinVocabulary

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestMembershipinVocabulary extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_24";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_24() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);

        assertTrue(cns.isCodeInSet(Constructors.createConceptReference("Bone", THES_SCHEME)).booleanValue());
        assertFalse(cns.isCodeInSet(Constructors.createConceptReference("fred", THES_SCHEME)).booleanValue());

    }
}