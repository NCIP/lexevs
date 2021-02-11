
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class WeightedDoubleMetaphoneSearch.java.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SpellingErrorTolerantSubStringSearch extends AbstractLiteralSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(SpellingErrorTolerantSubStringSearch.class.getName());
        ed
        .setDescription("Adds Spelling-error tolerance to 'subString' search.");
        ed.setName("SpellingErrorTolerantSubStringSearch");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {  
        searchText = QueryParser.escape(searchText);
//        searchText = searchText.toLowerCase();
        String[] tokens = searchText.split(" ");
     
        BooleanQuery.Builder booleanBuilder = new BooleanQuery.Builder();
        
        String[] tokensWithoutSpecialChars = super.getTokensWithoutSpecialCharacters(tokens); 
        Query doubleMetaphoneQuery = buildSpanNearQuery(tokensWithoutSpecialChars, 
                LuceneLoaderCode.DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, 
                tokens.length - tokensWithoutSpecialChars.length, false);
        booleanBuilder.add(new BooleanClause(doubleMetaphoneQuery, Occur.SHOULD));
        
        Query literalQuery = buildSpanNearQuery(tokens, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, 0, true);
  
        booleanBuilder.add(new BooleanClause(literalQuery, Occur.SHOULD));
        
        String[] specialCharacterTokens = getTokensWithSpecialCharacters(tokens);
        
        if(specialCharacterTokens != null && specialCharacterTokens.length > 0){
            Query literalRequiredQuery = buildSpanNearQuery(specialCharacterTokens, LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD, tokens.length, true);
            booleanBuilder.add(new BooleanClause(literalRequiredQuery, Occur.MUST));
        }

        return booleanBuilder.build();
    } 
    
    /**
     * Builds the span near query.
     * 
     * @param tokens the tokens
     * @param luceneSearchField the lucene search field
     * @param slop the slop
     * @param inOrder the in order
     * 
     * @return the query
     */
    protected Query buildSpanNearQuery(String[] tokens, String luceneSearchField, int slop, boolean boostLiteral) {

        PhraseQuery.Builder builder = new PhraseQuery.Builder();

        for (int i = 0; i < tokens.length; i++) {
            Query parsedQuery = null;
            try {
                parsedQuery = super.getQueryParser().parse(luceneSearchField + ":( " + tokens[i] + ")");
                parsedQuery.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (parsedQuery instanceof BooleanQuery) {
                BooleanQuery booleanQuery = (BooleanQuery) parsedQuery;                
                TermQuery tq = null;
                for (BooleanClause clause : booleanQuery.clauses()) {

                    if (clause.getQuery() instanceof BooleanQuery) {
                        tq = (TermQuery) clause.getQuery();
                    } else {
                        tq = (TermQuery) clause.getQuery();
                    }
                    builder.add(tq.getTerm());
                }
            } else {
                TermQuery tq = (TermQuery) parsedQuery;
                builder.add(tq.getTerm());
            }

        }
        builder.setSlop(slop);
        PhraseQuery returnQuery = builder.build();
        if (boostLiteral) {
            returnQuery.setBoost(0.5f);
        }
        return returnQuery;
    }
}