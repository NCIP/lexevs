
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_23	TestforCurrentOrObsoleteConcept

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;

public class TestforCurrentOrObsoleteConcept extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_23";

    @Override
    protected String getTestID() {
        return testID;
    }

    @SuppressWarnings("deprecation")
    public void testT1_FNC_23() throws LBException {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null, true);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 0.
        assertTrue(rcr.length == 0);

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null, false);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 1.
        assertTrue(rcr.length == 1);

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null, false);
        cns = cns.restrictToMatchingDesignations("olds", SearchDesignationOption.ALL, "startsWith", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);

        assertTrue(rcr[0].getEntity().getStatus().equals("Retired"));
        assertFalse(rcr[0].getEntity().getIsActive().booleanValue());
    }

    public void testT1_FNC_23a() throws LBException {

        // same as above, but this time, using the new methods (that aren't
        // deprecated)
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 0.
        assertTrue(rcr.length == 0);

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToStatus(ActiveOption.ALL, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 1.
        assertTrue(rcr.length == 1);

        // same test again - no status restriction
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 1.
        assertTrue(rcr.length == 1);

        // add a status restriction
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns = cns.restrictToMatchingDesignations("olds", SearchDesignationOption.ALL, "startsWith", null);
        cns = cns.restrictToStatus(ActiveOption.INACTIVE_ONLY, new String[] { "Retired" });
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);

        assertTrue(rcr[0].getEntity().getStatus().equals("Retired"));
        assertFalse(rcr[0].getEntity().getIsActive().booleanValue());
    }
}