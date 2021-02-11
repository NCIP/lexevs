
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.apache.lucene.search.Query;

import java.io.Serializable;
import java.util.Set;

/**
 * A factory for creating CodeHolder objects.
 */
public interface CodeHolderFactory extends Serializable {

    public CodeHolder buildCodeHolder(
            Set<? extends AbsoluteCodingSchemeVersionReference> references,
            Query query) throws LBInvocationException, LBParameterException;
    
    public CodeHolder buildCodeHolder(CodeHolder additiveHolder,
            Set<? extends AbsoluteCodingSchemeVersionReference> references,
            Query query) throws LBInvocationException, LBParameterException;

}