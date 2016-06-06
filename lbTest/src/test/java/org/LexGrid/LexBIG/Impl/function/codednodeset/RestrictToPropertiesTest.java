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
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

@RemoteApiSafeTest
public class RestrictToPropertiesTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToProperties Tests";
    }

    public void testRestrictToProperty() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("textualPresentation"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToPropertyDefinition() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("definition"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToPropertyTwo() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"definition", "textualPresentation"}), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToPropertyTwoSeperate() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"definition"}), null);
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"textualPresentation"}), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToWrongProperty() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("73"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("definition"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);  
    }
}