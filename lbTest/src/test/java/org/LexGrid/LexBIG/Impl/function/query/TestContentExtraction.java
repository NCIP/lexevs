
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_01	TestContentExtraction

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class TestContentExtraction extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_01";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_01() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, null);

        ResolvedConceptReferencesIterator iter = cns.resolve(null, null, null);

        // return 100 at a time -
        int count = 0;
        while (iter.hasNext()) {
            ResolvedConceptReference[] temp = iter.next(100).getResolvedConceptReference();
            count += temp.length;
            assertTrue(temp.length <= 100);

            if (count > 400) {
                iter.release();
                break;
            }
        }

        // pretty basic test - iterator will let you go over the entire set of
        // concepts.
        // you can serialize to whatever format you want.
        // have to lookup the sources and targets from each returned concept
        // seperately.

    }

}