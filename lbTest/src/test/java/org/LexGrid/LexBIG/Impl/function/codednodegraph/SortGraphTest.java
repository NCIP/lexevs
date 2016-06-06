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
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class SortGraphTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class SortGraphTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
    public void testSortGNodeAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"code"}, new Boolean[]{true}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 2);
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(assocCons[0].getCode().equals("73"));
        assertTrue(assocCons[1].getCode().equals("Chevy"));
    }
    
    public void testSortGNodeDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"code"}, new Boolean[]{false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 2);
      
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(assocCons[0].getCode().equals("Chevy"));
        assertTrue(assocCons[1].getCode().equals("73"));
    }
    
    public void testSortGAssociationAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount"}, new Boolean[]{true}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
        		assocs[0].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("uses"));
        
    }
    
    public void testSortGAssociationDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount"}, new Boolean[]{false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("uses"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("hasSubtype"));
        
    }
    
    public void testTwoSortsAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount", "code"}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("uses"));
        
        assertTrue("Found Code: " + assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode(),
                assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("C0001"));
        
        assertTrue(assocs[1].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("Batteries"));      
    }
    
    public void testTwoSortsDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount", "code"}, new Boolean[]{false, false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("uses"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Code: " + assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode(),
                assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("Tires"));
        
        assertTrue(assocs[1].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("T0001"));      
    }
}