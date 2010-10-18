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
package org.LexGrid.LexBIG.Impl.featureRequests;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge24191 extends LexBIGServiceTestCase {
    final static String testID = "GForge24191";
    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService(); 
    }

	@Override
	protected String getTestID() {
		return testID;
	}
	
	public void testForOrphanCodingScheme()throws LBException{
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
		CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.HL7_SCHEME, null, null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(Constructors.createConceptReference("URL:URL",LexBIGServiceTestCase.HL7_SCHEME), true, true ,-1,-1,null, null, null, -1);
        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
        assertTrue( cng.isCodeInGraph(Constructors.createConceptReference("URL:URL",LexBIGServiceTestCase.HL7_SCHEME)).booleanValue());
		
	}
	
	public void testForSchemeNotInCodeSystem()throws LBException{
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
		CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.HL7_SCHEME, null, null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(Constructors.createConceptReference("Country:Country",LexBIGServiceTestCase.HL7_SCHEME), true, true ,-1,-1,null, null, null, -1);
		 assertTrue(cng.isCodeInGraph(Constructors.createConceptReference("22664:POLB_TE004301UV",LexBIGServiceTestCase.HL7_SCHEME)).booleanValue());
	}
	
   public void testForSchemeInCodeSystem()throws LBException{
       if (!System.getProperties().getProperty("os.name").contains("Windows")) {
           // Connecting to ms access from Linux is beyond the scope of this
           // application.
           return;
       }
	   CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.HL7_SCHEME, null, null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(Constructors.createConceptReference("19802:ActMood",LexBIGServiceTestCase.HL7_SCHEME), true, true , -1,-1,null, null, null, -1);
		 assertTrue( cng.isCodeInGraph(Constructors.createConceptReference("19802:ActMood",LexBIGServiceTestCase.HL7_SCHEME)).booleanValue());
		 assertTrue( cng.isCodeInGraph(Constructors.createConceptReference("20936:_ActMoodPredicate",LexBIGServiceTestCase.HL7_SCHEME)).booleanValue());
		 assertTrue( cng.isCodeInGraph(Constructors.createConceptReference("18864:GOL",LexBIGServiceTestCase.HL7_SCHEME)).booleanValue());
   }
}