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

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class ValidateHashCode extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_16";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testCSRHashCodEquals() throws LBException {

        CodingSchemeReference csr = new CodingSchemeReference();
        Set<CodingSchemeReference> refList = new HashSet<CodingSchemeReference>();
        refList.add(csr);
        csr.setCodingScheme("MyScheme");
        refList.add(csr);
        csr.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        refList.add(csr);
        csr.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        refList.add(csr);
        CodingSchemeReference csr1 = new CodingSchemeReference();
        Set<CodingSchemeReference> refList1 = new HashSet<CodingSchemeReference>();
        refList1.add(csr1);
        csr1.setCodingScheme("MyScheme");
        refList1.add(csr1);
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        refList1.add(csr1);
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        refList1.add(csr1);
        
        assertFalse(csr.equals(csr1));
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        assertTrue(csr.equals(csr1));
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        assertFalse(csr.equals(csr1));
    }
}
