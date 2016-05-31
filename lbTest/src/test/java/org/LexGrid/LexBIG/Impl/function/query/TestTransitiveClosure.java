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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * This testcase checks that the transitive api works as desired.
 * 
 * @author Pradip Kanjamala
 * 
 */
@RemoteApiSafeTest
public class TestTransitiveClosure extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_150";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testAreCodesRelatedInferredOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
       
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        CodedNodeGraph cng = lbs.getNodeGraph(CELL_URN, csvt, null);
        boolean related= cng.areCodesRelated(ConvenienceMethods.createNameAndValue("is_a", CELL_URN),
                ConvenienceMethods.createConceptReference("CL:0000004", CELL_URN), ConvenienceMethods
                        .createConceptReference("CL:0000000", CELL_URN), false);
        assertTrue(related);
      
    }
    
    
    public void testAreCodesRelatedAssertedOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
       
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        CodedNodeGraph cng = lbs.getNodeGraph(CELL_URN, csvt, null);
        boolean related= cng.areCodesRelated(ConvenienceMethods.createNameAndValue("is_a", CELL_URN),
                ConvenienceMethods.createConceptReference("CL:0000003", CELL_URN), ConvenienceMethods
                        .createConceptReference("CL:0000000", CELL_URN), false);
        assertTrue(related);
       

    }
    


    
    
}