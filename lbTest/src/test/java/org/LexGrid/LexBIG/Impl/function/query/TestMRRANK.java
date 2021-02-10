
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.concepts.Presentation;

/**
 * Tests MRRANK ordering of the Presentations
 * 
 * @author Kevin Peterson
 * 
 */
public class TestMRRANK extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_50";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_50() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(SAMPLE_META_VERSION);
        CodedNodeSet cns = lbsi.getCodingSchemeConcepts(META_SCHEME, csvt);

        ConceptReferenceList crl = new ConceptReferenceList();
        ConceptReference cr = new ConceptReference();
        cr.setCodingSchemeName(META_SCHEME);
        cr.setConceptCode("C0000005");
        crl.addConceptReference(cr);
        cns = cns.restrictToCodes(crl);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);

        ResolvedConceptReference testConcept = rcr[0];

        Presentation[] presentations = testConcept.getEntity().getPresentation();

        for (int i = 0; i < presentations.length; i++) {
            String presentation = presentations[i].getValue().getContent();
            if (presentation.equals("(131)I-MAA")) {
                assertTrue(presentations[i].isIsPreferred());
            } 
        }
    }
}