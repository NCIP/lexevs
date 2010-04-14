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
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

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
    public enum AssociationDirection {/** The SOURC e_ of. */
SOURCE_OF, /** The TARGE t_ of. */
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
            final String codingSchemeUri,
            final String version,
            final String entityCode,
            final String entityCodeNamespace) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
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
                final String codingSchemeUri,
                final String version,
                final String entityCode,
                final String entityCodeNamespace) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
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
                final String codingSchemeUri,
                final String version,
                final String entityCode,
                final String entityCodeNamespace,
                final AssociationDirection direction) {

        return databaseServiceManager.getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<AssociationList>() {

                @Override
                public AssociationList execute(DaoManager daoManager) {
                    AssociationList returnList = new AssociationList();
                    
                    CodingSchemeDao codingSchemeDao = 
                        daoManager.getCodingSchemeDao(codingSchemeUri, version);
                    
                    AssociationDao associationDao = 
                        daoManager.getAssociationDao(codingSchemeUri, version);
                    
                    CodedNodeGraphDao codedNodeGraphDao = 
                        daoManager.getCodedNodeGraphDao(codingSchemeUri, version);
                    
                    String codingSchemeUid = codingSchemeDao.
                        getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
                    
                    List<String> relationsUids = associationDao.
                        getRelationsUIdsForCodingSchemeUId(codingSchemeUid);
                    
                    for(String relationUid : relationsUids){
                        List<String> associationPredicateUids = associationDao.
                            getAssociationPredicateIdsForRelationsId(codingSchemeUid, relationUid);
                  
                        for(String associationPredicateUid : associationPredicateUids) {
                           int tripleUidsCount;
                           if(direction.equals(AssociationDirection.SOURCE_OF)) {
                               tripleUidsCount = codedNodeGraphDao.getTripleUidsContainingSubjectCount(
                                        codingSchemeUid, 
                                        associationPredicateUid, 
                                        entityCode, 
                                        entityCodeNamespace);
                           } else {
                               tripleUidsCount = codedNodeGraphDao.getTripleUidsContainingObjectCount(
                                       codingSchemeUid, 
                                       associationPredicateUid, 
                                       entityCode, 
                                       entityCodeNamespace);   
                           }
                           
                           if(tripleUidsCount > 0) {
                               Association association = new Association();
                               
                               String associationName = associationDao.
                                   getAssociationPredicateNameForId(
                                           codingSchemeUid, 
                                           associationPredicateUid);

                               association.setAssociationName(associationName);
                               
                               AssociatedConceptList associatedConceptList =
                                   new LazyLoadableAssociatedConceptList(
                                           tripleUidsCount,
                                           codedNodeGraphDao, 
                                           codingSchemeUid, 
                                           associationPredicateUid, 
                                           entityCode, 
                                           entityCodeNamespace, 
                                           direction,
                                           associatedConceptPageSize);
                               
                               association.setAssociatedConcepts(associatedConceptList);
                               
                               returnList.addAssociation(association);
                               
                               
                           }
                        }
                        
                    }
                    return returnList;
                }
            
        });
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
