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

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.helpers.TestFilter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * The Class ResolveTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolveTest extends BaseCodedNodeSetTest {

	public void testResolveAllNodeSet() throws Exception{
		CodedNodeSet cns = lbs.getNodeSet(AUTO_SCHEME, null, null);
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertTrue(itr.numberRemaining() > 0);
	}

    /**
     * Test resolve all null1.
     * 
     * @throws Exception the exception
     */
    public void testResolveAllNull1() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
        assertTrue(itr.numberRemaining() > 0);
    }
    
    /**
     * Test resolve all null2.
     * 
     * @throws Exception the exception
     */
    public void testResolveAllNull2() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, null);
        assertTrue(itr.numberRemaining() > 0);
    }
    
    /**
     * Test number remaining.
     * 
     * @throws Exception the exception
     */
    public void testNumberRemaining() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, null);
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        assertTrue(itr.numberRemaining() == refs.length);
    }
 
    /**
     * Test resolve objects.
     * 
     * @throws Exception the exception
     */
    public void testResolveObjects() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, null, true);
        assertTrue(itr.hasNext());
        while(itr.hasNext()){
            ResolvedConceptReference ref = itr.next();
            assertTrue(ref.getEntity() != null);
        }
    }
    
    /**
     * Test dont resolve objects.
     * 
     * @throws Exception the exception
     */
    public void testDontResolveObjects() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, null, false);
        assertTrue(itr.hasNext());
        while(itr.hasNext()){
            ResolvedConceptReference ref = itr.next();
            assertTrue(ref.getEntity() == null);
        }
    }
    
    /**
     * Test sort.
     * 
     * @throws Exception the exception
     */
    public void testSort() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(Constructors.createSortOptionList(new String[]{"code"}), null, null);
        assertTrue(itr.hasNext());
        assertTrue(itr.next().getCode().equals("005"));  
    }
    
    /**
     * Test sort backwards.
     * 
     * @throws Exception the exception
     */
    public void testSortBackwards() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(Constructors.createSortOptionList(new String[]{"code"}, new Boolean[]{false}), null, null);
        assertTrue(itr.hasNext());
        assertTrue(itr.next().getCode().equals("T0001")); 
    }
   
    /**
     * Test two sorts.
     * 
     * @throws Exception the exception
     */
    public void testTwoSorts() throws Exception{
        ResolvedConceptReferencesIterator itr = cns.resolve(Constructors.createSortOptionList(new String[]{"codeSystem", "code"}, new Boolean[]{true,true}), null, null);
        assertTrue(itr.hasNext());
        assertTrue(itr.next().getCode().equals("005"));  
    }
    
    /**
     * Test multiple code system sort.
     * 
     * @throws Exception the exception
     */
    public void testMultipleCodeSystemSort() throws Exception{
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(PARTS_SCHEME, null);
        
        CodedNodeSet union = cns.union(cns2);
        
        union.restrictToMatchingDesignations("(rims^5 OR automobile^3 OR truck^4 OR piston^2 )", SearchDesignationOption.ALL, "LuceneQuery", null);

        ResolvedConceptReferencesIterator itr = union.resolve(
                Constructors.createSortOptionList(new String[]{"matchToQuery"}), null, null);
        
       String firstCode = itr.next().getCode();
       String secondCode = itr.next().getCode();
       String thirdCode = itr.next().getCode();
       String fourthCode = itr.next().getCode();
        
       assertTrue(firstCode, firstCode.equals("R0001"));
       assertTrue(secondCode, secondCode.equals("T0001"));   
       assertTrue(thirdCode, thirdCode.equals("A0001"));
       assertTrue(fourthCode, fourthCode.equals("P0001"));
       
       assertFalse(itr.hasNext());
     }
    
    /**
     * Test multiple code system sort different order.
     * 
     * @throws Exception the exception
     */
    public void testMultipleCodeSystemSortDifferentOrder() throws Exception{
        
        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(PARTS_SCHEME, null);
        
        CodedNodeSet union = cns2.union(cns);
        
        union.restrictToMatchingDesignations("( automobile OR truck^4 OR piston OR rims^5 )", SearchDesignationOption.ALL, "LuceneQuery", null);

        ResolvedConceptReferencesIterator itr = union.resolve(
                Constructors.createSortOptionList(new String[]{"matchToQuery"}), null, null);

       assertEquals("R0001", itr.next().getCode());
       assertEquals("T0001", itr.next().getCode());   
     }
    
//    /**
//     * Test multiple code system sort more resuts.
//     * 
//     * @throws Exception the exception
//     */
//    public void testMultipleCodeSystemSortMoreResuts() throws Exception{
//        
//        CodedNodeSet cns2 = lbs.getCodingSchemeConcepts(PARTS_SCHEME, null);
//        
//        CodedNodeSet union = cns2.union(cns);
//        
//        union.restrictToMatchingDesignations("(  an OR tires OR car OR automobile^4 OR truck^2 OR piston^3 OR rims^5 OR general )", SearchDesignationOption.ALL, "LuceneQuery", null);
//
//        ResolvedConceptReferencesIterator itr = union.resolve(
//                Constructors.createSortOptionList(new String[]{"matchToQuery"}), null, null);
//
//       assertEquals("R0001", itr.next().getCode());
//       assertEquals("A0001", itr.next().getCode());  
//       assertEquals("P0001", itr.next().getCode()); 
//       assertEquals("T0001",itr.next().getCode());   
//     }
    
    /**
     * Test filter options.
     * 
     * @throws LBException the LB exception
     */
    public void testFilterOptions() throws LBException{
        TestFilter filter = new TestFilter();

        try{
            filter.register();
        } catch (Exception e) {
            //if its already registered....
        }
        
        ResolvedConceptReferencesIterator itr = cns.resolve(null, Constructors.createLocalNameList("JUnit Test Filter"), null, null);
        
        for(int i=0;i<4;i++) {
        	itr.next();
        }
        
        assertFalse(itr.hasNext());
    }
    
    public void testFilterOptionsWithScroll() throws LBException{
        TestFilter filter = new TestFilter();

        try{
            filter.register();
        } catch (Exception e) {
            //if its already registered....
        }
        
        ResolvedConceptReferencesIterator itr = cns.resolve(null, Constructors.createLocalNameList("JUnit Test Filter"), null, null);
        
        itr.scroll(4);
        
        assertFalse(itr.hasNext());
    }
    
    /**
     * Test resolve property names presentation.
     * 
     * @throws Exception the exception
     */
    public void testResolvePropertyNamesPresentation() throws Exception {
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, Constructors.createLocalNameList("textualPresentation"), null, true);  
        
        ResolvedConceptReference ref = itr.next();
        assertFalse(itr.hasNext());
        
        assertTrue(ref.getEntity() != null);
        
        assertTrue(ref.getEntity().getPresentation().length == 1);
        
        assertTrue(ref.getEntity().getDefinition().length == 0);
    }
   
    /**
     * Test resolve property names definition.
     * 
     * @throws LBException the LB exception
     */
    public void testResolvePropertyNamesDefinition() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, Constructors.createLocalNameList("definition"), null, true);     
        
        ResolvedConceptReference ref = itr.next();
        
        assertFalse(itr.hasNext());
        
        assertTrue(ref.getEntity() != null);
        
        assertTrue(ref.getEntity().getPresentation().length == 0);
        
        assertTrue(ref.getEntity().getDefinition().length == 1);
    }
    
    /**
     * Test resolve property types presentation.
     * 
     * @throws LBException the LB exception
     */
    public void testResolvePropertyTypesPresentation() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, new PropertyType[]{PropertyType.PRESENTATION}, true);

        ResolvedConceptReference ref = itr.next();
        
        assertFalse(itr.hasNext());
        
        assertTrue(ref.getEntity() != null);
        
        assertTrue(ref.getEntity().getPresentation().length == 1);
        
        assertTrue(ref.getEntity().getDefinition().length == 0);
    }
   
    /**
     * Test resolve property types definition.
     * 
     * @throws LBException the LB exception
     */
    public void testResolvePropertyTypesDefinition() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null, new PropertyType[]{PropertyType.DEFINITION}, true);

        ResolvedConceptReference ref = itr.next();
        
        assertFalse(itr.hasNext());
        
        assertTrue(ref.getEntity() != null);
        
        assertTrue(ref.getEntity().getPresentation().length == 0);
        
        assertTrue(ref.getEntity().getDefinition().length == 1);
    }
    
    public void testResolveEntityTypes() throws LBException{
    	CodedNodeSet nodes = lbs.getNodeSet(AUTO_SCHEME, null, null);
    	nodes = nodes.restrictToCodes(Constructors.createConceptReferenceList("005"));
        ResolvedConceptReferencesIterator itr = nodes.resolve(null, null, null);

        ResolvedConceptReference ref = itr.next();
        
        assertTrue(ref.getEntityType().length > 0);
        assertTrue(ref.getEntityType(0).equals("concept"));
    }
    
    public void testResolveEntityTypeConcepts() throws LBException{
    	CodedNodeSet nodes = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
    	nodes = nodes.restrictToCodes(Constructors.createConceptReferenceList("005"));
        ResolvedConceptReferencesIterator itr = nodes.resolve(null, null, null);

        ResolvedConceptReference ref = itr.next();
        
        assertTrue(ref.getEntityType().length > 0);
        assertTrue(ref.getEntityType(0).equals("concept"));
    }
    
    public void testResolveEntityTypeValueDomain() throws LBException{
    	CodedNodeSet nodes = lbs.getNodeSet(AUTO_SCHEME, null, null);
    	nodes = nodes.restrictToCodes(Constructors.createConceptReferenceList("VD005"));
        ResolvedConceptReferencesIterator itr = nodes.resolve(null, null, null);

        ResolvedConceptReference ref = itr.next();
        
        assertTrue(ref.getEntityType().length > 0);
        assertTrue(ref.getEntityType(0).equals("valueDomain"));
    }
    
    public void testResolveEntityTypeAssociation() throws LBException{
    	CodedNodeSet nodes = lbs.getNodeSet(AUTO_SCHEME, null, null);
    	nodes = nodes.restrictToCodes(Constructors.createConceptReferenceList("AssocEntity"));
        ResolvedConceptReferencesIterator itr = nodes.resolve(null, null, null);

        ResolvedConceptReference ref = itr.next();
        
        assertTrue(ref.getEntityType().length > 0);
        assertTrue(ref.getEntityType(0).equals("association"));
    }
}