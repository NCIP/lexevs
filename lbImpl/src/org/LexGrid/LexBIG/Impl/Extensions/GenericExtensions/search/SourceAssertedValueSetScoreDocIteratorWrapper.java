package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.ScoreDoc;

public class SourceAssertedValueSetScoreDocIteratorWrapper extends SearchScoreDocIterator {
    /**
     * 
     */
    private static final long serialVersionUID = -8590261353875073492L;
    
    private SourceAssertedScoreDocTransformerExecutor transformerExecutor = new SourceAssertedScoreDocTransformerExecutor();
    protected SourceAssertedScoreDocTransformer transformer;

    protected SourceAssertedValueSetScoreDocIteratorWrapper(Set<AbsoluteCodingSchemeVersionReference> codeSystemRefs,
            List<ScoreDoc> list) {
        super(codeSystemRefs, list);
        // TODO Auto-generated constructor stub
    }



}
