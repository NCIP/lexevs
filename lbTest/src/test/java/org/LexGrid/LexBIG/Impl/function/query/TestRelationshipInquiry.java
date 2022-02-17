
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_12	TestRelationshipInquiry

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestRelationshipInquiry extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_12";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_12() throws LBException {

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);

        assertTrue(cng.areCodesRelated(Constructors.createNameAndValue("subClassOf", null),
                Constructors.createConceptReference("Ear_Part", THES_SCHEME),
                Constructors.createConceptReference("Membranous_Labyrinth", THES_SCHEME), true).booleanValue());

        assertFalse(cng.areCodesRelated(Constructors.createNameAndValue("subClassOf", null),
                Constructors.createConceptReference("Membranous_Labyrinth", THES_SCHEME),
                Constructors.createConceptReference("Otolymph", THES_SCHEME), true).booleanValue());

    }
}