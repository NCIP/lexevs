package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

public class BuildMatchAlgorithmQuery {
    
    private static final Term BASE_QUERY = new Term("propertyType", "presentation");
    private static final Term PREFERRED = new Term("isPreferred","T");
    
    private BooleanQuery matchAllDocs;
    private ToParentBlockJoinQuery codeExact;
    private BooleanQuery presentationExact;
    private BooleanQuery presentationContains;
    private BooleanQuery propertyContains;
    private BooleanQuery lucene;
    private String text;
    
    
    private BuildMatchAlgorithmQuery(String text) {
        this.text = text;
    }
    
    public class Builder{
        private BuildMatchAlgorithmQuery algorithmMatchBuilder;
        private boolean isAnon;
        private boolean isInActive;
        
        public Builder(String text, boolean includeAnonymous, boolean isInActive) {
            isAnon = includeAnonymous;
            this.isInActive = isInActive;
            algorithmMatchBuilder = new BuildMatchAlgorithmQuery(text);
        }
        
        public BuildMatchAlgorithmQuery buildMatchQuery() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new MatchAllDocsQuery(), Occur.MUST);
            algorithmMatchBuilder.setMatchAllDocs(queryBuilder.build());
            return algorithmMatchBuilder;
        }
        
        public Builder codeExact() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(new Term("code",text)),Occur.MUST);
            algorithmMatchBuilder.setCodeExact(finalizeQuery(queryBuilder, queryBuilder.build()));
            return this;
        }
        
        public Builder presentationExact() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(new Term(
                    LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,text.toLowerCase())), Occur.MUST);
            algorithmMatchBuilder.setPresentationExact(queryBuilder.build());
            return this;
        }
        
        public Builder presentationContains() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(PREFERRED), Occur.SHOULD);
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
                queryBuilder.add(new PrefixQuery(new Term(LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, token)), Occur.MUST);
            }
            text = QueryParser.escape(text);
            try {
                queryBuilder.add(parser.parse(text), Occur.SHOULD);
            } catch (ParseException e1) {
                throw new RuntimeException("Parser failed parsing text: " + text);
            }
            queryBuilder.add(new TermQuery(new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,text)), Occur.SHOULD);
            algorithmMatchBuilder.setPresentationContains(queryBuilder.build());
            return this;
        }
        
        public Builder propertyContains() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
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
                queryBuilder.add(new PrefixQuery(new Term(LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, token)), Occur.MUST);
            }
            text = QueryParser.escape(text);
            try {
                queryBuilder.add(parser.parse(text), Occur.SHOULD);
            } catch (ParseException e1) {
                throw new RuntimeException("Parser failed parsing text: " + text);
            }
            queryBuilder.add(new TermQuery(new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,text)), Occur.SHOULD);
            algorithmMatchBuilder.setPropertyContains(queryBuilder.build());
            return this;
        }
        
        public Builder lucene() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(PREFERRED), Occur.SHOULD);
            QueryParser luceneParser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            Query query;
            try {
                query = luceneParser.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException("Parser failed parsing text: " + text);
            }
            queryBuilder.add(query, Occur.MUST);
            algorithmMatchBuilder.setLucene(queryBuilder.build());
            return this;
        }
        
        private List<String> tokenize(Analyzer analyzer, String field, String keywords) throws IOException  {
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
        
        private BooleanQuery.Builder addInActiveQuery(BooleanQuery.Builder builder) {
            builder.add(new TermQuery(new Term("isActive", "F")), Occur.MUST_NOT);
            return builder;
        }
        
        private BooleanQuery.Builder addAnonymousQuery(BooleanQuery.Builder builder) {
            builder.add(new TermQuery(new Term("isAnonymous", "T")), Occur.MUST_NOT);
            return builder;
        }
        
        private void setActiveAndAnonymousQueries(BooleanQuery.Builder builder, Query currentQuery) {
            if(! isAnon || ! isInActive){

                builder.add(currentQuery, Occur.MUST);
                
                if(! isAnon){
                   addAnonymousQuery(builder);
                }
                if(! isInActive){
                   addInActiveQuery(builder);
                }
            }
        }
        
        private ToParentBlockJoinQuery getParentFilteredBlockJoinQuery(BooleanQuery.Builder builder, Query query){
            builder.add(query, Occur.MUST);
            builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);

            query = builder.build();

            QueryBitSetProducer parentFilter;
            try {
                parentFilter = new QueryBitSetProducer(new QueryParser("isParentDoc", 
                        new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
            } catch (ParseException e) {
                throw new RuntimeException("Query Parser Failed against parent query: ", e);
            }
            return new ToParentBlockJoinQuery(
                    query, parentFilter, ScoreMode.Total);
        }
        
        private ToParentBlockJoinQuery finalizeQuery(BooleanQuery.Builder builder, Query query){
            setActiveAndAnonymousQueries(builder, query);
            return getParentFilteredBlockJoinQuery(builder, query);
        }

    }
    

    
    public void setMatchAllDocs(BooleanQuery query) {
      matchAllDocs = query;
    }
    
    private void setCodeExact(ToParentBlockJoinQuery query) {
        codeExact = query;
    }

    private void setPresentationExact(BooleanQuery query) {
        presentationExact = query;
    }
    
    private void setPresentationContains(BooleanQuery query) {
        presentationContains = query;
    }
    
    public void setPropertyContains(BooleanQuery query) {
        propertyContains = query;
    }
    
    private void setLucene(BooleanQuery query) {
        lucene = query;
    }
    
    public Query getQuery() {
        return
            codeExact != null?codeExact:
            presentationExact != null?presentationExact:
            presentationContains != null?presentationContains:
            propertyContains != null?propertyContains:
            lucene != null?lucene:
            matchAllDocs;
    }
}
