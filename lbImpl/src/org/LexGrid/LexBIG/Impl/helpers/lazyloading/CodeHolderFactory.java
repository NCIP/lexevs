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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

/**
 * A factory for creating CodeHolder objects.
 */
public interface CodeHolderFactory extends Serializable {

    /**
     * Builds the code holder.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param query the query
     * 
     * @return the code holder
     * 
     * @throws Exception the exception
     */

//    public CodeHolder buildCodeHolder(String internalCodeSystemName,
//            String internalVersionString, 
//            List<BooleanQuery> combinedQueries,
//            List<Query> individualQueries) throws LBInvocationException, LBParameterException;
    
    public CodeHolder buildCodeHolderWithFilters(String internalCodeSystemName,
            String internalVersionString, 
            List<Query> queries,
            List<Filter> filters) throws LBInvocationException, LBParameterException;
    
    public CodeHolder buildCodeHolder(
            List<AbsoluteCodingSchemeVersionReference> references,
            Query query) throws LBInvocationException, LBParameterException;
}