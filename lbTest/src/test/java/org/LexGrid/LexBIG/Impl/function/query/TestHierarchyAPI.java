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

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;

import java.util.Iterator;

/**
 * This testcase checks that the hierarchy api works as desired.
 * 
 * @author Pradip Kanjamala
 * 
 */
public class TestHierarchyAPI extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_50";

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
    public void testFindRootConceptOBO() throws InterruptedException, LBException {
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

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyRoots(CELL_URN, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        
        assertEquals(1,rcr.length);
        assertTrue(rcr[0].getConceptCode().equals("CL:0000000"));

    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testFindRootConceptUMLS() throws InterruptedException, LBException {
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

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyRoots(AIR_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue("Length: " + rcr.length, rcr.length > 0);

    }

    /**
     * Test getting the root concept of when using the generic owl loader
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testFindRootConceptGenericOwl() throws InterruptedException, LBException {
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

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyRoots(AMINOACID_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length > 0);

    }
    
    /**
     * Test getting the next level count for a concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetHierarchyPathToRootOBO() throws InterruptedException, LBException {
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
        String  code= "CL:0000001";
        AssociationList associations = lbscm.getHierarchyPathToRoot(CELL_URN, csvt, hierarchyId, code, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);
        associations = lbscm.getHierarchyPathToRoot(CELL_URN, csvt, hierarchyId, code, null, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);

    }

    /**
     * Test getting the next level count for a concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetHierarchyPathToRootLexGridXMLWithCycle() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AUTO_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AUTO_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        String  code= "B";
        AssociationList associations = lbscm.getHierarchyPathToRoot(AUTO_URN, csvt, hierarchyId, code, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);
        associations = lbscm.getHierarchyPathToRoot(AUTO_URN, csvt, hierarchyId, code, null, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);

    }    
    
    public void testGetHierarchyPathToRootFromExtension() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AUTO_EXTENSION_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AUTO_EXTENSION_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        String  code= "DeVille";
        
        AssociationList associations = lbscm.getHierarchyPathToRoot(AUTO_EXTENSION_URN, csvt, hierarchyId, code, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);
        associations = lbscm.getHierarchyPathToRoot(AUTO_EXTENSION_URN, csvt, hierarchyId, code, null, false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);
    }    
    
    public void testGetHierarchyPathToRootFromExtensionWithNamespace() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(NPO_SCHEME_MULTI_NAMESPACE_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(NPO_SCHEME_MULTI_NAMESPACE_URL, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("is_a"))
                hierarchyId = hierarchy;
        }
        String  code= "NPO_1623";
        
        AssociationList associations = lbscm.getHierarchyPathToRoot(NPO_SCHEME_MULTI_NAMESPACE, csvt, hierarchyId, code, "GO", false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associations.getAssociation().length > 0);
        assertTrue(associations.getAssociation()[0].getAssociatedConcepts().getAssociatedConcept(0).getCode().equals("GO_0048518"));
        AssociationList associationsNPO = lbscm.getHierarchyPathToRoot(NPO_SCHEME_MULTI_NAMESPACE, csvt, hierarchyId, code, "npo", false, LexBIGServiceConvenienceMethods.HierarchyPathResolveOption.ALL, null);
        assertTrue(associationsNPO.getAssociation().length > 0);
        assertTrue(associationsNPO.getAssociation()[0].getAssociatedConcepts().getAssociatedConcept(0).getCode().equals("GO_0008156"));

    } 
    /**
     * Test getting the next level count for a concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountNextForOBOConcept() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(CELL_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        ConceptReference cr= ConvenienceMethods.createConceptReference("CL:0000000", CELL_URN);
        
        int count = lbscm.getHierarchyLevelNextCount(CELL_URN, csvt, hierarchyId, cr);
        assertTrue(count== 2);

    }

    
    
    /**
     * Test getting the previous level count for a concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountPrevForOBOConcept() throws InterruptedException, LBException {
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
        //The prev of root, shouldn't have any parents
        ConceptReference cr= ConvenienceMethods.createConceptReference("CL:0000000", CELL_URN);       
        int count = lbscm.getHierarchyLevelPrevCount(CELL_URN, csvt, hierarchyId, cr);
        assertTrue(count== 0);
        
        //This prev of concept next to root, should have only 1 parent
        cr= ConvenienceMethods.createConceptReference("CL:0000003", CELL_URN);        
        count = lbscm.getHierarchyLevelPrevCount(CELL_URN, csvt, hierarchyId, cr);
        assertTrue(count== 1);
        
        //This orphaned concept shouldn't have any previous
        cr= ConvenienceMethods.createConceptReference("CL:0000070", CELL_URN);        
        count = lbscm.getHierarchyLevelPrevCount(CELL_URN, csvt, hierarchyId, cr);
        assertTrue(count== 0);

    }
    
    
    /**
     * Test getting the next level count for a non existent concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountNextForNonExistentOBOConcept() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(CELL_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        ConceptReference cr= ConvenienceMethods.createConceptReference("NONEXISTANT", CELL_URN);
        
        int count = lbscm.getHierarchyLevelNextCount(CELL_URN, csvt, hierarchyId, cr);
        assertTrue(count== 0);
    }    
    /**
     * Test getting the previous and next level count for a UMLS concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountForUMLSConcept() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AIR_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        
        ConceptReference cr= ConvenienceMethods.createConceptReference("U000069", AIR_SCHEME);       
        int count = lbscm.getHierarchyLevelNextCount(AIR_SCHEME, csvt, hierarchyId, cr);
        assertTrue("Count: " + count, count== 1);
        
     
        cr= ConvenienceMethods.createConceptReference("MFSPI", AIR_SCHEME);       
        count = lbscm.getHierarchyLevelPrevCount(AIR_SCHEME, csvt, hierarchyId, cr);
        assertTrue(count== 1);
        
        cr= ConvenienceMethods.createConceptReference("U000055", AIR_SCHEME);       
        count = lbscm.getHierarchyLevelNextCount(AIR_SCHEME, csvt, hierarchyId, cr);
        assertTrue(count== 9);

       

    }

    /**
     * Test getting the previous and next level count for a OWL concept
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountForOWLConcept() throws InterruptedException, LBException {
      
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AMINOACID_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        
        ConceptReference cr= ConvenienceMethods.createConceptReference("RefiningFeature", AMINOACID_SCHEME);       
        int count = lbscm.getHierarchyLevelNextCount(AMINOACID_SCHEME, csvt, hierarchyId, cr);
        assertEquals(5,count);
        
        //This prev of concept of root, should have no parent
        cr= ConvenienceMethods.createConceptReference("RefiningFeature", AMINOACID_SCHEME);       
        count = lbscm.getHierarchyLevelPrevCount(AMINOACID_SCHEME, csvt, hierarchyId, cr);
        assertTrue(count== 0);


    }    
    
    
    /**
     * Test getting the previous and next level count for a list of concepts
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountForOBOConceptList() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(CELL_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        ConceptReferenceList crl= ConvenienceMethods.createConceptReferenceList(new String[] {"CL:0000000", "CL:0000003", "CL:0000070"}, CELL_URN);
        
        ConceptReferenceList countList = lbscm.getHierarchyLevelNextCount(CELL_URN, csvt, hierarchyId, crl);
        int count= findMatchingConcept("CL:0000000", countList);
        assertEquals(2,count);
        
        count= findMatchingConcept("CL:0000003", countList);
        assertTrue(count== 2);
        
        //This orphaned concept shouldn't have any previous
        count= findMatchingConcept("CL:0000070", countList);
        assertTrue(count== 0);
        
        countList = lbscm.getHierarchyLevelPrevCount(CELL_URN, csvt, hierarchyId, crl);
        count= findMatchingConcept("CL:0000000", countList);
        assertTrue(count== 0);
        
        count= findMatchingConcept("CL:0000003", countList);
        assertTrue(count== 1);
        
        //This orphaned concept shouldn't have any previous
        count= findMatchingConcept("CL:0000070", countList);
        assertTrue(count== 0);    
        
        
      

    }   
    
    /**
     * Test getting the previous and next level count for a very list of concepts
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testGetCountForLongConceptList() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
       
        String codingschemeURI=CELL_URN;
        CodingSchemeVersionOrTag csvt = null;
        String[] hierarchyIDs = lbscm.getHierarchyIDs(codingschemeURI, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }
        
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingschemeURI, csvt);
        ResolvedConceptReference[] rcrs= cns.resolveToList(null, null, null, null, false, 2500).getResolvedConceptReference();
        System.out.println("Length of array="+rcrs.length);
        ConceptReferenceList crl= new ConceptReferenceList();
        crl.setConceptReference(rcrs);
        ConceptReferenceList countList = lbscm.getHierarchyLevelNextCount(codingschemeURI, csvt, hierarchyId, crl);
        int count= findMatchingConcept("CL:0000136", countList);
        assertTrue(count== 3); 
       

    }     
    
    
    int findMatchingConcept(String code, ConceptReferenceList crl) {
        Iterator<? extends ConceptReference> i= crl.iterateConceptReference();
        while (i.hasNext()) {
            ConceptReference cr= i.next();
            if (code.equals(cr.getConceptCode())) {
                CountConceptReference ccr= (CountConceptReference) cr;
                return ccr.getChildCount();
            }
        }
        return 0;
    }
    
    
    
}