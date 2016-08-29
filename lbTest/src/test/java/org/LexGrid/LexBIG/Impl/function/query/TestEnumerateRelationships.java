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

// LexBIG Test ID: T1_FNC_31	TestEnumerateRelationships

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestEnumerateRelationships extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_31";

    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * Example of listCodeRelationships
     */
    @Test
    public void testT1_FNC_31() throws LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        CodedNodeGraph cng = lbs.getNodeGraph(AUTO_SCHEME, null, null);

        ConceptReference ref4 = ConvenienceMethods.createConceptReference("A0001", AUTO_SCHEME);
        ConceptReference ref5 = ConvenienceMethods.createConceptReference("C0001", AUTO_SCHEME);
        ConceptReference ref6 = ConvenienceMethods.createConceptReference("Tires", "ExpendableParts");

        List<String> assocs = cng.listCodeRelationships(ref4, ref5, true);

        assertTrue(assocs.contains("hasSubtype"));

        assocs = cng.listCodeRelationships(ref4, ref6, false);

        assertTrue(assocs.contains("uses"));

    }
}