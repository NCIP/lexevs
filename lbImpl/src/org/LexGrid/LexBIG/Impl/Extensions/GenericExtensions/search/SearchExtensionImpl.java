package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodeSystemReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;

    @Override
    public ResolvedConceptReferencesIterator search(String text) {
        SearchIndexService service = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
            getSearchIndexService();
        
        QueryParser parser = new QueryParser("description", service.getAnalyzer());

        Query query;
        try {
            query = parser.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService().
                query(null, query);

        return new SearchScoreDocIterator(scoreDocs);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodeSystemReference> codeSystems) {
        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().getIndexServiceManager().getSearchIndexService()
                .query(null, new MatchAllDocsQuery());

        return new SearchScoreDocIterator(scoreDocs);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodeSystemReference> codeSystemsToInclude,
            Set<CodeSystemReference> codeSystemsToExclude) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Simple Search Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        ed.setName("SearchExtension");
        ed.setVersion("1.0");

        return ed;
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

}
