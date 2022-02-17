
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_11	TestTraverseGraphviaRoleLinks

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestTraverseGraphviaRoleLinks extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_11";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_11() throws LBException {
        // in LexBig, a role is just an association, so this is traversing
        // associations.

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "subClassOf" }), null);

        // role "Anatomy_Kind"
        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Anatomy_Kind", THES_SCHEME),
                false, true, 1, 1, null, null, null, 0).getResolvedConceptReference();

        // check for some source (down) codes that I know should be there.
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Anatomy_Kind"));

        Association[] a = rcr[0].getTargetOf().getAssociation();
        assertTrue(contains(a, "subClassOf", "Anatomic_Structure_System_or_Substance"));

        // go down one more level from one of the codes.
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        a = new Association[] { cm.getAssociationForwardOneLevel("Anatomic_Structure_System_or_Substance", "roles", "subClassOf", THES_SCHEME, null, false, null) };

        contains(a, "subClassOf", "C13018");
        contains(a, "subClassOf", "Body_Fluid_or_Substance");

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