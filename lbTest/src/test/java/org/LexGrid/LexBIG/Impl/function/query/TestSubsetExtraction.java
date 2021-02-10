
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_02	TestSubsetExtraction

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestSubsetExtraction extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_02";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_02() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Vallecula", "Palate", "Hard_Palate" },
                THES_SCHEME));

        // this test is really lacking... but here is how you subset. You can
        // serialize however
        // you want from here...
        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 3);

    }

}