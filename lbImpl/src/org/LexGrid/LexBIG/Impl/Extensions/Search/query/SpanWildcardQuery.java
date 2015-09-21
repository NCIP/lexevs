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
package org.LexGrid.LexBIG.Impl.Extensions.Search.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.ToStringUtils;

/**
 * The Class SpanWildcardQuery.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SpanWildcardQuery extends SpanQuery {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6117703255777699916L;
    
    /** The term. */
    private Term term;

    /**
     * Instantiates a new span wildcard query.
     * 
     * @param term the term
     */
    public SpanWildcardQuery(Term term){
        this.term = term;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.Query#rewrite(org.apache.lucene.index.IndexReader)
     */
    public Query rewrite(IndexReader reader) throws IOException {
        WildcardQuery orig = new WildcardQuery(term);
        orig.setRewriteMethod(SpanMultiTermQueryWrapper.SCORING_SPAN_QUERY_REWRITE);

        Query rewritten = orig.rewrite(reader);
        SpanQuery[] spanQueries = null;
        SpanOrQuery termQuery = null;
        if(rewritten instanceof BooleanQuery){
            BooleanQuery booleanQuery = (BooleanQuery)rewritten;
            BooleanClause[] clauses = booleanQuery.getClauses();
            spanQueries = new SpanQuery[clauses.length];
            for (int i = 0; i < clauses.length; i++) {
                BooleanClause clause = clauses[i];

                // Clauses from RegexQuery.rewrite are always TermQuery's
                TermQuery tq = (TermQuery) clause.getQuery();

                spanQueries[i] = new SpanTermQuery(tq.getTerm());
                spanQueries[i].setBoost(tq.getBoost());
                return new SpanOrQuery(spanQueries);
            }

        } else {
            termQuery = (SpanOrQuery)rewritten;
//            SpanTermQuery spanTermQuery = new SpanTermQuery(termQuery.getTerm());
//            spanTermQuery.setBoost(termQuery.getBoost());

//           spanQueries = new SpanQuery[]{
 //                   spanTermQuery };                         
        }

 //       SpanOrQuery query = new SpanOrQuery(spanQueries);
        termQuery.setBoost(orig.getBoost());       

        return termQuery;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.spans.SpanQuery#getSpans(org.apache.lucene.index.IndexReader)
     */
    public Spans getSpans(IndexReader reader) throws IOException {
        throw new UnsupportedOperationException("Query should have been rewritten");
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.spans.SpanQuery#getField()
     */
    public String getField() {
        return term.field();
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.spans.SpanQuery#getTerms()
     */
    public Collection getTerms() {
        Collection terms = new ArrayList();
        terms.add(term);
        return terms;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.Query#extractTerms(java.util.Set)
     */
    public void extractTerms(Set terms) {
        terms.add(term);
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.Query#toString(java.lang.String)
     */
    public String toString(String field) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("spanWildcardQuery(");
        buffer.append(term);
        buffer.append(")");
        buffer.append(ToStringUtils.boost(getBoost()));
        return buffer.toString();
    }

   
    @Override
    public Spans getSpans(LeafReaderContext arg0, Bits arg1, Map<Term, TermContext> arg2) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}