
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestLiteral.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestLiteral extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "literal";

    /** The match code. */
    private static String matchCode = "SpecialCharactersConcept";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene Literal tests";
    }

    /**
     * Test starts with.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteral() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("(know/don't know)", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal two terms.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralTwoTerms() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s co{nce]pt", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("sp*cial", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }

    /**
     * Test starts with no match.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testStartsWithNoMatch() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("NO_MATCH_FOR_TESTING", SearchDesignationOption.ALL, getAlgorithm(), null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Gets the algorithm.
     * 
     * @return the algorithm
     */
    protected String getAlgorithm(){
        return algorithm;
    }
}