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
package org.LexGrid.LexBIG.Impl.featureRequests;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge17019 extends LexBIGServiceTestCase {

    final static String testID = "GForge17019";

    private CodedNodeGraph cng;

    @Override
    protected String getTestID() {
        return testID;
    }
    
    
    public void testRoleGroupInMeta() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(SAMPLE_META_VERSION);
        cng = lbs.getNodeGraph(META_SCHEME, csvt, null);  
        cng.restrictToAssociations(Constructors.createNameAndValueList("RB", null), 
                Constructors.createNameAndValueList("RG", "testRG"));
        ConceptReference cr = Constructors.createConceptReference("C0000005", META_SCHEME);
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(cr, false, true, 1, 1, null, null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount()>0);   
        ResolvedConceptReference[] rcrList = rcrl.getResolvedConceptReference();
        ArrayList<String> al = new ArrayList<String>();

        for(ResolvedConceptReference rcr: rcrList){
            AssociationList targetof = rcr.getTargetOf();
            Association[] associations = targetof.getAssociation();
            for (int i = 0; i < associations.length; i++) {
                Association assoc = associations[i];
                AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                for (int j = 0; j < acl.length; j++) {
                    AssociatedConcept ac = acl[j];
                    al.add(ac.getCode());
                }
            }
        }
        assertTrue(al.contains("C0036775"));
    }
    
    public void testBadRoleGroupInMeta() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(SAMPLE_META_VERSION);
        cng = lbs.getNodeGraph(META_SCHEME, csvt, null);  
        cng.restrictToAssociations(Constructors.createNameAndValueList("RB", null), 
                Constructors.createNameAndValueList("RG", "Bad_Role_Group"));
        ConceptReference cr = Constructors.createConceptReference("C0000005", META_SCHEME);
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(cr, true, true, 1, 1, null, null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount()>0);   
        ResolvedConceptReference[] rcrList = rcrl.getResolvedConceptReference();
        
        assertTrue(rcrList.length == 1);
        
        assertNull(rcrList[0].getSourceOf());
    }
}
