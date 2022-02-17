
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.Before;

/**
 * The Class BaseCodedNodeSetTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseCodedNodeSetTest extends LexBIGServiceTestCase {

    /** The cns. */
    protected CodedNodeSet cns;
    
    /** The lbs. */
    protected LexBIGService lbs;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Base CodedNodeSet Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    @Before
    public void setUp(){
        try {
            lbs = ServiceHolder.instance().getLexBIGService();
            cns = lbs.getCodingSchemeConcepts(getCodingScheme(), null);
        } catch (LBException e) {
          fail(e.getMessage());
        }
      }
    
    /**
     * Gets the coding scheme.
     * 
     * @return the coding scheme
     */
    protected String getCodingScheme(){
        return LexBIGServiceTestCase.AUTO_SCHEME;
    }
    
    /**
     * Test set up.
     */
    public void testSetUp(){
        assertNotNull(cns);
    }
    
    protected boolean contains(ResolvedConceptReference[] rcr, String code) {
        return contains(rcr, code, null, null);
     }
   
    protected boolean contains(ResolvedConceptReference[] rcr, String code, String codeSystem) {
       return contains(rcr, code, null, codeSystem);
    }
    

    protected boolean contains(ResolvedConceptReference[] rcr, String code, String namespace, String codeSystem) {
        boolean contains = false;
        for (int i = 0; i < rcr.length; i++) {
            if (rcrEquals(rcr[i], code, namespace, codeSystem)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    
    /**
     * Rcr equals.
     * 
     * @param rcr the rcr
     * @param code the code
     * @param codeSystem the code system
     * 
     * @return true, if successful
     */
    protected boolean rcrEquals(ResolvedConceptReference rcr, String code, String namespace, String codeSystem) {
        if (rcr.getConceptCode().equals(code)){
            boolean match = true;
            if(namespace != null){
                match = match && rcr.getCodeNamespace().equals(namespace);
            }
            if(codeSystem != null){
                match = match && rcr.getCodingSchemeName().equals(codeSystem);
            }
            
            if(match){
                return true;
            }
        }
        return false;
    }
}