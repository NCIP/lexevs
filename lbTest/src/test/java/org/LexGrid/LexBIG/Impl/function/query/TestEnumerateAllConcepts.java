
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_28	TestEnumerateAllConcepts

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestEnumerateAllConcepts extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_28";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testT1_FNC_28_a() throws LBException {
        // Perform the query ...
        CodedNodeSet nodes = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        // Process the result
        ResolvedConceptReferenceList matches = nodes.resolveToList(null, null, null, 0);
        int count = matches.getResolvedConceptReferenceCount();
        assertTrue("Found: " + count, count == 19);
        ConceptReference ref = (ConceptReference) matches.enumerateResolvedConceptReference().nextElement();
        assertNotNull(ref);

    }
}