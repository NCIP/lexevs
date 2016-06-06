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