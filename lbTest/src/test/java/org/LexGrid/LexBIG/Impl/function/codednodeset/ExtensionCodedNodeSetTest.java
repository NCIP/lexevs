
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class ExtensionCodedNodeSetTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class ExtensionCodedNodeSetTest extends BaseCodedNodeSetTest {
    
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Test Coding Scheme Extensions.";
    }
   
    @Test
    public void testExtensionResolveCount() throws Exception {
        
    	int autosEntities = lbs.getNodeSet(AUTO_URN, null, null).resolve(null, null, null).numberRemaining();
    	
    	int autosExtensionEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).resolve(null, null, null).numberRemaining();
    	
    	assertEquals(2, autosExtensionEntities - autosEntities);
    }
    
    @Test
    public void testSearchExtension() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList("DeVille")).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(1, foundEntities);
    }
    
    @Test
    public void testSearchParent() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList("GM")).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(1, foundEntities);
    }
    
    @Test
    public void testSearchBoth() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList(new String[]{"GM", "DeVille"})).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(2, foundEntities);
    }
    
}