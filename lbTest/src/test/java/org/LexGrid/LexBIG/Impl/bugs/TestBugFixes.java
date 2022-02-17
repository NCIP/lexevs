
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedAssociation;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class TestBugFixes extends LexBIGServiceTestCase {
    final static String testID = "TestBugFixes";

    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #2765 -
     * http://gforge.nci.nih.gov/tracker/?func=detail&atid=134
     * &aid=2765&group_id=14
     * 
     * Requesting to walk up a tree from a root node throws exceptions.
     */
    public void testNCIRootNodes1() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);

        // C43652 is a root node in NCI.
        // just running this query is enough to show the bug.
        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Anatomy_Kind", THES_SCHEME),
                true, true, 1, 1, null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 1);

    }

    /*
     * LexBIG Bug #2883 -
     * http://gforge.nci.nih.gov/tracker/index.php?func=detail
     * &aid=2883&group_id=14&atid=134
     * 
     * Get Supported Associations returning unexpected items.
     */
    public void testNCISupportedAssociations() throws LBException {

        CodingScheme cs = ServiceHolder.instance().getLexBIGService().resolveCodingScheme(THES_SCHEME, null);

        SupportedAssociation[] sa = cs.getMappings().getSupportedAssociation();
        assertEquals(sa.length, 167);
        assertTrue(contains(sa, "subClassOf"));
        assertTrue(contains(sa, "Anatomic_Structure_Has_Location"));
        assertTrue(contains(sa, "Disease_Excludes_Molecular_Abnormality"));

    }

    /*
     * Scott discovered a CodedNodeSet bug that was occuring on some databases
     * when you provided a language restriction.
     */
    public void testValidateLanguage() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);

        // priviously, just specifying a language restriction was causing an
        // error.
        cns.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", "en");

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertEquals(3,rcr.length);
    }

    /*
     * LexBIG Bug # 3686 Kim discovered two bugs - it wasn't allowing you to
     * restrict to association qualifiers, and it wasn't returning association
     * qualifications.
     */

    public void testAssociationQualifiers() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(AUTO_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"),
                Constructors.createNameAndValueList("hasEngine"));
        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("A0001", null), true,
                false, 1, 1, null, null, null, -1).getResolvedConceptReference();

        assertTrue(rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept(0)
                .getAssociationQualifiers().getNameAndValue(0).getName().equals("hasEngine"));
    }

    private boolean contains(SupportedAssociation[] sa, String association) {
        boolean found = false;
        for (int i = 0; i < sa.length; i++) {
            if (sa[i].getLocalId().equals(association)) {
                found = true;
                break;
            }

        }
        return found;
    }
}