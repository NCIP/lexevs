
package org.LexGrid.LexBIG.Impl.pagedgraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.pagedgraph.PagingCodedNodeGraphImpl.ArtificialRootResolvePolicy;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.ReferenceReturningCycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.StubReturningCycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.DefaultGraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver.ResolveDirection;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContainerName;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * The Class AbstractQueryBuildingCodedNodeGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractQueryBuildingCodedNodeGraph extends AbstractCodedNodeGraph {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1153282485482789848L;
    
    /** The builder. */
    private GraphQueryBuilder graphQueryBuilder;
    
    /** The coding scheme uri. */
    private String codingSchemeUri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
        
    private boolean strictFocusValidation = true;
    
    public AbstractQueryBuildingCodedNodeGraph() {
        super();
    }
    
    /**
     * Instantiates a new paging coded node graph impl.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @throws LBParameterException 
     */
    public AbstractQueryBuildingCodedNodeGraph(
            String codingSchemeUri, 
            String version,
            String relationsContainerName) throws LBParameterException {
        try {
            ServiceUtility.validateParameter(codingSchemeUri, version, relationsContainerName, SupportedContainerName.class);
        } catch (LBParameterException e) {
            LoggerFactory.getLogger().warn("Requested Relations Container Name is not registered as a SupportedContainerName: " + relationsContainerName);
        }
        
        this.codingSchemeUri = codingSchemeUri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
  
        graphQueryBuilder = new DefaultGraphQueryBuilder(codingSchemeUri, version);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#listCodeRelationships(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    @Override
    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
       return LexEvsServiceLocator.getInstance().
           getDatabaseServiceManager().
               getCodedNodeGraphService().
                   listCodeRelationships(
                           codingSchemeUri, 
                           version, 
                           relationsContainerName, 
                           sourceCode.getCode(), 
                           sourceCode.getCodeNamespace(), 
                           targetCode.getCode(), 
                           targetCode.getCodeNamespace(), 
                           this.graphQueryBuilder.getQuery(), 
                           !directOnly);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#areCodesRelated(org.LexGrid.LexBIG.DataModel.Core.NameAndValue, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    @Override
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
       boolean areRelatedForward = this.doGetAreCodesRelated(sourceCode, targetCode, association, directOnly);
       
       if(areRelatedForward) {return true;}
       
       boolean areRelatedBackward = this.doGetAreCodesRelated(targetCode, sourceCode, association, directOnly);
       
       if(areRelatedBackward) {return true;}
       
       return false;  
    }
    
    
    
    @Override
    public ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        
        ServiceUtility.validateParameter(this.getCodingSchemeUri(), this.getVersion(), propertyNames, SupportedProperty.class);
        ServiceUtility.validateSortOptions(sortOptions);
        //Implementation to return either the full reference or a stub upon detecting a cycle
        CycleDetectingCallback cycleDetectingCallback;
        
        if(resolveForward && resolveBackward) {
            cycleDetectingCallback = new ReferenceReturningCycleDetectingCallback();
        } else {
            cycleDetectingCallback = new StubReturningCycleDetectingCallback();
        }
        
        return this.doResolveAsValidatedParameterList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, 
                propertyNames, propertyTypes, sortOptions, 
                filterOptions, maxToReturn, keepLastAssociationLevelUnresolved, cycleDetectingCallback);
    }
    
    protected ResolvedConceptReferenceList doResolveAsValidatedParameterList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved, 
            CycleDetectingCallback cycleDetectingCallback)
            throws LBInvocationException, LBParameterException{
        return this.doResolveAsValidatedParameterList(
                graphFocus, 
                resolveForward, 
                resolveBackward, 
                resolveCodedEntryDepth, 
                resolveAssociationDepth, 
                propertyNames, 
                propertyTypes,
                sortOptions, 
                filterOptions,
                maxToReturn, 
                keepLastAssociationLevelUnresolved, 
                ArtificialRootResolvePolicy.RESOLVE_CHILDREN, 
                cycleDetectingCallback);
    }
    
    protected abstract ResolvedConceptReferenceList doResolveAsValidatedParameterList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved, 
            ArtificialRootResolvePolicy artificialRootResolvePolicy,
            CycleDetectingCallback cycleDetectingCallback)
            throws LBInvocationException, LBParameterException;

    protected boolean doGetAreCodesRelated(
            ConceptReference sourceCode, 
            ConceptReference targetCode, 
            NameAndValue association,
            boolean directOnly) throws LBParameterException, LBInvocationException {

        GraphQueryBuilder builder = new DefaultGraphQueryBuilder(this.codingSchemeUri, this.version, getClonedGraphQuery());
        
        NameAndValueList nvl = new NameAndValueList();
        nvl.addNameAndValue(association);
        
        builder.restrictToAssociations(nvl, null);
        builder.getQuery().getRestrictToTargetCodes().add(Constructors.createConceptReference(targetCode.getCode(), targetCode.getCodeNamespace(), null));
        
        CodedNodeGraphService service = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getCodedNodeGraphService();
        
        List<String> subjectAssocs = service.listCodeRelationships(
                codingSchemeUri, 
                version, 
                relationsContainerName, 
                sourceCode.getCode(), 
                sourceCode.getCodeNamespace(), 
                targetCode.getCode(),
                targetCode.getCodeNamespace(),
                builder.getQuery(),
                !directOnly);
        
        builder = new DefaultGraphQueryBuilder(this.codingSchemeUri, this.version, getClonedGraphQuery());

        builder.restrictToAssociations(nvl, null);
        builder.getQuery().getRestrictToSourceCodes().add(Constructors.createConceptReference(sourceCode.getCode(), sourceCode.getCodeNamespace(), null));
    
        List<String> objectAssocs = service.listCodeRelationships(
                codingSchemeUri, 
                version, 
                relationsContainerName, 
                targetCode.getCode(), 
                targetCode.getCodeNamespace(), 
                sourceCode.getCode(),
                sourceCode.getCodeNamespace(),
                builder.getQuery(),
                !directOnly);
        
        return !CollectionUtils.isEmpty(subjectAssocs) || !CollectionUtils.isEmpty(objectAssocs);
    }
 
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToAssociations(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)
            throws LBInvocationException, LBParameterException {
        this.graphQueryBuilder.restrictToAssociations(association, associationQualifiers);
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToCodeSystem(String codingScheme) throws LBInvocationException, LBParameterException {
        this.graphQueryBuilder.restrictToCodeSystem(codingScheme);
        return this;
    }

    @Override
    public CodedNodeGraph restrictToAnonymous(Boolean restrictToAnonymous) throws LBInvocationException,
            LBParameterException {
       this.graphQueryBuilder.restrictToAnonymous(restrictToAnonymous);
       return this;
    }

    @Override
    public CodedNodeGraph restrictToEntityTypes(LocalNameList localNameList) throws LBInvocationException,
            LBParameterException {
       this.graphQueryBuilder.restrictToEntityTypes(localNameList);
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graphQueryBuilder.restrictToCodes(codes);
        this.setStrictFocusValidation(false);
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToDirectionalNames(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) throws LBInvocationException, LBParameterException {
       this.graphQueryBuilder.restrictToDirectionalNames(directionalNames, associationQualifiers);
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
        this.graphQueryBuilder.restrictToSourceCodeSystem(codingScheme);
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graphQueryBuilder.restrictToSourceCodes(codes);
        this.setStrictFocusValidation(false);
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
        this.graphQueryBuilder.restrictToTargetCodeSystem(codingScheme);
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graphQueryBuilder.restrictToTargetCodes(codes);
        this.setStrictFocusValidation(false);
        return this;
    }
    
    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {

        MappingExtension mappingExtension = 
            (MappingExtension) LexBIGServiceImpl.defaultInstance().getGenericExtension("MappingExtension");
        
        ConceptReferenceList codeList;
        String altSchemeName = null;
        String altUri = null;
        String altVersion = null;
        if( graphFocus == null 
                &&
                mappingExtension.isMappingCodingScheme(
                codingSchemeUri, 
                Constructors.createCodingSchemeVersionOrTagFromVersion(version))) {
            List<String> relationContainerNames;
            if(relationsContainerName != null) {
                relationContainerNames = Arrays.asList(relationsContainerName);
            } else {
                relationContainerNames = 
                    LexEvsServiceLocator.getInstance().
                        getDatabaseServiceManager().
                            getCodedNodeGraphService().
                                getRelationNamesForCodingScheme(codingSchemeUri, version);
            }
            CodingSchemeVersionOrTag mappingVersion = new CodingSchemeVersionOrTag();
            mappingVersion.setVersion(version);
            CodingScheme mappingScheme = LexBIGServiceImpl.defaultInstance().resolveCodingScheme(codingSchemeUri, mappingVersion);
           
            if(!this.getGraphQueryBuilder().getQuery().getRestrictToSourceCodeSystem().isEmpty()){
                altSchemeName =  mappingScheme.getRelations()[0].getSourceCodingScheme();
                
            }
            else if(!this.getGraphQueryBuilder().getQuery().getRestrictToTargetCodeSystem().isEmpty()){
                altSchemeName = mappingScheme.getRelations()[0].getTargetCodingScheme();;
            }
            
            
            
            codeList = new ConceptReferenceList();
            
            for(String relationContainerName : relationContainerNames) {
                ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
                        codingSchemeUri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(version), 
                        relationContainerName, 
                        null);
                //We don't want to modify a cached result.  
                //So we need to clone it and reassign it before modification.
               ResolvedConceptReference[] rcrl = null;
                try {
                    rcrl = itr.next(itr.numberRemaining()).getResolvedConceptReference();
                    ResolvedConceptReference[] deepCopy = DaoUtility.deepClone(rcrl);
                    for( ResolvedConceptReference ref: deepCopy){
                        if(resolveForward || (resolveBackward && resolveAssociationDepth > 0)) {
                            ref.setCodeNamespace(correctNamepsaceForEquivalentCodingScheme(ref.getCodeNamespace(), mappingScheme));
                            codeList.addConceptReference(ref);
                        }
                        for(Association assoc : ref.getSourceOf().getAssociation()) {
                            for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                                if(resolveBackward || (resolveForward && resolveAssociationDepth > 0)) {
                                	ac.setCodeNamespace(correctNamepsaceForEquivalentCodingScheme(ref.getCodeNamespace(), mappingScheme));
                                    codeList.addConceptReference(ac);
                                }
                            }
                        }
                    }
                    
                    altUri = ServiceUtility.getUriForCodingSchemeName(altSchemeName);
                    CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                    csvt.setTag("PRODUCTION");
                    altVersion = ServiceUtility.getVersion(altUri, csvt);
                } catch (LBResourceUnavailableException e) {
                    throw new RuntimeException(e);
                } 
            }
        } else {
            ResolvedConceptReferenceList list = 
                this.doResolveAsValidatedParameterList(
                        graphFocus, 
                        resolveForward, 
                        resolveBackward, 
                        0, 
                        resolveAssociationDepth,
                        null, 
                        null, 
                        null, 
                        null, 
                        maxToReturn, 
                        false, 
                        ArtificialRootResolvePolicy.RESOLVE_CHILDREN,
                        new ReferenceReturningCycleDetectingCallback());
            
            codeList = this.traverseGraph(list, resolveForward, resolveBackward, maxToReturn);

        }
   
        try {
            if(altUri != null){
                this.setCodingSchemeUri(altUri);
                this.setVersion(altVersion);
            }
            CodedNodeSetImpl cns = new CodedNodeSetImpl(
                    this.getCodingSchemeUri(),
                    Constructors.createCodingSchemeVersionOrTagFromVersion(this.getVersion()), null, null);

            return cns.restrictToCodes(codeList);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    private String correctNamepsaceForEquivalentCodingScheme(String codeNamespace, CodingScheme mapping) {
       List<SupportedNamespace> namespaces = mapping.getMappings().getSupportedNamespaceAsReference();
       for(SupportedNamespace sn : namespaces){
           if(sn.getLocalId().equals(codeNamespace)){
               return getEquivalentCodingSchemeForNamespace(codeNamespace, mapping, sn);
           }
       }
        return codeNamespace;
    }

    private String getEquivalentCodingSchemeForNamespace(String codeNamespace, CodingScheme mapping, SupportedNamespace sn) {
        List<SupportedCodingScheme> schemes = mapping.getMappings().getSupportedCodingSchemeAsReference();
        for(SupportedCodingScheme scheme : schemes){
            if(scheme.getLocalId().equals(sn.getEquivalentCodingScheme())){
               return null;
            }
        }
        return codeNamespace;
    }
    

    private ConceptReferenceList traverseGraph(
            ResolvedConceptReferenceList list, 
            boolean resolveForward, 
            boolean resolveBackward, 
            int maxToReturn){
          
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        
        for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {
            returnList.addAll(traverseGraph(ref, resolveForward, resolveBackward, maxToReturn));
        }
        
        for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {
            returnList.addAll(traverseGraph(ref, resolveForward, resolveBackward, maxToReturn));
        }
        
        ConceptReferenceList refList = new ConceptReferenceList();
        refList.setConceptReference(returnList.toArray(new ConceptReference[returnList.size()] ));
        
        return refList;
    }
    
    private static class VisitedConceptReference {
        
        private boolean resolvedForward;
        private boolean resolvedBackward;
        
        private ConceptReference conceptReference;

        private VisitedConceptReference(ConceptReference conceptReference, ResolveDirection direction) {
            super();
            this.conceptReference = conceptReference;
            switch (direction) {
                case FORWARD : {this.resolvedForward = true;}
                case BACKWARD : {this.resolvedBackward = true;}
            }
        }   
        
        public int hashCode() {
            return getKey(conceptReference).hashCode();
        }
      
        private static String getKey(ConceptReference conceptReference) {
            return conceptReference.getCode() + conceptReference.getCodeNamespace();
        }
        
        public boolean equals(Object o) {
            return this.hashCode() == o.hashCode();
        }
    }
    private List<ConceptReference> traverseGraph(
            ResolvedConceptReference ref, 
            boolean resolveForward, 
            boolean resolveBackward,
            int maxToReturn){
        return this.traverseGraph(new HashMap<String,VisitedConceptReference>(), ref, resolveForward, resolveBackward, maxToReturn);
    }
    
    private List<ConceptReference> traverseGraph(
            Map<String,VisitedConceptReference> vistedMap,
            ResolvedConceptReference ref, 
            boolean resolveForward, 
            boolean resolveBackward,
            int maxToReturn){
        
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        returnList.add(ref);
        
        if(resolveForward) {
            if(ref.getSourceOf() != null) {
                for(Association assoc : ref.getSourceOf().getAssociation()) {
                    for(AssociatedConcept ac : buildAssociatedConceptArray(assoc, maxToReturn)) {
                        if(this.continueTraverse(ResolveDirection.FORWARD, ac, vistedMap)) {
                            
                            returnList.addAll(
                                    this.traverseGraph(vistedMap, ac, resolveForward, resolveBackward, maxToReturn));
                        }
                    }
                }
            }
        }
        
        if(resolveBackward) {
            if(ref.getTargetOf() != null) {
                for(Association assoc : ref.getTargetOf().getAssociation()) {
                    for(AssociatedConcept ac : buildAssociatedConceptArray(assoc, maxToReturn)) {
                        if(this.continueTraverse(ResolveDirection.BACKWARD, ac, vistedMap)) {
                           
                            returnList.addAll(
                                    this.traverseGraph(vistedMap, ac, resolveForward, resolveBackward, maxToReturn));
                        }
                    }
                }
            }
        }
        
        return returnList;
    }
    
    private boolean continueTraverse(ResolveDirection direction, ConceptReference ref, Map<String,VisitedConceptReference> vistedMap) {
        VisitedConceptReference visitedRef = vistedMap.get(VisitedConceptReference.getKey(ref));
        if(visitedRef == null) {
            vistedMap.put(VisitedConceptReference.getKey(ref),new VisitedConceptReference(ref, direction));
            return true;
        } else {
            switch (direction) {
                case FORWARD : {
                    if(visitedRef.resolvedForward) {
                        return false;
                    } else {
                        visitedRef.resolvedForward = true;
                        return true;
                    }
                    }
                case BACKWARD : {
                    if(visitedRef.resolvedBackward ) {
                        return false;
                    } else {
                        visitedRef.resolvedBackward = true;
                        return true;
                    }
                    }
                default : throw new RuntimeException("Error Traversing Graph.");
            }
        }   
    }
    
    private AssociatedConcept[] buildAssociatedConceptArray(Association association, int maxToReturn) {
        if(maxToReturn < 0) {
            maxToReturn = Integer.MAX_VALUE;
        }
        Iterator<? extends AssociatedConcept> itr =  association.getAssociatedConcepts().iterateAssociatedConcept();
        
        List<AssociatedConcept> list = new ArrayList<AssociatedConcept>();
        
        for(int i=0;i<maxToReturn && itr.hasNext();i++) {
            list.add(itr.next());
        }

        return list.toArray(new AssociatedConcept[list.size()]);
    }

    /**
     * Gets the graph query builder.
     * 
     * @return the graph query builder
     */
    public GraphQueryBuilder getGraphQueryBuilder() {
        return graphQueryBuilder;
    }

    /**
     * Sets the graph query builder.
     * 
     * @param graphQueryBuilder the new graph query builder
     */
    public void setGraphQueryBuilder(GraphQueryBuilder graphQueryBuilder) {
        this.graphQueryBuilder = graphQueryBuilder;
    }
    
    /**
     * Gets the coding scheme uri.
     * 
     * @return the coding scheme uri
     */
    public String getCodingSchemeUri() {
        return codingSchemeUri;
    }

    /**
     * Sets the coding scheme uri.
     * 
     * @param codingSchemeUri the new coding scheme uri
     */
    public void setCodingSchemeUri(String codingSchemeUri) {
        this.codingSchemeUri = codingSchemeUri;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * 
     * @param version the new version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the relations container name.
     * 
     * @return the relations container name
     */
    public String getRelationsContainerName() {
        return relationsContainerName;
    }

    /**
     * Sets the relations container name.
     * 
     * @param relationsContainerName the new relations container name
     */
    public void setRelationsContainerName(String relationsContainerName) {
        this.relationsContainerName = relationsContainerName;
    }
    
    private GraphQuery getClonedGraphQuery() {
        try {
            return this.graphQueryBuilder.getQuery().clone();
        } catch (CloneNotSupportedException e) {
           throw new RuntimeException(e);
        }
    }
    

    public void setStrictFocusValidation(boolean strictFocusValidation) {
        this.strictFocusValidation = strictFocusValidation;
    }

    public boolean isStrictFocusValidation() {
        return strictFocusValidation;
    }
}