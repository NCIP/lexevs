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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

import java.util.Arrays;
import java.util.List;

@RemoteApiSafeTest
public class TestEnumerateAssociationNames extends LexBIGServiceTestCase {
    final static String testID = "TEST_FNQRY_ASSOCIATIONNAME_FOR_CODINGSCHEME";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testTEST_FNQRY_ASSOCIATIONNAME_FOR_CODINGSCHEME() throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        String[] association_names;
        association_names = cm.getAssociationForwardNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "hasSubtype", "uses" }));
        association_names = cm.getAssociationReverseNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "isA", "usedBy" }));
        association_names = cm.getAssociationForwardAndReverseNames(AUTO_SCHEME, null);
        assertTrue(contains(association_names, new String[] { "hasSubtype", "uses", "isA", "usedBy" }));
    }

    private boolean contains(String[] association_names, String[] ref_list) {
        List association_names_list = Arrays.asList(association_names);
        boolean found = true;
        for (int i = 0; i < ref_list.length; i++) {
            if (!association_names_list.contains(ref_list[i])) {
                found = false;
                break;
            }
        }
        return found;
    }
}