
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.experimental.categories.Category;

/**
 * The Class TestWeightedDoubleMetaphone.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestLiteralSpellingErrorTolerantSubString extends TestLiteral {

    /** The algorithm. */
    protected String algorithm = "SpellingErrorTolerantSubStringSearch";

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.
     * BaseSearchAlgorithmTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Lucene SpellingErrorTolerantSubStringSearch tests";
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteral#getAlgorithm()
     */
    @Override
    protected String getAlgorithm() {
        return algorithm;
    }  
}