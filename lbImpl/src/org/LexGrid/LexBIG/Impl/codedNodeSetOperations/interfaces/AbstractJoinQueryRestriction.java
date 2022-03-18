
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.lexevs.exceptions.InternalException;

public abstract class AbstractJoinQueryRestriction implements Restriction {

/**
     * 
     */
private static final long serialVersionUID = 9067083083894835005L;

    @Override
    public final Query getQuery() throws LBException, InternalException {
        TermQuery termQuery = new TermQuery(new Term("isParentDoc", "true"));

        ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(
                this.doGetQuery(), new QueryBitSetProducer(termQuery), ScoreMode.Total);

        return blockJoinQuery;
    }

    protected abstract Query doGetQuery() throws LBException, InternalException;

}