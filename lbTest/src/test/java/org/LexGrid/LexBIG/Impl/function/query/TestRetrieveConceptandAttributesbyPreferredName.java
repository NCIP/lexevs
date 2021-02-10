
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_35	TestRetrieveConceptandAttributesbyPreferredName

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestRetrieveConceptandAttributesbyPreferredName extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_35";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_35() throws LBException {
        LexBIGService lbsvc = ServiceHolder.instance().getLexBIGService();
        
        CodedNodeSet cns = lbsvc.getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("Vallecula", SearchDesignationOption.PREFERRED_ONLY, "exactMatch", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, Constructors.createLocalNameList("Semantic_Type"),
                null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Vallecula"));
        assertTrue(rcr[0].getEntity().getPropertyCount() == 1);
        assertTrue(rcr[0].getEntity().getProperty()[0].getPropertyName().equals("Semantic_Type"));

        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbsvc
            .getGenericExtension("LexBIGServiceConvenienceMethods");
        
        String desc = lbscm.getEntityDescription(THES_SCHEME, null, "Vallecula");
        assertTrue("Vallecula".equalsIgnoreCase(desc));
    }

}