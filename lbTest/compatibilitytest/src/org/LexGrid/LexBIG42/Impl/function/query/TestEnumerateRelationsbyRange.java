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

// LexBIG Test ID: T1_FNC_09	TestEnumerateRelationsbyRange

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG42.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestEnumerateRelationsbyRange extends LexBIGServiceTestCase
{
    final static String testID = "T1_FNC_09";

    @Override
    protected String getTestID()
    {
        return testID;
    }

    public void testT1_FNC_09() throws LBException
    {

        // check if the supplied range is valid

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());

        String rangeCode = "C43652";

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList("Role_Has_Range"), null);
        cng.restrictToTargetCodes(cm.createCodedNodeSet(new String[]{rangeCode}, THES_SCHEME, null));

        assertTrue(cng.isCodeInGraph(Constructors.createConceptReference(rangeCode, THES_SCHEME)).booleanValue());

        // now we have validated that the value supplied is a range. The answer to the test is the graph
        // that is focused on that code (rangeCode)

        // I'll go down two levels for the heck of it.
        cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference(rangeCode, THES_SCHEME),
                                                           true, false, -1, 2, null, null, null, 0)
                .getResolvedConceptReference();

        // focus
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals(rangeCode));

        Association[] a = rcr[0].getSourceOf().getAssociation();

        // one level deep
        assertTrue(a.length == 1);
        assertTrue(a[0].getAssociationName().equals("hasSubtype"));

        AssociatedConcept[] ac = a[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(contains(ac, "C12219"));

        // two levels deep
        a = ac[0].getSourceOf().getAssociation();

        contains(a, "hasSubtype", "C32221");
        contains(a, "hasSubtype", "C13236");
        contains(a, "hasSubtype", "C12680");

    }

    private boolean contains(Association[] a, String association, String conceptCode)
    {
        boolean found = false;
        for (int i = 0; i < a.length; i++)
        {
            if (a[i].getAssociationName().equals(association)
                    && contains(a[i].getAssociatedConcepts().getAssociatedConcept(), conceptCode))
            {
                found = true;
                break;
            }
        }

        return found;
    }

    private boolean contains(AssociatedConcept[] ac, String conceptCode)
    {
        boolean found = false;
        for (int i = 0; i < ac.length; i++)
        {
            if (ac[i].getConceptCode().equals(conceptCode))
            {
                found = true;
                break;
            }

        }

        return found;
    }

}