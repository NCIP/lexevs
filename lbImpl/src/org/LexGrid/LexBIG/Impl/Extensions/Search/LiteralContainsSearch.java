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
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class LiteralContainsSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LiteralContainsSearch extends AbstractContainsSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /** The lucene search field. */
    private static String luceneSearchField = LuceneLoaderCode.LITERAL_PROPERTY_VALUE_FIELD;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(LiteralContainsSearch.class.getName());
        ed
        .setDescription("Equivalent to '* term* *' - in other words - a trailing wildcard on a term (but no leading wild card) and the term can appear at any position. Includes special characters.");
        ed.setName("literalContains");
        ed.setVersion("1.0");
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractContainsSearch#buildQuery(java.lang.String)
     */
    @Override
    public Query buildQuery(String searchText) {
        String excapedString = AbstractLiteralSearch.excapeSpecialCharacters(searchText);
        return super.buildQuery(searchText);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractContainsSearch#getLuceneSearchField()
     */
    @Override
    public String getLuceneSearchField() {
        return luceneSearchField;
    }    
}