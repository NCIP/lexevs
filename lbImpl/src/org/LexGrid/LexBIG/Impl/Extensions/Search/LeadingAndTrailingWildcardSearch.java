
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import java.util.StringTokenizer;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.springframework.util.StringUtils;

/**
 * The Class LeadingAndTrailingWildcardSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LeadingAndTrailingWildcardSearch extends AbstractExactMatchBoostingSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(LeadingAndTrailingWildcardSearch.class.getName());
        ed
        .setDescription("Equivalent to '*term*'.");
        ed.setName("LeadingAndTrailingWildcard");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        StringBuffer buffer = new StringBuffer();
        QueryParser queryParser = super.getQueryParser();
        
        StringTokenizer tokenizer = 
            new StringTokenizer
                (searchText, " ");

        while(tokenizer.hasMoreTokens()){
            buffer.append(
                    addLeadingAndTrailingWildcards(tokenizer.nextToken()));
            buffer.append(" ");
        }
        
        try {
            return queryParser.parse(
                    StringUtils.trimTrailingWhitespace(
                            buffer.toString()));
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
    } 
    
    /**
     * Adds the leading and trailing wildcards.
     * 
     * @param input the input
     * 
     * @return the string
     */
    protected String addLeadingAndTrailingWildcards(String input){
        if (!input.endsWith("*")) {
            input += "*";
        }
        
        if (!input.startsWith("*")) {
            input = "*" + input;
        }
        return input;
    }
}