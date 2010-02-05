/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG42.Impl.function.query;

// LexBIG Test ID: T1_FNC_18 TestOtherMatchingTechniques

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG42.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class TestOtherMatchingTechniques extends LexBIGServiceTestCase
{
    final static String testID = "T1_FNC_18";

    @Override
    protected String getTestID()
    {
        return testID;
    }

    public void testT1_FNC_18() throws LBException
    {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
                .getCodingSchemeConcepts(THES_SCHEME, null);

        cns.restrictToMatchingDesignations("base heart", null, "LuceneQuery", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // should have found the concept code C48589 - "Base of the Heart"
        boolean found = false;
        for (int i = 0; i < rcr.length; i++)
        {
            if (rcr[i].getConceptCode().equals("C48589"))
            {
                found = true;
            }
        }
        assertTrue(found);

    }

    public void testRegExp() throws LBException
    {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
                .getCodingSchemeConcepts(AUTO_SCHEME, null);

        cns.restrictToMatchingDesignations(".*lds", null, "RegExp", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // should have found the concept code: 73

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("73"));

    }

    public void testPreferredNonPreferredAll() throws LBException
    {
        // non preferred should have 1 hit.
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
                .getCodingSchemeConcepts(AUTO_SCHEME, null);

        cns.restrictToMatchingDesignations("\"American Car\"", SearchDesignationOption.NON_PREFERRED_ONLY,
                                           "LuceneQuery", null);
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("005"));

        // preferred should have 0 hits.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"American Car\"", SearchDesignationOption.PREFERRED_ONLY, "LuceneQuery",
                                           null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 0);

        // Any should have 1 hit.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"American Car\"", SearchDesignationOption.ALL, "LuceneQuery", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("005"));

        // null should have 1 hit.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"American Car\"", null, "LuceneQuery", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("005"));

        // now, do the reverse queries. preferred should match, non-preferred should not.
        // preferred should have 1 hits.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"General Motors\"", SearchDesignationOption.PREFERRED_ONLY, "LuceneQuery",
                                           null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);

        // non-preferred should have 0 hits.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"General Motors\"", SearchDesignationOption.NON_PREFERRED_ONLY,
                                           "LuceneQuery", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 0);

        // all should have 1 hits.
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"General Motors\"", SearchDesignationOption.ALL, "LuceneQuery", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);

        // and so should null
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToMatchingDesignations("\"General Motors\"", null, "LuceneQuery", null);
        rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcr.length == 1);

    }

}