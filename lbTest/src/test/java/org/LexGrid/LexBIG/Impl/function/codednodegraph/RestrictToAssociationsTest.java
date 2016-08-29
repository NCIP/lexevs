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

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RestrictToAssociationsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RestrictToAssociationsTest extends BaseCodedNodeGraphTest {

    /**
     * Test one directional name null qualifiers.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
	@Test
    public void testOneAssociationNameNullQualifiers() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), 
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
        
        assertTrue(assocs.length == 1);
        
        assertTrue(assocs[0].getAssociationName().equals("uses"));
    }
    
    /**
     * Test one directional name null qualifiers no match.
     * @throws LBParameterException 
     * @throws LBInvocationException 
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
	@Test
    public void testOneAssociationNameNullQualifiersNoMatch() throws Exception{

    	cng = cng.restrictToAssociations(Constructors.createNameAndValueList("NOT_A_MATCH"), 
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

    	assertEquals(1,rcr.length);
        assertNull(rcr[0].getSourceOf());
        assertNull(rcr[0].getTargetOf());
    }
    
    /**
     * Test two directional names null qualifiers.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
	@Test
    public void testTwoAssociationNamesNullQualifiers() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList(new String[]{"uses", "hasSubtype"}),
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
        
        assertTrue(assocs.length == 2);
        assertTrue(associationListContains(assocs, "uses"));
        assertTrue(associationListContains(assocs, "hasSubtype"));

        
        AssociatedConcept[] assocCons1 = assocs[0].getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] assocCons2 = assocs[1].getAssociatedConcepts().getAssociatedConcept();
        
        AssociatedConcept[] all = new AssociatedConcept[assocCons1.length + assocCons2.length];

        
        System.arraycopy(assocCons1, 0, all, 0, assocCons1.length);
        System.arraycopy(assocCons2, 0, all, assocCons1.length, assocCons2.length);
        
        assertEquals(5, all.length);
        
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
	@Test
    public void testOneAssociationNameOneQualifierWithoutValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), 
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
        
        assertTrue("Length: " + assocCons.length, assocCons.length == 1);
        
        assertTrue(associatedConceptListContains(assocCons, "C0001")); 
    }
    
    /**
     * Test one directional name one qualifier with value.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
	@Test
    public void testOneAssociationNameOneQualifierWithValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), 
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
	@Test
    public void testOneAssociationNameOneQualifierWithWrongValue() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), 
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
	@Test
    public void testOneAssociationNameOneQualifierWithValueResolveAllQuals() throws LBInvocationException, LBParameterException{
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), 
                Constructors.createNameAndValueList("since", "1998"));
        
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("Ford", LexBIGServiceTestCase.AUTO_SCHEME), 
                    true, 
                    false, 
                    1, 
                    1, 
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

        assertTrue(associatedConceptListContains(assocCons, "Jaguar")); 
        
        NameAndValueList quals = assocCons[0].getAssociationQualifiers();
        
        assertTrue("Quals: " + quals.getNameAndValueCount(),
        		quals.getNameAndValueCount() == 2);
    }
}