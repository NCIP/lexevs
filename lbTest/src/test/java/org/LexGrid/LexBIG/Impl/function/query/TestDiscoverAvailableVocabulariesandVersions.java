/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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