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

// LexBIG Test ID: T1_FNC_33 TestVersioningandAuthorityEnumeration

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.LexGrid.codingSchemes.CodingScheme;

@RemoteApiSafeTest
public class TestVersioningandAuthorityEnumeration extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_33";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_33() throws LBException {

        CodingScheme cs = ServiceHolder.instance().getLexBIGService().resolveCodingScheme(AUTO_SCHEME, null);
        assertTrue(cs.getCopyright().getContent().equals("Copyright by Mayo Clinic."));
        assertTrue(cs.getRepresentsVersion().equals("1.0"));
        assertEquals(1,cs.getSource().length);
        assertTrue(cs.getSource()[0].getContent().equals("lexgrid.org"));

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        assertTrue(cm.getRenderingDetail(AUTO_SCHEME, null).getRenderingDetail().getLastUpdateTime() != null);
    }

}