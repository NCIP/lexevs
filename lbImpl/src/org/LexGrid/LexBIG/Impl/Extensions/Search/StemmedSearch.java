
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class StemmedSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StemmedSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(StemmedSearch.class.getName());
        ed
        .setDescription("Search with the Lucene query syntax, using stemmed terms.  A search for 'trees' will get a hit on 'tree'  See http://lucene.apache.org/java/docs/queryparsersyntax.html)");
        ed.setName("StemmedLuceneQuery");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();

        try {
            return queryParser.parse(LuceneLoaderCode.STEMMING_PROPERTY_VALUE_FIELD + ":(" + 
//        super.handleWhiteSpaceCharacters(
                searchText
//                )
                + ")");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    } 
}