
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.dataAccess.IndexQueryParserFactory;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.lexevs.dao.indexer.api.generators.QueryGenerator;
import org.springframework.util.StringUtils;

/**
 * The Class AbstractSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSearch extends AbstractExtendable implements Search {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8821994690004828366L;
    
    /** The extra whitespace characters. */
    private Set extraWhitespaceCharaters;

    /**
     * Instantiates a new abstract search.
     */
    protected AbstractSearch(){
        super();
        IndexQueryParserFactory queryParserFactory = new IndexQueryParserFactory();
        this.extraWhitespaceCharaters = queryParserFactory.getExtraWhitespaceCharaters();
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerSearchExtension(description);
    }
    
    /*
     * white space characters need special treatment before going into the query
     * parser - needs to align with what was done during indexing. Also, I need
     * to escape ':' for them if they provided a colon but didn't escape it.
     */
    /**
     * Handle white space characters.
     * 
     * @param query the query
     * 
     * @return the string
     */
    protected String handleWhiteSpaceCharacters(String query) {
        int pos = query.indexOf(':');
        if (pos > 0) {
            StringBuffer temp = new StringBuffer(query);
            while (pos > 0) {
                if (temp.charAt(pos - 1) != '\\') {
                    temp.insert(pos, '\\');
                    pos++;
                }
                pos++;
                if (pos > temp.length()) {
                    pos = -1;
                } else {
                    pos = temp.indexOf(":", pos);
                }
            }
            query = temp.toString();
        }
        return QueryGenerator.removeExtraWhiteSpaceCharacters(query, extraWhitespaceCharaters);
    }
    
    /**
     * Tokenize by space.
     * 
     * @param searchString the search string
     * 
     * @return the string[]
     */
    protected String[] tokenizeBySpace(String searchString){
        return searchString.split(" ");
    }
    
    /**
     * Adds the trailing wildcard.
     * 
     * @param tokens the tokens
     * 
     * @return the string[]
     */
    protected String[] addTrailingWildcard(String[] tokens){
        String[] returnArray = new String[tokens.length];
        for(int i=0;i<tokens.length;i++){
            String token = tokens[i];
            if(!token.endsWith("*")) {
                token += "*";
            }
            returnArray[i] = token;
        }
        return returnArray;
    }
    
    /**
     * Adds the trailing wildcard to all tokens.
     * 
     * @param searchString the search string
     * 
     * @return the string
     */
    protected String addTrailingWildcardToAllTokens(String searchString){
       String[] tokens = addTrailingWildcard(
               tokenizeBySpace(searchString));
       StringBuffer buffer = new StringBuffer();
       for(String token : tokens){
           buffer.append(token);
           buffer.append(" ");
       }
       return StringUtils.trimTrailingWhitespace(buffer.toString());
    }

    /**
     * Gets the query parser.
     * 
     * @return the query parser
     */
    public QueryParser getQueryParser() {
        IndexQueryParserFactory queryParserFactory = new IndexQueryParserFactory();
        
        return queryParserFactory.getQueryProcessor();
    }
}