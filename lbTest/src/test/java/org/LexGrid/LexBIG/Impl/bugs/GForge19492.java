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
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
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
public class GForge19492 extends LexBIGServiceTestCase {
    final static String testID = "GForge19492";

    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();   
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #19492 -
     * https://gforge.nci.nih.gov/tracker/?func=detail&aid=19492&group_id=491&atid=1850
     */
    public void testResolveGraphAcrossCodingSchemeBounary() throws LBException {
        CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.AUTO_SCHEME, null, null);
        ConceptReference ref = ConvenienceMethods.createConceptReference("Ford", AUTO_SCHEME);
        
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
        
        ResolvedConceptReference ford = rcr[0];
        
        assertTrue(ford.getCode().equals("Ford"));
        assertTrue(ford.getCodingSchemeName().equals(AUTO_SCHEME));
        
        Association[] fordAssocs = ford.getSourceOf().getAssociation();
        
        //this is the source of two Associations
        assertTrue(fordAssocs.length == 3);
        
        //we want the 'uses' association
        Association assoc =  null;
        for(Association foundAssoc : fordAssocs){
            if(foundAssoc.getAssociationName().equals("uses")){
                assoc = foundAssoc;
            }
        }
        //verify we found it - and its correct
        assertTrue(assoc != null);
        assertTrue(assoc.getAssociationName().equals("uses"));
        
        AssociatedConcept[] associatedConcepts = assoc.getAssociatedConcepts().getAssociatedConcept();  
        assertTrue(associatedConcepts.length == 1); 
        AssociatedConcept r0001 = associatedConcepts[0];
        
        assertTrue(r0001.getCode().equals("R0001"));
        assertTrue(r0001.getCodingSchemeName().equals(LexBIGServiceTestCase.PARTS_SCHEME));
        assertTrue(r0001.getCodeNamespace().equals(LexBIGServiceTestCase.PARTS_NAMESPACE));
    }
}