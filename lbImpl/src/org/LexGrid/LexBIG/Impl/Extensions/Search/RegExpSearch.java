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
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexQuery;

/**
 * The Class RegExpSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegExpSearch extends AbstractSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3665438105387456725L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(RegExpSearch.class.getName());
        ed
        .setDescription("A Regular Expression query.  Searches against the lowercased text, so a regular expression that specifies an uppercase character will never return a match."
                + "  Additionally, this searches against the entire string as a single token, rather than the tokenized string - so write your regular expression accordingly."
                + "  Supported syntax is documented here: http://jakarta.apache.org/regexp/apidocs/org/apache/regexp/RE.html");
        ed.setName("RegExp");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query buildQuery(String searchText) {
            return new SerializableRegexQuery(new Term(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, 
                    searchText));    
    } 
}