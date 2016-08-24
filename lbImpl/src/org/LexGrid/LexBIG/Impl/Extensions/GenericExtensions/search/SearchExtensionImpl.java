package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.util.CollectionUtils;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;

    @Override
    public ResolvedConceptReferencesIterator search(String text, MatchAlgorithm matchAlgorithm) throws LBParameterException {
        return this.search(text, null, matchAlgorithm);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> codeSystems, MatchAlgorithm matchAlgorithm) throws LBParameterException {
        return this.search(text, codeSystems, null, matchAlgorithm);
    }
    
    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm) throws LBParameterException {
        return this.search(text, codeSystemsToInclude, codeSystemsToExclude, matchAlgorithm, false);
    }
    
    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm,
            boolean includeAnonymous) throws LBParameterException {      
        return this.search(
                text, 
                codeSystemsToInclude, 
                codeSystemsToExclude, 
                matchAlgorithm, 
                includeAnonymous, 
                false);
    }

    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm,
            boolean includeAnonymous,
            boolean includeInactive) throws LBParameterException {
        
        LexEvsServiceLocator lexEvsServiceLocator = LexEvsServiceLocator.getInstance();
        List<RegistryEntry> entries = 
                lexEvsServiceLocator.getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
        
        for(RegistryEntry entry : entries){
            if(! entry.getStatus().equals(CodingSchemeVersionStatus.ACTIVE.toString())){
                CodingSchemeReference ref = new CodingSchemeReference();
                ref.setCodingScheme(entry.getResourceUri());
                ref.setVersionOrTag(
                        Constructors.createCodingSchemeVersionOrTagFromVersion(entry.getResourceVersion()));
                
                if(codeSystemsToExclude == null){
                    codeSystemsToExclude = new HashSet<CodingSchemeReference>();
                }
                
                codeSystemsToExclude.add(ref);
            }
        }
        
        SearchIndexService service = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService();
        
        Analyzer analyzer = service.getAnalyzer();

        Query query = this.parseQuery(this.decorateQueryString(text, analyzer, matchAlgorithm), analyzer);
        
        if(! includeAnonymous || ! includeInactive){
            BooleanQuery booleanQuery = new BooleanQuery();
            booleanQuery.add(query, Occur.MUST);
            
            if(! includeAnonymous){
                booleanQuery.add(new TermQuery(new Term("anonymous", "true")), Occur.MUST_NOT);
            }
            if(! includeInactive){
                booleanQuery.add(new TermQuery(new Term("active", "false")), Occur.MUST_NOT);
            }
            
            query = booleanQuery; 
        }

        List<ScoreDoc> scoreDocs = lexEvsServiceLocator.
                getIndexServiceManager().
                getSearchIndexService().
                query(this.resolveCodeSystemReferences(codeSystemsToInclude), 
                        this.resolveCodeSystemReferences(codeSystemsToExclude),
                        query);

        return new SearchScoreDocIterator(scoreDocs);
    }
    
    protected String decorateQueryString(String text, Analyzer analyzer, MatchAlgorithm matchAlgorithm) {
        if(StringUtils.isBlank(text)) {
          return text;  
        }
        
        switch(matchAlgorithm){
        case PRESENTATION_EXACT:
            return "exactDescription:\"" + QueryParser.escape(text) + "\"";
        case CODE_EXACT:
            return "code:" + QueryParser.escape(text);
        case PRESENTATION_CONTAINS:
            text = QueryParser.escape(text);
            List<String> tokens = tokenize(analyzer, "description", text);
            
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for(String token : tokens){
               sb.append("description:");
               sb.append(token);
               sb.append("* ");
            }
            sb.append(")");
            sb.append(" OR description:\""+text+"\"");
            sb.append(" OR exactDescription:\"" + QueryParser.escape(text) + "\"");
            return sb.toString().trim();
        case LUCENE:
            return text;
        default:
            throw new IllegalStateException("Unrecognized MatchAlgorithm: " + matchAlgorithm.name());
        }
    }

    protected Query parseQuery(String text, Analyzer analyzer) {
        if (StringUtils.isBlank(text)) {
            return new MatchAllDocsQuery();
        } else {
            QueryParser parser = this.createQueryParser(analyzer);

            try {
                return parser.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    protected QueryParser createQueryParser(Analyzer analyzer){
        QueryParser parser = new QueryParser("description", analyzer);
        parser.setDefaultOperator(Operator.AND);
        
        return parser;
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
    
    public List<String> tokenize(Analyzer analyzer, String field, String keywords) {
        List<String> result = new ArrayList<String>();
        TokenStream stream  = analyzer.tokenStream(field, new StringReader(keywords));

        Token token = new Token();
        try {
            Token t;
            while((t = stream.next(token)) != null) {
                result.add(t.term());
            }
        }
        catch(IOException e) {
            throw new IllegalStateException(e);
        }

        return result;
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
