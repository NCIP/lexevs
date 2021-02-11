
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_34	TestRetrieveConceptandAttributesbyCode

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestRetrieveConceptandAttributesbyCode extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_34";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_34() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Vallecula" }, THES_SCHEME));

        ResolvedConceptReference[] rcr = cns.resolveToList(null, Constructors.createLocalNameList("Semantic_Type"),
                null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Vallecula"));
        assertTrue(rcr[0].getEntity().getPropertyCount() == 1);
        assertTrue(rcr[0].getEntity().getProperty()[0].getPropertyName().equals("Semantic_Type"));

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "invalidCode" }, THES_SCHEME));

        rcr = cns.resolveToList(null, Constructors.createLocalNameList("Semantic_Type"), null, 0)
                .getResolvedConceptReference();

        assertTrue(rcr.length == 0);

        assertFalse(cns.isCodeInSet(Constructors.createConceptReference("invalidCode", THES_SCHEME)).booleanValue());

    }

}