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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class FocusTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class FocusTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
    public void testFocusWithJustCode() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
    public void testFocusWithWrongCode() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("WRONG_CODE");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
    public void testFocusWithCodeAndNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
    public void testFocusWithCodeAndNonExistentNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Not_a_real_namespace");
    	
    	try {
			ResolvedConceptReferenceList roots = 
				this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
		} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
    public void testFocusWithCodeAndWrongNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("GermanMadePartsNamespace");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
    public void testFocusWithCodeAndCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodingSchemeName("Automobiles");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }

    public void testFocusWithCodeAndNamespaceAndCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodingSchemeName("Automobiles");
    	ref.setCodeNamespace("Automobiles");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
    public void testFocusWithCodeAndNonExistentNamespaceAndCorrectCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Not_a_real_namespace");
    	ref.setCodingSchemeName("Automobiles");
    	
    	try {
			ResolvedConceptReferenceList roots = 
				this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
		} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	try {
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
    public void testFocusWithCodeAndCorrectNamespaceAndCorrectCodingSchemeWithUnion() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("E0001");
    	ref.setCodeNamespace("GermanMadePartsNamespace");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);
  
    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("E0001",roots.getResolvedConceptReference(0).getCode());
    }
    
    public void testFocusWithCodeAndCorrectNamespaceAndCorrectCodingSchemeWithUnionOtherCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("A0001");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("Automobiles");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);
  
    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("A0001",roots.getResolvedConceptReference(0).getCode());
    }
    
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingSchemeWithUnion() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);

    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingSchemeWithIntersection() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.intersect(cng);

    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
}