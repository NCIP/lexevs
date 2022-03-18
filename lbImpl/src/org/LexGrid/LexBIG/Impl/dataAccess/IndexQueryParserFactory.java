
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.indexer.api.generators.QueryGenerator;
import org.lexevs.logging.LoggerFactory;

/**
 * Build the query parser to use for parsing the text portion of a user query.
 * One parser is shared across all indexes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexQueryParserFactory {

    private QueryParser parser_;
    private Set extraWhiteSpaceChars_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }   

    public IndexQueryParserFactory() {
        parser_ = new QueryParser(SQLTableConstants.TBLCOL_PROPERTYVALUE, LuceneLoaderCode.getAnaylzer());
        
        //Allow leading wildcards for searches.
        parser_.setAllowLeadingWildcard(true);
        
        parser_.setDefaultOperator(QueryParser.AND_OPERATOR);
    }

    /*
     * white space characters need special treatment before going into the query
     * parser - needs to align with what was done during indexing. Also, I need
     * to escape ':' for them if they provided a colon but didn't escape it.
     */
    private String handleWhiteSpaceCharacters(String query) {
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
        return QueryGenerator.removeExtraWhiteSpaceCharacters(query, extraWhiteSpaceChars_);
    }

    /*
     * special method for use in constructing a query that searches by concept
     * code
     */
    public Query parseQueryForField(String field, String text) throws LBParameterException {
        try {
            String modifiedMatchText = handleWhiteSpaceCharacters(text);
            synchronized (parser_) {
                return parser_.parse(field + ":(" + modifiedMatchText + ")");
            }
        } catch (ParseException e) {
            throw new LBParameterException("Invalid match text" + text, "matchText", text);
        }
    }
    
    public QueryParser getQueryProcessor(){
        return this.parser_;
    }
        
    public Set getExtraWhitespaceCharaters(){
        return this.extraWhiteSpaceChars_;
    }
}