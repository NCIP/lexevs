
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.experimental.categories.Category;

/**
 * The Class TestLiteralLiteralContains.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestLiteralLiteralContains extends TestLiteral {

    /** The algorithm. */
    private static String algorithm = "literalContains";

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteral#getAlgorithm()
     */
    @Override
    protected String getAlgorithm() {
        return algorithm;
    }
}