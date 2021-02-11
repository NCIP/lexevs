
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * The Class AbstractLiteralSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLiteralSearch extends AbstractSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3232896063519315405L;
    
    /** The Constant LUCENE_ESCAPE_CHARS. */
    private static final String LUCENE_ESCAPE_CHARS = "[\\\\+\\-\\!\\(\\)\\:\\^\\[\\]\\{\\}\\~\\*\\?]";
    
    /** The Constant LUCENE_PATTERN. */
    private static final Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
    
    /** The Constant REPLACEMENT_STRING. */
    private static final String REPLACEMENT_STRING = "\\\\$0";
     
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        return doBuildQuery(searchText);
    } 
    
    /**
     * Do build query.
     * 
     * @param searchText the search text
     * 
     * @return the query
     */
    public abstract Query doBuildQuery(String searchText);
    
    /**
     * Excape special characters.
     * 
     * @param searchText the search text
     * 
     * @return the string
     */
    protected static String excapeSpecialCharacters(String searchText){
        return QueryParser.escape(searchText);
    }
    
    /**
     * Does search string contain special characters.
     * 
     * @param searchString the search string
     * 
     * @return true, if successful
     */
    protected boolean doesSearchStringContainSpecialCharacters(String searchString){
        return searchString.contains("\\");
    }
    
    /**
     * Gets the tokens with special characters.
     * 
     * @param tokens the tokens
     * 
     * @return the tokens with special characters
     */
    protected String[] getTokensWithSpecialCharacters(String[] tokens){
        String[] returnArray = new String[0];
        
        for(String token : tokens){
            if(doesSearchStringContainSpecialCharacters(token)){
                returnArray = (String[])ArrayUtils.add(returnArray, token);
            }
        }
        return returnArray;
    }
    
    /**
     * Gets the tokens without special characters.
     * 
     * @param tokens the tokens
     * 
     * @return the tokens without special characters
     */
    protected String[] getTokensWithoutSpecialCharacters(String[] tokens){
        String[] returnArray = new String[0];
        
        for(String token : tokens){
            if(!doesSearchStringContainSpecialCharacters(token)){
                returnArray = (String[])ArrayUtils.add(returnArray, token);
            }
        }
        return returnArray;
    }
}