
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_38	TestMapSynonymtoPreferredNames

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestMapSynonymtoPreferredNames extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_38";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_38() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("skeleton", SearchDesignationOption.ALL, "exactMatch", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, Constructors.createLocalNameList("Display_Name"),
                null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Skeletal_System"));
        assertTrue(rcr[0].getEntity().getPresentation().length == 1);
        assertTrue(rcr[0].getEntity().getPresentation()[0].getValue().getContent().equals("Skeletal System"));

    }

}