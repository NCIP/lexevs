
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class LiteralSubStringSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LiteralSubStringSearch extends AbstractSubStringSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(LiteralSubStringSearch.class.getName());
        ed
        .setDescription("Search based on a \"*some sub-string here*\". Functions much like the Java String.indexOf method. Special characters are included.");
        ed.setName("literalSubString");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractSubStringSearch#getFirstTermLuceneSearchField()
     */
    @Override
    public String getFirstTermLuceneSearchField() {
        return LuceneLoaderCode.LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractSubStringSearch#getSubsequentTermLuceneSearchField()
     */
    @Override
    public String getSubsequentTermLuceneSearchField() {
        return LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD;
    }   
}