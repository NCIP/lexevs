
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * The Class AbstractExactMatchSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExactMatchSearch extends AbstractSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9210430691844903074L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        return new TermQuery(new Term(getLuceneSearchField(), searchText.toLowerCase()));
    }
    
    /**
     * Gets the lucene search field.
     * 
     * @return the lucene search field
     */
    public abstract String getLuceneSearchField();
}