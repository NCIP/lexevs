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
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.util.CollectionUtils;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;
    private static final Term baseQuery = new Term("propertyType", "presentation");
    private static final Term preferred = new Term("isPreferred","T");

    @Override
    public ResolvedConceptReferencesIterator search(String text, MatchAlgorithm matchAlgorithm) throws LBParameterException, IOException {
        return this.search(text, null, matchAlgorithm);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> codeSystems, MatchAlgorithm matchAlgorithm) throws LBParameterException, IOException {
        return this.search(text, codeSystems, null, matchAlgorithm);
    }
    
    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm) throws LBParameterException, IOException {
        return this.search(text, codeSystemsToInclude, codeSystemsToExclude, matchAlgorithm, false);
    }
    
    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm,
            boolean includeAnonymous) throws LBParameterException, IOException {      
        return this.search(
                text, 
                codeSystemsToInclude, 
                codeSystemsToExclude, 
                matchAlgorithm, 
                includeAnonymous, 
                true);
    }

    @Override
    public ResolvedConceptReferencesIterator search(
            final String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude, 
            MatchAlgorithm matchAlgorithm,
            boolean includeAnonymous,
            boolean includeInactive) throws LBParameterException, IOException {
        
        LexEvsServiceLocator lexEvsServiceLocator = LexEvsServiceLocator.getInstance();
        List<RegistryEntry> entries = 
                lexEvsServiceLocator.getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
        
        //We'll want any and all systems if this contains none.
        if(codeSystemsToInclude == null){
            codeSystemsToInclude = new HashSet<CodingSchemeReference>();
        }
        
        for(RegistryEntry entry : entries){
            CodingSchemeReference ref = new CodingSchemeReference();
            ref.setCodingScheme(entry.getResourceUri());
            ref.setVersionOrTag(
                    Constructors.createCodingSchemeVersionOrTagFromVersion(entry.getResourceVersion()));
            if(! entry.getStatus().equals(CodingSchemeVersionStatus.ACTIVE.toString())){
                //We'll only initialize it if we need it
                if(codeSystemsToExclude == null){
                    codeSystemsToExclude  = new HashSet<CodingSchemeReference>();
                }
                codeSystemsToExclude.add(ref);
            }
            codeSystemsToInclude.add(ref);
        }
        
        SearchIndexService service = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService();
        
        Analyzer analyzer = service.getAnalyzer();

        Query query = this.buildOnMatchAlgorithm(text, analyzer, matchAlgorithm);
        BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();

        if(! includeAnonymous || ! includeInactive){

            newBuilder.add(query, Occur.MUST);
            
            if(! includeAnonymous){
                newBuilder.add(new TermQuery(new Term("isAnonymous", "T")), Occur.MUST_NOT);
            }
            if(! includeInactive){
                newBuilder.add(new TermQuery(new Term("isActive", "T")), Occur.MUST_NOT);
            }
            
            query = newBuilder.build(); 
        }
        
 //       TermQuery termQuery = new TermQuery(new Term("isParentDoc", "true"));
        QueryBitSetProducer parentFilter = null;
        try {
            parentFilter = new QueryBitSetProducer(new QueryParser("isParentDoc", 
                    new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
        } catch (ParseException e) {
            throw new RuntimeException("Query Parser Failed against parent query: ", e);
        }
        ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(
                query, parentFilter, ScoreMode.Total);
        
        List<ScoreDoc> scoreDocs = lexEvsServiceLocator.
                getIndexServiceManager().
                getSearchIndexService().
                query(this.resolveCodeSystemReferences(codeSystemsToInclude), 
                        this.resolveCodeSystemReferences(codeSystemsToExclude),
                        blockJoinQuery);

        return new SearchScoreDocIterator(scoreDocs);
    }
    
    protected String decorateQueryString(String text, Analyzer analyzer, MatchAlgorithm matchAlgorithm) throws IOException {
        if(StringUtils.isBlank(text)) {
          return text;  
        }
        
        switch(matchAlgorithm){
        case PRESENTATION_EXACT:
            return LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD + ":" + QueryParser.escape(text);
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
    
    protected BooleanQuery buildOnMatchAlgorithm(String text, Analyzer analyzer, MatchAlgorithm matchAlgorithm){
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if(StringUtils.isBlank(text))
        {return builder.build();}
        
        switch(matchAlgorithm){
        case PRESENTATION_EXACT:
            builder.add(new TermQuery(baseQuery), Occur.MUST);
            builder.add(new TermQuery(preferred), Occur.MUST);
            builder.add(new TermQuery(new Term(
                    LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,QueryParser.escape(text) )), Occur.MUST);
            return  builder.build();
        case CODE_EXACT:
            builder.add(new TermQuery(new Term("code",text)),Occur.MUST);
            return builder.build();
        case PRESENTATION_CONTAINS:
            builder.add(new TermQuery(baseQuery), Occur.MUST);
            builder.add(new TermQuery(preferred), Occur.MUST);
            text = QueryParser.escape(text);

            List<String> tokens;
            try {
                tokens = tokenize(analyzer, LuceneLoaderCode.PROPERTY_VALUE_FIELD, text);
            } catch (IOException e) {
               throw new RuntimeException("Tokenizing query text failed", e);
            }

            for(String token : tokens){
               builder.add(new TermQuery(new Term(LuceneLoaderCode.PROPERTY_VALUE_FIELD, token + "*")), Occur.MUST);
            }
            builder.add(new TermQuery(new Term(LuceneLoaderCode.PROPERTY_VALUE_FIELD,text)), Occur.SHOULD);
            builder.add(new TermQuery(new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,QueryParser.escape(text))), Occur.SHOULD);
            return builder.build();
        case LUCENE:
            builder.add(new TermQuery(new Term(LuceneLoaderCode.PROPERTY_VALUE_FIELD, text)), Occur.MUST);
            return builder.build();
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
        QueryParser parser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, analyzer);
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
    
    public List<String> tokenize(Analyzer analyzer, String field, String keywords) throws IOException  {
        List<String> result = new ArrayList<String>();
        TokenStream stream  = analyzer.tokenStream(field, new StringReader(keywords));
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
        try{
            stream.reset();
            while(stream.incrementToken()){
                result.add(termAtt.toString());
            }
            stream.close();
        }finally{
            stream.close();
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
