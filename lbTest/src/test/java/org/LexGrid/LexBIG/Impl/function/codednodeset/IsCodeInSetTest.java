
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class IsCodeInSetTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class IsCodeInSetTest extends BaseCodedNodeSetTest {
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "IsCodeInSetTest Union Test";
    }
    
   /**
    * Test is code in set.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
    @Test
   public void testIsCodeInSet() throws LBInvocationException, LBParameterException{
       assertTrue(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code not in set.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
    @Test
   public void testIsCodeNotInSet() throws LBInvocationException, LBParameterException{
       assertFalse(
               cns.isCodeInSet(
                       Constructors.createConceptReference("BOGUS_CODE", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code in set with restriction.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
    @Test
   public void testIsCodeInSetWithRestriction() throws LBInvocationException, LBParameterException{
       cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
       assertTrue(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code not in set with restriction.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
    @Test
   public void testIsCodeNotInSetWithRestriction() throws LBInvocationException, LBParameterException{
       cns = cns.restrictToCodes(Constructors.createConceptReferenceList("T0001"));
       assertFalse(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
}