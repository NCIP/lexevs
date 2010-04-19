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
package org.LexGrid.LexBIG.Impl.pagedgraph.builder;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Impl.pagedgraph.model.LazyLoadableAssociatedConceptList;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * The Class AssociationListBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationListBuilder {
    
    /** The associated concept page size. */
    private int associatedConceptPageSize = 100;
    
    /**
     * The Enum AssociationDirection.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public enum AssociationDirection {
        SOURCE_OF,
        TARGET_OF}
    
    /** The database service manager. */
    private DatabaseServiceManager databaseServiceManager =
        LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    
    /**
     * Builds the source of association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * 
     * @return the association list
     */
    public AssociationList buildSourceOfAssociationList(
            String codingSchemeUri,
            String version,
            String entityCode,
            String entityCodeNamespace,
            String relationsContainerName,
            int resolveAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
                relationsContainerName,
                resolveAssociationDepth,
                resolveCodedEntryDepth,
                graphQuery,
                AssociationDirection.SOURCE_OF);
    }
    
    /**
     * Builds the target of association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * 
     * @return the association list
     */
    public AssociationList buildTargetOfAssociationList(
                String codingSchemeUri,
                String version,
                String entityCode,
                String entityCodeNamespace,
                String relationsContainerName,
                int resolveAssociationDepth,
                int resolveCodedEntryDepth,
                GraphQuery graphQuery) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
                relationsContainerName,
                resolveAssociationDepth,
                resolveCodedEntryDepth,
                graphQuery,
                AssociationDirection.TARGET_OF); }
    
    
    /**
     * Do build association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * @param direction the direction
     * 
     * @return the association list
     */
    protected AssociationList doBuildAssociationList(
            String codingSchemeUri,
            String version,
            String entityCode,
            String entityCodeNamespace,
            String relationsContainerName,
            int resolveAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery,
            AssociationDirection direction) {
        Assert.notNull(graphQuery, "Must pass in a GraphQuery.");

        CodedNodeGraphService codedNodeGraphService =
            databaseServiceManager.getCodedNodeGraphService();
        
        List<String> associationPredicateNames;
        if(CollectionUtils.isEmpty(graphQuery.getRestrictToAssociations())) {
            associationPredicateNames = getAssociationPredicateNames(
                codingSchemeUri,
                version);
        } else {
            associationPredicateNames = graphQuery.getRestrictToAssociations();
        }

        AssociationList returnList = new AssociationList();

        for(String associationPredicateName : associationPredicateNames) {
            int tripleUidsCount;
            if(direction.equals(AssociationDirection.SOURCE_OF)) {
                tripleUidsCount = codedNodeGraphService.getTripleUidsContainingSubjectCount(
                        codingSchemeUri, 
                        version, 
                        relationsContainerName, 
                        associationPredicateName, 
                        entityCode, 
                        entityCodeNamespace, 
                        graphQuery);
            } else {
                tripleUidsCount = codedNodeGraphService.getTripleUidsContainingObjectCount(
                        codingSchemeUri, 
                        version, 
                        relationsContainerName, 
                        associationPredicateName, 
                        entityCode, 
                        entityCodeNamespace, 
                        graphQuery);
            }

            if(tripleUidsCount > 0) {
                Association association = new Association();

                association.setAssociationName(associationPredicateName);

                AssociatedConceptList associatedConceptList =
                    new LazyLoadableAssociatedConceptList(
                            tripleUidsCount,
                            codingSchemeUri,
                            version,
                            relationsContainerName,
                            associationPredicateName, 
                            entityCode, 
                            entityCodeNamespace, 
                            graphQuery,
                            direction,
                            associatedConceptPageSize);

                association.setAssociatedConcepts(associatedConceptList);

                returnList.addAssociation(association);


            }

        }
        return returnList;
    }

    protected List<String> getAssociationPredicateNames(
            String codingSchemeUri, 
            String codingSchemeVersion) {
        CodedNodeGraphService codedNodeGraphService =
            databaseServiceManager.getCodedNodeGraphService();
        
        return codedNodeGraphService.getAssociationPredicateNamesForCodingScheme(
                codingSchemeUri,
                codingSchemeVersion);
    }

    /**
     * Gets the database service manager.
     * 
     * @return the database service manager
     */
    public DatabaseServiceManager getDatabaseServiceManager() {
        return databaseServiceManager;
    }

    /**
     * Sets the database service manager.
     * 
     * @param databaseServiceManager the new database service manager
     */
    public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
        this.databaseServiceManager = databaseServiceManager;
    }
}
