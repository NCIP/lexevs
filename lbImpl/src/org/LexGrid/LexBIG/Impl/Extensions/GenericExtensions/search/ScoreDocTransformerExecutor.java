package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.apache.lucene.search.ScoreDoc;


public class ScoreDocTransformerExecutor implements Serializable {
    

    private static final long serialVersionUID = 7939532917237511648L;

    public ResolvedConceptReferenceList transform(ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for(ScoreDoc item : items){
            ProxyProtectedScoreDocWrapper wrapper = 
                    new ProxyProtectedScoreDocWrapper();
            wrapper.setScoreDoc(item);
            list.addResolvedConceptReference(transformer.doTransform(wrapper));
        }
        
        return list;
    }

}
