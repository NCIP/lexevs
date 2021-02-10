
package org.LexGrid.LexBIG.Impl.dataAccess;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.system.ResourceManager;

/**
 * The Class ResourceManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResourceManagerTest extends LexBIGServiceTestCase {

    /** The resource manager. */
    private ResourceManager resourceManager;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Resource Manager Tests";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        resourceManager = ResourceManager.instance();
    }
    
    public void testInit(){
        assertNotNull(this.resourceManager);
    }
}