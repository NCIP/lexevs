package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.AbstractListBackedResolvedConceptReferencesIterator;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.locator.LexEvsServiceLocator;

public class SearchScoreDocIterator extends AbstractListBackedResolvedConceptReferencesIterator<ScoreDoc>{

    protected SearchScoreDocIterator(List<ScoreDoc> list) {
        super(list);
    }

    private static final long serialVersionUID = -7112239106786189568L;

    @Override
    protected ResolvedConceptReference doTransform(ScoreDoc item) {
        Document doc = 
            LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService().
                getById(item.doc);
        
        String code = doc.get("code");
        
        ResolvedConceptReference ref = new ResolvedConceptReference();
        ref.setCode(code);
        
        return ref;
    }

}
