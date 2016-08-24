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
package org.LexGrid.LexBIG.Impl;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Intersect;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToAnonymous;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToEntityTypes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingDesignations;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Union;
import org.LexGrid.LexBIG.Impl.helpers.TestFilter;
import org.LexGrid.LexBIG.Impl.helpers.TestFilter2;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;

/**
 * JUnit Tests for the CodedNodeSetImpl
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodedNodeSetImplTest extends TestCase {
    
    private LexBIGService lbsi;
    
    public void setUp(){
     	lbsi = ServiceHolder.instance().getLexBIGService();
    }

    public void testOptimizePendingOpsOrder() throws Exception {
    	/*
       
        CodedNodeSet cns = lbsi.getCodingSchemeConcepts("Automobiles", null);

        cns.restrictToMatchingDesignations("GM", null, "startsWith", null);

        CodedNodeSet cns2 = lbsi.getCodingSchemeConcepts("Automobiles", null);

        cns2.restrictToMatchingProperties(null, new PropertyType[] { PropertyType.PRESENTATION }, "GM", "startsWith",
                null);

        cns.union(cns2);

        cns.restrictToProperties(null, new PropertyType[] { PropertyType.DEFINITION });

        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(4) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(5) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.size() == 6);

        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(3) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.size() == 4);

        ((CodedNodeSetImpl) cns).optimizePendingOpsOrder();

        // restrictions should be above union.
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(4) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(5) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.size() == 6);

        // cns2 shouldn't change, because it is cloned. - would be confusing if
        // it changed.
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(3) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.size() == 4);

        // but the one inside of the cns union op should have changed.
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(5))).getCodes()).pendingOperations_
                .get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(5))).getCodes()).pendingOperations_
                .get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(5))).getCodes()).pendingOperations_
                .get(3) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(5))).getCodes()).pendingOperations_
                .get(4) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(5))).getCodes()).pendingOperations_
                .size() == 5);

        // so far so good, lets make it more complex.

        CodedNodeSet cns3 = lbsi.getCodingSchemeConcepts("GermanMadeParts", null);
        cns3.restrictToMatchingDesignations("Piston", null, "startsWith", null);

        cns.union(cns3);

        cns.restrictToMatchingProperties(Constructors.createLocalNameList("textualPresentation"), null, "Engine",
                "contains", null);

        cns.intersect(new CodedNodeSetImpl("Automobiles", null, true));

        cns.restrictToProperties(Constructors.createLocalNameList("definition"), null);
        
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(4) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(5) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(6) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(7) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(8) instanceof Intersect);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(9) instanceof RestrictToProperties);

        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.size() == 10);

        ((CodedNodeSetImpl) cns).optimizePendingOpsOrder();

        // one of the restrictions should have moved up, but nothing changes
        // below the intersection.
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(4) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(5) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(6) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(7) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(8) instanceof Union);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(9) instanceof Intersect);


        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.size() == 10);

        // cns2 shouldn't change, because it is cloned. - would be confusing if
        // it changed.
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(3) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.size() == 4);

        // cns3 shouldn't change, because it is cloned. - would be confusing if
        // it changed.
        assertTrue(((CodedNodeSetImpl) cns2).pendingOperations_.get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) cns).pendingOperations_.get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) cns3).pendingOperations_.get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) cns3).pendingOperations_.size() == 4);

        // both existing unions should have had one restriction added.
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(3) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(4) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(5) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .get(6) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(7))).getCodes()).pendingOperations_
                .size() == 7);

        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .get(1) instanceof RestrictToEntityTypes);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .get(2) instanceof RestrictToAnonymous);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .get(3) instanceof RestrictToMatchingDesignations);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .get(4) instanceof RestrictToMatchingProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .get(5) instanceof RestrictToProperties);
        assertTrue(((CodedNodeSetImpl) ((Union) (((CodedNodeSetImpl) cns).pendingOperations_.get(8))).getCodes()).pendingOperations_
                .size() == 6);
        
        */
    }


    public void testResultLimit() throws LBException {

        CodedNodeSet cns = lbsi.getCodingSchemeConcepts("Automobiles", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 1).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
    }

    public void testFilters() throws LBException {

        CodedNodeSet cns = lbsi.getCodingSchemeConcepts("Automobiles", null);

        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        // no filters - length of 17
        assertEquals(18,rcr.length);

        try {
            TestFilter.register();
        } catch (LBParameterException e1) {
            // may have been registered by a previous test - thats ok.
        }

        rcr = cns.resolveToList(null, Constructors.createLocalNameList(TestFilter.name_), null, null, 0)
                .getResolvedConceptReference();
        // filter for second letter of entity description being 'a'- length of 2
        assertEquals(4,rcr.length);

        assertTrue(contains(rcr, "C0001", "Automobiles"));
        assertTrue(contains(rcr, "C0002", "Automobiles"));
        assertTrue(contains(rcr, "Jaguar", "Automobiles"));
        assertTrue(contains(rcr, "C0011(5564)", "Automobiles"));

        // test with iterator

        ResolvedConceptReferencesIterator rcri = cns.resolve(null, Constructors.createLocalNameList(TestFilter.name_),
                null, null);
        // filter for second letter of entity description being 'a'- length of 2
        assertTrue(rcri.numberRemaining() == -1); // number remaining isn't
                                                  // calculated with filters

        // there should be 2 results.

        assertTrue(rcri.hasNext());
        rcr = new ResolvedConceptReference[] { rcri.next() };
        assertTrue(contains(rcr, "C0001", "Automobiles") || 
        		contains(rcr, "Jaguar", "Automobiles") ||
                contains(rcr, "C0002", "Automobiles") ||
                contains(rcr, "C0011(5564)", "Automobiles"));
       
        assertTrue(rcri.hasNext());
        rcr = new ResolvedConceptReference[] { rcri.next() };
        assertTrue(contains(rcr, 
        		"C0001", "Automobiles") || 
        		contains(rcr, "Jaguar", "Automobiles") ||
                contains(rcr, "C0002", "Automobiles") ||
                contains(rcr, "C0011(5564)", "Automobiles"));
        
        assertTrue(rcri.hasNext());
        rcr = new ResolvedConceptReference[] { rcri.next() };
        assertTrue(contains(rcr, "C0001", "Automobiles") || 
        		contains(rcr, "Jaguar", "Automobiles") ||
                contains(rcr, "C0002", "Automobiles") ||
                contains(rcr, "C0011(5564)", "Automobiles"));
        
        assertTrue(rcri.hasNext());
        rcr = new ResolvedConceptReference[] { rcri.next() };
        assertTrue(contains(rcr, "C0001", "Automobiles") || 
        		contains(rcr, "Jaguar", "Automobiles") ||
                contains(rcr, "C0002", "Automobiles") ||
                contains(rcr, "C0011(5564)", "Automobiles"));

        // another call to next should return null - they all got removed by the
        // filter.
        try {
	        assertTrue(rcri.next() == null);
	
	        assertFalse(rcri.hasNext());
	        // yet another call should throw an exception
     
            rcri.next();
            fail("Didn't throw LBResourceUnavailableException");
        } catch (LBResourceUnavailableException e) {
            // expected path
        }

        // test with iterator and get(start, end) method. also sort, so we know
        // what order they should be in

        rcri = cns.resolve(Constructors.createSortOptionList(new String[] { "code" },
                new Boolean[] { new Boolean(true) }), Constructors.createLocalNameList(TestFilter.name_), null, null);
        // filter for second letter of entity description being 'a'- length of 2
        assertTrue(rcri.numberRemaining() == -1); // number remaining isn't
                                                  // calculated with filters

        // there should be 2 results.

        rcr = rcri.get(0, 1).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcrEquals(rcr[0], "C0001", "Automobiles"));
        
        rcr = rcri.get(1, 2).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcrEquals(rcr[0], "C0002", "Automobiles"));
        
        rcr = rcri.get(2, 3).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcrEquals(rcr[0], "C0011(5564)", "Automobiles"));

        rcr = rcri.get(3, 4).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcrEquals(rcr[0], "Jaguar", "Automobiles"));

        try {
            rcr = rcri.get(4, 5).getResolvedConceptReference();
            fail("Didn't throw LBParameterException");
        } catch (LBParameterException e) {
            // expected path
        }

        // go backwards
        rcr = rcri.get(0, 1).getResolvedConceptReference();
        assertTrue(rcr.length == 1);
        assertTrue(rcrEquals(rcr[0], "C0001", "Automobiles"));

        // get both
        rcr = rcri.get(0, 2).getResolvedConceptReference();
        assertTrue(rcr.length == 2);
        assertTrue(rcrEquals(rcr[0], "C0001", "Automobiles"));
        assertTrue(rcrEquals(rcr[1], "C0002", "Automobiles"));

        rcri.release();

        // try an invalid filter...
        try {
            rcr = cns.resolveToList(null, Constructors.createLocalNameList("invalid"), null, null, 0)
                    .getResolvedConceptReference();
            fail("Didn't throw LBParameterException");
        } catch (LBParameterException e) {
            // expected path
        }

        // try two filters.

        try {
            TestFilter2.register();
        } catch (LBParameterException e) {
            // can happen if is already registered by another test.
        }

        rcr = cns.resolveToList(null,
                Constructors.createLocalNameList(new String[] { TestFilter.name_, TestFilter2.name_ }), null, null, 0)
                .getResolvedConceptReference();
        // only 1 should pass both filters
        assertEquals(3,rcr.length);

        assertTrue(contains(rcr, "C0001", "Automobiles"));
        assertTrue(contains(rcr, "C0002", "Automobiles"));
        assertTrue(contains(rcr, "C0011(5564)", "Automobiles"));
    }

    public void testRestrictPropertyTypeReturns() throws LBException {

        CodedNodeSet cns = lbsi.getCodingSchemeConcepts("Automobiles", null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "A0001" }, "Automobiles"));

        // no type restriction
        ResolvedConceptReference[] rcrs = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        Entity node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 1);
        assertTrue(node.getDefinitionCount() == 1);
        assertTrue(node.getPresentationCount() == 1);

        // restrict to a couple of presentation types
        rcrs = cns.resolveToList(null, null,
                new PropertyType[] { PropertyType.COMMENT, PropertyType.PRESENTATION, PropertyType.DEFINITION }, 0)
                .getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 0);
        assertTrue(node.getDefinitionCount() == 1);
        assertTrue(node.getPresentationCount() == 1);

        assertTrue(node.getPresentation()[0].getValue().getContent().equals("Automobile"));
        assertTrue(node.getDefinition()[0].getValue().getContent().equals("An automobile"));

        // restrict to one presentation type
        rcrs = cns.resolveToList(null, null, new PropertyType[] { PropertyType.PRESENTATION }, 0)
                .getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 0);
        assertTrue(node.getDefinitionCount() == 0);
        assertTrue(node.getPresentationCount() == 1);

        // restrict to one presentation type
        rcrs = cns.resolveToList(null, null, new PropertyType[] { PropertyType.COMMENT }, 0)
                .getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 0);
        assertTrue(node.getDefinitionCount() == 0);
        assertTrue(node.getPresentationCount() == 0);

        // restrict to one presentation type and one property name
        rcrs = cns.resolveToList(null, Constructors.createLocalNameList("definition"),
                new PropertyType[] { PropertyType.DEFINITION }, 0).getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 0);
        assertTrue(node.getDefinitionCount() == 1);
        assertTrue(node.getPresentationCount() == 0);

        // restrict to one presentation type and one property name (which don't
        // line up)
        rcrs = cns.resolveToList(null, Constructors.createLocalNameList("textualPresentation"),
                new PropertyType[] { PropertyType.DEFINITION }, 0).getResolvedConceptReference();
        assertTrue(rcrs.length == 1);
        node = rcrs[0].getEntity();

        assertTrue(node.getCommentCount() == 0);
        assertTrue(node.getPropertyCount() == 0);
        assertTrue(node.getDefinitionCount() == 0);
        assertTrue(node.getPresentationCount() == 0);

    }

    private boolean contains(ResolvedConceptReference[] rcr, String code, String codeSystem) {
        boolean contains = false;
        for (int i = 0; i < rcr.length; i++) {
            if (rcrEquals(rcr[i], code, codeSystem)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    private boolean rcrEquals(ResolvedConceptReference rcr, String code, String codeSystem) {
        if (rcr.getConceptCode().equals(code) && rcr.getCodingSchemeName().equals(codeSystem)) {
            return true;
        }
        return false;
    }
}