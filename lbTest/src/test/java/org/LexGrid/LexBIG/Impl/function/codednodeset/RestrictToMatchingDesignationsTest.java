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
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class RestrictToMatchingDesignationsTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToMatchingDesignations Tests";
    }

    public void testRestrictToMatchingDesignationsNodeSet() throws LBException{
    	CodedNodeSet cns = lbs.getNodeSet(AUTO_SCHEME, null, null);
        cns = cns.restrictToMatchingDesignations("General Motors", SearchDesignationOption.ALL, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("GM"));
    }
     
    public void testRestrictToMatchingDesignationsALL() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.ALL, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictToMatchingDesignationsPreferredOnlyMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictToMatchingDesignationsPreferredOnlyNoMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("American", SearchDesignationOption.PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingDesignationsNonPreferredOnlyMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("American", SearchDesignationOption.NON_PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictToMatchingDesignationsNonPreferredOnlyNoMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.NON_PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingDesignationsLanguage() throws LBException{
        cns = cns.restrictToMatchingDesignations("Truck", SearchDesignationOption.ALL, "contains", "en");
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("T0001"));
    }
}