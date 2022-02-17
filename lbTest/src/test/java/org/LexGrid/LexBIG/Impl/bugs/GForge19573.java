
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge19573 extends LexBIGServiceTestCase {
    final static String testID = "GForge19573";

    @Override
    protected String getTestID() {
        return testID;
    }
    
    //This will expose the bug ONLY when run on a MS Access
    //or Postgresql database
    public void testIsActive() throws Exception {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "73" }, AUTO_SCHEME));
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null, null, 0).getResolvedConceptReference();

        // 73 is inactive - so this should be 0.
        assertTrue(rcr.length == 0);
    }   
}