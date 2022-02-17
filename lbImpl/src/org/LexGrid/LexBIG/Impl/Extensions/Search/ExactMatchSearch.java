
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class ExactMatchSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExactMatchSearch extends AbstractExactMatchSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9210430691844903074L;

    /** The lucene search field. */
    private static String luceneSearchField = LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(ExactMatchSearch.class.getName());
        ed.setDescription("Exact match (case insensitive)");
        ed.setName("exactMatch");
        ed.setVersion("1.0");
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractExactMatchSearch#getLuceneSearchField()
     */
    @Override
    public String getLuceneSearchField() {
        return luceneSearchField;
    }
}