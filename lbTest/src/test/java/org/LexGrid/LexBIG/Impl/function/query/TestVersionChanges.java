/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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

// LexBIG Test ID: T1_FNC_03	TestVersionChanges

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;

public class TestVersionChanges extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_03";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_03() throws URISyntaxException, LBException {

        HistoryService hs = ServiceHolder.instance().getLexBIGService().getHistoryService(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getEditActionList(null,
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1133935200000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

    }
}