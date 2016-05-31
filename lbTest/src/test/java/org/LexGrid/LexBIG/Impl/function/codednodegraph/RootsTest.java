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
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class RootsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class RootsTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
    public void testGetRoots() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(2,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "005");
    	assertNotNull(ref1);
    	ResolvedConceptReference ref2 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref2);
    }
    
    public void testGetRootsWithAssociationRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), null).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(2,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref1);
    	ResolvedConceptReference ref2 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "Ford");
    	assertNotNull(ref2);
    }
    
    public void testGetRootsWithAssociationAndQualifierRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), Constructors.createNameAndValueList("hasEngine", "true")).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref1);
    }
    
    public void testGetRootsWithAssociationAndWrongQualifierRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), Constructors.createNameAndValueList("hasEngine", "false")).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
}