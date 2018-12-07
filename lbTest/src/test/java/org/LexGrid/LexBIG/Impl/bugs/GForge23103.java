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
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * LexBIG Bug #23103 -
 * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=23103&group_id=491&atid=1850
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge23103 extends LexBIGServiceTestCase {
    
    /** The Constant testID. */
    final static String testID = "GForge23103";
    
    /** The lbs. */
    private LexBIGService lbs;
    
    /** The test entity. */
    private Entity testEntity;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        lbs = ServiceHolder.instance().getLexBIGService();  
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AUTO_SCHEME, null);
        
        cns.restrictToCodes(Constructors.createConceptReferenceList("005"));
        
        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue(rcrl.length == 1);
        
        testEntity = rcrl[0].getEntity();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return testID;
    }
   
    /**
     * Test entity not null.
     * 
     * @throws LBException the LB exception
     */
    public void testEntityNotNull() throws LBException {
       assertNotNull(testEntity);
    }
    
    /**
     * Test source count.
     */
    public void testSourceCount(){
        Presentation pres = getPreferredPresentation(testEntity.getPresentation());
        assertTrue("Length: " + pres.getSource().length, pres.getSource().length == 2);  
    }
    
    /**
     * Test source content.
     */
    public void testSourceContent(){
        Presentation pres = getPreferredPresentation(testEntity.getPresentation());
        Source[] sources = pres.getSource();
        for(Source source : sources){
            assertTrue(
                    sourceEquals(source, "lexgrid.org", "sampleSource", "sampleSubRef1") ||
                    sourceEquals(source, "lexgrid.org", "sampleSource", "sampleSubRef2")
            );
        }
    }
    
    /**
     * Source equals.
     * 
     * @param source the source
     * @param content the content
     * @param role the role
     * @param subRef the sub ref
     * 
     * @return true, if successful
     */
    protected boolean sourceEquals(Source source, String content, String role, String subRef){
        return source.getContent().equals(content) &&
        source.getRole().equals(role) &&
        source.getSubRef().equals(subRef);
    }
    
    /**
     * Gets the preferred presentation.
     * 
     * @param presentations the presentations
     * 
     * @return the preferred presentation
     */
    protected Presentation getPreferredPresentation(Presentation[] presentations){
        for(Presentation pres : presentations){
            if(pres.isIsPreferred()){
                return pres;
            }
        }
        throw new RuntimeException("Preferred Presentation not found.");
    }
}