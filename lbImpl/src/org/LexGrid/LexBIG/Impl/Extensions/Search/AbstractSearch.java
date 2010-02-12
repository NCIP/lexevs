/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.Search;

import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.IndexQueryParserFactory;
import org.apache.lucene.queryParser.QueryParser;
import org.lexevs.system.ResourceManager;
import org.springframework.util.StringUtils;

import edu.mayo.informatics.indexer.api.generators.QueryGenerator;

/**
 * The Class AbstractSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSearch extends AbstractExtendable implements Search {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8821994690004828366L;
    
    /** The query parser. */
    private QueryParser queryParser;
    
    /** The extra whitespace charaters. */
    private Set extraWhitespaceCharaters;

    /**
     * Instantiates a new abstract search.
     */
    protected AbstractSearch(){
        super();
        IndexQueryParserFactory queryParserFactory = IndexQueryParserFactory.getInstance();
        this.queryParser = queryParserFactory.getQueryProcessor();
        this.extraWhitespaceCharaters = queryParserFactory.getExtraWhitespaceCharaters();
    }
    
    /**
     * Register.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBException the LB exception
     */
    public void register() throws LBParameterException, LBException {
        
        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerSearchExtension(
                super.getExtensionDescription());
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
        return queryParser;
    }

    /**
     * Sets the query parser.
     * 
     * @param queryParser the new query parser
     */
    public void setQueryParser(QueryParser queryParser) {
        this.queryParser = queryParser;
    }
}
