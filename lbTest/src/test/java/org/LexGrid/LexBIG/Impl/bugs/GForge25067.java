
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;

public class GForge25067 extends LexBIGServiceTestCase {

/** The Constant testID. */
final static String testID = "GForge25067";
    /** The lbs. */
    private LexBIGService lbs;
    
    /** The test entity. */
    private Entity testEntity;

	protected String getTestID() {
		return testID;
	}
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
    	
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
        lbs = ServiceHolder.instance().getLexBIGService();  
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_SCHEME, null);
        
        cns.restrictToCodes(Constructors.createConceptReferenceList("10198:DEF"));
        
        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue(rcrl.length == 1);
        
        testEntity = rcrl[0].getEntity();
}
    /**
     * Test entity not null.
     * 
     * @throws LBException the LB exception
     */
    public void testEntityNotNull() throws LBException {
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
       assertNotNull(testEntity);
    }
    
    public void testDefinitionExists()throws LBException{
        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            // Connecting to ms access from Linux is beyond the scope of this
            // application.
            return;
        }
    	assertTrue(testEntity.getDefinitionCount()> 0);
    }
}