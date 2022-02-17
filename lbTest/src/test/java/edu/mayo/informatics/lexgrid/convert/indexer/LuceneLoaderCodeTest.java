
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