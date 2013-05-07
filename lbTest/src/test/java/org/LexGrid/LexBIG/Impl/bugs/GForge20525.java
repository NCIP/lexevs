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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge20525 extends LexBIGServiceTestCase {
    final static String testID = "GForge20525";
    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();  
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #20525 -
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=20525&group_id=491&atid=1850
     */
    public void testResolveGraphOrphanedConcept() throws LBException {
        CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.AUTO_SCHEME, null, null);
        ConceptReference ref = ConvenienceMethods.createConceptReference("NoRelationsConcept", AUTO_SCHEME);
        
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(ref, 
                true, 
                false, 
                10, 
                10, 
                null, 
                null, 
                null, 
                10);
        
        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
        
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference norels = rcr[0];
        
        assertTrue(norels.getCode().equals("NoRelationsConcept"));
        assertTrue(norels.getCodingSchemeName().equals(AUTO_SCHEME));
        
        assertNull(norels.getSourceOf());
        assertNull(norels.getTargetOf());   
    }
}