
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_21	TestSetofVocabulariesforSearch

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class TestSetofVocabulariesforSearch extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_21";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_21() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);

        CodedNodeSet cns2 = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(PARTS_SCHEME, null);

        CodedNodeSet union = cns.union(cns2);

        union.restrictToMatchingDesignations("t", SearchDesignationOption.ALL, "startsWith", null);

        ResolvedConceptReference[] rcr = union.resolveToList(null, null, null, 0).getResolvedConceptReference();

        assertEquals(4,rcr.length);
        
        // coincidence that both codes are the same - they are different concept
        // codes
        // one if for Truck and one is for tires.
        assertTrue(contains(rcr, PARTS_SCHEME, "T0001"));
        assertTrue(contains(rcr, AUTO_SCHEME, "T0001"));

    }

    private boolean contains(ResolvedConceptReference[] rcr, String codeSystem, String conceptCode) {
        boolean result = false;
        for (int i = 0; i < rcr.length; i++) {
            if (rcr[i].getCodingSchemeName().equals(codeSystem) && rcr[i].getConceptCode().equals(conceptCode)) {
                result = true;
                break;
            }
        }
        return result;
    }

}