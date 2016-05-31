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
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class RestrictToDirectionalNamesTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class RestrictToDirectionalNamesTest extends BaseCodedNodeGraphTest {

    /**
     * Test one directional name null qualifiers.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testOneDirectionalNameNullQualifiers() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToDirectionalNames(Constructors.createNameAndValueList("uses"), 
                null);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
     
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference ref = rcr[0];
        
        Association[] assocs = ref.getSourceOf().getAssociation();
        
        assertEquals(1,assocs.length);
        
        assertEquals("uses", assocs[0].getAssociationName());
        
        AssociatedConcept[] assocCons = assocs[0].getAssociatedConcepts().getAssociatedConcept();
        
        assertTrue(assocCons.length == 3);
        
        assertTrue(associatedConceptListContains(assocCons, "Batteries"));
        assertTrue(associatedConceptListContains(assocCons, "Tires"));
        assertTrue(associatedConceptListContains(assocCons, "Brakes"));
    }
    
    /**
     * Test one directional name null qualifiers no match.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testOneDirectionalNameNullQualifiersNoMatch() {
        try {
            cng = cng.restrictToDirectionalNames(Constructors.createNameAndValueList("NOT_A_MATCH"),
			        null);
		} catch (LBInvocationException e) {
			fail();
		} catch (LBParameterException e) {
			return;
		}
        
      fail();
    }
    
    /**
     * Test two directional names null qualifiers.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testTwoDirectionalNamesNullQualifiers() throws LBInvocationException, LBParameterException{
        cng.restrictToDirectionalNames(Constructors.createNameAndValueList(new String[]{"uses", "hasSubtype"}),
                null);
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
     
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference ref = rcr[0];
        
        Association[] assocs = ref.getSourceOf().getAssociation();
        
        assertEquals(2,assocs.length);
        assertTrue(associationListContains(assocs, "uses"));
        assertTrue(associationListContains(assocs, "hasSubtype"));

        
        AssociatedConcept[] assocCons1 = assocs[0].getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] assocCons2 = assocs[1].getAssociatedConcepts().getAssociatedConcept();
        
        AssociatedConcept[] all = new AssociatedConcept[assocCons1.length + assocCons2.length];

        
        System.arraycopy(assocCons1, 0, all, 0, assocCons1.length);
        System.arraycopy(assocCons2, 0, all, assocCons1.length, assocCons2.length);
        
        assertTrue(all.length == 5);
        
        assertTrue(associatedConceptListContains(all, "Batteries"));
        assertTrue(associatedConceptListContains(all, "Tires"));
        assertTrue(associatedConceptListContains(all, "Brakes"));
        
        assertTrue(associatedConceptListContains(all, "T0001"));
        assertTrue(associatedConceptListContains(all, "C0001"));     
    }
    
    /**
     * Test one directional name one qualifier without value.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testOneDirectionalNameOneQualifierWithoutValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToDirectionalNames(Constructors.createNameAndValueList("hasSubtype"),
                Constructors.createNameAndValueList("hasEngine"));
        
        ResolvedConceptReference[] rcr =
            cng.resolveAsList(Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME),
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
     
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference ref = rcr[0];
        
        Association[] assocs = ref.getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 1);
        
        assertTrue(assocs[0].getAssociationName().equals("hasSubtype"));
        
        AssociatedConcept[] assocCons = assocs[0].getAssociatedConcepts().getAssociatedConcept();
        
        assertEquals(1,assocCons.length);
        
        assertTrue(associatedConceptListContains(assocCons, "C0001")); 
    }
    
    /**
     * Test one directional name one qualifier with value.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testOneDirectionalNameOneQualifierWithValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToDirectionalNames(Constructors.createNameAndValueList("hasSubtype"),
                Constructors.createNameAndValueList("hasEngine", "true"));
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
     
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference ref = rcr[0];

        Association[] assocs = ref.getSourceOf().getAssociation();

        assertTrue(assocs.length == 1);

        assertTrue(assocs[0].getAssociationName().equals("hasSubtype"));

        AssociatedConcept[] assocCons = assocs[0].getAssociatedConcepts().getAssociatedConcept();

        assertTrue(assocCons.length == 1);

        assertTrue(associatedConceptListContains(assocCons, "C0001")); 
    }

    /**
     * Test one directional name one qualifier with wrong value.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testOneDirectionalNameOneQualifierWithWrongValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToDirectionalNames(Constructors.createNameAndValueList("hasSubtype"),
                Constructors.createNameAndValueList("hasEngine", "false"));

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();

        assertEquals(1,rcr.length);
        assertNull(rcr[0].getSourceOf());
        assertNull(rcr[0].getTargetOf());
    }
}