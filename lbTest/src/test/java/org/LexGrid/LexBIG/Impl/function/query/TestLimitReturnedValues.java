
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_43	TestLimitReturnedValues

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class TestLimitReturnedValues extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_43";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_43() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);

        cns = cns.restrictToMatchingDesignations("heaart", SearchDesignationOption.ALL, "DoubleMetaphoneLuceneQuery", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 1).getResolvedConceptReference();

        assertTrue(rcr.length == 1);

    }

}