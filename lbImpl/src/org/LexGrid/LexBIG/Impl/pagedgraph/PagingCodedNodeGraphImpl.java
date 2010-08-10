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
package org.LexGrid.LexBIG.Impl.pagedgraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandler;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandlerFactory;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.model.LazyLoadableResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.DefaultGraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.ValidatedParameterResolvingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * The Class PagingCodedNodeGraphImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagingCodedNodeGraphImpl extends AbstractQueryBuildingCodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1153282485482789848L;

    private RootsResolver rootsResolver = new NullFocusRootsResolver();
    
    private AssociationListBuilder associationListBuilder = new AssociationListBuilder();
    
    public PagingCodedNodeGraphImpl() {
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
    public PagingCodedNodeGraphImpl(
            String codingSchemeUri, 
            String version,
            String relationsContainerName) throws LBParameterException{
        super(codingSchemeUri, version, StringUtils.isBlank(relationsContainerName) ? null : relationsContainerName);
    }
    
  
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsValidatedParameterList(
    		ConceptReference graphFocus, 
    		boolean resolveForward,
            boolean resolveBackward, 
            int resolveCodedEntryDepth, 
            int resolveAssociationDepth,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortOptions,
            LocalNameList filterOptions, 
            int maxToReturn,
            boolean keepLastAssociationLevelUnresolved,
            CycleDetectingCallback cycleDetectingCallback) throws LBInvocationException, LBParameterException {
              
        String codingSchemeUri = this.getCodingSchemeUri();
        String version = this.getVersion();
        String relationsContainerName = this.getRelationsContainerName();
        GraphQueryBuilder graphQueryBuilder = this.getGraphQueryBuilder(); 
        
        Filter[] filters = ServiceUtility.validateFilters(filterOptions);
        
        if (graphFocus == null && resolveForward && resolveBackward) {
            throw new LBParameterException(
                    "If you do not provide a focus node, you must choose resolve forward or resolve reverse, not both."
                    + "  Choose resolve forward to start at root nodes.  Choose resolve reverse to start at tail nodes.");
        }
        
        ResolvedConceptReference focus = null;
        
        if(graphFocus != null) {

            if(StringUtils.isNotBlank(graphFocus.getCodeNamespace())
                    &&
                    StringUtils.isNotBlank(graphFocus.getCodingSchemeName())){
                String codingSchemeName =
                    this.getNamespaceHandler().getCodingSchemeNameForNamespace(codingSchemeUri, version, graphFocus.getCodeNamespace());

                if(! StringUtils.equals(graphFocus.getCodingSchemeName(), codingSchemeName)){
                    return ServiceUtility.throwExceptionOrReturnDefault(
                            new LBParameterException("Based on the namespace provided as a focus (" + graphFocus.getCodeNamespace() + ")" +
                                    " there is no match to the provided Coding Scheme Name (" + graphFocus.getCodingSchemeName() + ")." +
                                    " If " + graphFocus.getCodeNamespace() + " is meant to be equivalent to the CodingScheme " + graphFocus.getCodingSchemeName() + ", " +
                                    " this must be declared in the SupportedNamespaces."),
                            new ResolvedConceptReferenceList(),
                            this.isStrictFocusValidation());
                }
            }
            
            focus =
                LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
                getEntityService().
                getResolvedCodedNodeReference(
                        codingSchemeUri, 
                        version, 
                        graphFocus.getCode(), 
                        graphFocus.getCodeNamespace(),
                        this.shouldResolveNextLevel(resolveCodedEntryDepth),
                        DaoUtility.localNameListToString(propertyNames),
                        DaoUtility.propertyTypeArrayToString(propertyTypes));
            
            if(focus == null) {
                if(graphFocus.getCodeNamespace() != null) {
                    AbsoluteCodingSchemeVersionReference ref = null;
                    try {
                        ref = this.getNamespaceHandler().getCodingSchemeForNamespace(codingSchemeUri, version, graphFocus.getCodeNamespace());
                    } catch (LBParameterException e) {
                       LoggerFactory.getLogger().warn(e.getMessage());
                    }
                
                    if(ref != null) {
                        focus =
                            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
                            getEntityService().
                            getResolvedCodedNodeReference(
                                    ref.getCodingSchemeURN(), 
                                    ref.getCodingSchemeVersion(), 
                                    graphFocus.getCode(), 
                                    graphFocus.getCodeNamespace(),
                                    this.shouldResolveNextLevel(resolveCodedEntryDepth),
                                    DaoUtility.localNameListToString(propertyNames),
                                    DaoUtility.propertyTypeArrayToString(propertyTypes));
                    }
                }  
            }
           
            if(focus == null) {
               focus = new ResolvedConceptReference();
               focus.setCode(graphFocus.getCode());
               
               String namespace = graphFocus.getCodeNamespace();
               String codingSchemeName = graphFocus.getCodingSchemeName();
             
               if(StringUtils.isBlank(namespace)){
                   if(StringUtils.isBlank(codingSchemeName)) {
                       namespace = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
                   } else {
                       List<String> namespaces = this.getNamespaceHandler().getNamespacesForCodingScheme(codingSchemeUri, version, codingSchemeName);
                       if(CollectionUtils.isEmpty(namespaces)) {
                           return ServiceUtility.throwExceptionOrReturnDefault(
                                   new LBParameterException("The provided focus did not contain a namespace, and information in the " +
                                   "SupportedNamespaces was unable to generate the correct one."),
                                   new ResolvedConceptReferenceList(),
                                   this.isStrictFocusValidation());
                       }
                       if(namespaces.size() > 1) {
                           return ServiceUtility.throwExceptionOrReturnDefault(
                                   new LBParameterException("The provided focus did not contain a namespace, and information in the " +
                                   "SupportedNamespaces did not provide a unique namespace. Please provide a namespace with the focus"),
                                   new ResolvedConceptReferenceList(),
                                   this.isStrictFocusValidation());
                       }
                       
                       namespace = namespaces.get(0);
                   }
               } 
               
               String expectedCodingSchemeName = this.getNamespaceHandler().getCodingSchemeNameForNamespace(codingSchemeUri, version, namespace);
               if(StringUtils.isBlank(codingSchemeName)){
                   codingSchemeName = expectedCodingSchemeName;
               } else {
                   if(!codingSchemeName.equals(expectedCodingSchemeName)) {
                       return ServiceUtility.throwExceptionOrReturnDefault(
                               new LBParameterException("Based on the namespace provided as a focus (" + namespace + ")" +
                                       " there is no match to the provided Coding Scheme Name (" + codingSchemeName + ")." +
                                       " If " + namespace + " is meant to be equivalent to the CodingScheme " + codingSchemeName + ", " +
                                       " this must be declared in the SupportedNamespaces."
                               ), 
                               new ResolvedConceptReferenceList(),
                               this.isStrictFocusValidation()
                       );
                   }
               }

               if(StringUtils.isBlank(codingSchemeName)) {
                   return ServiceUtility.throwExceptionOrReturnDefault(
                           new LBParameterException("Could not determine a Coding Scheme for the requested Focus Code."),
                           new ResolvedConceptReferenceList(),
                           this.isStrictFocusValidation()
                           );
               }
               
               focus.setCodeNamespace(namespace);    
               
               focus.setCodingSchemeName(codingSchemeName);
            }
            
            boolean isValidFocus = this.checkFocus(focus, resolveForward, resolveBackward, filters);
            
            if(! isValidFocus) {
                return new ResolvedConceptReferenceList();
            }
            
        } else {

            return new LazyLoadableResolvedConceptReferenceList(
                    new ValidatedParameterResolvingCallback(this), 
                    this.getCodingSchemeUri(),
                    this.getVersion(),
                    this.getRelationsContainerName(),
                    resolveForward, 
                    resolveBackward, 
                    resolveAssociationDepth, 
                    resolveCodedEntryDepth, 
                    keepLastAssociationLevelUnresolved,
                    this.getGraphQueryBuilder().getQuery(),
                    propertyNames, 
                    propertyTypes,
                    sortOptions, 
                    filterOptions, 
                    cycleDetectingCallback,
                    maxToReturn);

        }
        
        if(this.rootsResolver.isRootOrTail(focus) && resolveAssociationDepth >= 0) {
            resolveAssociationDepth++;
        }

        if(resolveForward && shouldResolveNextLevel(resolveAssociationDepth)) {
 
            focus.setSourceOf(
            		associationListBuilder.buildSourceOfAssociationList(
            		this.getCodingSchemeUri(),
            		this.getVersion(),
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveAssociationDepth - 1, 
                    resolveAssociationDepth,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    propertyNames,
                    propertyTypes,
                    sortOptions,
                    filterOptions,
                    cycleDetectingCallback));
        }
        
        if(resolveBackward && shouldResolveNextLevel(resolveAssociationDepth)) {
            focus.setTargetOf(
            		associationListBuilder.buildTargetOfAssociationList(
            		this.getCodingSchemeUri(),
                    this.getVersion(),
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveAssociationDepth, 
                    resolveAssociationDepth - 1,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    propertyNames,
                    propertyTypes,
                    sortOptions,
                    filterOptions,
                    cycleDetectingCallback));
        }
        
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        if(! this.rootsResolver.isRootOrTail(focus)) {
            returnList.addResolvedConceptReference(focus);
        } else {
            returnList = flattenRootList(focus);
        }
    
        return returnList;
    }

    private ResolvedConceptReferenceList flattenRootList(ResolvedConceptReference root) {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        if(root.getSourceOf() != null) {
            for(Association assoc : root.getSourceOf().getAssociation()) {
                for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                    returnList.addResolvedConceptReference(ac);
                }
            }
        }
        
        if(root.getTargetOf() != null) {
            for(Association assoc : root.getTargetOf().getAssociation()) {
                for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                    returnList.addResolvedConceptReference(ac);
                }
            }
        }
        
        return returnList;
    }

    protected boolean checkFocus(ResolvedConceptReference focus, boolean resolveForward, boolean resolveBackward, Filter[] filters) {
        if(focus == null) {return false;}
 
        boolean hasReferenceToSourceCodeRestriction = hasReferenceToSourceCodeRestriction(focus);
        boolean hasReferenceToTargetCodeRestriction = hasReferenceToTargetCodeRestriction(focus);
        boolean isInvalidMatchConceptReference = isNotInvalidMatchConceptReference(focus);
        boolean isNotFilteredOut = isNotFilteredConceptReference(focus, filters);
        boolean existsInGraph = existsInGraph(focus);
      
        return 
            hasReferenceToSourceCodeRestriction && 
            hasReferenceToTargetCodeRestriction && 
            isInvalidMatchConceptReference &&
            isNotFilteredOut &&
            existsInGraph;  
    }
    
    private boolean isNotFilteredConceptReference(ResolvedConceptReference focus, Filter[] filters) {
        return ServiceUtility.passFilters(focus, filters);
    }


    private boolean isNotInvalidMatchConceptReference(ConceptReference focus) {
        return !PagedGraphUtils.areCodedNodeReferencesEquals(focus, DefaultGraphQueryBuilder.INVALID_MATCH_CONCEPT_REFERENCE);
    }
    
    private boolean hasReferenceToTargetCodeRestriction(ConceptReference focus) {
        List<ConceptReference>  restrictToTargetCodes = this.getGraphQueryBuilder().getQuery().getRestrictToTargetCodes();
        if(CollectionUtils.isEmpty(restrictToTargetCodes)) {
            return true;
        }
        if(containsConceptReference(focus, restrictToTargetCodes)) {
            return true;
        }
        
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        Map<String,Integer> count = 
        service.getTripleUidsContainingSubjectCount(
                this.getCodingSchemeUri(), 
                this.getVersion(), 
                this.getRelationsContainerName(), 
                focus.getCode(), 
                focus.getCodeNamespace(), 
                this.getGraphQueryBuilder().getQuery());
        
        return !count.isEmpty();
    }

    private boolean hasReferenceToSourceCodeRestriction(ConceptReference focus) {
        List<ConceptReference>  restrictToSourceCodes = this.getGraphQueryBuilder().getQuery().getRestrictToSourceCodes(); 
        if(CollectionUtils.isEmpty(restrictToSourceCodes)) {
            return true;
        }
        if(containsConceptReference(focus, restrictToSourceCodes)) {
            return true;
        }
        
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        Map<String,Integer> count = 
        service.getTripleUidsContainingObjectCount(
                this.getCodingSchemeUri(), 
                this.getVersion(), 
                this.getRelationsContainerName(), 
                focus.getCode(), 
                focus.getCodeNamespace(), 
                this.getGraphQueryBuilder().getQuery());
        
        return !count.isEmpty();
    }
    
    private boolean existsInGraph(ConceptReference focus) {
 
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        Map<String,Integer> subjectCount = 
        service.getTripleUidsContainingSubjectCount(
                this.getCodingSchemeUri(), 
                this.getVersion(), 
                this.getRelationsContainerName(), 
                focus.getCode(), 
                focus.getCodeNamespace(), 
                this.getGraphQueryBuilder().getQuery());
        
        Map<String,Integer> objectCount = 
            service.getTripleUidsContainingObjectCount(
                    this.getCodingSchemeUri(), 
                    this.getVersion(), 
                    this.getRelationsContainerName(), 
                    focus.getCode(), 
                    focus.getCodeNamespace(), 
                    this.getGraphQueryBuilder().getQuery());
        
        return !subjectCount.isEmpty() || !objectCount.isEmpty();
    }
    
    private static boolean containsConceptReference(ConceptReference ref, List<ConceptReference> list) {
        for(ConceptReference conceptRef : list) {
            if(ref.getCode().equals(conceptRef.getCode()) &&
                    ref.getCodeNamespace().equals(conceptRef.getCodeNamespace())){
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Should resolve next level.
     * 
     * @param depth the depth
     * 
     * @return true, if successful
     */
    private boolean shouldResolveNextLevel(int depth) {
        return ! (depth == 0);
    }
    
    private NamespaceHandler getNamespaceHandler() {
        return NamespaceHandlerFactory.getNamespaceHandler();
    }
}
