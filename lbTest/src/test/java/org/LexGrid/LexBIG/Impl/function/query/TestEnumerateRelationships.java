
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_31	TestEnumerateRelationships

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class TestEnumerateRelationships extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_31";

    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * Example of listCodeRelationships
     */
    @Test
    public void testT1_FNC_31() throws LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        CodedNodeGraph cng = lbs.getNodeGraph(AUTO_SCHEME, null, null);

        ConceptReference ref4 = ConvenienceMethods.createConceptReference("A0001", AUTO_SCHEME);
        ConceptReference ref5 = ConvenienceMethods.createConceptReference("C0001", AUTO_SCHEME);
        ConceptReference ref6 = ConvenienceMethods.createConceptReference("Tires", "ExpendableParts");

        List<String> assocs = cng.listCodeRelationships(ref4, ref5, true);

        assertTrue(assocs.contains("hasSubtype"));

        assocs = cng.listCodeRelationships(ref4, ref6, false);

        assertTrue(assocs.contains("uses"));

    }
}