/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.Impl.Extensions.Search.query.SpanWildcardQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.FieldMaskingSpanQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class AbstractSubStringSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSubStringSearch extends AbstractExactMatchBoostingSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /** The masking field. */
    protected String maskingField = LuceneLoaderCode.PROPERTY_VALUE_FIELD;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        searchText = searchText.toLowerCase();
        String[] tokens = super.tokenizeBySpace(searchText);

        SpanQuery[] spanTermQuery = new FieldMaskingSpanQuery[tokens.length];

        //There we can optimize if there are only 1 query
        int numberOfQueries = tokens.length;

        if(numberOfQueries == 1){
            
            
            return handleSingleTermQuery(tokens[0]);
        } else {
            for(int i=0;i<numberOfQueries;i++){
                Term term = null;
                if(i==0){
                    term = new Term(getFirstTermLuceneSearchField(), StringUtils.reverse(
                            tokens[i]) + "*");
                    spanTermQuery[i] = new FieldMaskingSpanQuery(new SpanWildcardQuery(term), maskingField);
                } else if (i == numberOfQueries -1){
                    term = new Term(getSubsequentTermLuceneSearchField(), tokens[i] + "*");
                    spanTermQuery[i] = new FieldMaskingSpanQuery(new SpanWildcardQuery(term), maskingField);
                } else {
                    term = new Term(getSubsequentTermLuceneSearchField(), tokens[i]);
                    spanTermQuery[i] = new FieldMaskingSpanQuery(new SpanTermQuery(term), maskingField);
                }
            }

            SpanNearQuery spanNearQuery = new SpanNearQuery(spanTermQuery, 0, true);

            return spanNearQuery;
        }
    } 
    
    protected Query handleSingleTermQuery(String term){
        return new WildcardQuery(new Term(getSubsequentTermLuceneSearchField(), "*" + term + "*"));
    }
    
    /**
     * Gets the first term lucene search field.
     * 
     * @return the first term lucene search field
     */
    public abstract String getFirstTermLuceneSearchField();
    
    /**
     * Gets the subsequent term lucene search field.
     * 
     * @return the subsequent term lucene search field
     */
    public abstract String getSubsequentTermLuceneSearchField();
}