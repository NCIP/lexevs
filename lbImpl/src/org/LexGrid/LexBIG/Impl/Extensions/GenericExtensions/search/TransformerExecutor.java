package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Impl.helpers.AbstractListBackedResolvedConceptReferencesIterator.Transformer;
import org.LexGrid.annotations.LgProxyClass;

@LgProxyClass
public class TransformerExecutor<T> implements Serializable {

    private static final long serialVersionUID = -6065733883014163743L;

    public ResolvedConceptReferenceList transform(Transformer<T> transformer, Iterable<T> items) {
        return transformer.transform(items);
    }
    
}
