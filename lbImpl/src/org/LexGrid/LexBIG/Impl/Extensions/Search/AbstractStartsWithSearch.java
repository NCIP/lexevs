
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;

/**
 * The Class AbstractStartsWithSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractStartsWithSearch extends AbstractExactMatchBoostingSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        return new PrefixQuery(new Term(getLuceneSearchField(), searchText.toLowerCase()));
    } 
    
    /**
     * Gets the lucene search field.
     * 
     * @return the lucene search field
     */
    public abstract String getLuceneSearchField();
}