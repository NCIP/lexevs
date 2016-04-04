package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.annotations.LgProxyClass;
import org.apache.lucene.search.ScoreDoc;

@LgProxyClass
public class ScoreDocTransformerExecutor implements Serializable {
    

    private static final long serialVersionUID = 7939532917237511648L;

    public ResolvedConceptReferenceList transform(ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        return transformer.transform(items);
    }

}
