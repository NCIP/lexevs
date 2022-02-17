
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.apache.lucene.search.ScoreDoc;

import java.util.List;

/**
 * A factory for creating NonProxyCodeHolder objects.
 */
public class NonProxyCodeHolderFactory extends AbstractLazyCodeHolderFactory {

    private static final long serialVersionUID = 6173168645707807187L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.AbstractLazyCodeHolderFactory#buildCodeToReturn(org.apache.lucene.search.ScoreDoc, java.lang.String, java.lang.String)
     */
    @Override
    protected CodeToReturn buildCodeToReturn(ScoreDoc doc, String internalCodeSystemName, String internalVersionString) {
        return new NonProxyLazyCodeToReturn(doc, internalCodeSystemName, internalVersionString);
    }
    
    @Override
    protected CodeToReturn buildCodeToReturn(ScoreDoc doc, List<AbsoluteCodingSchemeVersionReference> references) {
        return new CommonIndexLazyLoadableCodeToReturn(references, doc);
    }


}