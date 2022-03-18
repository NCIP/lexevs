
package org.LexGrid.LexBIG.Impl.featureRequests;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

/**
 * This class should be used as a place to write JUnit tests which demonstrate a Feature Request.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class ChangeConfigFileName extends LexBIGServiceTestCase {
    final static String testID = "ChangeConfigFileName";
 
    @Override
    protected String getTestID() {
        return testID;
    }
    
    public void testLexEVSStart(){
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 
        assert(lbs != null);
    }

    public void testCheckResourceManagerStart() throws LBException {
        ResourceManager rm = ResourceManager.instance();
        assert(rm != null);
    }
    
    public void testCheckResourceManagerReinit() throws LBException {
        ResourceManager.reInit(null);
    }
    
    public void testCheckForCorrectPropertyFileName() throws LBException {
       assertTrue(SystemVariables.CONFIG_FILE_NAME.equals("lbconfig.props"));
    }    
}