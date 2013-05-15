package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.util.CollectionUtils;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;

    @Override
    public ResolvedConceptReferencesIterator search(String text) throws LBParameterException {
        return this.search(text, null, null);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> codeSystems) throws LBParameterException {
        return this.search(text, codeSystems, null);
    }

    @Override
    public ResolvedConceptReferencesIterator search(
            String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude) throws LBParameterException {
        SearchIndexService service = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService();
        
        Analyzer analyzer = service.getAnalyzer();

        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService().
                query(this.resolveCodeSystemReferences(codeSystemsToInclude), 
                        this.resolveCodeSystemReferences(codeSystemsToExclude),
                        this.parseQuery(text, analyzer));

        return new SearchScoreDocIterator(scoreDocs);
    }
    
    protected Query parseQuery(String text, Analyzer analyzer) {
        if (StringUtils.isBlank(text)) {
            return new MatchAllDocsQuery();
        } else {
            QueryParser parser = new QueryParser("description", analyzer);

            try {
                return parser.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private Set<AbsoluteCodingSchemeVersionReference> 
        resolveCodeSystemReferences(Set<CodingSchemeReference> references) throws LBParameterException{
        if(CollectionUtils.isEmpty(references)){
            return null;
        }
        
        Set<AbsoluteCodingSchemeVersionReference> returnSet = new HashSet<AbsoluteCodingSchemeVersionReference>();
        
        for(CodingSchemeReference ref : references){
            returnSet.add(
                    ServiceUtility.getAbsoluteCodingSchemeVersionReference(
                            ref.getCodingScheme(),
                            ref.getVersionOrTag(), 
                            true));
        }
        
        return returnSet;
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
