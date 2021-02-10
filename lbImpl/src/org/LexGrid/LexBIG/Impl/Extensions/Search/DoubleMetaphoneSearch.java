
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class DoubleMetaphoneSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DoubleMetaphoneSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(DoubleMetaphoneSearch.class.getName());
        ed
        .setDescription("Search with the Lucene query syntax, using a 'sounds like' algorithm.  A search for 'atack' will get a hit on 'attack'  See http://lucene.apache.org/java/docs/queryparsersyntax.html)");
        ed.setName("DoubleMetaphoneLuceneQuery");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();
        
        try {
            return queryParser.parse(
                    LuceneLoaderCode.DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD + 
                    ":(" + 
//                            this.handleWhiteSpaceCharacters(
                                    searchText
//                                    ) 
                                    + ")");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    } 
}