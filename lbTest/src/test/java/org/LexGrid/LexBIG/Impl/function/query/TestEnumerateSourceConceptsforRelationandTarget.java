
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_06	TestEnumerateSourceConceptsforRelationandTarget

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestEnumerateSourceConceptsforRelationandTarget extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_06";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_06() throws LBException {

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);

        cng.restrictToAssociations(Constructors.createNameAndValueList("Anatomic_Structure_Has_Location"), null);

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());

        cng = cng.restrictToTargetCodes(cm.createCodedNodeSet(new String[] { "Membranous_Labyrinth" }, THES_SCHEME, null));

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Membranous_Labyrinth", THES_SCHEME),
                false, true, 1, 1, null, null, null, 0).getResolvedConceptReference();

        AssociatedConcept[] ac = rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();

        assertEquals(1,ac.length);

    }

}