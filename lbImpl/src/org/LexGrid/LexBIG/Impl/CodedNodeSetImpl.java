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
package org.LexGrid.LexBIG.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.MatchToQuerySort;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Difference;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.GetAllConcepts;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Intersect;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToAnonymous;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToCodes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToEntityTypes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingDesignations;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToStatus;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Union;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.SetOperation;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorImpl;
import org.LexGrid.LexBIG.Impl.helpers.ToNodeListResolvedConceptReferencesIteratorDecorator;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.LexBIG.Impl.helpers.lazyloading.CodeHolderFactory;
import org.LexGrid.LexBIG.Impl.helpers.lazyloading.NonProxyCodeHolderFactory;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.utility.CodingSchemeReference;

/**
 * Implementation of the CodedNodeSet Interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodedNodeSetImpl implements CodedNodeSet, Cloneable {
    private static final long serialVersionUID = 6108466665548985484L;
    protected ArrayList<Operation> pendingOperations_ = new ArrayList<Operation>();
    protected CodeHolder codesToInclude_ = null;
    private List<Query> queries = new ArrayList<Query>();
    private List<org.apache.lucene.search.Filter> filters = new ArrayList<org.apache.lucene.search.Filter>();
    protected CodeHolderFactory codeHolderFactory = new NonProxyCodeHolderFactory();
    
    private Set<CodingSchemeReference> references = new HashSet<CodingSchemeReference>();
    
    private CodeHolder toNodeListCodes = new DefaultCodeHolder();

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * This constructor is only here for Apache Axis to work correctly. It
     * should not be used by anyone.
     */
    public CodedNodeSetImpl() {

    }

    /**
     * @throws LBResourceUnavailableException
     * 
     */
    public CodedNodeSetImpl(String codingScheme, CodingSchemeVersionOrTag tagOrVersion, Boolean activeOnly, LocalNameList entityTypes)
            throws LBInvocationException, LBParameterException, LBResourceUnavailableException {
        getLogger().logMethod(new Object[] { codingScheme, tagOrVersion, activeOnly });
        try {
            pendingOperations_.add(new GetAllConcepts(codingScheme, tagOrVersion));
            if (activeOnly != null && activeOnly.booleanValue()) {
                pendingOperations_.add(new RestrictToStatus(ActiveOption.ACTIVE_ONLY, null));
            }
            
            if(entityTypes != null && entityTypes.getEntryCount() > 0 ) {
                this.restrictToEntityTypes(entityTypes);
            }
            
            String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingScheme);
            String version = ServiceUtility.getVersion(codingScheme, tagOrVersion);
            
            CodingSchemeReference ref = new CodingSchemeReference();
            ref.setCodingSchemeURN(uri);
            ref.setCodingSchemeVersion(version);
            
            this.references.add(ref);
        } catch (LBParameterException e) {
            throw e;
        } catch (LBResourceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * This is used when I converter a CodedNodeGraph to a CodedNodeSet.
     * 
     * @param codes
     * @param tagOrVersion
     * @throws LBInvocationException
     * @throws LBParameterException
     */
    public CodedNodeSetImpl(CodeHolder codes, String internalCodingSchemeName, String internalVersionString)
            throws LBInvocationException {
        getLogger().logMethod(new Object[] { codes, internalCodingSchemeName, internalVersionString });
        try {
            codesToInclude_ = codes;
            pendingOperations_.add(new GetAllConcepts(internalCodingSchemeName, internalVersionString));
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }
    
    public CodedNodeSetImpl(CodeHolder codes)
        throws LBInvocationException {
        codesToInclude_ = codes;
    }

    /**
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#intersect(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeSet intersect(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codes });
        try {
            pendingOperations_.add(new Intersect(((CodedNodeSetImpl) codes).clone()));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#union(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeSet union(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codes });
        try {
            pendingOperations_.add(new Union(((CodedNodeSetImpl) codes).clone()));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#difference(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeSet difference(CodedNodeSet codesToRemove) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codesToRemove });
        try {
            pendingOperations_.add(new Difference(((CodedNodeSetImpl) codesToRemove).clone()));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#isCodeInSet(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    public Boolean isCodeInSet(ConceptReference code) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { code });
        try {
            runPendingOps();
            this.toBruteForceMode(this.getInternalCodeSystemName(), this.getInternalVersionString());

            if (code == null) {
                throw new LBParameterException("The parameter is required", "codeObject");
            }

            if (code.getCodingSchemeName() == null || code.getCodingSchemeName().length() == 0) {
                throw new LBParameterException("The parameter is required", "codingScheme");
            }

            if (code.getConceptCode() == null || code.getConceptCode().length() == 0) {
                throw new LBParameterException("The parameter is required", "conceptCode");
            }

            // The code is not validated any further, as we don't know what
            // version to validate against.
            String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(code.getCodingSchemeName());

            CodeToReturn temp = new CodeToReturn(code.getConceptCode(), null, uri, null);

            return new Boolean(codesToInclude_.contains(temp));
        } catch (LBInvocationException e) {
            throw e;
        }

        catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }

    }

    /**
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToMatchingDesignations(String,
     *      boolean, String, String)
     * @deprecated
     */
    @Deprecated
    public CodedNodeSet restrictToMatchingDesignations(String matchText, boolean preferredOnly, String matchAlgorithm,
            String language) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { matchText, new Boolean(preferredOnly), matchAlgorithm, language });
        return restrictToMatchingDesignations(matchText, preferredOnly ? SearchDesignationOption.PREFERRED_ONLY
                : SearchDesignationOption.ALL, matchAlgorithm, language);
    }

    public CodedNodeSet restrictToMatchingDesignations(String matchText, SearchDesignationOption option,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { matchText, option, matchAlgorithm, language });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToMatchingDesignations(matchText, option, matchAlgorithm, language,
                    getInternalCodeSystemName(), getInternalVersionString()));
            return this;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToProperties(org.LexGrid.LexBIG.DataModel.Collections.PropertyDetailList)
     */
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { propertyList, propertyTypes, sourceList, contextList, qualifierList });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToProperties(propertyList, propertyTypes, sourceList, contextList,
                    qualifierList, getInternalCodeSystemName(), getInternalVersionString()));
            return this;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToProperties(org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { propertyList, propertyTypes });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToProperties(propertyList, propertyTypes, null, null, null,
                    getInternalCodeSystemName(), getInternalVersionString()));
            return this;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToCodes(org.LexGrid
     * .LexBIG.DataModel.Collections.ConceptReferenceList)
     */
    @LgClientSideSafe
    public CodedNodeSet restrictToCodes(ConceptReferenceList codeList) throws LBInvocationException,
            LBParameterException {
        getLogger().logMethod(new Object[] { codeList });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToCodes(codeList));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    protected CodedNodeSet restrictToEntityTypes(LocalNameList typeList) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { typeList });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToEntityTypes(typeList));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList, String matchText,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { propertyList, propertyTypes, sourceList, contextList, qualifierList, matchText,
                        matchAlgorithm, language });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToMatchingProperties(propertyList, propertyTypes, sourceList,
                    contextList, qualifierList, matchText, matchAlgorithm, language, getInternalCodeSystemName(),
                    getInternalVersionString()));
            return this;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToMatchingProperties(org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      String, String, String)
     */
    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            String matchText, String matchAlgorithm, String language) throws LBInvocationException,
            LBParameterException {
        getLogger().logMethod(new Object[] { propertyList, propertyTypes, matchText, matchAlgorithm, language });
        try {
            this.clearToNodeListCodes();
            pendingOperations_.add(new RestrictToMatchingProperties(propertyList, propertyTypes, null, null, null,
                    matchText, matchAlgorithm, language, getInternalCodeSystemName(), getInternalVersionString()));
            return this;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolveToList(org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    public ResolvedConceptReferenceList resolveToList(SortOptionList sortOptions, LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes, int maxToReturn) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { sortOptions, restrictToProperties, restrictToPropertyTypes, new Integer(maxToReturn) });
        return resolveToList(sortOptions, null, restrictToProperties, restrictToPropertyTypes, maxToReturn);
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolveToList(org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    public ResolvedConceptReferenceList resolveToList(SortOptionList sortByProperty, LocalNameList filterOptions,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, int maxToReturn)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes,
                        new Integer(maxToReturn) });
        return resolveToList(sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes, true,
                maxToReturn);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolveToList(org.LexGrid
     * .LexBIG.DataModel.Collections.SortOptionList,
     * org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     * org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], boolean,
     * int)
     */
    public ResolvedConceptReferenceList resolveToList(SortOptionList sortByProperty, LocalNameList filterOptions,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, boolean resolveObjects,
            int maxToReturn) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes,
                        resolveObjects, new Integer(maxToReturn) });
        LexEvsServiceLocator serviceLocator = LexEvsServiceLocator.getInstance();
        
        // Currently, the supported sortByProperties are:
        // matchToQuery, code, codeSystem, entityDescription, conceptStatus,
        // isActive.  However - matchToQuery can not be combined with entityDescription,
        // conceptStatus, or isActive.  All other combinations are legal.
        try {
            Filter[] filters = ServiceUtility.validateFilters(filterOptions);

           runPendingOps();
           this.toBruteForceMode(this.getInternalCodeSystemName(), this.getInternalVersionString());
           
            // now, all of the operations are complete, and the codesToInclude
            // set is populated.
            // turn it into what they actual want returned.

            List<CodeToReturn> codes = codesToInclude_.getAllCodes();
            
            if(sortByProperty == null || sortByProperty.getEntryCount() == 0){
                sortByProperty = Constructors.createSortOptionList(
                        new String[]{MatchToQuerySort.name_});
            }
            ResultComparator<CodeToReturn> preComparator = 
                getResultComparator(sortByProperty, SortContext.SETLISTPRERESOLVE, CodeToReturn.class);
            Collections.sort(codes, preComparator);  

            ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
            
            int max = codes.size() + this.getToNodeListCodes().getNumberOfCodes();

            if (maxToReturn > 0 && maxToReturn < max) {
                max = maxToReturn;
                rcrl.setIncomplete(new Boolean(true));
                getLogger().info(
                        "Available results exceeded the provided maxToReturn variable.  " + (codes.size() - max)
                                + " results skipped");
            }

            if (max > serviceLocator.getSystemResourceService().getSystemVariables().getMaxResultSize()) {
                throw new LBParameterException("The number of results exceeded the system limit of "
                        + serviceLocator.getSystemResourceService().getSystemVariables().getMaxResultSize()
                        + ".  You will need to set the maxToReturn flag, or use the iterator resolve method.");
            }

            ResolvedConceptReferencesIterator itr = getResolvedConceptReferencesIterator(
                    codesToInclude_, restrictToProperties, restrictToPropertyTypes, filters, resolveObjects);
            
            ResolvedConceptReferenceList list = itr.next(max);
            
            List<ResolvedConceptReference> refs = Arrays.asList(list.getResolvedConceptReference());
            
            if(sortByProperty != null){
                ResultComparator<ResolvedConceptReference> postComparator = 
                    getResultComparator(sortByProperty, SortContext.SETLISTPOSTRESOLVE, ResolvedConceptReference.class);
                Collections.sort(refs, postComparator);  
            }

            for(ResolvedConceptReference ref : refs){
                rcrl.addResolvedConceptReference(ref);
            }
            
            itr.release();
 
            return rcrl;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }
    
    protected <T> ResultComparator<T> getResultComparator(SortOptionList sortOptionList, SortContext sortContext, Class<T> clazz) throws LBParameterException{ 

        ResultComparator<T> comparator = new ResultComparator<T>();

        for (SortOption sortOption : sortOptionList.getEntry()) {
            String algorithm = sortOption.getExtensionName();
            if (ServiceUtility.isSortAlgorithmValid(algorithm, sortContext)) {
                if(comparator.validateSortOptionForClass(sortOption, clazz)){
                    comparator.addSortOption(sortOption);
                    comparator.setSortClazz(clazz);
                } else {
                    throw new LBParameterException(
                            "The provided sort algorithm " + algorithm  + 
                            " is invalid for the search class: " + clazz.getName() );
                }
            }   
        }
        return comparator;
    }

    protected String getInternalCodeSystemName() {
        return ((GetAllConcepts) pendingOperations_.get(0)).getInternalCodingSchemeName();
    }

    protected String getInternalVersionString() {
        return ((GetAllConcepts) pendingOperations_.get(0)).getInternalVersionString();
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolve(org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */
    public ResolvedConceptReferencesIterator resolve(SortOptionList sortOptions, LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { sortOptions, restrictToProperties });
        return resolve(sortOptions, null, restrictToProperties, restrictToPropertyTypes, true);
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolve(org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */

    public ResolvedConceptReferencesIterator resolve(SortOptionList sortByProperty, LocalNameList filterOptions,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes) throws LBInvocationException,
            LBParameterException {
        getLogger().logMethod(
                new Object[] { sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes });
        return resolve(sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes, true);
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#resolve(org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */
    public ResolvedConceptReferencesIterator resolve(SortOptionList sortByProperty, LocalNameList filterOptions,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, boolean resolveObjects)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { sortByProperty, filterOptions, restrictToProperties, restrictToPropertyTypes,
                        resolveObjects });
        // Currently, only 'presorted' sort types are allowed in the iterator.
        // That cuts the list to:
        // matchToQuery, code, codeSystem
        try {
            Filter[] filters = ServiceUtility.validateFilters(filterOptions);

            // we are not validating the properties at all, as they would have
            // to specify which properties
            // come from which code system, etc.
         
            if (sortByProperty != null && sortByProperty.getEntryCount() > 0) {
                for (int i = 0; i < sortByProperty.getEntryCount(); i++) {
                    String entry = sortByProperty.getEntry(i).getExtensionName();
                    if (! ServiceUtility.isSortAlgorithmValid(entry, SortContext.SETITERATION)) {
                        throw new LBParameterException(
                                "The provided sort algorithm is invalid.  Please call LexBIGService.getSortAlgorithms to see the valid algorithms.  Sort algorithm choices are restricted to 'matchToQuery', 'code', and 'codeSystem' when you ask for an iterator.",
                                "sortByProperty", entry);
                    }
                }
            }

            runPendingOps();
            this.toBruteForceMode(this.getInternalCodeSystemName(), this.getInternalVersionString());

            if(sortByProperty == null || sortByProperty.getEntryCount() == 0){
                sortByProperty = Constructors.createSortOptionList(
                        new String[]{MatchToQuerySort.name_});
            }
            
            ResultComparator<CodeToReturn> comparator = getResultComparator(sortByProperty, SortContext.SETITERATION, CodeToReturn.class);
            Collections.sort(codesToInclude_.getAllCodes(), comparator);
            
            return getResolvedConceptReferencesIterator(codesToInclude_, restrictToProperties, restrictToPropertyTypes,
                    filters, resolveObjects);
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error: " + e.getLocalizedMessage(), logId);
        }
    }
    

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#restrictToStatus(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption,
     *      java.lang.String[])
     */
    @LgClientSideSafe
    public CodedNodeSet restrictToStatus(ActiveOption activeOption, String[] conceptStatus)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { activeOption, conceptStatus });
        try {
            pendingOperations_.add(new RestrictToStatus(activeOption, conceptStatus));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }
    
    @Override
    public CodedNodeSet restrictToAnonymous(AnonymousOption anonymousOption) 
        throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { anonymousOption });
        try {
            boolean foundPrevious = false;
            int previousIndex = 0;
            
            for(int i=0;i<pendingOperations_.size();i++) {
                Operation operation = pendingOperations_.get(i);
                if(operation instanceof RestrictToAnonymous) {
                    foundPrevious = true;
                    previousIndex = i;
                }
            }
            if(foundPrevious) {
                pendingOperations_.remove(previousIndex);
            }
            pendingOperations_.add(new RestrictToAnonymous(anonymousOption));
            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * Run all of the stacked up operations.
     */
    public void runPendingOps() throws LBInvocationException, LBParameterException {
        try {
            if(this.codesToInclude_ != null) {return;}
            
            boolean areMultipleDesignationQueries = areMultipleDesignationQueries();
            
            BooleanQuery combinedQuery = new BooleanQuery();
            
            EntityIndexService entityIndexService = 
                  LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
            
            optimizePendingOpsOrder();
            // will always be at least size 1...

            GetAllConcepts gac = (GetAllConcepts) pendingOperations_.get(0);
            
            String internalCodeSystemName = gac.getInternalCodingSchemeName();
            String internalVersionString = gac.getInternalVersionString();
            
            String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(internalCodeSystemName);

            this.queries.add(new MatchAllDocsQuery());
            
            for (int i = 1; i < pendingOperations_.size(); i++) {
                Operation operation = pendingOperations_.get(i);

                if(operation instanceof RestrictToMatchingDesignations ||
                        operation instanceof RestrictToMatchingProperties){
                    Query query = RestrictionImplementations.getQuery((Restriction) operation, internalCodeSystemName, internalVersionString);   
  
                    if(areMultipleDesignationQueries) {
                        combinedQuery.add(query, Occur.SHOULD);
                        org.apache.lucene.search.Filter
                        filter = entityIndexService.getBoundaryDocsHitAsAWholeFilter(uri, internalVersionString, query);
                        this.filters.add(filter);
                    } else {
                        this.queries.add(query);
                    }
                } else if(operation instanceof RestrictToStatus ||
                        operation instanceof RestrictToAnonymous ||
                        operation instanceof RestrictToCodes ||
                        operation instanceof RestrictToEntityTypes){
                    Query query = RestrictionImplementations.getQuery((Restriction) operation, internalCodeSystemName, internalVersionString);   
                    this.queries.add(query);
                } else  if(operation instanceof RestrictToProperties){
                    Query query = RestrictionImplementations.getQuery((Restriction) operation, internalCodeSystemName, internalVersionString);
                    org.apache.lucene.search.Filter
                        filter = entityIndexService.getBoundaryDocsHitAsAWholeFilter(uri, internalVersionString, query);
                    this.filters.add(filter);
                 }
  
                else if (operation instanceof Union) {
                    
                    Union union = (Union) operation;
                    doUnion(internalCodeSystemName, internalVersionString, union);
                } else if (operation instanceof Intersect) {
                      
                    Intersect intersect = (Intersect) operation;
                    
                    doIntersect(internalCodeSystemName, internalVersionString, intersect);
                } else if (operation instanceof Difference) {
                     
                    Difference difference = (Difference) operation;
                    
                    doDifference(internalCodeSystemName, internalVersionString, difference);
                } 
            }
            
            if(combinedQuery.getClauses().length > 0) {
                queries.add(combinedQuery);
            }

            // remove the completed pending ops.
            for (int i = 1; i < pendingOperations_.size(); i++) {
                pendingOperations_.remove(i);
            }

        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Internal Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }
    
    private boolean areMultipleDesignationQueries() {
        int count = 0;
        for(Operation op : this.pendingOperations_) {
            if(op instanceof RestrictToMatchingDesignations
                    || op instanceof RestrictToMatchingProperties) {
                count++;
            }
        }
        return count > 1;
    }
    
    protected ResolvedConceptReferencesIterator getResolvedConceptReferencesIterator(
            CodeHolder codesToInclude, 
            LocalNameList restrictToProperties, 
            PropertyType[] restrictToPropertyTypes, 
            Filter[] filters, 
            boolean resolveObjects) {
        
        ResolvedConceptReferencesIterator itr = new ResolvedConceptReferencesIteratorImpl(
                codesToInclude, restrictToProperties, restrictToPropertyTypes, filters, resolveObjects);
        
        if(this.getToNodeListCodes().getAllCodes().size() > 0) {
            return new ToNodeListResolvedConceptReferencesIteratorDecorator(itr, this.getToNodeListCodes());
        } else {
            return itr;
        }  
    }
    
    protected void doUnion(String internalCodeSystemName, String internalVersionString, Union union) throws LBException {
        toBruteForceMode(internalCodeSystemName, internalVersionString);

        codesToInclude_.union(union.getCodes().getCodeHolder());
    }
    
    protected void doIntersect(String internalCodeSystemName, String internalVersionString, Intersect intersect) throws LBException {
        toBruteForceMode(internalCodeSystemName, internalVersionString);

        codesToInclude_.intersect(intersect.getCodes().getCodeHolder());
    }
    
    protected void doDifference(String internalCodeSystemName, String internalVersionString, Difference difference) throws LBException {
        toBruteForceMode(internalCodeSystemName, internalVersionString);

        codesToInclude_.difference(difference.getCodes().getCodeHolder());
    }

    /*
     * Get the populatedCodedHolder object for this CodedNodeSet.
     */
    public CodeHolder getCodeHolder() throws LBInvocationException, LBParameterException {
        runPendingOps();
        this.toBruteForceMode(this.getInternalCodeSystemName(), this.getInternalVersionString());
        return codesToInclude_;
    }
    
    protected void clearToNodeListCodes() {
        this.setToNodeListCodes(new DefaultCodeHolder());
    }

    /*
     * Turn the bit set into a populated CodeHolder.
     */
    protected void toBruteForceMode(String internalCodeSystemName, String internalVersionString)
    throws LBInvocationException, LBParameterException {
        if(this.codesToInclude_ != null) {return;}

        if(codesToInclude_ == null ){
            codesToInclude_ = 
                codeHolderFactory.buildCodeHolderWithFilters(
                        internalCodeSystemName, 
                        internalVersionString, 
                        queries, 
                        filters
                        );
        }
    }

    /*
     * Rearrange the pending operations so that we can stay in bit-set mode as
     * long as possible.
     */
    protected void optimizePendingOpsOrder() {
        // TODO [Performance] can I also pull up intersections and difference?
        // Want to pull all restrictions up above any unions.
        // This lets me do cheap restrictions for a longer time - union forces a
        // brute force approach
        // that uses much more memory.
        int firstSetOperationPos = -1;
        for (int i = 0; i < pendingOperations_.size(); i++) {
            Operation currentOperation = pendingOperations_.get(i);

            if (currentOperation instanceof Restriction && firstSetOperationPos >= 0) {
                // move this restriction above the firstUnionPos - shift
                // everything else down.
                for (int j = i; j > firstSetOperationPos; j--) {
                    pendingOperations_.set(j, pendingOperations_.get(j - 1));
                    // need to put this restriction into each union that should
                    // occur before it.
                    if (pendingOperations_.get(j) instanceof SetOperation) {
                        ((CodedNodeSetImpl) ((SetOperation) pendingOperations_.get(j)).getCodes())
                                .addPendingOperation(currentOperation);
                    }
                }

                pendingOperations_.set(firstSetOperationPos, currentOperation);
                firstSetOperationPos++;

            }
            if (firstSetOperationPos == -1 && currentOperation instanceof SetOperation) {
                firstSetOperationPos = i;
            }
        }
    }

    /*
     * make a clone of this CodedNodeSet - used before doing unions, joins, etc
     * since the optimize process may insert new operations.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    @SuppressWarnings("unchecked")
    @LgClientSideSafe
    public CodedNodeSetImpl clone() throws CloneNotSupportedException {
        CodedNodeSetImpl cns = (CodedNodeSetImpl) super.clone();
        cns.pendingOperations_ = (ArrayList<Operation>) this.pendingOperations_.clone();
        return cns;
    }
    
    public Set<CodingSchemeReference> getCodingSchemeReferences(){
        return this.references;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public List<org.apache.lucene.search.Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<org.apache.lucene.search.Filter> filters) {
        this.filters = filters;
    }

    /*
     * Add a new operation to the list to be executed, used by the optimize
     * process.
     */
    private void addPendingOperation(Operation operation) {
        pendingOperations_.add(operation);
    }

    public CodeHolderFactory getCodeHolderFactory() {
        return codeHolderFactory;
    }

    public void setCodeHolderFactory(CodeHolderFactory codeHolderFactory) {
        this.codeHolderFactory = codeHolderFactory;
    }

    public void setToNodeListCodes(CodeHolder toNodeListCodes) {
        this.toNodeListCodes = toNodeListCodes;
    }

    public CodeHolder getToNodeListCodes() {
        return toNodeListCodes;
    }
}