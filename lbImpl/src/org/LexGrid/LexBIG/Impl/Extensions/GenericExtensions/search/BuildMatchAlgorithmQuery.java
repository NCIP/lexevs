package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class BuildMatchAlgorithmQuery {
    private BooleanQuery matchAllDocs;
    private BooleanQuery codeExact;
    private BooleanQuery presentationExact;
    private BooleanQuery presentationContains;
    private BooleanQuery propertyContains;
    private BooleanQuery lucene;
    
    
    private BuildMatchAlgorithmQuery() {
        
    }
    
    public class Builder{
        private BuildMatchAlgorithmQuery algorithmMatchBuilder;
        
        public Builder() {
            algorithmMatchBuilder = new BuildMatchAlgorithmQuery();
        }
        
        public BuildMatchAlgorithmQuery buildMatchQuery() {
            return new BuildMatchAlgorithmQuery();
        }
        
        public Builder matchAllDocs() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setMatchAllDocs(queryBuilder.build());
            return this;
        }
        
        public Builder codeExact() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setCodeExact(queryBuilder.build());
            return this;
        }
        
        public Builder presentationExact() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setPresentationExact(queryBuilder.build());
            return this;
        }
        
        public Builder presentationContains() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setPresentationContains(queryBuilder.build());
            return this;
        }
        
        public Builder propertyContains() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setPropertyContains(queryBuilder.build());
            return this;
        }
        
        public Builder lucene() {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            algorithmMatchBuilder.setLucene(queryBuilder.build());
            return this;
        }
    }
    
    public void setMatchAllDocs(BooleanQuery query) {
      matchAllDocs = query;
    }
    
    private void setCodeExact(BooleanQuery query) {
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
