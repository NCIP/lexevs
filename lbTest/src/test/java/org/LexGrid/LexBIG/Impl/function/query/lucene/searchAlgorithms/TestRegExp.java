
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestRegExp.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestRegExp extends BaseSearchAlgorithmTest {

    /** The algorithm. */
    private static String algorithm = "RegExp";

    /** The match code. */
    private static String matchCode = "A0001";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene RegExp tests";
    }

    /**
     * Test reg exp.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRegExp() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("automobi.*", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test reg exp leading wildcard.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRegExpLeadingWildcard() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations(".*utomobile", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
    /**
     * Test reg exp middle wildcard.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testRegExpMiddleWildcard() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("aut.*ile", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
    
     /**
      * Test reg exp brackets.
      * 
      * @throws Exception the exception
      */
    @Test
     public void testRegExpBrackets() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
        cns = cns.restrictToMatchingDesignations("au[s-u]omobile", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

        ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

        assertTrue("Length: " + rcrl.length, rcrl.length == 1);

        assertTrue(checkForMatch(rcrl, matchCode));
    }
     
     /**
      * Test reg exp brackets no match.
      * 
      * @throws Exception the exception
      */
    @Test
     public void testRegExpBracketsNoMatch() throws Exception {
         CodedNodeSet cns = super.getAutosCodedNodeSet();
         cns = cns.restrictToMatchingDesignations("au[c-f]omobile", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

         ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

         assertTrue("Length: " + rcrl.length, rcrl.length == 0);
     }
     
     /**
      * Test reg exp complex.
      * 
      * @throws Exception the exception
      */
    @Test
     public void testRegExpComplex() throws Exception {
         CodedNodeSet cns = super.getAutosCodedNodeSet();
         cns = cns.restrictToMatchingDesignations("a[^z]t*o(m|o)*bi.*e$", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

         ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

         assertTrue("Length: " + rcrl.length, rcrl.length == 1);

         assertTrue(checkForMatch(rcrl, matchCode));
     }
     
     /**
      * Test reg exp complex no match.
      * 
      * @throws Exception the exception
      */
    @Test
     public void testRegExpComplexNoMatch() throws Exception {
         CodedNodeSet cns = super.getAutosCodedNodeSet();
         cns = cns.restrictToMatchingDesignations("a[s]o(o|l)*bi.*e$", SearchDesignationOption.PREFERRED_ONLY, algorithm, null);

         ResolvedConceptReference[] rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();

         assertTrue("Length: " + rcrl.length, rcrl.length == 0);
     }
}