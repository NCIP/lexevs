
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;

/**
 * The Class AbstractExactMatchBoostingSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExactMatchBoostingSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    public Query buildQuery(String searchText) {
        Query specificAlgorithmQuery = doBuildQuery(searchText);
        
        Query standardLuceneQuery = new LiteralSearch().buildQuery(searchText);
        standardLuceneQuery.setBoost(50.0f);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        
        builder.add(specificAlgorithmQuery, Occur.MUST);
        builder.add(standardLuceneQuery, Occur.SHOULD);
        
        return builder.build();
    } 
    
    public abstract Query doBuildQuery(String searchText);
}