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
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

/**
 * The Class TestSameCodeDifferentNamespace.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class TestSameCodeDifferentNamespace extends BaseCodedNodeSetTest{

    /**
     * Test no restrictions.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testNoRestrictions() throws LBInvocationException, LBParameterException{
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    }
    
    /**
     * Test restrict to codes no namespace size.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesNoNamespaceSize() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 3);
    }
    
    /**
     * Test restrict to codes no namespace if namespace is correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesNoNamespaceIfNamespaceIsCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    } 
    
    /**
     * Test restrict to codes no namespace if properties count are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesNoNamespaceIfPropertiesCountAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 
    
    /**
     * Test restrict to codes no namespace if properties types are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesNoNamespaceIfPropertiesTypesAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue(prop instanceof Presentation);
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue(prop instanceof Presentation);
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 
    
    /**
     * Test restrict to codes no namespace if properties values are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesNoNamespaceIfPropertiesValuesAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue("Value: " + prop.getValue().getContent(), prop.getValue().getContent().equals("Concept for testing same code but different Namespace - 1"));
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue("Value: " + prop.getValue().getContent(), prop.getValue().getContent().equals("Concept for testing same code but different Namespace - 2"));
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 

    /**
     * Test restrict to codes with namespace1.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesWithNamespace1() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept",
                AUTO_SCHEME, AUTO_SCHEME));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 1);
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
    }
    
    /**
     * Test restrict to codes with namespace2.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void testRestrictToCodesWithNamespace2() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept",
                "TestForSameCodeNamespace", AUTO_SCHEME));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 1);
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    }
}