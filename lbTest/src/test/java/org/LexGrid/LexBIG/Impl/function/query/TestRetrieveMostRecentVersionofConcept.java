
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_39	TestRetrieveMostRecentVersionofConcept

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestRetrieveMostRecentVersionofConcept extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_39";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_39() throws LBException {

        // not providing a version number gets you the PRODUCTION (which can be
        // assumed to be the latest
        // good version)

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "External_Lip" }, THES_SCHEME));

        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 1);

    }
}