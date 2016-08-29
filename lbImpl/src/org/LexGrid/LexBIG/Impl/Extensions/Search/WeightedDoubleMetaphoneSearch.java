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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/**
 * The Class WeightedDoubleMetaphoneSearch.java.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class WeightedDoubleMetaphoneSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(WeightedDoubleMetaphoneSearch.class.getName());
        ed
        .setDescription("Search with the Lucene query syntax, using a 'sounds like' algorithm.  A search for 'atack' will get a hit on 'attack'  See http://lucene.apache.org/java/docs/queryparsersyntax.html). " +
        		"Also, the exact user-entered text is taken into account -- so correct spelling will override the 'sounds like' algorithm.");
        ed.setName("WeightedDoubleMetaphoneLuceneQuery");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
        QueryParser queryParser = super.getQueryParser();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        Query query;
        try {
            query = queryParser.parse("dm_propertyValue:(" + searchText + ")");

            builder.add(new BooleanClause(query, BooleanClause.Occur.MUST));

            Query realTextQuery = queryParser.parse("propertyValue:(" + searchText + ")");
            builder.add(new BooleanClause(realTextQuery, BooleanClause.Occur.SHOULD));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    } 
}