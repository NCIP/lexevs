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
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class NonLeadingWildcardLiteralSubStringSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NonLeadingWildcardLiteralSubStringSearch extends LiteralSubStringSearch {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(NonLeadingWildcardLiteralSubStringSearch.class.getName());
        ed
        .setDescription("Search based on a \"*some sub-string here*\". " +
        		"Functions much like the Java String.indexOf method. " +
        		"Singe term searches will match '*term' and 'term*' but" +
        		"not '*term*'. This is because leading wildcards are very inefficient." +
        		"Special characters are included.");
        ed.setName("nonLeadingWildcardLiteralSubString");
        ed.setVersion("1.0");
        return ed;
    }

    @Override
    protected Query handleSingleTermQuery(String term) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(             
                new WildcardQuery(
                        new Term(
                                LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD,
                                term + "*")), Occur.SHOULD);

        builder.add(
                new WildcardQuery(
                        new Term(
                                LuceneLoaderCode.LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD,
                                StringUtils.reverse(term) + "*")), Occur.SHOULD);
       
        return builder.build();
    }
}