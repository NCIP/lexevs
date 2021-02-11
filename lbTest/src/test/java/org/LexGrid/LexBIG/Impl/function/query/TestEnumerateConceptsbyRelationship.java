
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_05	TestEnumerateConceptsbyRelationship

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestEnumerateConceptsbyRelationship extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_05";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_05() throws LBException {

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList("Anatomic_Structure_is_Physical_Part_of"), null);

        ResolvedConceptReference[] rcr = cng.toNodeList(Constructors.createConceptReference("External_Lip", THES_SCHEME),
                true, false, -1, -1).resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertEquals(1,rcr.length);

    }

}