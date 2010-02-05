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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;

public class GForge25067 extends LexBIGServiceTestCase {
    /** The Constant testID. */
    final static String testID = "GForge25067";
    /** The lbs. */
    private LexBIGService lbs;
    
    /** The test entity. */
    private Entity testEntity;

	protected String getTestID() {
		return testID;
	}
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
    	
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
        lbs = ServiceHolder.instance().getLexBIGService();  
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_SCHEME, null);
        
        cns.restrictToCodes(Constructors.createConceptReferenceList("10198:DEF"));
        
        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue(rcrl.length == 1);
        
        testEntity = rcrl[0].getEntity();
}
    /**
     * Test entity not null.
     * 
     * @throws LBException the LB exception
     */
    public void testEntityNotNull() throws LBException {
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
       assertNotNull(testEntity);
    }
    
    public void testDefinitionExists()throws LBException{
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
    	assertTrue(testEntity.getDefinitionCount()> 0);
    }
}
