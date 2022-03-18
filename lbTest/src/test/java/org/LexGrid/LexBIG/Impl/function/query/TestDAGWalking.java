
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_07	TestDAGWalking

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestDAGWalking extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_07";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_07() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "subClassOf",
                "Anatomic_Structure_is_Physical_Part_of" }), null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Bone", THES_SCHEME),
                true, true, 1, 1, null, null, null, 0).getResolvedConceptReference();

        // check for some target (up) codes that I know should be there.
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Bone"));

        Association[] a = rcr[0].getTargetOf().getAssociation();

       
        assertTrue(contains(a, "subClassOf", "Vertebra"));
        assertTrue(contains(a, "subClassOf", "Bone_of_the_Extremity"));

        // check for some source (down) codes that I know should be there
        a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "subClassOf", "Musculoskeletal_System_Part"));

        // go down one more level from one of the codes.
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        a = new Association[] { cm.getAssociationForwardOneLevel("Vertebra", "roles", "subClassOf", THES_SCHEME, null, false, null) };

        contains(a, "subClassOf", "Thoracic_Vertebra");

        // go up one more level from one of the codes.
        a = new Association[] { cm.getAssociationReverseOneLevel("Musculoskeletal_System_Part", "roles", "subClassOf", THES_SCHEME, null, false, null) };

        contains(a, "subClassOf", "Body_Part");

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