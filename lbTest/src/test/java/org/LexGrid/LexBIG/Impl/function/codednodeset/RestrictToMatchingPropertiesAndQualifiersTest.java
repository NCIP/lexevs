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
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class RestrictToMatchingPropertiesAndQualifiersTest extends LexBIGServiceTestCase {
    /** The cns. */
    protected CodedNodeSet cns;
    
    /** The lbs. */
    protected LexBIGService lbs;
 
	protected String getTestID() {
        return "RestrictToMatchingPropertiesAndQualifiers Tests";
    }

    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp(){
        try {
            lbs = ServiceHolder.instance().getLexBIGService();
            cns = lbs.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
            		Constructors.createCodingSchemeVersionOrTagFromVersion(
            				OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        } catch (LBException e) {
          fail(e.getMessage());
        }
    }
      
    


    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameAndValueMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("comment", "per discussion with Barry Smith"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameMatchMiddleNameAndValue() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("has axiom label", "http://purl.obolibrary.org/obo/bfo/axiom/0000004"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameMatchLastNameAndValue() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("seeAlso", "http://www.referent-tracking.com/_RTU/papers/CeustersICbookRevised.pdf"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
    
    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameMatch() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("comment"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameMatchMiddleName() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("has axiom label"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
    
    @Test
    public void testRestrictToMatchingPropertiesQualifierNameMatchLastName() throws LBException{
        cns = cns.restrictToMatchingProperties(
        		Constructors.createLocalNameList("IAO_0000116"), 
        		new PropertyType[]{PropertyType.GENERIC}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("seeAlso"), 
        		"entity", 
        		"contains", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertEquals(rcrl.getResolvedConceptReferenceCount(),1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("BFO_0000001"));
    }
}