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

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class RestrictToAnonymousTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictToEntityTypesTest extends BaseCodedNodeGraphTest {

    public void testRestrictToAssociationType() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(CAMERA_SCHEME_MANIFEST_URN, null, null);
    	
    	cng = cng.restrictToEntityTypes(Constructors.createLocalNameList("association"));

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("focal-length", null, null), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        assertEquals(1,rcr[0].getSourceOf().getAssociation().length);
    }
 
// We no longer load entities of this type as invalid and as an association entity it
// it can't have other associations.  No longer a valid test.
//    public void testRestrictToAssociationTypeWithWrong() throws Exception {
//    	CodedNodeGraph cng = lbs.getNodeGraph(CAMERA_SCHEME_MANIFEST_URN, null, null);
//    	
//    	cng = cng.restrictToEntityTypes(Constructors.createLocalNameList("invalid"));
//
//        ResolvedConceptReference[] rcr = 
//            cng.resolveAsList(Constructors.createConceptReference("focal-length", null, null), 
//                    true, 
//                    false, 
//                    0, 
//                    1, 
//                    null, 
//                    null, 
//                    null, 
//                    -1).getResolvedConceptReference();
//        
//        assertEquals(1, 
//                rcr.length);
//        
//        assertEquals(rcr[0].getSourceOf().getAssociation().length, 1);
//    }
    
    public void testRestrictToEntityTypeForMappingScheme() throws Exception {
    	CodedNodeGraph cng = lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	cng = cng.restrictToEntityTypes(Constructors.createLocalNameList("concept"));

        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("C0001", AUTO_SCHEME, AUTO_SCHEME), 
                    true, 
                    false, 
                    0, 
                    1, 
                    null, 
                    null, 
                    null, 
                    -1).getResolvedConceptReference();
        
        assertEquals(1, 
                rcr.length);
        
        assertTrue(rcr[0].getSourceOf() != null);
    }
}