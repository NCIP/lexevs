/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.apache.lucene.search.Query;

/**
 * The Class AbstractSearchTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AbstractSearchTest extends LexBIGServiceTestCase {
    
    /** The abstract search. */
    private AbstractSearch abstractSearch;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "AbstractSearch Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        try {
            abstractSearch = new TestAbstractSearchTest();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Test set up.
     */
    public void testSetUp(){
        assertNotNull(abstractSearch);
    }
    
    /**
     * Test add trailing wildcard one token.
     */
    public void testAddTrailingWildcardOneToken(){
        String[] testArray = new String[]{"one"};
        String[] wildCardArray = abstractSearch.addTrailingWildcard(testArray);
        assertTrue(wildCardArray.length == 1);
        assertTrue("Value: " + wildCardArray[0], wildCardArray[0].equals("one*"));  
    }
    
    /**
     * Test add trailing wildcard two tokens.
     */
    public void testAddTrailingWildcardTwoTokens(){
        String[] testArray = new String[]{"one", "two"};
        String[] wildCardArray = abstractSearch.addTrailingWildcard(testArray);
        assertTrue(wildCardArray.length == 2);
        assertTrue(wildCardArray[0].equals("one*"));
        assertTrue(wildCardArray[1].equals("two*"));
    }
    
    /**
     * Test add trailing wildcard to all tokens one token.
     */
    public void testAddTrailingWildcardToAllTokensOneToken(){
        String returnString = abstractSearch.addTrailingWildcardToAllTokens("one");
        assertTrue(returnString.equals("one*"));
    }
    
    /**
     * Test add trailing wildcard to all tokens two tokens.
     */
    public void testAddTrailingWildcardToAllTokensTwoTokens(){
        String returnString = abstractSearch.addTrailingWildcardToAllTokens("one two");
        assertTrue(returnString.equals("one* two*"));
    }
    
    /**
     * The Class TestAbstractSearchTest.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestAbstractSearchTest extends AbstractSearch {
        
        /* (non-Javadoc)
         * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
         */
        @Override
        protected ExtensionDescription buildExtensionDescription() { 
            return null;
        }

        /* (non-Javadoc)
         * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
         */
        public Query buildQuery(String searchText) {
            return null;
        }    
    }
}
