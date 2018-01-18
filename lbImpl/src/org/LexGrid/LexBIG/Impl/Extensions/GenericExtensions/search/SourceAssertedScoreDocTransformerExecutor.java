package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.ScoreDoc;

public class SourceAssertedScoreDocTransformerExecutor {
    
    public ResolvedConceptReferenceList transform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
            ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
                return null;
        
    }

}
