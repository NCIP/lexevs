
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestChildIndicator extends LexBIGServiceTestCase {
    final static String testID = "T1_CHILD_INDICATOR";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_CHILD_INDICATOR() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(CELL_SCHEME, null, null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(
                Constructors.createConceptReference("CL:0000047", CELL_SCHEME), true, true, 2, 2, null, null, null,
                null, 0, true).getResolvedConceptReference();
        assertTrue(rcr[0].getConceptCode().equals("CL:0000047"));

        Association[] a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "develops_from", "CL:0000133"));
        AssociatedConcept ac = getMatchingAssociatedConcept(a, "develops_from", "CL:0000133");
        assertTrue(ac.getSourceOf().getAssociation().length > 0);

        assertEquals(1,ac.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
        assertTrue(contains(a, "is_a", "CL:0000048"));

        cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(CELL_SCHEME, null, null);

        rcr = cng.resolveAsList(Constructors.createConceptReference("CL:0000047", CELL_SCHEME), true, true, 2, 2, null,
                null, null, null, 0, false).getResolvedConceptReference();
        assertTrue(rcr[0].getConceptCode().equals("CL:0000047"));

        a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "develops_from", "CL:0000133"));
        ac = getMatchingAssociatedConcept(a, "develops_from", "CL:0000133");
        assertTrue(ac.getSourceOf().getAssociation().length > 0);
        // The associatedConcepts should be resolved even at the 2nd level
        assertFalse(ac.getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 0);

        assertTrue(contains(a, "is_a", "CL:0000048"));
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

    private AssociatedConcept getMatchingAssociatedConcept(Association[] a, String association, String conceptCode) {
        AssociatedConcept found = null;
        for (int i = 0; i < a.length; i++) {
            if (a[i].getAssociationName().equals(association)) {
                AssociatedConcept ac[] = a[i].getAssociatedConcepts().getAssociatedConcept();
                for (int j = 0; j < ac.length; j++) {
                    if (ac[j].getConceptCode().equals(conceptCode)) {
                        found = ac[j];
                        break;
                    }

                }
            }

        }

        return found;
    }

}