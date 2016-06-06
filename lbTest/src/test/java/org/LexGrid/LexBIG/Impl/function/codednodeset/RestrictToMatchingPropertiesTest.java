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
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class RestrictToMatchingPropertiesTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToMatchingProperties Tests";
    }

    public void testRestrictToMatchingPropertiesError() throws LBInvocationException {

        try {
            cns = cns.restrictToMatchingProperties(null, null, "Domestic", "contains", null);
            ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        } catch (LBParameterException e) {
            //this is good - pass the test.
            return;
        }

        fail("Didn't throw an Exception with no PropertyNames or PropertyTypes");
      
    }
    
    public void testRestrictToMatchingPropertiesPropertyNameMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(Constructors.createLocalNameList("definition"), null, "an automobile", "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertyNameNoMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(Constructors.createLocalNameList("textualPresentation"), null, "An", "startsWith", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingPropertiesPropertyTypeMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.DEFINITION}, "An", "startsWith", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertyTypeNoMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, "An", "startsWith", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingPropertiesPropertyGenericProperty() throws LBException{
        cns = cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.GENERIC}, "A Generic Property", "exactMatch", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertySearchAllProperties() throws LBException{
    	PropertyType[] types = new PropertyType[]{PropertyType.GENERIC, PropertyType.COMMENT, PropertyType.DEFINITION, PropertyType.PRESENTATION};
    	
        cns = cns.restrictToMatchingProperties(null, types, "A Generic Property", "exactMatch", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
}