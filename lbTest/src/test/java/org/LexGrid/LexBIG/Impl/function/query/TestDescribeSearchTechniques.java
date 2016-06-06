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

// LexBIG Test ID: T1_FNC_26	TestDescribeSearchTechniques

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class TestDescribeSearchTechniques extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_26";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_26() {

        ModuleDescription[] ed = ServiceHolder.instance().getLexBIGService().getMatchAlgorithms()
                .getModuleDescription();

        assertTrue(ed.length > 0);
        assertTrue(ed[0].getName().length() > 0);
        assertTrue(ed[0].getDescription().length() > 0);

    }
}