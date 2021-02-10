
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_40	TestRetrieveRelationsforConcept

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestRetrieveRelationsforConcept extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_40";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_40() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "External_Lip" }, THES_SCHEME));

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");

        cng.restrictToSourceCodes(cns);
        cng.restrictToAssociations(Constructors.createNameAndValueList("Anatomic_Structure_is_Physical_Part_of"), null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("External_Lip", THES_SCHEME),
                true, false, 1, 1, Constructors.createLocalNameList("Semantic_Type"), null, null, 0)
                .getResolvedConceptReference();
        assertTrue(rcr[0].getEntity().getProperty()[0].getPropertyName().equals("Semantic_Type"));
        assertTrue(rcr[0].getEntity().getProperty().length == 1);
    }

}