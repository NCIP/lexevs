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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class WeightedDoubleMetaphoneSearch.java.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SpellingErrorTolerantSubStringSearch extends AbstractLiteralSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(SpellingErrorTolerantSubStringSearch.class.getName());
        ed
        .setDescription("Adds Spelling-error tolerance to 'subString' search.");
        ed.setName("SpellingErrorTolerantSubStringSearch");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {    
        searchText = searchText.toLowerCase();
        String[] tokens = searchText.split(" ");
     
        BooleanQuery booleanQuery = new BooleanQuery();
        
        String[] tokensWithoutSpecialChars = super.getTokensWithoutSpecialCharacters(tokens); 
        Query doubleMetaphoneQuery = buildSpanNearQuery(tokensWithoutSpecialChars, 
                LuceneLoaderCode.DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, 
                tokens.length - tokensWithoutSpecialChars.length, true);
        booleanQuery.add(new BooleanClause(doubleMetaphoneQuery, Occur.SHOULD));
        
        Query literalQuery = buildSpanNearQuery(tokens, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, 0, true);
  
        booleanQuery.add(new BooleanClause(literalQuery, Occur.SHOULD));
        
        String[] specialCharacterTokens = getTokensWithSpecialCharacters(tokens);
        
        if(specialCharacterTokens != null && specialCharacterTokens.length > 0){
            Query literalRequiredQuery = buildSpanNearQuery(specialCharacterTokens, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, tokens.length, true);
            booleanQuery.add(new BooleanClause(literalRequiredQuery, Occur.MUST));
        }

        return booleanQuery;
    } 
    
    /**
     * Builds the span near query.
     * 
     * @param tokens the tokens
     * @param luceneSearchField the lucene search field
     * @param slop the slop
     * @param inOrder the in order
     * 
     * @return the query
     */
    protected Query buildSpanNearQuery(String[] tokens, String luceneSearchField, int slop, boolean inOrder){
        SpanQuery[] spanQuery = new SpanQuery[tokens.length];
        
        for(int i=0;i<tokens.length;i++){
            try {
                TermQuery termQuery = (TermQuery)super.getQueryParser().parse(luceneSearchField + ":( " + tokens[i] + ")");
                spanQuery[i] = new SpanTermQuery(termQuery.getTerm());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        SpanNearQuery spanNearQuery = new SpanNearQuery(spanQuery, slop, inOrder);
        return spanNearQuery;
    }
}