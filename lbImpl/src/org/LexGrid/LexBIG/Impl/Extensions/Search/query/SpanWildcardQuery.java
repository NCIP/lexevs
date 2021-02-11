
package org.LexGrid.LexBIG.Impl.Extensions.Search.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.ToStringUtils;

/**
 * The Class SpanWildcardQuery.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SpanWildcardQuery extends SpanQuery {

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
            List<BooleanClause> clauses = booleanQuery.clauses();
            spanQueries = new SpanQuery[clauses.size()];
            for (int i = 0; i < clauses.size(); i++) {
                
                BooleanClause clause = clauses.get(i);
                // Clauses from RegexQuery.rewrite are always TermQuery's
                TermQuery tq = (TermQuery) clause.getQuery();
                spanQueries[i] = new SpanTermQuery(tq.getTerm());
                spanQueries[i].setBoost(tq.getBoost());
                
            }
            return new SpanOrQuery(spanQueries);
        } else {
            termQuery = (SpanOrQuery)rewritten;                        
        };
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
    public Collection<Term> getTerms() {
        Collection<Term> terms = new ArrayList<Term>();
        terms.add(term);
        return terms;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.search.Query#extractTerms(java.util.Set)
     */
    public void extractTerms(Set<Term> terms) {
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
    public SpanWeight createWeight(IndexSearcher arg0, boolean arg1) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}