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
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Before;
import org.junit.experimental.categories.Category;

/**
 * The Class BaseCodedNodeGraphTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */

public class BaseCodedNodeGraphTest extends LexBIGServiceTestCase {

    /** The cng. */
    protected CodedNodeGraph cng;
    
    /** The lbs. */
    protected LexBIGService lbs;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Base CodedNodeGraph Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    @Before
    public void setUp() throws Exception {
        try {
            lbs = ServiceHolder.instance().getLexBIGService();
            cng = lbs.getNodeGraph(getCodingScheme(), null, null);
        } catch (LBException e) {
          e.printStackTrace();
          fail(e.getMessage());
        }
      }
    
    /**
     * Gets the coding scheme.
     * 
     * @return the coding scheme
     */
    protected String getCodingScheme(){
        return LexBIGServiceTestCase.AUTO_SCHEME;
    }
    
    /**
     * Test set up.
     */
    public void testSetUp(){
        assertNotNull(cng);
    }
    
    /**
     * Resolved concept list contains.
     * 
     * @param concepts the concepts
     * @param code the code
     * 
     * @return true, if successful
     */
    public static boolean resolvedConceptListContains(ResolvedConceptReference[] concepts, String code){
        for(ResolvedConceptReference con : concepts){
            if(con.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Associated concept list contains.
     * 
     * @param concepts the concepts
     * @param code the code
     * 
     * @return true, if successful
     */
    public static boolean associatedConceptListContains(AssociatedConcept[] concepts, String code){
       return resolvedConceptListContains(concepts, code);
    }
    
    /**
     * Association list contains.
     * 
     * @param assocs the assocs
     * @param assoc the assoc
     * 
     * @return true, if successful
     */
    public static boolean associationListContains(Association[] assocs, String assoc){
        for(Association a : assocs){
            if(a.getAssociationName().equals(assoc)){
                return true;
            }
        }
        return false;
    }
}