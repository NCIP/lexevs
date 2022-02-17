
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_17	TestLexicalMatchingTechniques

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class TestLexicalMatchingTechniques extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_17";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_17() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);

        cns = cns.restrictToMatchingDesignations("heaart", SearchDesignationOption.ALL, "DoubleMetaphoneLuceneQuery", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // should have found the concept code C48589 - "Base of the Heart"
        boolean found = false;
        for (int i = 0; i < rcr.length; i++) {
            if (rcr[i].getConceptCode().equals("Base_of_the_Heart")) {
                found = true;
            }
        }
        assertTrue(found);

    }

}