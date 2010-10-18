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

// LexBIG Test ID: T1_FNC_42	TestPagedReturns
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class TestPagedReturns extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_42";

    private LexBIGService lbs;

    @Override
    protected String getTestID()
    {
        return testID;
    }
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();
    }

    public void testT1_FNC_42() throws LBException {

        CodedNodeSet cns = lbs.getCodingSchemeConcepts(THES_SCHEME, null);

        // invalid property - quicker - doesn't resolve any properties this way.
        ResolvedConceptReferencesIterator iter = cns.resolve(null, Constructors.createLocalNameList("invalidProperty"),
                null);

        // should be a total of 67
        int count = 0;
        while (iter.hasNext()) {
            ResolvedConceptReference[] temp = iter.next(100).getResolvedConceptReference();
            count += temp.length;
            assertTrue(temp.length <= 100);
        }

        assertTrue("Actual Count: " + count, count == 66);

    }

    public void testT1_FNC_42a() throws LBException {

        // test the new get(start, end) method
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(THES_SCHEME, null);

        // invalid property - quicker - doesn't resolve any properties this way.
        ResolvedConceptReferencesIterator iter = cns.resolve(null, Constructors.createLocalNameList("invalidProperty"),
                null);

        // return 100 at a time - should be a total of 210
        int start = 0;
        int increment = 100;
        int count = 0;
        // iter.numberRemaining can be used for this purpose if you are not
        // using filters.
        while (start < iter.numberRemaining()) {
            ResolvedConceptReference[] temp = iter.get(start, start + increment).getResolvedConceptReference();
            count += temp.length;
            assertTrue(temp.length <= 100);
            start = start + temp.length;
        }

        // going backwards with this method is legal.
        ResolvedConceptReference[] temp = iter.get(20, 50).getResolvedConceptReference();
        assertTrue(temp.length == 30);

        try {
            // going to far with a start point throws an exception
            iter.get(210, 300).getResolvedConceptReference();
            fail("Did not throw LBParameter Exception");
        } catch (LBParameterException e) {
            // supposed to happen
        }
    }
}