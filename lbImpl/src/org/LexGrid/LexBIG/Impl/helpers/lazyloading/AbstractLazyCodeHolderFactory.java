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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
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
    public CodeHolder buildCodeHolder(String internalCodeSystemName,
            String internalVersionString, 
            List<BooleanQuery> combinedQuery, 
            List<Query> bitSetQueries) throws LBInvocationException, LBParameterException {
        IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
        EntityIndexService entityService = indexServiceManager.getEntityIndexService();
        
        SystemResourceService resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        String uri = resourceService.getUriForUserCodingSchemeName(internalCodeSystemName);
        
        AbsoluteCodingSchemeVersionReference ref =
            Constructors.createAbsoluteCodingSchemeVersionReference(
                uri, internalVersionString);

        //Match all docs (excluding code boundry docs) if no queries are provided
        if(combinedQuery == null || combinedQuery.size() == 0){
            combinedQuery = new ArrayList<BooleanQuery>();
            BooleanQuery booleanQuery = new BooleanQuery();
            booleanQuery.add(
            		entityService.getMatchAllDocsQuery(ref), Occur.MUST);
            booleanQuery.add(
                    new TermQuery(new Term("codeBoundry", "T")), Occur.MUST_NOT);
            combinedQuery.add(booleanQuery);
        }          

        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();

        List<ScoreDoc> scoreDocs = entityService.query(ref, 
                        combinedQuery, bitSetQueries);

        for(ScoreDoc doc : scoreDocs){
            codeHolder.add(buildCodeToReturn(doc, internalCodeSystemName, internalVersionString));
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
}
