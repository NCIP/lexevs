
package org.LexGrid.LexBIG.Impl.helpers;

import static org.easymock.EasyMock.*;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * The Class ResolvedConceptReferencesIteratorImplTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedConceptReferencesIteratorImplTest extends LexBIGServiceTestCase {

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "ResolvedConceptReferencesIteratorImpl Tests";
    }

    /**
     * Test has next.
     * 
     * @throws LBResourceUnavailableException the LB resource unavailable exception
     */
    public void testHasNext() throws Exception{   
        CodeHolder holder = createMock(CodeHolder.class);
        expect(holder.clone()).andReturn(holder).anyTimes();
        expect(holder.getNumberOfCodes()).andReturn(10).anyTimes();
        replay(holder);
        
        ResolvedConceptReferencesIteratorImpl itr = new ResolvedConceptReferencesIteratorImpl(holder, null, null, null, false);
        
        assertTrue(itr.hasNext());
    }
    
    /**
     * Test has next with empty code holder.
     * 
     * @throws LBResourceUnavailableException the LB resource unavailable exception
     */
    public void testHasNextWithEmptyCodeHolder() throws Exception{   
        CodeHolder holder = createMock(CodeHolder.class);
        expect(holder.clone()).andReturn(holder).anyTimes();
        expect(holder.getNumberOfCodes()).andReturn(0).anyTimes();
        replay(holder);
        
        ResolvedConceptReferencesIteratorImpl itr = new ResolvedConceptReferencesIteratorImpl(holder, null, null, null, false);
        
        assertFalse(itr.hasNext());
    }
    
    /**
     * Test has next with filter.
     * 
     * @throws LBException the LB exception
     */
    public void testHasNextWithFilter() throws LBException{
        TestFilter filter = new TestFilter();

        try{
            filter.register();
        } catch (Exception e) {
            //if its already registered....
        }
        
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        
        ResolvedConceptReferencesIterator itr = cns.resolve(null, Constructors.createLocalNameList("JUnit Test Filter"), null, null);
        
        int max = 100;
        int counter = 0;
        
        //check to see if we get stuck in a loop
        while(itr.hasNext() && counter < max){
            itr.next();
            counter++;
        }
        
        assertTrue(counter > 0);
        assertTrue(counter < max);
    }
}