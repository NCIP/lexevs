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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class MultipeRestrictionsTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "MultipeRestrictionsTest Tests";
    }

    public void testRestrictDesignationAndHasPropertyType() throws LBException{
        cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		null, 
        		null, 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictDesignationAndDefinition() throws LBException{
        cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "exactMatch", null);
        cns = cns.restrictToMatchingProperties(null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		"An automobile", 
        		"exactMatch", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictDesignationAndWrongDefinition() throws LBException{
        cns = cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToMatchingProperties(null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		"An automobileWRONG", 
        		"exactMatch", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0); 
    }
    
    public void testRestrictDesignationAndSource() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
     
    public void testRestrictDesignationAndSourceAndActive() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndWrongPresentation() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        cns = cns.restrictToMatchingDesignations("WRONG__DONT_MATCH_ANYTHING", SearchDesignationOption.ALL, "contains", null);
        
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0);      
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndCode() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("005"));
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndWrongCode() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns = cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("WRONG_CODE"));
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0);
    }
}