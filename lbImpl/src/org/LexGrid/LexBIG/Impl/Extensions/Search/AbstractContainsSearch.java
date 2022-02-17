
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * The Class AbstractContainsSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractContainsSearch extends AbstractExactMatchBoostingSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();
        searchText = QueryParser.escape(searchText);
        searchText = super.addTrailingWildcardToAllTokens(searchText);
        try {
            return queryParser.parse(getLuceneSearchField() + ":(" + searchText + ")");
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
    } 
    
    /**
     * Gets the lucene search field.
     * 
     * @return the lucene search field
     */
    public abstract String getLuceneSearchField();
}