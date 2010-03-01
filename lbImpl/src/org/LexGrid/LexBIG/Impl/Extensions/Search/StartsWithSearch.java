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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;


/**
 * The Class StartsWithSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StartsWithSearch extends AbstractStartsWithSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /** The lucene search field. */
    private static String luceneSearchField = LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(Search.class.getName());
        ed.setExtensionClass(StartsWithSearch.class.getName());
        ed.setDescription("Equivalent to 'term*' (case insensitive)");
        ed.setName("startsWith");
        ed.setVersion("1.0");
        return ed;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractStartsWithSearch#getLuceneSearchField()
     */
    @Override
    public String getLuceneSearchField() {
        return luceneSearchField;
    }
}
