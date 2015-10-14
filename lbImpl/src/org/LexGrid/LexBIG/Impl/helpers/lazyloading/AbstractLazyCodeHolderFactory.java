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

import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.BitDocIdSetCachingWrapperFilter;
import org.apache.lucene.search.join.BitDocIdSetFilter;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
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
    
    //Doesn't seem to have any calling code.  commenting out for now.  The BitSet query mechanism is complex  
//    public CodeHolder buildCodeHolder(
//            String internalCodeSystemName,
//            String internalVersionString, 
//            List<BooleanQuery> combinedQuery, 
//            List<Query> bitSetQueries) throws LBInvocationException, LBParameterException {
//        IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
//        EntityIndexService entityService = indexServiceManager.getEntityIndexService();
//        
//        SystemResourceService resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
//        String uri = resourceService.getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);
//        
//        AbsoluteCodingSchemeVersionReference ref =
//            Constructors.createAbsoluteCodingSchemeVersionReference(
//                uri, internalVersionString);
//
//        List<ScoreDoc> scoreDocs = entityService.query(
//                ref.getCodingSchemeURN(), 
//                ref.getCodingSchemeVersion(), 
//                        combinedQuery, bitSetQueries);
//
//        return buildCodeHolder(internalCodeSystemName, internalVersionString, scoreDocs);
//    }
    
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
       QueryBitSetProducer parentFilter = null;
        try {
            parentFilter = new QueryBitSetProducer(new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
        } catch (ParseException e) {
          new RuntimeException("Unparsable Query generated.  Unexpected error on parent filter", e);
        }
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        BooleanQuery combinedQuery = new BooleanQuery();
        for(Query query : queries) {
            combinedQuery.add(query, Occur.MUST);
        }
        ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
                combinedQuery, 
                parentFilter,
                ScoreMode.Total);
        
        List<ScoreDoc> scoreDocs = entityService.query(uri, internalVersionString, termJoinQuery);;
//        List<ScoreDoc> finalDocs = scoreDocs;
//        for(ScoreDoc sd: scoreDocs){
//            Document doc = entityService.getDocumentById(uri, internalVersionString, sd.doc);
//            TermQuery query = new TermQuery(new Term("code", doc.get("code")));
//            ToChildBlockJoinQuery childQuery = new ToChildBlockJoinQuery(query, parentFilter);
//            BooleanQuery finalQuery = new BooleanQuery();
//            finalQuery.add(new CachingWrapperQuery(childQuery), Occur.MUST);
//            finalQuery.add(combinedQuery, Occur.MUST);
//            finalDocs = entityService.query(uri, internalVersionString, finalQuery);
//            
//        }
        
        return buildCodeHolder(internalCodeSystemName, internalVersionString, scoreDocs);
    }
    
    private List<ScoreDoc> validateAndAdjustScoreDocsPerEntity(BooleanQuery combinedQuery, List<ScoreDoc> scoreDocs) {
        List<ScoreDoc> temp = new ArrayList<ScoreDoc>();
        for(ScoreDoc sd: scoreDocs){
         
        }
        return temp;
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
        BitDocIdSetFilter parentFilter = null;
        try {
            parentFilter = new BitDocIdSetCachingWrapperFilter(
                    new QueryWrapperFilter(new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("true")));
        } catch (ParseException e) {
          new RuntimeException("Unparsable Query generated.  Unexpected error on parent filter", e);
        }
        

        ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
                query, 
                parentFilter,
                ScoreMode.Max);
        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
                getEntityIndexService().queryCommonIndex(references, termJoinQuery);
        
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