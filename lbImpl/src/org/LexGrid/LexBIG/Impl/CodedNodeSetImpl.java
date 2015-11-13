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
package org.LexGrid.LexBIG.Impl;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.MatchToQuerySort;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.GetAllConcepts;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToAnonymous;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToCodes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToEntityTypes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingDesignations;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToStatus;
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
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.database.ibatis.codednodegraph.model.EntityReferencingAssociatedConcept;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.utility.CodingSchemeReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    private BooleanQuery.Builder builder = new BooleanQuery.Builder();

    protected CodeHolderFactory codeHolderFactory = new NonProxyCodeHolderFactory();
    
    private Set<CodingSchemeReference> references = new HashSet<CodingSchemeReference>();

    private AbsoluteCodingSchemeVersionReference primaryReference;
    
    //This can be very large an expensive to send remotely. Make this transient
    //for now and rely on Lucene matches to reconstruct the 'toNodeListCodes' matches.
    private transient CodeHolder toNodeListCodes = null;

    private ActiveOption currentActiveOption;
    
    private boolean shouldCodingSchemeSpecificRestriction = true;

    private transient DefaultCodeHolder nonEntityConceptReferenceList;

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

            if (activeOnly != null && activeOnly.booleanValue()) {
                this.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
            }
            
            if(entityTypes != null && entityTypes.getEntryCount() > 0 ) {
                this.restrictToEntityTypes(entityTypes);
            }
            
            String version = ServiceUtility.getVersion(codingScheme, tagOrVersion);
            String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingScheme, version);
              
            CodingSchemeReference ref = new CodingSchemeReference();
            ref.setCodingSchemeURN(uri);
            ref.setCodingSchemeVersion(version);

            builder.add(new GetAllConcepts(uri, version).getQuery(), Occur.MUST);
            
            this.references.add(ref);
            this.primaryReference = ref;
        } catch (LBParameterException e) {
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
     * @throws LBInvocationException
     * @throws LBParameterException
     */
    public CodedNodeSetImpl(CodeHolder codes, String internalCodingSchemeName, String internalVersionString)
            throws LBInvocationException {
        getLogger().logMethod(new Object[] { codes, internalCodingSchemeName, internalVersionString });
        try {
            this.toNodeListCodes = codes;
            builder.add(new GetAllConcepts(internalCodingSchemeName, internalVersionString).getQuery(), Occur.MUST);
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeSet#intersect(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeSet intersect(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        try {
            BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();

            newBuilder.add(this.builder.build(), Occur.MUST);
            newBuilder.add(((CodedNodeSetImpl) codes).clone().getQuery(), Occur.MUST);

            this.builder = newBuilder;

            this.references.addAll(((CodedNodeSetImpl) codes).references);

            return this;
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
        try {
            BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();
            newBuilder.setMinimumNumberShouldMatch(1);

            newBuilder.add(this.builder.build(), Occur.SHOULD);
            newBuilder.add(((CodedNodeSetImpl) codes).getQuery(), Occur.SHOULD);

            this.builder = newBuilder;

            this.references.addAll(((CodedNodeSetImpl) codes).references);

            return this;
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
        try {
            BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();
            newBuilder.add(this.builder.build(), Occur.MUST);
            newBuilder.add((((CodedNodeSetImpl) codesToRemove).clone()).getQuery(), Occur.MUST_NOT);

            this.builder = newBuilder;

            this.references.addAll(((CodedNodeSetImpl) codesToRemove).references);

            return this;
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
            CodeHolder codesToInclude_ = this.toBruteForceMode();

            if (code == null) {
                throw new LBParameterException("The parameter is required", "codeObject");
            }

            if (code.getCodingSchemeName() == null || code.getCodingSchemeName().length() == 0) {
                throw new LBParameterException("The parameter is required", "codingScheme");
            }

            if (code.getConceptCode() == null || code.getConceptCode().length() == 0) {
                throw new LBParameterException("The parameter is required", "conceptCode");
            }

            CodeToReturn temp = new CodeToReturn(code.getConceptCode(), code.getCodeNamespace());

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
            RestrictToMatchingDesignations op = new RestrictToMatchingDesignations(matchText, option, matchAlgorithm, language,
                    this.primaryReference.getCodingSchemeURN(), this.primaryReference.getCodingSchemeVersion());

            BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();
            newBuilder.add(this.builder.build(), Occur.MUST);
            newBuilder.add(op.getQuery(), Occur.MUST);

            this.builder = newBuilder;

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
            builder.add(new RestrictToProperties(propertyList, propertyTypes, sourceList, contextList,
                    qualifierList, null, null).getQuery(), Occur.MUST);

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
            builder.add(new RestrictToProperties(propertyList, propertyTypes, null, null, null,
                    null, null).getQuery(), Occur.MUST);
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
        try {
            codeList = getCleanedCodeList(codeList);
            this.builder.add(new RestrictToCodes(codeList).getQuery(), Occur.MUST);

            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    private ConceptReferenceList getCleanedCodeList(ConceptReferenceList codeList) {
        Iterator<? extends ConceptReference> itr = codeList.iterateConceptReference();
        ConceptReferenceList cleanedList = new ConceptReferenceList();
        nonEntityConceptReferenceList = new DefaultCodeHolder();
        while (itr.hasNext()) {
            ConceptReference cr = itr.next();
            if (cr instanceof EntityReferencingAssociatedConcept
                    && (((EntityReferencingAssociatedConcept) cr).getEntityGuid() == null)
                    || (cr instanceof ResolvedConceptReference && ((ResolvedConceptReference) cr)
                            .getEntityDescription() == null)) {
                CodeToReturn ch = convertConceptReferenceToCodeHolder(cr);
                if (!nonEntityConceptReferenceList.contains(ch)) {
                    nonEntityConceptReferenceList.add(ch);
                }
            } else {
                cleanedList.addConceptReference(cr);
            }
        }

        // Can't send an empty list to the builder. If the focus node was an
        // unreferenced
        // entity from a relationship then it will return nothing from the
        // indexes. Should be a no-op.
        if (cleanedList.getConceptReferenceCount() == 0) {
            ConceptReferenceList newList = new ConceptReferenceList();
            newList.addConceptReference(codeList.getConceptReference(0));
            return newList;
        }
        return cleanedList;
    }

    private CodeToReturn convertConceptReferenceToCodeHolder(ConceptReference cr) {
        CodeToReturn ctr = new CodeToReturn();;
        ctr.setCode(cr.getCode());
        ctr.setEntityTypes(ctr.getEntityTypes());
        ctr.setNamespace(cr.getCodeNamespace());
        ctr.setEntityTypes(new String[]{"concept"});
        return ctr;
    }

    protected CodedNodeSet restrictToEntityTypes(LocalNameList typeList) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { typeList });
        try {
            builder.add(new RestrictToEntityTypes(typeList).getQuery(), Occur.MUST);

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
            builder.add(new RestrictToMatchingProperties(propertyList, propertyTypes, sourceList,
                    contextList, qualifierList, matchText, matchAlgorithm, language, null, null).getQuery(), Occur.MUST);
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
            builder.add(new RestrictToMatchingProperties(propertyList, propertyTypes, null, null, null,
                    matchText, matchAlgorithm, language, null, null).getQuery(), Occur.MUST);
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

            CodeHolder codesToInclude_ = this.toBruteForceMode();
           
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
            
            int max = codes.size() + 
                    (this.getToNodeListCodes() != null ? this.getToNodeListCodes().getNumberOfCodes() : 0);

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

            CodeHolder codesToInclude_ = this.toBruteForceMode();

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
        try {
            this.currentActiveOption = activeOption;
            builder.add(new RestrictToStatus(activeOption, conceptStatus).getQuery(), Occur.MUST);
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
            BooleanQuery.Builder newBuilder = new BooleanQuery.Builder();
            newBuilder.add(this.builder.build(), Occur.MUST);
            newBuilder.add(new RestrictToAnonymous(anonymousOption).getQuery(), Occur.MUST);

            this.builder = newBuilder;

            return this;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    protected ResolvedConceptReferencesIterator getResolvedConceptReferencesIterator(
            CodeHolder codesToInclude, 
            LocalNameList restrictToProperties, 
            PropertyType[] restrictToPropertyTypes, 
            Filter[] filters, 
            boolean resolveObjects) {
        
        ResolvedConceptReferencesIterator itr = new ResolvedConceptReferencesIteratorImpl(
                codesToInclude, restrictToProperties, restrictToPropertyTypes, filters, resolveObjects);
        
        if(this.getToNodeListCodes() != null && 
                this.getToNodeListCodes().getAllCodes().size() > 0) {
            return new ToNodeListResolvedConceptReferencesIteratorDecorator(itr, this.getToNodeListCodes(), this.currentActiveOption);
        } else {
            return itr;
        }  
    }

    /*
     * Turn the bit set into a populated CodeHolder.
     */
    protected CodeHolder toBruteForceMode()
    throws LBInvocationException, LBParameterException {

        return codeHolderFactory.buildCodeHolder(nonEntityConceptReferenceList,
                        this.getCodingSchemeReferences(),
                        this.getQuery());

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

        return cns;
    }
    
    public Set<CodingSchemeReference> getCodingSchemeReferences(){
        return this.references;
    }

    public CodeHolderFactory getCodeHolderFactory() {
        return codeHolderFactory;
    }

    public void setCodeHolderFactory(CodeHolderFactory codeHolderFactory) {
        this.codeHolderFactory = codeHolderFactory;
    }

    public CodeHolder getToNodeListCodes() {
        return toNodeListCodes;
    }

    public void setShouldCodingSchemeSpecificRestriction(boolean shouldCodingSchemeSpecificRestriction) {
        this.shouldCodingSchemeSpecificRestriction = shouldCodingSchemeSpecificRestriction;
    }

    public boolean isShouldCodingSchemeSpecificRestriction() {
        return shouldCodingSchemeSpecificRestriction;
    }

    public Query getQuery() {
        return this.builder.build();
    }

}