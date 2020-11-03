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
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class BaseSearchAlgorithmTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseSearchAlgorithmTest extends LexBIGServiceTestCase {

    /** The lbs. */
    protected LexBIGService lbs;

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Base Search Algorithm Test";
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        lbs = ServiceHolder.instance().getLexBIGService();
    }

    /**
     * Gets the autos coded node set.
     * 
     * @return the autos coded node set
     * 
     * @throws Exception the exception
     */
    protected CodedNodeSet getOWLSchemeCodedNodeSet() throws Exception {
        return lbs.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        		Constructors.createCodingSchemeVersionOrTagFromVersion(
        				OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
    }
    
    /**
     * Gets the autos coded node set.
     * 
     * @return the autos coded node set
     * 
     * @throws Exception the exception
     */
    protected CodedNodeSet getAutosCodedNodeSet() throws Exception {
        return lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
    }


    /**
     * Check for match.
     * 
     * @param rcrl the rcrl
     * @param code the code
     * 
     * @return true, if successful
     */
    protected boolean checkForMatch(ResolvedConceptReference[] rcrl, String code) {
        return checkForMatch(rcrl, Arrays.asList(code));
    }

    /**
     * Check for match.
     * 
     * @param rcrl the rcrl
     * @param codes the codes
     * 
     * @return true, if successful
     */
    protected boolean checkForMatch(ResolvedConceptReference[] rcrl, List<String> codes) {
        Map<String, Boolean> matches = new HashMap<String, Boolean>();
        for (String code : codes) {
            matches.put(code, false);
        }

        for (ResolvedConceptReference ref : rcrl) {
            String code = ref.getCode();
            matches.put(code, true);
        }

        return !matches.containsValue(false);
    }

    /**
     * Test set up.
     */
    public void testSetUp() {
        assertNotNull(lbs);
    }
}