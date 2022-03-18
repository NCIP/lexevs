
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
    
    private final ToParentBlockJoinQuery matchAllDocs;
    private final ToParentBlockJoinQuery codeExact;
    private final ToParentBlockJoinQuery presentationExact;
    private final ToParentBlockJoinQuery presentationContains;
    private final ToParentBlockJoinQuery propertyExact;
    private final ToParentBlockJoinQuery propertyContains;
    private final ToParentBlockJoinQuery lucene;
    
    
    private BuildMatchAlgorithmQuery(Builder build) {
        this.matchAllDocs = build.getMatchAllDocs();
        this.codeExact = build.getCodeExact();
        this.presentationExact = build.getPresentationExact();
        this.presentationContains = build.getPresentationContains();
        this.propertyExact = build.getPropertyExact();
        this.propertyContains = build.getPropertyContains();
        this.lucene = build.getLucene();
    }
    
    public static class Builder{
        private ToParentBlockJoinQuery matchAllDocs;
        private ToParentBlockJoinQuery codeExact;
        private ToParentBlockJoinQuery presentationExact;
        private ToParentBlockJoinQuery presentationContains;
        private ToParentBlockJoinQuery propertyExact;
        private ToParentBlockJoinQuery propertyContains;
        private ToParentBlockJoinQuery lucene;
        private boolean isAnon;
        private boolean isInActive;
        private boolean querySelected;
        private String matchText;
        private List<String> uris;
        
        public Builder(String matchText, 
                boolean includeAnonymous, 
                boolean isInActive,
                List<String> uris) {
            isAnon = includeAnonymous;
            this.isInActive = isInActive;
            this.matchText = matchText;
            this.uris = uris;
        }
        
        public BuildMatchAlgorithmQuery buildMatchQuery() {
            return new BuildMatchAlgorithmQuery(this);
        }
        
        public Builder matchAllDocs() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new MatchAllDocsQuery(), Occur.MUST);
            matchAllDocs = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }
        
        public Builder codeExact() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(new Term("code", matchText)),Occur.MUST);
            codeExact = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }
        
        public Builder presentationExact() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(PREFERRED), Occur.SHOULD);
            queryBuilder.add(new TermQuery(new Term(
                    LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, matchText.toLowerCase())), Occur.MUST);
            presentationExact = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }
        
        public Builder presentationContains() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(PREFERRED), Occur.SHOULD);
           matchText = matchText.toLowerCase();

            List<String> tokens;
            Analyzer tokenAnalyzer = new WhitespaceAnalyzer();
            try {
                tokens = tokenize(tokenAnalyzer, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, matchText);
            } catch (IOException e) {
               throw new RuntimeException("Tokenizing query text failed", e);
            }
            QueryParser parser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            for(String token : tokens){
                queryBuilder.add(new PrefixQuery(new Term(LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, token)), Occur.MUST);
            }
            matchText = QueryParser.escape(matchText);
            try {
                queryBuilder.add(parser.parse(matchText), Occur.SHOULD);
            } catch (ParseException e1) {
                throw new RuntimeException("Parser failed parsing matchText: " + matchText);
            }
            queryBuilder.add(new TermQuery(
                    new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD,
                    matchText)), Occur.SHOULD);
            presentationContains = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }
        
        public Builder propertyContains() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
           matchText = matchText.toLowerCase();

            List<String> tokens;
            Analyzer tokenAnalyzer = new WhitespaceAnalyzer();
            try {
                tokens = tokenize(tokenAnalyzer, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, matchText);
            } catch (IOException e) {
               throw new RuntimeException("Tokenizing query text failed", e);
            }

            for(String token : tokens){
                queryBuilder.add(new PrefixQuery(new Term(
                        LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, token)), 
                        Occur.MUST);
            }
            QueryParser parser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            matchText = QueryParser.escape(matchText);
            try {
                queryBuilder.add(parser.parse(matchText), Occur.SHOULD);
            } catch (ParseException e1) {
                throw new RuntimeException("Parser failed parsing matchText: " + matchText);
            }
            queryBuilder.add(new TermQuery(new Term(
                    LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, matchText)), 
                    Occur.SHOULD);
            propertyContains = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }


        public Builder propertyExact() {
            if(querySelected) {return this;}
                BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
                queryBuilder.add(new TermQuery(new Term(
                        LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, matchText.toLowerCase())), 
                        Occur.MUST);
                propertyExact = finalizeQuery(queryBuilder, queryBuilder.build());
                querySelected = true;
                return this;
        }
        
        public Builder lucene() {
            if(querySelected) {return this;}
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(new TermQuery(BASE_QUERY), Occur.MUST);
            queryBuilder.add(new TermQuery(PREFERRED), Occur.SHOULD);
            QueryParser luceneParser = new QueryParser(LuceneLoaderCode.PROPERTY_VALUE_FIELD, LuceneLoaderCode.getAnaylzer());
            Query query;
            try {
                query = luceneParser.parse(matchText);
            } catch (ParseException e) {
                throw new RuntimeException("Parser failed parsing matchText: " + matchText);
            }
            queryBuilder.add(query, Occur.MUST);
            lucene = finalizeQuery(queryBuilder, queryBuilder.build());
            querySelected = true;
            return this;
        }
        
        public String getMatchText() {
            return matchText;
        }

        public void setMatchText(String matchText) {
            this.matchText = matchText;
        }

        public ToParentBlockJoinQuery getMatchAllDocs() {
            return matchAllDocs;
        }

        public ToParentBlockJoinQuery getCodeExact() {
            return codeExact;
        }

        public ToParentBlockJoinQuery getPresentationExact() {
            return presentationExact;
        }

        public ToParentBlockJoinQuery getPresentationContains() {
            return presentationContains;
        }
        
        public ToParentBlockJoinQuery getPropertyExact() {
            return propertyExact;
        }

        public ToParentBlockJoinQuery getPropertyContains() {
            return propertyContains;
        }

        public ToParentBlockJoinQuery getLucene() {
            return lucene;
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
        
        private BooleanQuery.Builder addNonAnonymousQuery(BooleanQuery.Builder builder) {
            builder.add(new TermQuery(new Term("isAnonymous", "T")), Occur.MUST_NOT);
            return builder;
        }
        
        private BooleanQuery.Builder addValueSetCodingSchemeRestrictions(BooleanQuery.Builder builder) {
            if(uris == null || uris.size() == 0) {return builder;}
            BooleanQuery.Builder uriBuilder = new BooleanQuery.Builder();
            uris.forEach(uri -> uriBuilder.add(new TermQuery(
                    new Term("codingSchemeUri", uri)), Occur.SHOULD));
            builder.add(uriBuilder.build(), Occur.MUST);
            return builder;
        }
        
        private void setActiveAndAnonymousQueries(BooleanQuery.Builder builder, Query currentQuery) {
            if(! isAnon || ! isInActive){
                
                if(! isAnon){
                   addNonAnonymousQuery(builder);
                }
                if(! isInActive){
                   addInActiveQuery(builder);
                }
            }
        }
        
        private ToParentBlockJoinQuery getParentFilteredBlockJoinQuery(BooleanQuery.Builder builder, Query query){
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
            addValueSetCodingSchemeRestrictions(builder);
            return getParentFilteredBlockJoinQuery(builder, query);
        }

    }
   
    public Query getQuery() {
        return
            codeExact != null?codeExact:
            presentationExact != null?presentationExact:
            presentationContains != null?presentationContains:
            propertyExact != null?propertyExact:
            propertyContains != null?propertyContains:
            lucene != null?lucene:
            matchAllDocs;
    }


}