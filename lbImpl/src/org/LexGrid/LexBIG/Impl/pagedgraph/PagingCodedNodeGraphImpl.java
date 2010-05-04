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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.StubReturningCycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
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
   
    
    /**
     * Instantiates a new paging coded node graph impl.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param relationsContainerName the relations container name
     */
    public PagingCodedNodeGraphImpl(
            String codingSchemeUri, 
            String version,
            String relationsContainerName){
        super(codingSchemeUri, version, relationsContainerName);
    }
    
  
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsList(
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
            boolean isValidFocus = this.checkFocus(focus, resolveForward, resolveBackward);
            
            if(! isValidFocus) {
                return null;
            }
            
        } else {
            List<CodeNamespacePair> codes = 
                resolveForward ? graphQueryBuilder.getQuery().getRestrictToSourceCodes() :
                    graphQueryBuilder.getQuery().getRestrictToTargetCodes();
                
                if(CollectionUtils.isEmpty(graphQueryBuilder.getQuery().getRestrictToSourceCodes())) {
                    CodeNamespacePair root = new CodeNamespacePair(
                            resolveForward ? "@" : "@@",
                                    LexEvsServiceLocator.getInstance().getSystemResourceService().
                                    getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version));
                    codes.add(root);
                }

                ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();

                for(CodeNamespacePair pair : codes) {
                    
                    ResolvedConceptReferenceList list = this.doResolveAsList(
                            PagedGraphUtils.codeNamespacePairToConceptReference(pair), 
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

                        for(ResolvedConceptReference ref :
                            this.doResolveAsList(
                                    PagedGraphUtils.codeNamespacePairToConceptReference(pair), 
                                    resolveForward, 
                                    resolveBackward, 
                                    resolveCodedEntryDepth, 
                                    resolveAssociationDepth, 
                                    propertyNames, 
                                    propertyTypes,
                                    sortOptions, 
                                    filterOptions, 
                                    maxToReturn, 
                                    keepLastAssociationLevelUnresolved).getResolvedConceptReference()) {

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
                    cycleDetectingCallback));
        }
        
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        returnList.addResolvedConceptReference(focus);
    
        return returnList;
    }

    protected boolean checkFocus(ConceptReference focus,  boolean resolveForward, boolean resolveBackward) {
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        int count = 0;
        //if(resolveForward) {
           count += service.
            getTripleUidsContainingSubjectCount(
                    this.getCodingSchemeUri(), 
                    this.getVersion(), 
                    this.getRelationsContainerName(), 
                    null, 
                    focus.getCode(), 
                    focus.getCodeNamespace(), 
                    new GraphQuery());
       // } 
        //if(resolveBackward) {
            count += service.
            getTripleUidsContainingObjectCount(
                    this.getCodingSchemeUri(), 
                    this.getVersion(), 
                    this.getRelationsContainerName(), 
                    null, 
                    focus.getCode(), 
                    focus.getCodeNamespace(), 
                    new GraphQuery());
       // }
        return count > 0;
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
