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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * A factory for creating LazyCodeHolder objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLazyCodeHolderFactory implements CodeHolderFactory {
    
    private static final long serialVersionUID = 4365560788599358013L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.CodeHolderFactory#buildCodeHolder(java.lang.String, java.lang.String, org.apache.lucene.search.Query)
     */
    public CodeHolder buildCodeHolder(
            String internalCodeSystemName,
            String internalVersionString, 
            List<BooleanQuery> combinedQuery, 
            List<Query> bitSetQueries) throws LBInvocationException, LBParameterException {
        IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
        EntityIndexService entityService = indexServiceManager.getEntityIndexService();
        
        SystemResourceService resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        String uri = resourceService.getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);
        
        AbsoluteCodingSchemeVersionReference ref =
            Constructors.createAbsoluteCodingSchemeVersionReference(
                uri, internalVersionString);

        List<ScoreDoc> scoreDocs = entityService.query(
                ref.getCodingSchemeURN(), 
                ref.getCodingSchemeVersion(), 
                        combinedQuery, bitSetQueries);

        return buildCodeHolder(internalCodeSystemName, internalVersionString, scoreDocs);
    }
    
    @Override
    public CodeHolder buildCodeHolderWithFilters(
            String internalCodeSystemName, 
            String internalVersionString,
            List<Query> queries, 
            List<Filter> filters) throws LBInvocationException, LBParameterException {
        IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
        EntityIndexService entityService = indexServiceManager.getEntityIndexService();
        
        SystemResourceService resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        String uri = resourceService.getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);

        BooleanQuery combinedQuery = new BooleanQuery();
        for(Query query : queries) {
            combinedQuery.add(query, Occur.MUST);
        }
        
        //TODO.  Figure out what the filters were doing and duplicate if necessary for the wrapper filter.
        Filter chainedFilter = new QueryWrapperFilter(combinedQuery);
        Query query;
        if(CollectionUtils.isNotEmpty(filters)) {
            query = new FilteredQuery(combinedQuery, chainedFilter);
        } else {
            query = combinedQuery;
        }
        
        List<ScoreDoc> scoreDocs = entityService.query(uri, internalVersionString, query);

        return buildCodeHolder(internalCodeSystemName, internalVersionString, scoreDocs);
    }
    
    protected CodeHolder buildCodeHolder(
            String internalCodeSystemName, 
            String internalVersionString,
            List<ScoreDoc> scoreDocs) {
        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();
        
        for(ScoreDoc doc : scoreDocs){
            codeHolder.add(buildCodeToReturn(doc, internalCodeSystemName, internalVersionString));
        }

        return codeHolder;
    }

    @Override
    public CodeHolder buildCodeHolder(
            List<AbsoluteCodingSchemeVersionReference> references, 
            Query query) throws LBInvocationException, LBParameterException {
        
        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
                getEntityIndexService().queryCommonIndex(references, query);
        
        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();
        
        for(ScoreDoc doc : scoreDocs){
            codeHolder.add(buildCodeToReturn(doc, references));
        }

        return codeHolder;
    }

    /**
     * Builds the code to return.
     * 
     * @param doc the doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @return the code to return
     */
    protected abstract CodeToReturn buildCodeToReturn(ScoreDoc doc, String internalCodeSystemName, String internalVersionString);
    
    protected abstract CodeToReturn buildCodeToReturn(ScoreDoc doc, List<AbsoluteCodingSchemeVersionReference> references);
}