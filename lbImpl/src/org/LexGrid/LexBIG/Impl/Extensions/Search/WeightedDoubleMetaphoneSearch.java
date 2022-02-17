
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/**
 * The Class WeightedDoubleMetaphoneSearch.java.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class WeightedDoubleMetaphoneSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(WeightedDoubleMetaphoneSearch.class.getName());
        ed
        .setDescription("Search with the Lucene query syntax, using a 'sounds like' algorithm.  A search for 'atack' will get a hit on 'attack'  See http://lucene.apache.org/java/docs/queryparsersyntax.html). " +
        		"Also, the exact user-entered text is taken into account -- so correct spelling will override the 'sounds like' algorithm.");
        ed.setName("WeightedDoubleMetaphoneLuceneQuery");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        Query query;
        try {
            query = queryParser.parse("dm_propertyValue:(" + searchText + ")");

            builder.add(new BooleanClause(query, BooleanClause.Occur.MUST));

            Query realTextQuery = queryParser.parse("propertyValue:(" + searchText + ")");
            builder.add(new BooleanClause(realTextQuery, BooleanClause.Occur.SHOULD));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    } 
}