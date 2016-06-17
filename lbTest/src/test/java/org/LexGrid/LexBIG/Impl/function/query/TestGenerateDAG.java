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

// LexBIG Test ID: T1_FNC_04	TestGenerateDAG

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestGenerateDAG extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_04";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_FNC_04() throws LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        CodedNodeGraph cng = lbs.getNodeGraph(AUTO_SCHEME, null, "relations");

        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Ford", AUTO_SCHEME),
                true, true, 10, -1, null, null, null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Ford"));

        Association[] a = rcr[0].getTargetOf().getAssociation();

        assertTrue(a.length == 1);
        assertTrue(a[0].getAssociationName().equals("hasSubtype"));

        AssociatedConcept[] ac = a[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(contains(ac, "005"));

        a = rcr[0].getSourceOf().getAssociation();

        assertTrue(a.length == 1);
        assertTrue(a[0].getAssociationName().equals("hasSubtype"));

        ac = a[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(contains(ac, "Jaguar"));

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