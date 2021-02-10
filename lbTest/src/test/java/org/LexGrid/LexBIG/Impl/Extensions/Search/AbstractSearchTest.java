
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