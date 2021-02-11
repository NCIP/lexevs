
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_10	TestQuerybyRelationshipDomain

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestQuerybyRelationshipDomain extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_10";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_10() throws LBException {

        String domainCode = "Anatomy_Kind";

        // check if the supplied domain is valid

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("domain"), null);
        cng = cng.restrictToTargetCodes(cm.createCodedNodeSet(new String[] { domainCode }, THES_SCHEME, null));

        assertTrue(cng.isCodeInGraph(Constructors.createConceptReference(domainCode, THES_SCHEME)).booleanValue());

        // now we have validated that the value supplied is a domain. The answer
        // to the test is the graph
        // that
        // is focused on that code (domainCode)

        // I'll go down two levels for the heck of it.
        cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");

        ResolvedConceptReference[] rcr = cng.resolveAsList(
                Constructors.createConceptReference(domainCode, THES_SCHEME), false, true, -1, 2, null, null, null, 0)
                .getResolvedConceptReference();

        // focus
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals(domainCode));

        Association[] a = rcr[0].getTargetOf().getAssociation();

        // one level deep
        assertEquals(1,a.length);
        assertTrue(a[0].getAssociationName().equals("subClassOf"));

        AssociatedConcept[] ac = a[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(contains(ac, "Anatomic_Structure_System_or_Substance"));

        // two levels deep
        a = ac[0].getTargetOf().getAssociation();

        contains(a, "subClassOf", "Body_Part");
        contains(a, "subClassOf", "Body_Fluid_or_Substance");
        contains(a, "subClassOf", "Body Region");

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