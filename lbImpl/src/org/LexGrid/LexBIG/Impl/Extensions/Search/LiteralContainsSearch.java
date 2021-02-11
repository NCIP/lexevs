
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class LiteralContainsSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LiteralContainsSearch extends AbstractContainsSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /** The lucene search field. */
    private static String luceneSearchField = LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(LiteralContainsSearch.class.getName());
        ed
        .setDescription("Equivalent to '* term* *' - in other words - a trailing wildcard on a term (but no leading wild card) and the term can appear at any position. Includes special characters.");
        ed.setName("literalContains");
        ed.setVersion("1.0");
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractContainsSearch#buildQuery(java.lang.String)
     */
    @Override
    public Query buildQuery(String searchText) {
        String excapedString = AbstractLiteralSearch.excapeSpecialCharacters(searchText);
        return super.buildQuery(searchText);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractContainsSearch#getLuceneSearchField()
     */
    @Override
    public String getLuceneSearchField() {
        return luceneSearchField;
    }    
}