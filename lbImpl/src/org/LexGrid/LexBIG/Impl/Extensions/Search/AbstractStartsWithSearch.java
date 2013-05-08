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

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;

/**
 * The Class AbstractStartsWithSearch.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractStartsWithSearch extends AbstractExactMatchBoostingSearch {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7352943717333165742L;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Search#buildQuery(java.lang.String)
     */
    public Query doBuildQuery(String searchText) {
        return new PrefixQuery(new Term(getLuceneSearchField(), searchText.toLowerCase()));
    } 
    
    /**
     * Gets the lucene search field.
     * 
     * @return the lucene search field
     */
    public abstract String getLuceneSearchField();
}