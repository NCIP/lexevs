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

// LexBIG Test ID: T1_FNC_01	TestContentExtraction

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class TestContentExtraction extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_01";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_01() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);

        ResolvedConceptReferencesIterator iter = cns.resolve(null, null, null);

        // return 100 at a time -
        int count = 0;
        while (iter.hasNext()) {
            ResolvedConceptReference[] temp = iter.next(100).getResolvedConceptReference();
            count += temp.length;
            assertTrue(temp.length <= 100);

            if (count > 400) {
                iter.release();
                break;
            }
        }

        // pretty basic test - iterator will let you go over the entire set of
        // concepts.
        // you can serialize to whatever format you want.
        // have to lookup the sources and targets from each returned concept
        // seperately.

    }

}