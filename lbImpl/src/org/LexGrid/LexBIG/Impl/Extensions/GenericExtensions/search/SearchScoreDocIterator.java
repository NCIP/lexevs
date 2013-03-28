package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.AbstractListBackedResolvedConceptReferencesIterator;
import org.apache.lucene.search.ScoreDoc;

public class SearchScoreDocIterator extends AbstractListBackedResolvedConceptReferencesIterator<ScoreDoc>{

    protected SearchScoreDocIterator(List<ScoreDoc> list) {
        super(list);
    }

    private static final long serialVersionUID = -7112239106786189568L;

    @Override
    protected ResolvedConceptReference doTransform(ScoreDoc item) {
        return new ResolvedConceptReference();
    }

}
