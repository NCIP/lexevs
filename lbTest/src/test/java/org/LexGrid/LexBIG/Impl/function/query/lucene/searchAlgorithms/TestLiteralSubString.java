
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestSubString.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestLiteralSubString extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "literalSubString";

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
        return "Lucene subString tests";
    }

    /**
     * Test starts with.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubString() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal sub string invalid special character.
     * 
     * @throws Exception the exception
     */
    
    @Test
    public void testLiteralSubStringInvalidSpecialCharacter() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("sp!cial", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test literal sub string invalid special character case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringInvalidSpecialCharacterCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("Sp!ciAL", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test literal sub string two term.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringTwoTerm() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp*cial", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }

    /**
     * Test literal sub string all terms.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringAllTerms() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp*cial co{nce]pt", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal sub string invalid distance.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringInvalidDistance() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s co{nce]pt", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    /**
     * Test literal sub string invalid order.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringInvalidOrder() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("co{nce]pt sp*cial ", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }
    
    
    /**
     * Test literal sub string leading wildcard.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringLeadingWildcard() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("^s sp*cial co{nce]pt", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
   
    /**
     * Test literal sub string trailing wildcard.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringTrailingWildcard() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("a^s sp*cial co{nce]p", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test literal sub string leading and trailing wildcard.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringLeadingAndTrailingWildcard() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("(know/don't know)", SearchDesignationOption.ALL, algorithm, null);

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
    public void testLiteralSubStringNoMatch() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("NO_MATCH_FOR_TESTING", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 0);
    }

    /**
     * Test starts with case insensitive.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testLiteralSubStringCaseInsensitive() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("A^s Sp*cial CO{nce]pt", SearchDesignationOption.ALL, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
}