/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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