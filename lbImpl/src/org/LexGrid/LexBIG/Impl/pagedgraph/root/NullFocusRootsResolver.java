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
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.database.utility.DaoUtility.SortContainer;
import org.lexevs.locator.LexEvsServiceLocator;

public class NullFocusRootsResolver implements RootsResolver {

    private static final long serialVersionUID = 9219617549685434658L;


    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String version, 
            String containerName,
            ResolveDirection direction, 
            GraphQuery graphQuery,
            SortOptionList sortOptionList) {
        return this.resolveRoots(codingSchemeUri, version, containerName, direction, graphQuery, sortOptionList, 0, -1);
    }
    
    
    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            ResolveDirection direction,
            GraphQuery query,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize) {
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        
        if(direction.equals(ResolveDirection.FORWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return query.getRestrictToSourceCodes();
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return this.getRelatedSourceCodes(
                        codingSchemeUri, 
                        codingSchemeVersion, 
                        query, 
                        sortOptionList,
                        currentPosition,
                        pageSize);
            }
            
           return this.getRoots(
                   codingSchemeUri, 
                   codingSchemeVersion, 
                   relationsContainerName, 
                   query.getRestrictToAssociations(),
                   query.getRestrictToAssociationsQualifiers(),
                   query.getRestrictToSourceCodeSystem(),
                   query.getRestrictToTargetCodeSystem(),
                   sortOptionList,
                   currentPosition,
                   pageSize);
        }
        
        if(direction.equals(ResolveDirection.BACKWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return query.getRestrictToTargetCodes();
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return this.getRelatedTargetCodes(
                        codingSchemeUri, 
                        codingSchemeVersion, 
                        query,
                        sortOptionList,
                        currentPosition,
                        pageSize);
            }
            
            return this.getTails(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    relationsContainerName, 
                    query.getRestrictToAssociations(),
                    query.getRestrictToAssociationsQualifiers(),
                    query.getRestrictToSourceCodeSystem(),
                    query.getRestrictToTargetCodeSystem(),
                    sortOptionList,
                    currentPosition,
                    pageSize);
        }
        
        return returnList;
    }
    
    @Override
    public boolean isRootOrTail(ConceptReference ref) {
       return isRefRootOrTail(ref);
    }
    
    public static boolean isRefRootOrTail(ConceptReference ref) {
        boolean isRootOrTail = (ref.getCode().equals(AbstractEndNode.ROOT) || ref.getCode().equals(AbstractEndNode.TAIL));
        
        return isRootOrTail;
    }
    
    protected List<ConceptReference> getRelatedTargetCodes(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            GraphQuery query,
            SortOptionList sortOptionList,
            int currentPosition,
            int pageSize){
         
        List<String> uids = new ArrayList<String>();
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        for(ConceptReference ref : query.getRestrictToSourceCodes()) {
            uids.addAll( 
            service.
            getTripleUidsContainingSubject(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    null, 
                    null, 
                    ref.getCode(), 
                    ref.getCodeNamespace(), 
                    query, 
                    sortContainer.getSorts(), 
                    currentPosition, 
                    pageSize));
        }
        
        return service.getConceptReferencesFromUidTarget(
                codingSchemeUri, 
                codingSchemeVersion, 
                sortContainer.getSorts(), 
                uids);
    }
    
    protected List<ConceptReference> getRelatedSourceCodes(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            GraphQuery query, 
            SortOptionList sortOptionList,
            int currentPosition,
            int pageSize){
         
        List<String> uids = new ArrayList<String>();
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        for(ConceptReference ref : query.getRestrictToTargetCodes()) {
            uids.addAll( 
            service.
            getTripleUidsContainingObject(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    null, 
                    null, 
                    ref.getCode(), 
                    ref.getCodeNamespace(), 
                    query, 
                    sortContainer.getSorts(), 
                    currentPosition, 
                    pageSize));
        }
        
        return service.getConceptReferencesFromUidSource(
                codingSchemeUri, 
                codingSchemeVersion, 
                sortContainer.getSorts(), 
                uids);
    }
    
    protected List<ConceptReference> getRoots(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames,
            List<QualifierNameValuePair> qualifiers,
            List<String> subjectEntityCodeNamespaces,
            List<String> objectEntityCodeNamespaces,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        TraverseAssociations traverseAssociations;
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
            traverseAssociations = TraverseAssociations.TOGETHER;
        } else {
           traverseAssociations = TraverseAssociations.TOGETHER;
        }
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        List<ConceptReference> roots = 
            service.getRootConceptReferences(
            		codingSchemeUri, 
            		codingSchemeVersion, 
            		relationsContainerName, 
            		associationNames, 
            		qualifiers,
            		subjectEntityCodeNamespaces,
            		objectEntityCodeNamespaces,
            		traverseAssociations, 
            		sortContainer.getSorts(), 
            		currentPosition, 
                    pageSize);
        
        return roots;
    }
    
    protected List<ConceptReference> getTails(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames,
            List<QualifierNameValuePair> qualifiers,
            List<String> subjectEntityCodeNamespaces,
            List<String> objectEntityCodeNamespaces,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        TraverseAssociations traverseAssociations;
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
            traverseAssociations = TraverseAssociations.TOGETHER;
        } else {
        	 traverseAssociations = TraverseAssociations.TOGETHER;
        }
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        return 
            service.getTailConceptReferences(
            		codingSchemeUri, 
            		codingSchemeVersion, 
            		relationsContainerName, 
            		associationNames, 
            		qualifiers,
            		subjectEntityCodeNamespaces,
            		objectEntityCodeNamespaces,
            		traverseAssociations,
            		sortContainer.getSorts(), 
                    currentPosition, 
                    pageSize);
    }
}