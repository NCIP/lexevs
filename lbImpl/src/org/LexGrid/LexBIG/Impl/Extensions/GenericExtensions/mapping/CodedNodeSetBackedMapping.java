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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class CodedNodeSetBackedMapping.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodedNodeSetBackedMapping implements Mapping {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1523037493728989141L;
    
    /** The coded node set. */
    private CodedNodeSet sourceCodesCodedNodeSet;
    
    private CodedNodeSet targetCodesCodedNodeSet;
    
    private CodedNodeSet sourceOrTargetCodesCodedNodeSet;
    
    private Map<String, String> sourceIdAndNamespaceMap;
    private Map<String, String> targetIdAndNamespaceMap;
    private Map<String, List<String>> mapOfMap;
    
    private AbsoluteCodingSchemeVersionReference sourceReference;
    private AbsoluteCodingSchemeVersionReference targetReference;
    
    private CodingScheme mappingSchemeMetadata;
    
    private List<RelationshipRestriction> relationshipRestrictions = 
        new ArrayList<RelationshipRestriction>();
    
    /** The mapping uri. */
    private String mappingUri;
    
    /** The mapping version. */
    private String mappingVersion;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /**
     * Instantiates a new coded node set backed mapping.
     */
    public CodedNodeSetBackedMapping(){
        super();
    }
    
    /**
     * Instantiates a new coded node set backed mapping.
     * 
     * @param codingScheme the coding scheme
     * @param codingSchemeVersionOrTag the coding scheme version or tag
     * @param relationsContainerName the relations container name
     * 
     * @throws LBException the LB exception
     */
    public CodedNodeSetBackedMapping(AbsoluteCodingSchemeVersionReference ref, 
            String relationsContainerName) throws LBException{
        this.mappingUri = ref.getCodingSchemeURN();
        this.mappingVersion = ref.getCodingSchemeVersion();
        this.relationsContainerName = relationsContainerName;
        initSourceAndTargetCaching(mappingUri, mappingVersion, relationsContainerName);
        resolveSourceAndTargetSchemeReferences();
    }
    
    private void resolveSourceAndTargetSchemeReferences() {
      try {
        sourceReference = this.getSourceSchemeReference(this.mappingSchemeMetadata);
        targetReference = this.getTargetSchemeReference(this.mappingSchemeMetadata);
    } catch (LBParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        
    }

    private AbsoluteCodingSchemeVersionReference getTargetSchemeReference(CodingScheme mappingScheme) throws LBParameterException {
        String target = mappingScheme.getRelationsAsReference().get(0).getTargetCodingScheme();
        if(target == null){throw new RuntimeException("Target cannot be null");}
        String targetVersion = mappingScheme.getRelationsAsReference().get(0).getTargetCodingSchemeVersion();
        target = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(target, targetVersion);
        if(targetVersion == null){targetVersion = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalVersionStringForTag(target, "PRODUCTION");}
        return Constructors.createAbsoluteCodingSchemeVersionReference(target, targetVersion);
    }

    protected void initSourceAndTargetCaching(String mappingUri, String mappingVersion, String relationsContainerName){
        List<Triple> triples = LexEvsServiceLocator.getInstance().
        getDatabaseServiceManager().getCodedNodeGraphService().
      getMappingTripleForContainerOnly(
                mappingUri, mappingVersion, relationsContainerName);
        mappingSchemeMetadata = resolveMappingMetaData();
        sourceIdAndNamespaceMap = getSourceMappingIdsAndNamespace(mappingSchemeMetadata, relationsContainerName, triples);
        targetIdAndNamespaceMap = getTargetMappingIdsAndNamespace(mappingSchemeMetadata, relationsContainerName, triples);
        mapOfMap = getTotalMappingsAsMap(relationsContainerName, triples);
    }
    
    private Map<String, List<String>> getTotalMappingsAsMap(String relationsContainerName2, List<Triple> triples) {
        triples.sort(new Comparator<Triple>(){
            @Override
            public int compare(Triple o1, Triple o2) {
               return o1.getSourceEntityCode().compareTo(o2.getSourceEntityCode());
            }
        });
        for(int i = 0; i < triples.size(); i++){
           Triple t = triples.get(i);
        }
        
        return null;
    }

    private Map<String, String> getTargetMappingIdsAndNamespace(
            CodingScheme scheme, 
            String relationsContainer, 
            List<Triple> triples) {
        return triples.stream().collect(
                Collectors.toMap(Triple::getSourceEntityCode, 
                        Triple::getSourceEntityNamespace));
    }
//TODO: These methods need to deal in some manner with the duplicate keys where there is a record of the value when that value is different
    private Map<String, String> getSourceMappingIdsAndNamespace(
            CodingScheme scheme, 
            String relationsContainer, 
            List<Triple> triples) {
        return triples.stream().collect(
                Collectors.toMap(Triple::getSourceEntityCode, 
                        Triple::getSourceEntityNamespace, (k1,k2)-> k1 ));
    }

    private AbsoluteCodingSchemeVersionReference getSourceSchemeReference(CodingScheme mappingScheme) throws LBParameterException {
        if(mappingScheme.getRelations() == null || mappingScheme.getRelations().length < 1){
            throw new RuntimeException("Invalid mapping scheme " + mappingScheme.getCodingSchemeName() + " has no relations containers");
        }
        
        String source = mappingScheme.getRelationsAsReference().get(0).getSourceCodingScheme();
        if(source == null){ throw new RuntimeException("Source cannot be null");}
        String sourceVersion = mappingScheme.getRelationsAsReference().get(0).getSourceCodingSchemeVersion();
        if(sourceVersion == null){sourceVersion = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalVersionStringForTag(source, "PRODUCTION");}
       return null;
    }

    private CodingScheme resolveMappingMetaData() {
        CodingScheme scheme;
        try {
            scheme = LexBIGServiceImpl.defaultInstance().resolveCodingScheme(mappingUri, 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion));
        } catch (LBInvocationException | LBParameterException e) {
            throw new RuntimeException("Mapping Scheme " + mappingUri + ":" + mappingVersion + " may not exist", e);
        }
        return scheme;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping#resolveMapping()
     */
    @Override
    public ResolvedConceptReferencesIterator resolveMapping() throws LBException {
        return this.resolveMapping(null);
    }
    
    protected int estimateMappingNumber(
            ResolvedConceptReferencesIterator sourceResolvedConceptReferencesIterator, 
            ResolvedConceptReferencesIterator targetResolvedConceptReferencesIterator, 
            ResolvedConceptReferencesIterator sourceAndTargetresolvedConceptReferencesIterator) throws LBResourceUnavailableException{
        if(sourceResolvedConceptReferencesIterator == null && 
                targetResolvedConceptReferencesIterator == null &&
                sourceAndTargetresolvedConceptReferencesIterator == null){
            return IteratorBackedResolvedConceptReferencesIterator.UNKNOWN_NUMBER;
        }
        
        //if the sourceAndTarget iterator is there -- the other two must be null
        if(sourceAndTargetresolvedConceptReferencesIterator != null){   
            int number = sourceAndTargetresolvedConceptReferencesIterator.numberRemaining();
            return this.getEstimateNumber(number);
        }

        if(sourceResolvedConceptReferencesIterator == null){
            return targetResolvedConceptReferencesIterator.numberRemaining();
        }

        if(targetResolvedConceptReferencesIterator == null){
            return sourceResolvedConceptReferencesIterator.numberRemaining();
        }

        int number = sourceResolvedConceptReferencesIterator.numberRemaining()
                    +
                    sourceResolvedConceptReferencesIterator.numberRemaining();
        return this.getEstimateNumber(number);     
    }
    
    private int getEstimateNumber(int number){
        if(number == 0 || number == 1){
            return number;
        } else {
            return number / 2;
        }
    }
    
    //TODO: Find a more efficient way of doing this
    protected void processRelationshipRestrictions() throws LBException {        
        if(CollectionUtils.isEmpty(this.relationshipRestrictions)){
            return;
        }
        
        int ITERATOR_PAGE_SIZE = 100;
        
        for(RelationshipRestriction restriction : this.relationshipRestrictions){
            
            List<String> associationNames = 
                DaoUtility.localNameListToString(restriction.getRelationshipNames());
            
            CodedNodeSet cns = restriction.getCodedNodeSet();
            
            ResolvedConceptReferencesIterator itr = 
                cns.resolve(null, null, null, null, false);
            
            List<ConceptReference> targetCodes = new ArrayList<ConceptReference>(); 
            
            while(itr.hasNext()){
                targetCodes.addAll(DaoUtility.createList(ConceptReference.class, itr.next(ITERATOR_PAGE_SIZE).getResolvedConceptReference()));
            }
            
            for(ConceptReference conceptReference : targetCodes) {
                conceptReference.setCodeNamespace(null);
            }
            
            List<ConceptReference> requiredSourceCodes = this.getRequiredSourcesCodesFromRelationshipRestriction(targetCodes, associationNames);
            
            for(ConceptReference conceptReference : requiredSourceCodes) {
                conceptReference.setCodeNamespace(null);
            }
            
            if(CollectionUtils.isEmpty(requiredSourceCodes)){
                requiredSourceCodes = new
                    ArrayList<ConceptReference>();
                
                requiredSourceCodes.add(RestrictingMappingTripleUidIterator.INVALID_CONCEPT_REFERENCE);
            }
            
            ConceptReferenceList crl = new ConceptReferenceList();
            crl.setConceptReference(requiredSourceCodes.toArray(new ConceptReference[requiredSourceCodes.size()]));
            
            this.sourceCodesCodedNodeSet =
                this.getCodedNodeSet(SearchContext.SOURCE_CODES).restrictToMappingCodes(crl);
        }
    }

    protected List<ConceptReference> getRequiredSourcesCodesFromRelationshipRestriction(final List<ConceptReference> targetCodes, final List<String> associations){
        return LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<List<ConceptReference>>() {
    
                @Override
                public List<ConceptReference> execute(DaoManager daoManager) {
                    String codingSchemeUid = 
                        daoManager.getCodingSchemeDao(mappingUri, mappingVersion).
                        getCodingSchemeUIdByUriAndVersion(mappingUri, mappingVersion);
  
                    return daoManager.getCodedNodeGraphDao(mappingUri, mappingVersion).
                        getConceptReferencesContainingObject(
                                codingSchemeUid, 
                                relationsContainerName, 
                                targetCodes,
                                associations,
                                null, 
                                null,
                                null, 
                                null, 
                                null,
                                null,
                                0, 
                                -1);
                }
            });
    }

    @Override
    public ResolvedConceptReferencesIterator resolveMapping(final List<MappingSortOption> sortOptionList) throws LBException {      
        processRelationshipRestrictions();
        
        Iterator<ResolvedConceptReference> iterator;
        
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        int count;
        
        if(areAllCodedNodeSetsNull()){
           iterator = new MappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName,
                    sortOptionList);
           
           count = service.getMappingTriplesCount(mappingUri, mappingVersion, relationsContainerName);
           
        } else {
            RestrictingMappingTripleIterator restrictingIterator = 
                new RestrictingMappingTripleIterator(
                        mappingUri,
                        mappingVersion,
                        relationsContainerName, 
                        this.sourceCodesCodedNodeSet,
                        this.targetCodesCodedNodeSet,
                        this.sourceOrTargetCodesCodedNodeSet,
                        this.relationshipRestrictions,
                        sortOptionList);
            
            if(CollectionUtils.isNotEmpty(sortOptionList)){
                count = service.getMappingTriplesCountForCodes(
                        mappingUri, 
                        mappingVersion, 
                        relationsContainerName, 
                        restrictingIterator.getTripleUidIterator().getSourceResolvedIteratorConceptReferences(), 
                        restrictingIterator.getTripleUidIterator().getTargetResolvedIteratorConceptReferences(),
                        restrictingIterator.getTripleUidIterator().getSourceOrTargetResolvedIteratorConceptReferences());
            } else {
                count = service.getMappingTriplesCountForCodes(
                        mappingUri, 
                        mappingVersion, 
                        relationsContainerName, 
                        restrictingIterator.getTripleUidIterator().getSourceResolvedIteratorConceptReferences(), 
                        restrictingIterator.getTripleUidIterator().getTargetResolvedIteratorConceptReferences(),
                        restrictingIterator.getTripleUidIterator().getSourceOrTargetResolvedIteratorConceptReferences()
                        ); 
            }
            
            iterator = restrictingIterator;
        }
        
        return 
            new MappingResolvedConceptReferenceIterator(iterator, count, areAllCodedNodeSetsNull(), mappingUri, 
                    mappingUri, mappingUri, sortOptionList, sourceCodesCodedNodeSet, 
                    sourceCodesCodedNodeSet, sourceCodesCodedNodeSet, relationshipRestrictions);
    }

    @Override
    public Mapping restrictToMatchingDesignations(
            final String matchText, 
            final SearchDesignationOption option,
            final String matchAlgorithm, 
            final String language, 
            final SearchContext searchContext) throws LBInvocationException,
            LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                return codedNodeSet.
                    restrictToMatchingDesignations(
                            matchText, 
                            option, 
                            matchAlgorithm, 
                            language);
            }
            
        }, searchContext);
        
        return this;
    }
    
    private boolean areAllCodedNodeSetsNull(){
        return this.sourceCodesCodedNodeSet == null &&
            this.targetCodesCodedNodeSet == null &&
            this.sourceOrTargetCodesCodedNodeSet == null;
    }

    @Override
    public Mapping restrictToMatchingProperties(
            final LocalNameList propertyNames, 
            final PropertyType[] propertyTypes,
            final LocalNameList sourceList, 
            final LocalNameList contextList, 
            final NameAndValueList qualifierList, 
            final String matchText,
            final String matchAlgorithm, 
            final String language, 
            SearchContext searchContext) throws LBInvocationException,
            LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                return codedNodeSet.restrictToMatchingProperties(
                        propertyNames, 
                        propertyTypes, 
                        sourceList, 
                        contextList, 
                        qualifierList, 
                        matchText, 
                        matchAlgorithm, 
                        language);
            }
            
        }, searchContext);
        
        return this;
    }

    @Override
    public Mapping restrictToCodes(
            final ConceptReferenceList codeList, 
            SearchContext searchContext)
            throws LBInvocationException, LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                ConceptReferenceList updatedCodeList = areCodesContainedInContext(codeList, searchContext);
                if(updatedCodeList.getConceptReferenceCount() > 0)
                {return codedNodeSet.restrictToCodes(updatedCodeList);}
                else return codedNodeSet;
            }

            private ConceptReferenceList areCodesContainedInContext(ConceptReferenceList codeList, SearchContext searchContext) {
             ConceptReferenceList results = new ConceptReferenceList();
                if(searchContext.equals(SearchContext.SOURCE_CODES)){
                results.setConceptReference(
                        (ConceptReference[]) Arrays
                        .stream(codeList.getConceptReference())
                        .filter(x-> sourceIdAndNamespaceMap.containsValue(
                                x.getConceptCode()))
                        .toArray(ConceptReference[]::new));
                
                return results;
            }
            if (searchContext.equals(SearchContext.TARGET_CODES)){
                results.setConceptReference(
                        (ConceptReference[]) Arrays
                        .stream(codeList.getConceptReference())
                        .filter(x-> targetIdAndNamespaceMap.containsValue(
                                x.getConceptCode()))
                        .toArray(ConceptReference[]::new));
                
                return results;
            }
            
            if (searchContext.equals(SearchContext.SOURCE_OR_TARGET_CODES)){

                       ConceptReference[] targets = Arrays
                        .stream(codeList.getConceptReference())
                        .filter(x-> targetIdAndNamespaceMap.containsValue(
                                x.getConceptCode()))
                        .toArray(ConceptReference[]::new);
                       

                       ConceptReference[] sources =  Arrays
                        .stream(codeList.getConceptReference())
                        .filter(x-> sourceIdAndNamespaceMap.containsValue(
                                x.getConceptCode()))
                        .toArray(ConceptReference[]::new);
                       
                       results.setConceptReference(
                               Stream.concat(Arrays
                                .stream(targets), 
                                       Arrays.stream(sources))
                               .toArray(ConceptReference[]::new));

                return results;
            }
                return results;
            }
            
        }, searchContext);
        
        return this;
    }

    @Override
    public Mapping restrictToRelationship(
            String matchText, 
            SearchDesignationOption option, 
            String matchAlgorithm,
            String language, 
            LocalNameList relationshipList) throws LBInvocationException, LBParameterException {
        
        CodedNodeSet cns = this.createCodedNodeSet();
        cns = cns.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
 
        this.relationshipRestrictions.add(new RelationshipRestriction(cns, relationshipList));
        
        return this;
    }
    
    private CodedNodeSet getCodedNodeSet(SearchContext searchContext) throws LBParameterException{
        switch(searchContext){
        
            case SOURCE_CODES : {
                if(this.sourceCodesCodedNodeSet == null){
                    this.sourceCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.sourceCodesCodedNodeSet;
            }
            
            case TARGET_CODES : {
                if(this.targetCodesCodedNodeSet == null){
                    this.targetCodesCodedNodeSet = this.createTargetCodedNodeSet();
                }
                return this.targetCodesCodedNodeSet;
            }
            
            case SOURCE_OR_TARGET_CODES : {
                if(this.sourceOrTargetCodesCodedNodeSet == null){
                    this.sourceOrTargetCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.sourceOrTargetCodesCodedNodeSet;
            }
        }
        
        throw new RuntimeException("Invalid SearchContext.");
    }
    
    protected CodedNodeSet createCodedNodeSet() throws LBParameterException{
        try {
            return LexBIGServiceImpl.defaultInstance().
                getNodeSet(
                        this.mappingUri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion), 
                        null);
        } catch (LBException e) {
            throw new LBParameterException(e.getMessage());
        }
    }
    
    protected CodedNodeSet createSourceCodedNodeSet() throws LBParameterException{
        try {
            return LexBIGServiceImpl.defaultInstance().
                getNodeSet(
                        this.sourceReference.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(this.sourceReference.getCodingSchemeVersion()), 
                        null);
        } catch (LBException e) {
            throw new LBParameterException(e.getMessage());
        }
    }
    
    protected CodedNodeSet createTargetCodedNodeSet() throws LBParameterException{
        try {
            return LexBIGServiceImpl.defaultInstance().
                getNodeSet(
                        this.targetReference.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(this.targetReference.getCodingSchemeVersion()), 
                        null);
        } catch (LBException e) {
            throw new LBParameterException(e.getMessage());
        }
    }
    
    protected CodedNodeGraph createCodedNodeGraph() throws LBParameterException{
        try {
            return LexBIGServiceImpl.defaultInstance().
                getNodeGraph(
                        this.mappingUri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion), 
                        null);
        } catch (LBException e) {
            throw new LBParameterException(e.getMessage());
        }
    }
    
    private interface DoRestrict {
        
        public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException;
    }
    
    protected void doRestrict(DoRestrict doRestrict, SearchContext searchContext) throws LBParameterException, LBInvocationException{
        
        if(searchContext == null){
            throw new LBParameterException("SearchContext cannot be null", "searchContext");
        }

        CodedNodeSet cns = this.getCodedNodeSet(searchContext);

        cns = doRestrict.restrict(cns);

        switch(searchContext){

            case SOURCE_CODES : {
                this.sourceCodesCodedNodeSet = cns;
                break;
            }
    
            case TARGET_CODES : {
                this.targetCodesCodedNodeSet = cns;
                break;
            }
    
            case SOURCE_OR_TARGET_CODES : {
                this.sourceOrTargetCodesCodedNodeSet = cns;
                break;
            }
        }
    }
    
    protected class RelationshipRestriction implements Serializable {
  
        private static final long serialVersionUID = -171885936580958085L;
        private CodedNodeSet codedNodeSet;
        private LocalNameList relationshipNames;
        
        protected RelationshipRestriction(){
            super();
        }

        protected RelationshipRestriction(CodedNodeSet codedNodeSet, LocalNameList relationshipNames) {
            super();
            this.codedNodeSet = codedNodeSet;
            this.relationshipNames = relationshipNames;
        }

        public CodedNodeSet getCodedNodeSet() {
            return codedNodeSet;
        }
        public void setCodedNodeSet(CodedNodeSet codedNodeSet) {
            this.codedNodeSet = codedNodeSet;
        }
        public LocalNameList getRelationshipNames() {
            return relationshipNames;
        }
        public void setRelationshipName(LocalNameList relationshipNames) {
            this.relationshipNames = relationshipNames;
        }   
    }

    /**
     * Gets the mapping uri.
     * 
     * @return the mapping uri
     */
    public String getMappingUri(){
        return this.mappingUri;
    }
    
    /**
     * Gets the mapping version.
     * 
     * @return the mapping version
     */
    public String getMappingVersion(){
        return this.mappingVersion;
    }
    
    /**
     * Gets the relations container name.
     * 
     * @return the relations container name
     */
    public String getRelationsContainerName(){
        return this.relationsContainerName;
    }
}
