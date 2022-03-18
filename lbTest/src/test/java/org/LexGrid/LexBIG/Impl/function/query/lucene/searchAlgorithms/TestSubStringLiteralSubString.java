
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.experimental.categories.Category;

/**
 * The Class TestSubStringLiteralSubString.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestSubStringLiteralSubString extends TestSubString {

    /** The algorithm. */
    private static String algorithm = "literalSubString";

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSubString#getAlgorithm()
     */
    @Override
    protected String getAlgorithm() {
        return algorithm;
    }
}