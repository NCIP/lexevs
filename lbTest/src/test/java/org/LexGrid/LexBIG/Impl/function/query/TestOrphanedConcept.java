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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * This testcase checks that the hierarchy api works as desired.
 * 
 * @author Pradip Kanjamala
 * 
 */
public class TestOrphanedConcept extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_150";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(CELL_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(CELL_URN, csvt, hierarchyId);
        assertTrue(findMatchingConcept("CL:0000070", rcrl));

    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptUMLS() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AIR_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AIR_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(AIR_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length > 0);

    }

    /**
     * Test getting the root concept of when using the generic owl loader
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptGenericOwl() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AMINOACID_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AMINOACID_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(AMINOACID_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length > 0);

    }
    

    

    
    
    boolean findMatchingConcept(String code, ResolvedConceptReferenceList rcrl) {        
        for (ResolvedConceptReference rcr: rcrl.getResolvedConceptReference()) {
            if (code.equals(rcr.getConceptCode())) {                
                return true;
            }
        }
        return false;
    }
    
    
    
}