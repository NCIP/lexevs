
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_31	TestEnumerateRelationships

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestRestrictToDirectionalNames extends LexBIGServiceTestCase {
    final static String testID = "TEST_RESTRICT_TO_DIRECTIONAL_NAMES";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testRestrictToDirectionalNames() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(CELL_SCHEME, null, null);
        cng.restrictToDirectionalNames(Constructors.createNameAndValueList(new String[] { "develops_from" }), null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(
                Constructors.createConceptReference("CL:0000047", CELL_SCHEME), true, true, 1, 1, null, null, null, 0)
                .getResolvedConceptReference();
        assertTrue(rcr[0].getConceptCode().equals("CL:0000047"));

        Association[] a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "develops_from", "CL:0000133"));
        // We have restricted to directional name develops_from, so is_a
        // shouldn't be found.
        assertFalse(contains(a, "is_a", "CL:0000048"));

        // Creating a CodedNodeGraph and restricting on directional names is_a
        // and develops_from
        cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(CELL_SCHEME, null, null);
        cng.restrictToDirectionalNames(Constructors.createNameAndValueList(new String[] { "is_a", "develops_from" }),
                null);

        rcr = cng.resolveAsList(Constructors.createConceptReference("CL:0000047", CELL_SCHEME), true, true, 1, 1, null,
                null, null, 0).getResolvedConceptReference();

        a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "develops_from", "CL:0000133"));
        // We have restricted to directional name develops_from, so is_a
        // shouldn't be found.
        assertTrue(contains(a, "is_a", "CL:0000048"));
        // check for some target (up) codes that I know should be there
        a = rcr[0].getTargetOf().getAssociation();

        assertTrue(contains(a, "is_a", "CL:0000031"));
        assertTrue(contains(a, "is_a", "CL:0000468"));

        // check to see the convenience methods that helps determine
        // directionality of the direction name for the association works.
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        assertTrue(cm.isForwardName(CELL_SCHEME, null, "is_a"));
        assertTrue(cm.isForwardName(CELL_SCHEME, null, "develops_from"));
        assertFalse(cm.isReverseName(CELL_SCHEME, null, "is_a"));

    }

    private boolean contains(Association[] a, String association, String conceptCode) {
        boolean found = false;
        for (int i = 0; i < a.length; i++) {
            if (a[i].getAssociationName().equals(association)
                    && contains(a[i].getAssociatedConcepts().getAssociatedConcept(), conceptCode)) {
                found = true;
                break;
            }
        }

        return found;
    }

    private boolean contains(AssociatedConcept[] ac, String conceptCode) {
        boolean found = false;
        for (int i = 0; i < ac.length; i++) {
            if (ac[i].getConceptCode().equals(conceptCode)) {
                found = true;
                break;
            }

        }

        return found;
    }

}