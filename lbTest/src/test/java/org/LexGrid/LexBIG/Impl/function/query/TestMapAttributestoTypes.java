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

// LexBIG Test ID: T1_FNC_32	TestMapAttributestoTypes

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedProperty;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestMapAttributestoTypes extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_32";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_FNC_32() throws LBException {

        CodingScheme cs = ServiceHolder.instance().getLexBIGService().resolveCodingScheme(AUTO_SCHEME, null);

        SupportedProperty[] sp = cs.getMappings().getSupportedProperty();

        assertTrue(contains(sp, "definition"));
        assertTrue(contains(sp, "textualPresentation"));

    }

    private boolean contains(SupportedProperty[] sp, String item) {
        boolean result = false;

        for (int i = 0; i < sp.length; i++) {
            if (sp[i].getContent().equals(item)) {
                result = true;
                break;
            }

        }
        return result;
    }

}