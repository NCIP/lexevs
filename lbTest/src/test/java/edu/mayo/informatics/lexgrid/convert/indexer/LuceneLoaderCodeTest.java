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
package edu.mayo.informatics.lexgrid.convert.indexer;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class LuceneLoaderCodeTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneLoaderCodeTest extends LexBIGServiceTestCase {

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "LuceneLoaderCode Tests";
    }

    /**
     * Test reverse terms in property value.
     */
    public void testReverseTermsInPropertyValue(){
        String string = "test string for reverse";
        LuceneLoaderCode luceneLoaderCode = new TestLuceneLoaderCode();
        String reverseString = luceneLoaderCode.reverseTermsInPropertyValue(string);
        
        assertTrue("Actual String: " + reverseString, reverseString.equals("tset gnirts rof esrever"));
    }
    
    /**
     * Test reverse terms in property value one term.
     */
    public void testReverseTermsInPropertyValueOneTerm(){
        String string = "test";
        LuceneLoaderCode luceneLoaderCode = new TestLuceneLoaderCode();
        String reverseString = luceneLoaderCode.reverseTermsInPropertyValue(string);
        
        assertTrue("Actual String: " + reverseString, reverseString.equals("tset"));
    }
    
    /**
     * Test reverse terms in property value empty.
     */
    public void testReverseTermsInPropertyValueEmpty(){
        String string = "";
        LuceneLoaderCode luceneLoaderCode = new TestLuceneLoaderCode();
        String reverseString = luceneLoaderCode.reverseTermsInPropertyValue(string);
        
        assertTrue("Actual String: " + reverseString, reverseString.equals(""));
    }
    
    /**
     * The Class TestLuceneLoaderCode.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestLuceneLoaderCode extends LuceneLoaderCode {}
}
