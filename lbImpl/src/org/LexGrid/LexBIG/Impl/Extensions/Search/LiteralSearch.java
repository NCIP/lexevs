
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class LiteralSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LiteralSearch extends AbstractLiteralSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3232896063519315405L;
     
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(LiteralSearch.class.getName());
        ed
        .setDescription("All special characters are taken litterally.");
        ed.setName("literal");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();
        String escapedValue = excapeSpecialCharacters(searchText);
        try {
            return queryParser.parse(LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD + ":(" + escapedValue + ")");
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
    } 
}