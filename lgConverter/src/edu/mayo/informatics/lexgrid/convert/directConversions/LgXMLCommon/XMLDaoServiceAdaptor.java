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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.association.VersionableEventAssociationService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */

public class XMLDaoServiceAdaptor {

    LexEvsServiceLocator locator = null;
    DatabaseServiceManager dbManager = null;
    EntityService entityService = null;
    CodingSchemeService codingSchemeService = null;
    AssociationService assocService = null;
    VersionableEventAssociationService assocServiceForPred = null;
    DaoCallbackService daoCallbackService;
    PropertyService propertyService = null;
    PickListDefinitionService pickListService;
    ValueSetDefinitionService valueSetService;
    
    ArrayList<AssociationPredicate> associationList = null;
    ArrayList<Relations> relationList = null;;
    

    /**
     * constructor initializes all DAO services
     */
    public XMLDaoServiceAdaptor() {
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        entityService = dbManager.getEntityService();
        codingSchemeService = dbManager.getCodingSchemeService();
        assocService = dbManager.getAssociationService();
        associationList = new ArrayList<AssociationPredicate>();
        relationList = new ArrayList<Relations>();
        assocServiceForPred = (VersionableEventAssociationService) assocService;
        daoCallbackService = dbManager.getDaoCallbackService();
        propertyService = dbManager.getPropertyService();
        pickListService = dbManager.getPickListDefinitionService();
        valueSetService = dbManager.getValueSetDefinitionService();
    }

    /**
     * Store a Coding Scheme using the DAO service. Intended to capture
     * the meta data of the coding scheme
     * @param scheme
     * @throws CodingSchemeAlreadyLoadedException
     */
    public void storeCodingScheme(CodingScheme scheme) throws CodingSchemeAlreadyLoadedException {
        codingSchemeService.insertCodingScheme(scheme, null);
    }

    /**
     * Store an Entity using the DAO service
     * @param entity
     * @param c
     */
    public void storeEntity(Entity entity, CodingScheme c) {
        entityService.insertEntity(c.getCodingSchemeURI(), c.getRepresentsVersion(), entity);
    }

    /**
     * Store a coding scheme relation
     * @param codingSchemeUri
     * @param version
     * @param relation
     */
    public void storeRelation(String codingSchemeUri, String version, Relations relation) {
        if (relationList.contains(relation))
            return;
        assocService.insertRelation(codingSchemeUri, version, relation);
        relationList.add(relation);
    }

    /**
     * Store an association (source)
     * @param uri
     * @param version
     * @param containerName
     * @param associationPredicateName
     * @param source
     */
    public void storeAssociation(String uri, String version, String containerName, String associationPredicateName,
            AssociationSource source) {
        assocService.insertAssociationSource(uri, version, containerName, associationPredicateName, source);
    }

    /**
     * Store an association predicate
     * @param uri
     * @param version
     * @param relationsName
     * @param predicate
     */
    public void storeAssociationPredicate(final String uri, final String version, final String relationsName,
            final AssociationPredicate predicate) {

        daoCallbackService.executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager) {
                String codingSchemeId = daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(uri,
                        version);
                String relationsId = daoManager.getCurrentAssociationDao()
                        .getRelationsId(codingSchemeId, relationsName);
                daoManager.getCurrentAssociationDao().insertAssociationPredicate(codingSchemeId, relationsId,
                        predicate, true);
                return null;
            }

        });
    }

    /**
     * Store properties.
     * @param codingSchemeUri
     * @param version
     * @param entityCode
     * @param entityCodeNamespace
     * @param property
     */
    public void storeProperty(String codingSchemeUri, String version, String entityCode, String entityCodeNamespace,
            Property property) {
        propertyService.insertEntityProperty(codingSchemeUri, version, entityCode, entityCodeNamespace, property);
    }
    public void storeRevision(String codingSchemeUri, String version, String revsionId,
            Revision revision) {
        //revisionService.insertRevision(codingSchemeUri, version, revisionId,  revision);
    }
    
    public void storeSystemRelease(String codingSchemeUri, String version,
            SystemRelease release) {
        //revisionService.insertRevision(codingSchemeUri, version, release );
    }
    
    public void storeValueSet(ValueSetDefinition valueSet, String systemReleaseURI, Mappings mappings) throws LBException {
        valueSetService.insertValueSetDefinition(valueSet, systemReleaseURI, mappings);
    }
    
    public void storePickList(PickListDefinition picklist, String systemReleaseURI, Mappings mappings) throws LBParameterException, LBException {
        pickListService.insertPickListDefinition(picklist, systemReleaseURI, mappings);
    }
    
    /**
     * Activate a given coding scheme
     * @param urn
     * @param version
     * @throws LBParameterException
     */
    public void activateScheme(String urn, String version) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference codingScheme = new AbsoluteCodingSchemeVersionReference();
        codingScheme.setCodingSchemeURN(urn);
        codingScheme.setCodingSchemeVersion(version);
        locator.getSystemResourceService().updateCodingSchemeResourceStatus(codingScheme,
                CodingSchemeVersionStatus.ACTIVE);
    }
}
