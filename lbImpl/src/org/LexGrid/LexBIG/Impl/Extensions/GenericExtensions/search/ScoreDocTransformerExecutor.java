package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.annotations.LgProxyClass;
import org.apache.lucene.search.ScoreDoc;

@LgProxyClass
public class ScoreDocTransformerExecutor {
    

    public ResolvedConceptReferenceList transform(ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        return transformer.transform(items);
    }

}
