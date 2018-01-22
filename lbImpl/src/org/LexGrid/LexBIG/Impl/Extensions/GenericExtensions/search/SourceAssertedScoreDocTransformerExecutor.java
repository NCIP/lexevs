package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.ScoreDoc;

public class SourceAssertedScoreDocTransformerExecutor {

    public ResolvedConceptReferenceList transform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
            SourceAssertedScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for (ScoreDoc item : items) {
            ProxyProtectedScoreDocWrapper wrapper = new ProxyProtectedScoreDocWrapper();
            wrapper.setScoreDoc(item);
            list.addResolvedConceptReference(transformer.doTransform(codeSystemsToInclude, wrapper));
        }
        return list;
    }

}
