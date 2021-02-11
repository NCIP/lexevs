
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_09	TestEnumerateRelationsbyRange

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestEnumerateRelationsbyRange extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_09";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_09() throws LBException {

        String rangeCode = "Anatomy_Kind";

        // check if the supplied range is valid

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList("range"), null);
        cng.restrictToTargetCodes(cm.createCodedNodeSet(new String[] { rangeCode }, THES_SCHEME, null));
        boolean value = cng.isCodeInGraph(Constructors.createConceptReference(rangeCode, THES_SCHEME));
        assertTrue(value);

        // now we have validated that the value supplied is a range. The answer
        // to the test is the graph
        // that is focused on that code (rangeCode)

        // I'll go down two levels for the heck of it.
        cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference(rangeCode, THES_SCHEME),
                false, true, -1, 2, null, null, null, 0).getResolvedConceptReference();

        // focus
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals(rangeCode));

        Association[] a = rcr[0].getTargetOf().getAssociation();

        // one level deep
        assertTrue(a.length == 1);
        assertTrue(a[0].getAssociationName().equals("subClassOf"));

        AssociatedConcept[] ac = a[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(contains(ac, "Anatomic_Structure_System_or_Substance"));

        // two levels deep
        a = ac[0].getTargetOf().getAssociation();

        contains(a, "subClassOf", "Body_Part");
        contains(a, "subClassOf", "Body_Fluid_or_Substance");
        contains(a, "subClassOf", "Body_Region");

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