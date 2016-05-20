package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;
    private static final Term baseQuery = new Term("propertyType", "presentation");
    private static final Term preferred = new Term("isPreferred","T");

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
                true);
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
        
        codeSystemsToInclude = sanitizeReferences(codeSystemsToInclude);
        codeSystemsToExclude = sanitizeReferences(codeSystemsToExclude);
        
        Set<CodingSchemeReference> tempSystemsToInclude = new HashSet<CodingSchemeReference>();
        for(RegistryEntry entry : entries){
            CodingSchemeReference ref = new CodingSchemeReference();
            ref.setCodingScheme(entry.getResourceUri());
            ref.setVersionOrTag(
                    Constructors.createCodingSchemeVersionOrTagFromVersion(entry.getResourceVersion()));
            if(! entry.getStatus().equals(CodingSchemeVersionStatus.ACTIVE.toString())){

                if(codeSystemsToExclude == null){
                    codeSystemsToExclude  = new HashSet<CodingSchemeReference>();
                }
                codeSystemsToExclude.add(ref);
            }
            tempSystemsToInclude.add(ref);
        }
        
        //We'll want any and all systems if this contains none.
        if(codeSystemsToInclude == null){
            codeSystemsToInclude = tempSystemsToInclude;
        }
        
        SearchIndexService service = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService();

        Query query = this.buildOnMatchAlgorithm(text, matchAlgorithm);
        BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();

        if(! includeAnonymous || ! includeInactive){

            newBuilder.add(query, Occur.MUST);
            
            if(! includeAnonymous){
                newBuilder.add(new TermQuery(new Term("isAnonymous", "T")), Occur.MUST_NOT);
            }
            if(! includeInactive){
                newBuilder.add(new TermQuery(new Term("isActive", "F")), Occur.MUST_NOT);
            }
        }

        newBuilder.add(query, Occur.MUST);
        newBuilder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);

        query = newBuilder.build();

        QueryBitSetProducer parentFilter;
        try {
            parentFilter = new QueryBitSetProducer(new QueryParser("isParentDoc", 
                    new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
        } catch (ParseException e) {
            throw new RuntimeException("Query Parser Failed against parent query: ", e);
        }
        ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(
                query, parentFilter, ScoreMode.Total);

        if(codeSystemsToExclude != null &&
                codeSystemsToInclude.size() > 0 && 
                codeSystemsToExclude.size() > 0){
        codeSystemsToInclude.removeAll(codeSystemsToExclude);
        }
        Set<AbsoluteCodingSchemeVersionReference> codeSystemRefs = this.resolveCodeSystemReferences(codeSystemsToInclude);
        List<ScoreDoc> scoreDocs = lexEvsServiceLocator.
                getIndexServiceManager().
                getSearchIndexService().
                query(codeSystemRefs, 
                        blockJoinQuery);
        return new SearchScoreDocIterator( codeSystemRefs, scoreDocs);
    }
    
    protected BooleanQuery buildOnMatchAlgorithm(String text, MatchAlgorithm matchAlgorithm){
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if(StringUtils.isBlank(text))
        {
            builder.add(new MatchAllDocsQuery(), Occur.MUST);
            return builder.build();
        }
        switch(matchAlgorithm){
        case PRESENTATION_EXACT:
            builder.add(new TermQuery(baseQuery), Occur.MUST);
            builder.add(new TermQuery(new Term(
                    LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,text.toLowerCase())), Occur.MUST);
            return  builder.build();
        case CODE_EXACT:
            builder.add(new TermQuery(new Term("code",text)),Occur.MUST);
            return builder.build();
        case PRESENTATION_CONTAINS:
            builder.add(new TermQuery(baseQuery), Occur.MUST);
            builder.add(new TermQuery(preferred), Occur.SHOULD);
           text = text.toLowerCase();

            List<String> tokens;
            Analyzer tokenAnalyzer = new WhitespaceAnalyzer();
            try {
                tokens = tokenize(tokenAnalyzer, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, text);
            } catch (IOException e) {
               throw new RuntimeException("Tokenizing query text failed", e);
            }
            QueryParser parser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            for(String token : tokens){
                builder.add(new PrefixQuery(new Term(LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, token)), Occur.MUST);
            }
            text = QueryParser.escape(text);
            try {
                builder.add(parser.parse(text), Occur.SHOULD);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            builder.add(new TermQuery(new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,text)), Occur.SHOULD);
            return builder.build();
        case LUCENE:
            builder.add(new TermQuery(baseQuery), Occur.MUST);
            builder.add(new TermQuery(preferred), Occur.SHOULD);
            QueryParser luceneParser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            Query query;
            try {
                query = luceneParser.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException("Parser failed parsing text: " + text);
            }
            builder.add(query, Occur.MUST);
            return builder.build();
        default:
            throw new IllegalStateException("Unrecognized MatchAlgorithm: " + matchAlgorithm.name());
        }
    }

    protected Set<AbsoluteCodingSchemeVersionReference> 
        resolveCodeSystemReferences(Set<CodingSchemeReference> references) throws LBParameterException{
        if(CollectionUtils.isEmpty(references)){
            return null;
        }
        
        Set<AbsoluteCodingSchemeVersionReference> returnSet = new HashSet<AbsoluteCodingSchemeVersionReference>();
        ConcurrentMetaData metadata = ConcurrentMetaData.getInstance();
        for(CodingSchemeReference ref : references){
        	CodingSchemeMetaData csm = metadata.getCodingSchemeMetaDataForNameAndVersion(ref.getCodingScheme(), ref.getVersionOrTag().getVersion());
        	if(csm == null){csm = metadata.getCodingSchemeMetaDataForUriAndVersion(ref.getCodingScheme(), ref.getVersionOrTag().getVersion());}
        	if(csm == null){ continue;}
        	if((ref.getCodingScheme().equals(csm.getCodingSchemeName()) || ref.getCodingScheme().equals(csm.getCodingSchemeUri()) 
        			&& ref.getVersionOrTag().getVersion().equals(csm.getCodingSchemeVersion()))){
            returnSet.add(csm.getRef());
        	}
        }
        return returnSet;
    }
    
    private Set<CodingSchemeReference> sanitizeReferences(Set<CodingSchemeReference> references) throws LBParameterException{
        if(CollectionUtils.isEmpty(references)){
            return null;
        }
        Set<CodingSchemeReference> tempReferences = new HashSet<CodingSchemeReference>();
        for(CodingSchemeReference ref: references){
            AbsoluteCodingSchemeVersionReference abc = ServiceUtility.getAbsoluteCodingSchemeVersionReference(
                    ref.getCodingScheme(),
                    ref.getVersionOrTag(), 
                    true);
                    if(!ref.equals(abc.getCodingSchemeURN())){
                        ref.setCodingScheme(abc.getCodingSchemeURN());
                    }
                    if(ref.getVersionOrTag() == null) {
                        ref.setVersionOrTag(new CodingSchemeVersionOrTag());
                    }
                    if(  ref.getVersionOrTag().getVersion() == null){
                        ref.getVersionOrTag().setVersion(abc.getCodingSchemeVersion());
                    }
                    tempReferences.add(ref);
        }
        return tempReferences;
    }
    
    public List<String> tokenize(Analyzer analyzer, String field, String keywords) throws IOException  {
        List<String> result = new ArrayList<String>();
        StringReader reader = new StringReader(keywords);
        TokenStream stream  = analyzer.tokenStream(field, reader);
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
