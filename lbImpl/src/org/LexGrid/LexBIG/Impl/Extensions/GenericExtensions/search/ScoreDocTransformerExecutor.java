package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.apache.lucene.search.ScoreDoc;


public class ScoreDocTransformerExecutor implements Serializable {
    

    private static final long serialVersionUID = 7939532917237511648L;

    public ResolvedConceptReferenceList transform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
            ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for(ScoreDoc item : items){
            ProxyProtectedScoreDocWrapper wrapper = 
                    new ProxyProtectedScoreDocWrapper();
            wrapper.setScoreDoc(item);
            list.addResolvedConceptReference(transformer.doTransform(codeSystemsToInclude, wrapper));
        }
        
        return list;
    }

}
