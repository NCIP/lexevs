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

// LexBIG Test ID: T1_FNC_14	TestAttributePresenceMatch

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestAttributePresenceMatch extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_14";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public boolean matchAttribute(String attribute) throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        LocalNameList lnl = new LocalNameList();
        lnl.addEntry(attribute);

        CodedNodeSet matches = null;
        try {
            matches = cns.restrictToProperties(lnl, null);
        } catch (LBParameterException e) {
            return (false);
        }

        int count = matches.resolveToList(null, null, null, 0).getResolvedConceptReferenceCount();
        return (count > 0);
    }
    
    @Test
    public void testT1_FNC_14a() throws LBException {
        assertTrue(matchAttribute("definition"));
    }

    @Test
    public void testT1_FNC_14b() throws LBException {
        assertFalse(matchAttribute("defunition"));
    }

}