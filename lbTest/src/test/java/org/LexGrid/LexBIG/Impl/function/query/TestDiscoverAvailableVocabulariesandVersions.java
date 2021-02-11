
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_25  TestDiscoverAvailableVocabulariesandVersions

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;

public class TestDiscoverAvailableVocabulariesandVersions extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_25";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_25() throws LBInvocationException {

        CodingSchemeRendering[] csr = ServiceHolder.instance().getLexBIGService().getSupportedCodingSchemes()
                .getCodingSchemeRendering();

        assertTrue(contains(csr, THES_SCHEME, THES_VERSION));
        assertTrue(contains(csr, META_SCHEME, META_VERSION));
        assertTrue(contains(csr, PARTS_SCHEME, PARTS_VERSION));
        assertTrue(contains(csr, AUTO_SCHEME, AUTO_VERSION));

    }

    public boolean contains(CodingSchemeRendering[] csr, String codingSchemeName, String codingSchemeVersion) {
        boolean result = false;
        for (int i = 0; i < csr.length; i++) {
            if ( 
            		(csr[i].getCodingSchemeSummary().getLocalName().equals(codingSchemeName) ||
            				csr[i].getCodingSchemeSummary().getLocalName().equals("Thesaurus") ||
            				csr[i].getCodingSchemeSummary().getLocalName().equals(
            						"ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl")
            				)
                    && csr[i].getCodingSchemeSummary().getRepresentsVersion().equals(codingSchemeVersion)
                    && csr[i].getRenderingDetail().getLastUpdateTime() != null) {
                result = true;
                break;

            }
        }
        return result;
    }
}