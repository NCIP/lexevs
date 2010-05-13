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
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.StubReturningCycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.DefaultGraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver.ResolveDirection;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.util.CollectionUtils;

/**
 * The Class PagingCodedNodeGraphImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagingCodedNodeGraphImpl extends AbstractQueryBuildingCodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1153282485482789848L;
    
    /** The builder. */
    private AssociationListBuilder associationListBuilder = new AssociationListBuilder();
    
    //Implementation to return either the full reference or a stub upon detecting a cycle
    //private CycleDetectingCallback cycleDetectingCallback = new ReferenceReturningCycleDetectingCallback();
    private CycleDetectingCallback cycleDetectingCallback = new StubReturningCycleDetectingCallback();
   
    private RootsResolver rootsResolver = new NullFocusRootsResolver();
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
            boolean keepLastAssociationLevelUnresolved) throws LBInvocationException, LBParameterException {
        this.getLogger().warn("Paged Graph is currently an incomplete implementation. Graph functionality will be implemented incrementally.");
        
        String codingSchemeUri = this.getCodingSchemeUri();
        String version = this.getVersion();
        String relationsContainerName = this.getRelationsContainerName();
        GraphQueryBuilder graphQueryBuilder = this.getGraphQueryBuilder();
        
        
        if (graphFocus == null && resolveForward && resolveBackward) {
            throw new LBParameterException(
                    "If you do not provide a focus node, you must choose resolve forward or resolve reverse, not both."
                            + "  Choose resolve forward to start at root nodes.  Choose resolve reverse to start at tail nodes.");
        }
        
        ResolvedConceptReference focus = null;
        
        if(graphFocus != null) {
            focus =
                LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
                getEntityService().
                getResolvedCodedNodeReference(codingSchemeUri, version, graphFocus.getCode(), graphFocus.getCodeNamespace());
            if(focus == null) {
               focus = new ResolvedConceptReference();
               focus.setCode(graphFocus.getCode());
               focus.setCodeNamespace(graphFocus.getCodeNamespace());
               focus.setCodingSchemeName(graphFocus.getCodingSchemeName());
            }
            boolean isValidFocus = this.checkFocus(focus);
            
            if(! isValidFocus) {
                return new ResolvedConceptReferenceList();
            }
            
        } else {
            List<ConceptReference> codes = 
                rootsResolver.resolveRoots(
                        codingSchemeUri, 
                        version,
                        this.getRelationsContainerName(),
                        this.getDirection(resolveForward, resolveBackward), 
                        graphQueryBuilder.getQuery());

                ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();

                for(ConceptReference root : codes) {
                    
                    if(this.rootsResolver.isRootOrTail(root) && resolveAssociationDepth >= 0) {
                        resolveAssociationDepth++;
                    }
                    
                    ResolvedConceptReferenceList list = this.doResolveAsList(
                            root, 
                            resolveForward, 
                            resolveBackward, 
                            resolveCodedEntryDepth, 
                            resolveAssociationDepth, 
                            propertyNames, 
                            propertyTypes,
                            sortOptions, 
                            filterOptions, 
                            maxToReturn, 
                            keepLastAssociationLevelUnresolved);
                    
                    if(list != null) {

                        for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {

                            returnList.addResolvedConceptReference(ref);    
                        }
                    }
                }
                return returnList;
        }

        if(resolveForward && shouldResolveNextLevel(resolveAssociationDepth)) {
            focus.setSourceOf(
            		associationListBuilder.buildSourceOfAssociationList(
                    codingSchemeUri,
                    version, 
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveAssociationDepth - 1, 
                    resolveAssociationDepth,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    sortOptions,
                    cycleDetectingCallback));
        }
        
        if(resolveBackward && shouldResolveNextLevel(resolveAssociationDepth)) {
            focus.setTargetOf(
            		associationListBuilder.buildTargetOfAssociationList(
                    codingSchemeUri,
                    version, 
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveAssociationDepth, 
                    resolveAssociationDepth - 1,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    sortOptions,
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

    protected boolean checkFocus(ConceptReference focus) {
 
        boolean hasReferenceToSourceCodeRestriction = hasReferenceToSourceCodeRestriction(focus);
        boolean hasReferenceToTargetCodeRestriction = hasReferenceToTargetCodeRestriction(focus);
        boolean isInvalidMatchConceptReference = isNotInvalidMatchConceptReference(focus);
     
        return hasReferenceToSourceCodeRestriction && hasReferenceToTargetCodeRestriction && isInvalidMatchConceptReference;  
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
    
    private static boolean containsConceptReference(ConceptReference ref, List<ConceptReference> list) {
        for(ConceptReference conceptRef : list) {
            if(ref.getCode().equals(conceptRef.getCode()) &&
                    ref.getCodeNamespace().equals(conceptRef.getCodeNamespace())){
                return true;
            }
        }
        return false;
    }
    
    private ResolveDirection getDirection(boolean resolveForward, boolean resolveBackward) {
        if(resolveForward && resolveBackward) {
            throw new RuntimeException();
        }
        
        if(!resolveForward && !resolveBackward) {
            throw new RuntimeException();
        }
        
        if(resolveForward) {
            return ResolveDirection.FORWARD;
        } else {
            return ResolveDirection.BACKWARD;
        }
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
}
