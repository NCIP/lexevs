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
package org.LexGrid.LexBIG.Impl.codednodeset;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Difference;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Intersect;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Union;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.compass.core.lucene.support.ChainedFilter;

public abstract class AbstractMultiSingleLuceneIndexCodedNodeSet extends CodedNodeSetImpl {
    
    private static final long serialVersionUID = -5959522938971242708L;
    
    private CodedNodeSetImpl cns1;
    private CodedNodeSetImpl cns2;
    
    protected AbstractMultiSingleLuceneIndexCodedNodeSet(){
        super();
    }

    public AbstractMultiSingleLuceneIndexCodedNodeSet(CodedNodeSetImpl cns1, CodedNodeSetImpl cns2) { 
       this.cns1 = cns1;
       this.cns2 = cns2;
       
       this.getCodingSchemeReferences().addAll(cns1.getCodingSchemeReferences());
       this.getCodingSchemeReferences().addAll(cns2.getCodingSchemeReferences());
    }

    @Override
    public CodedNodeSet restrictToAnonymous(AnonymousOption anonymousOption) throws LBInvocationException,
            LBParameterException {
       cns1.restrictToAnonymous(anonymousOption);
       cns2.restrictToAnonymous(anonymousOption);
       return this;
    }

    @Override
    public CodedNodeSet restrictToCodes(ConceptReferenceList codeList) throws LBInvocationException,
            LBParameterException {
        cns1.restrictToCodes(codeList);
        cns2.restrictToCodes(codeList);
        return this;
    }

    @Override
    public CodedNodeSet restrictToMatchingDesignations(String matchText, boolean preferredOnly, String matchAlgorithm,
            String language) throws LBInvocationException, LBParameterException {
        cns1.restrictToMatchingDesignations(matchText, preferredOnly, matchAlgorithm, language);
        cns2.restrictToMatchingDesignations(matchText, preferredOnly, matchAlgorithm, language);
        return this;
    }

    @Override
    public CodedNodeSet restrictToMatchingDesignations(String matchText, SearchDesignationOption option,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        cns1.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
        cns2.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
        return this;
    }

    @Override
    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList, String matchText,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        cns1.restrictToMatchingProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList, matchText, matchAlgorithm, language);
        cns2.restrictToMatchingProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList, matchText, matchAlgorithm, language);
        return this;
    }

    @Override
    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            String matchText, String matchAlgorithm, String language) throws LBInvocationException,
            LBParameterException {
       cns1.restrictToMatchingProperties(propertyList, propertyTypes, matchText, matchAlgorithm, language);
       cns2.restrictToMatchingProperties(propertyList, propertyTypes, matchText, matchAlgorithm, language);
       return this;
    }

    @Override
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList)
            throws LBInvocationException, LBParameterException {
        cns1.restrictToProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList);
        cns2.restrictToProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList);
        return this;
    }

    @Override
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes)
            throws LBInvocationException, LBParameterException {
       cns1.restrictToProperties(propertyList, propertyTypes);
       cns2.restrictToProperties(propertyList, propertyTypes);
       return this;
    }

    @Override
    public CodedNodeSet restrictToStatus(ActiveOption activeOption, String[] conceptStatus)
            throws LBInvocationException, LBParameterException {
       cns1.restrictToStatus(activeOption, conceptStatus);
       cns2.restrictToStatus(activeOption, conceptStatus);
       return this;
    }
    
    public CodedNodeSet union(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl) {
            return new UnionSingleLuceneIndexCodedNodeSet((CodedNodeSetImpl)codes,this);
        } else {
            return super.union(codes);
        }
    }
    
    public CodedNodeSet intersect(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl) {
            return new IntersectSingleLuceneIndexCodedNodeSet((CodedNodeSetImpl)codes,this);
        } else {
            return super.intersect(codes);
        }
    }
    
    public CodedNodeSet difference(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl) {
            return new DifferenceSingleLuceneIndexCodedNodeSet((CodedNodeSetImpl)codes,this);
        } else {
            return super.difference(codes);
        }
    }

    @Override
    public void runPendingOps() throws LBInvocationException, LBParameterException {
        this.cns1.runPendingOps();
        this.cns2.runPendingOps();
  
        this.getQueries().add(
                combineQueries(
                        combineQueriesAndFilters(cns1),
                        combineQueriesAndFilters(cns2)));
    }
    
    protected void toBruteForceMode(String internalCodeSystemName, String internalVersionString)
        throws LBInvocationException, LBParameterException {
        
        if(this.codesToInclude_ != null) {return;}        

        if(codesToInclude_ == null ){
            codesToInclude_ = 
                codeHolderFactory.buildCodeHolder(new ArrayList<AbsoluteCodingSchemeVersionReference>(
                        this.getCodingSchemeReferences()), combineQueriesAndFilters(this));
        }
    }

    protected abstract Query combineQueries(Query query1, Query query2);

    private Query combineQueriesAndFilters(CodedNodeSetImpl cns) {
        List<Filter> filters = cns.getFilters();
        List<Query> queries = cns.getQueries();
        
        Filter chainedFilter = new ChainedFilter(filters.toArray(new Filter[filters.size()]), ChainedFilter.AND);
        
        BooleanQuery combinedQuery = new BooleanQuery();
        for(Query query : queries) {
            combinedQuery.add(query, Occur.MUST);
        }
        
       Query query;
        if(CollectionUtils.isNotEmpty(filters)) {
            query = new FilteredQuery(combinedQuery, chainedFilter);
        } else {
            query = combinedQuery;
        }
        
        return query;
    }

    @Override
    protected void doUnion(String internalCodeSystemName, String internalVersionString, Union union) throws LBException {
       //
    }
    
    protected void doIntersect(String internalCodeSystemName, String internalVersionString, Intersect intersect) throws LBException {
       //
    }
    
    protected void doDifference(String internalCodeSystemName, String internalVersionString, Difference difference) throws LBException {
        //
    }

    @Override
    protected String getInternalCodeSystemName() {
        return null;
    }

    @Override
    protected String getInternalVersionString() {
        return null;
    }

}