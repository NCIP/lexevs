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
package org.LexGrid.LexBIG.Impl.performance;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Provides a check on Constructors that create a ConceptReference with a code only.
 * Should now return a value when used to retrieve CodedNodeSets and Graphs. 
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class ResolveConceptReferenceTest extends LexBIGServiceTestCase {
    final static String testID = "GForge15976";

    private LexBIGService lbs;
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    //main method for profiling outside of JUnit framework.
    public static void main(String[] args) throws Exception {
       ResolveConceptReferenceTest test = new ResolveConceptReferenceTest();
        test.setUp();
       test.testGetResolvedReferences();
    }

    //setup and do a dummy query to make sure everything is initialized.
    public void setUp(){
        try {
            System.setProperty("LG_CONFIG_FILE", "W:/user/m005256/LexEVSMetaTest/resources/config/lbconfig.props");
            lbs = LexBIGServiceImpl.defaultInstance();
            
            CodedNodeSet cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
         
            cns.restrictToCodes(Constructors.createConceptReferenceList("U000035"));
            
            cns.resolve(null, null, null);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
 
    //Baseline 5.0.1 : 790ms
    //June 24 2009 - Join EntryState on SQLImplementedMethods.buildCodedEntry : 650ms
    public void testGetResolvedReferences() throws Exception {
        CodedNodeSet cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
        cns.restrictToMatchingDesignations("heart", SearchDesignationOption.ALL, "RegExp", null);
        
        int runs = 1;
        
        long totaltime = 0;
        
        for(int i=0;i<runs;i++){
            Long time = System.currentTimeMillis();
            cns.resolveToList(null, null, null, 10);
            totaltime += (System.currentTimeMillis() - time);
        }
        
        System.out.println(totaltime/runs);
        
    }
   
    public void testGetUnresolvedReferences() throws Exception {
       // assertNotNull(rcr2);
        //assertTrue(rcr2.getResolvedConceptReferenceCount() > 0);
    }
       
}