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

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

//LexBIG Test ID: TEST_FNQRY_CSRNDR_FOR_SUPPASSOC TestCodingSchemesWithSupportedAssociation

@Category(IncludeForDistributedTests.class)
public class TestCodingSchemesWithSupportedAssociation extends LexBIGServiceTestCase {
    final static String testID = "TEST_FNQRY_CSRNDR_FOR_SUPPASSOC";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void test_FNQRY_CSRNDR_FOR_SUPPASSOC() throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        CodingSchemeRenderingList csrl = cm.getCodingSchemesWithSupportedAssociation("uses");
        CodingSchemeRendering csr[] = csrl.getCodingSchemeRendering();
        boolean found = false;

        for (int i = 0; i < csr.length; i++) {
            if (csr[i].getCodingSchemeSummary().getLocalName().equals(AUTO_SCHEME))
                found = true;
        }
        assertTrue(found);
    }
}