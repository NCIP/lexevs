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

// LexBIG Test ID: T1_FNC_21	TestSetofVocabulariesforSearch

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG42.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class TestSetofVocabulariesforSearch extends LexBIGServiceTestCase
{
    final static String testID = "T1_FNC_21";

    @Override
    protected String getTestID()
    {
        return testID;
    }

    public void testT1_FNC_21() throws LBException
    {

        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
                .getCodingSchemeConcepts(AUTO_SCHEME, null);

        CodedNodeSet cns2 = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(PARTS_SCHEME, null);

        cns.union(cns2);

        cns.restrictToMatchingDesignations("t", SearchDesignationOption.ALL, "startsWith", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // coincidence that both codes are the same - they are different concept codes
        // one if for Truck and one is for tires.
        assertTrue(contains(rcr, PARTS_SCHEME, "T0001"));
        assertTrue(contains(rcr, AUTO_SCHEME, "T0001"));

    }

    private boolean contains(ResolvedConceptReference[] rcr, String codeSystem, String conceptCode)
    {
        boolean result = false;
        for (int i = 0; i < rcr.length; i++)
        {
            if (rcr[i].getCodingSchemeName().equals(codeSystem) && rcr[i].getConceptCode().equals(conceptCode))
            {
                result = true;
                break;
            }
        }
        return result;
    }

}